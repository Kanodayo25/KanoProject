package com.kano.project.common.Exception;


import com.kano.project.common.Exception.code.ErrorTypeEnum;
import com.kano.project.common.Exception.code.ServiceErrorCodeEnum;
import com.kano.project.common.model.ResultCode;
import com.kano.project.common.utils.ThreadLocalUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

@Activate(group = {CommonConstants.PROVIDER})
@Slf4j
public class GlobalExceptionHandler extends ListenableFilter {
    public GlobalExceptionHandler() {
        super.listener = new TmsExceptionListener();
    }

    @SneakyThrows
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result;
        // 记录RPC服务调用时间
        long startTime = System.currentTimeMillis();
        result = invoker.invoke(invocation);
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("IP:" + RpcContext.getContext().getRemoteHost()
                + ", Service:" + invoker.getInterface().getName()
                + ", Method:" + invocation.getMethodName()
                + ", RpcExecutionTime: " + executionTime + " ms."
        );
        long overtimeTime = 4000L;
        if (executionTime >= overtimeTime) {
            // 需要超时预警才判断是否是异步方法
            Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            if (method.getAnnotation(IgnoreTimeout.class) == null) {
                //sendTimeoutWarning(invoker.getInterface().getName(), invocation.getMethodName(), executionTime);
            }
        }
        return result;

    }

    /**
     * 发送超时预警
     *
     */
    /*private void sendTimeoutWarning(String interfaceName, String methodName, long executionTime) {
        wechatAlarm("Rpc超时预警:" + interfaceName + "." + methodName + ":executionTime = " + executionTime + "ms");
    }

    static void wechatAlarm(String alarm) {
        String robotUrl = ConfigService.getAppConfig().getProperty("robot.url", "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=1709d2fe-ad03-4ee6-8ed6-f66de1758823");
        RobotVo robotVo = RobotVo.builder()
                .content(alarm)
                .webhookAddress(robotUrl).build();
        RobotUtil.sendAlarmMsg(robotVo);
    }*/
        //微信预紧暂时不做

    static class TmsExceptionListener implements Listener {

        @Override
        public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
            // Clear the cache value set by the current thread
            ThreadLocalUtils.clear("base");
            ThreadLocalUtils.clear("create");
            ThreadLocalUtils.clear("update");

            if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
                try {
                    Throwable exception = appResponse.getException();
                    // 打印日志，方便排查问题
                    log.error("Dubbo提供者端发生异常", exception);
                    // directly throw if it's checked exception
                    if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                        return;
                    }

                    // Handling custom exceptions
                    if (exception instanceof BaseProjectException) {
                        BaseProjectException baseTmsException = (BaseProjectException) exception;
                        appResponse.setValue(com.kano.project.common.model.Result.fail(baseTmsException.getCode(), baseTmsException.getMessage()));
                        appResponse.setException(null);
                        return;
                    } else if (exception instanceof IllegalArgumentException) {
                        IllegalArgumentException paramException = (IllegalArgumentException) exception;
                        appResponse.setValue(com.kano.project.common.model.Result.fail(ResultCode.NET_ILLEGAL_ARGUMENT.getCode(), paramException.getMessage()));
                        appResponse.setException(null);
                        return;
                    } else if (exception instanceof ConstraintViolationException) {
                        ConstraintViolationException ce = (ConstraintViolationException) exception;
                        Set<ConstraintViolation<?>> constraintViolations = ce.getConstraintViolations();
                        if (constraintViolations == null || constraintViolations.isEmpty()) {
                            appResponse.setValue(com.kano.project.common.model.Result.fail(ResultCode.ILLEGAL_ARGUMENT.getCode(), ResultCode.ILLEGAL_ARGUMENT.getMsg()));
                            appResponse.setException(null);
                            return;
                        }
                        Optional<String> first = constraintViolations.stream().map(ConstraintViolation::getMessage).findFirst();
                        appResponse.setValue(com.kano.project.common.model.Result.fail(ResultCode.ILLEGAL_ARGUMENT.getCode(), first.orElse(ResultCode.ILLEGAL_ARGUMENT.getMsg())));
                        appResponse.setException(null);
                        return;
                    }
                    // directly throw if the exception appears in the signature
                    try {
                        Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                        Class<?>[] exceptionClassses = method.getExceptionTypes();
                        for (Class<?> exceptionClass : exceptionClassses) {
                            if (exception.getClass().equals(exceptionClass)) {
                                return;
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        return;
                    }

                    // for the exception not found in method's signature, print ERROR message in server's log.
                    log.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);
                    // send wechat alarm
                    StringWriter errorTrace = new StringWriter();
                    PrintWriter pw = new PrintWriter(errorTrace);
                    exception.printStackTrace(pw);
                    String alarm = "发现未受查异常:" + RpcContext.getContext().getRemoteHost() + ". service: "
                            + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
                            + ", exception: " + exception.getClass().getName() + ": "
                            + exception.getMessage() + ", trace: " + errorTrace;
                    //wechatAlarm(alarm);
                    // tracking
                    log.error(exception.getMessage() + ErrorCodeGenerator.errorCode(ServiceErrorCodeEnum.common, ErrorTypeEnum.sysErr, "001"), exception);
                    // directly throw if exception class and interface class are in the same jar file.
                    String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                    String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                    if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                        return;
                    }
                    // directly throw if it's JDK exception
                    String className = exception.getClass().getName();
                    if (className.startsWith("java.") || className.startsWith("javax.")) {
                        return;
                    }
                    // directly throw if it's dubbo exception
                    if (exception instanceof RpcException) {
                        return;
                    }

                    // otherwise, wrap with RuntimeException and throw back to the client
                    appResponse.setException(exception);
                    return;
                } catch (Throwable e) {
                    log.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
                    return;
                }
            }
        }

        @Override
        public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
            log.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
        }

    }
}

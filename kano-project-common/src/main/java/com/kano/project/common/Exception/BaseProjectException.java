package com.kano.project.common.Exception;

import com.kano.project.common.Exception.code.ErrorTypeEnum;
import com.kano.project.common.Exception.code.ServiceErrorCodeEnum;
import com.kano.project.common.model.ResultCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseProjectException extends RuntimeException{
    private Integer code;

    private final Integer errCode = -1001;

    private Throwable errCause;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public Throwable getErrCause() {
        return errCause;
    }

    public String getMsg() {
        return msg;
    }

    public BaseProjectException(ResultCode resultExceptionCode) {
        super(resultExceptionCode.getMsg(), null, false, false);
        this.code = resultExceptionCode.getCode();
    }

    public BaseProjectException(ResultCode resultExceptionCode, String msg) {
        super(msg, null, false, false);
        this.code = resultExceptionCode.getCode();
        this.msg = msg;
    }


    public BaseProjectException(Integer errCode, String msg) {
        super(msg, null, false, false);
        this.code = errCode;
        this.msg = msg;
    }


    /**
     * 自定义错误码堆栈抛出
     *
     * @param resultExceptionCode ResultCode
     * @param cause               堆栈
     */
    public BaseProjectException(ResultCode resultExceptionCode, Throwable cause) {
        super(resultExceptionCode.getMsg(), cause, false, true);
        this.errCause = cause;
        this.msg = resultExceptionCode.getMsg();
        this.code = resultExceptionCode.getCode();
    }


    /**
     * 自定义错误信息抛出,此方法将默认-1001错误码
     *
     * @param msg msg
     */
    public BaseProjectException(String msg) {
        super(msg, null, false, false);
        this.msg = msg;
        this.code = errCode;
    }

    /**
     * 自定义堆栈抛出,此方法将默认-1001错误码
     *
     * @param msg   msg
     * @param cause 堆栈
     */
    public BaseProjectException(String msg, Throwable cause) {
        super(msg, cause, false, true);
        this.errCause = cause;
        this.msg = msg;
        this.code = errCode;
    }

    /**
     * 抛出tracking异常
     *
     * @param serviceCode ServiceErrorCodeEnum
     * @param typeEnum    ErrorTypeEnum
     * @param detailCode  详情码
     * @param msg         错误信息
     * @param cause       堆栈,可为空
     */
    public BaseProjectException(ServiceErrorCodeEnum serviceCode, ErrorTypeEnum typeEnum, String detailCode, String msg, Throwable cause) {
        super(msg, cause, false, true);
        this.errCause = cause;
        this.msg = msg;
        this.code = errCode;
        log.error(msg + ErrorCodeGenerator.errorCode(serviceCode, typeEnum, detailCode), cause);
    }


    /**
     * 不要使用!仅仅为了老项目不必重写新的父类构造方法
     * Don't use it! Just for the old project without having to rewrite the new parent class constructor
     */
    @Deprecated
    public BaseProjectException() {
        super("BaseTmsException 无参构造已过时", null, false, false);
    }
}

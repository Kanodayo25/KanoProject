package com.kano.project.common.Exception;

import com.kano.project.common.Exception.code.ErrorLevelEnum;
import com.kano.project.common.Exception.code.ErrorTypeEnum;
import com.kano.project.common.Exception.code.ServiceErrorCodeEnum;

public class ErrorCodeGenerator {

        /**
         * 固定标识
         */
        private static final String WE = "#ErrorCode:WE";

        /**
         * 系统编码
         */
        private static final String SYS_CODE = "008";

        /**
         * 获取info级别错误码
         *
         * @param serviceCode ServiceErrorCodeEnum
         * @param typeEnum    ErrorTypeEnum
         * @param detailCode  明细码,强制3位,例如:002
         * @return WE008XXXXXXXX
         */
        public static String infoCode(ServiceErrorCodeEnum serviceCode, ErrorTypeEnum typeEnum, String detailCode) {
            verificationCode(detailCode);
            return WE + SYS_CODE + ErrorLevelEnum.info.getCode() + typeEnum.getCode() + serviceCode.getCode() + detailCode + " ";
        }

        /**
         * 获取warn级别错误码
         *
         * @param serviceCode ServiceErrorCodeEnum
         * @param typeEnum    ErrorTypeEnum
         * @param detailCode  明细码,强制3位,例如:002
         * @return WE008XXXXXXXX
         */
        public static String warnCode(ServiceErrorCodeEnum serviceCode, ErrorTypeEnum typeEnum, String detailCode) {
            verificationCode(detailCode);
            return WE + SYS_CODE + ErrorLevelEnum.warn.getCode() + typeEnum.getCode() + serviceCode.getCode() + detailCode + " ";
        }

        /**
         * 获取error级别错误码
         *
         * @param serviceCode ServiceErrorCodeEnum
         * @param typeEnum    ErrorTypeEnum
         * @param detailCode  明细码,强制3位,例如:002
         * @return WE008XXXXXXXX
         */
        public static String errorCode(ServiceErrorCodeEnum serviceCode, ErrorTypeEnum typeEnum, String detailCode) {
            verificationCode(detailCode);
            return WE + SYS_CODE + ErrorLevelEnum.error.getCode() + typeEnum.getCode() + serviceCode.getCode() + detailCode + " ";
        }

        /**
         * 获取fatal级别错误码
         *
         * @param serviceCode ServiceErrorCodeEnum
         * @param typeEnum    ErrorTypeEnum
         * @param detailCode  明细码,强制3位,例如:002
         * @return WE008XXXXXXXX
         */
        public static String fatalCode(ServiceErrorCodeEnum serviceCode, ErrorTypeEnum typeEnum, String detailCode) {
            verificationCode(detailCode);
            return WE + SYS_CODE + ErrorLevelEnum.fatal.getCode() + typeEnum.getCode() + serviceCode.getCode() + detailCode + " ";
        }

        /**
         * verificationCode
         */
        private static void verificationCode(String detailCode) {
            if(detailCode.length() == 3){
                throw new BaseProjectException("The length of the error code is incorrect");
            }
        }
}

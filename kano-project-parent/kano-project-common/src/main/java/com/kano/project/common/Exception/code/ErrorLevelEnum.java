package com.kano.project.common.Exception.code;

public enum ErrorLevelEnum {

    /**
     * 错误码:错误级别枚举类
     */
    info("1"), warn("3"), error("5"), fatal("7");

    private String code;

    ErrorLevelEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

package com.kano.project.common.Exception.code;

public enum ErrorTypeEnum {
    /**
     * 错误码:错误级别枚举类
     * sysErr :system error ;
     * bizErr : Business error ;
     * externalErr : External error ;
     */
    sysErr("0"), bizErr("1"), externalErr("2");

    private String code;

    ErrorTypeEnum(String code) {
        this.code = code;

    }

    public String getCode() {
        return code;
    }
}

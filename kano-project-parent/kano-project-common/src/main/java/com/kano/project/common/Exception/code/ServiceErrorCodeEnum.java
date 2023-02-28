package com.kano.project.common.Exception.code;

public enum ServiceErrorCodeEnum {

    /**
     * 错误码:场景错误码枚举类
     */
    common("000"), delivery("001"), schedule("002"), accessGate("003"), basic("004"), job("005"), fare("006"), gps("007");

    private String code;

    ServiceErrorCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}

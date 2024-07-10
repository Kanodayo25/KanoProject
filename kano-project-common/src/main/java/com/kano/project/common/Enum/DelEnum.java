package com.kano.project.common.Enum;

public enum DelEnum {

    NOT_DEL(Boolean.FALSE,"未删除"),
    DEL(Boolean.TRUE,"已删除");


    private Boolean code;
    private String desc;

    DelEnum(Boolean code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Boolean getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

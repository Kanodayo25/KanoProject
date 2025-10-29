package com.kano.project.common.Enum;

public enum BaseYesOrNoEnum {
    NO(Boolean.FALSE,"否"),
    YES(Boolean.TRUE,"是");


    private Boolean code;
    private String desc;

    BaseYesOrNoEnum(Boolean code, String desc) {
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

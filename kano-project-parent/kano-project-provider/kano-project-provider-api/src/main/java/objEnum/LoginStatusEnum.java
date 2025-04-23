package objEnum;

public enum LoginStatusEnum {

    LOGINED("1","已登录"),
    UNLOGIN("0","未登录");

    private String code;
    private String desc;

    LoginStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }

    public LoginStatusEnum getEnumByCode(String code){
        return LoginStatusEnum.valueOf(code);
    }
}

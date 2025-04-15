package objEnum;

public enum RoleTypeEnum {

    READ("read","查询"),
    WRITE("write","新增"),
    UPDATE("update","修改"),
    DELETE("delete","删除");

    private String code;
    private String desc;

    RoleTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

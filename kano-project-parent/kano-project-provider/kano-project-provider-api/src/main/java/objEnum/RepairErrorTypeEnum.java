package objEnum;

public enum RepairErrorTypeEnum {
    HARDWARE(0, "硬件"),
    NETWORK(1, "网络"),
    HIS(2, "HIS"),
    EMR(3, "EMR"),
    PLATFORM(4, "平台"),
    OTHER(5, "其他");

    private final int code;
    private final String description;

    RepairErrorTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取对应的枚举常量
     * @param code 错误类型代码
     * @return 对应的枚举常量，如果未找到返回null
     */
    public static RepairErrorTypeEnum getByCode(int code) {
        for (RepairErrorTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据代码获取描述信息
     * @param code 错误类型代码
     * @return 对应的描述信息，如果未找到返回"未知错误类型"
     */
    public static String getDescriptionByCode(int code) {
        RepairErrorTypeEnum type = getByCode(code);
        return type != null ? type.getDescription() : "未知错误类型";
    }
}

package objEnum;

public enum DepartmentType {

    INPATIENT(0, "住院"),
    OUTPATIENT(1, "门诊"),
    MEDICAL_TECHNOLOGY(2, "医技"),
    PHYSICAL_EXAM(3, "体检"),
    OTHER(4, "其他");

    private final int code;
    private final String description;

    DepartmentType(int code, String description) {
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
     * @param code 科室类型代码
     * @return 对应的枚举常量，如果未找到返回null
     */
    public static DepartmentType getByCode(int code) {
        for (DepartmentType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据代码获取描述信息
     * @param code 科室类型代码
     * @return 对应的描述信息，如果未找到返回"未知科室类型"
     */
    public static String getDescriptionByCode(int code) {
        DepartmentType type = getByCode(code);
        return type != null ? type.getDescription() : "未知科室类型";
    }

    /**
     * 验证代码是否有效
     * @param code 科室类型代码
     * @return 如果代码有效返回true，否则返回false
     */
    public static boolean isValidCode(int code) {
        return getByCode(code) != null;
    }

    /**
     * 获取所有科室类型的代码和描述
     * @return 包含所有科室类型代码和描述的字符串
     */
    public static String getAllTypes() {
        StringBuilder sb = new StringBuilder();
        for (DepartmentType type : values()) {
            sb.append(type.code).append(":").append(type.description);
            if (type.ordinal() < values().length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}

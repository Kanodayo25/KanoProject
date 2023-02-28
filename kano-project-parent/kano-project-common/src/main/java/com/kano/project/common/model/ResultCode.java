package com.kano.project.common.model;

public class ResultCode {
    /**
     * success
     */
    public static ResultCode SUCCESS =new ResultCode(1001, "操作成功");
    /**
     * NET_SUCCESS
     */
    public static ResultCode NET_SUCCESS =new ResultCode(1001, "操作成功");
    /**
     * NET_ERROR
     */
    public static ResultCode NET_ERROR =new ResultCode(-1001, "操作失败");

    /**
     * NET_ERROR_参数异常
     */
    public static ResultCode NET_ILLEGAL_ARGUMENT =new ResultCode(-1001, "参数异常");
    /**
     * 系统出异常啦!,请联系管理员
     */
    public static ResultCode UNKNOWN_ERROR =new ResultCode(-1001, "系统出异常啦!,请联系管理员!!!");
    /**
     * 参数异常
     */
    public static   ResultCode ILLEGAL_ARGUMENT =new ResultCode(-1001, "参数异常");
    /**
     * 全局ID生成失败
     */
    public static   ResultCode ID_GENERATOR_FAILED =new ResultCode(-1001, "全局ID生成失败");
    /**
     * 密码已期
     */
    public static   ResultCode PWD_EXPIRE_FAILED =new ResultCode(-1002, "密码已期");
    /**
     * 账户锁定
     */
    public static   ResultCode ACCOUNT_CHCKED_FAILED =new ResultCode(-1003, "账户锁定");

    /**
     * TOKEN过期
     */
    public static   ResultCode WMS_TOKEN_EXPIRE =new ResultCode(999999, "WMS账号Token过期");

    /**
     * 配送编号不存在
     */
    public static   ResultCode NOT_DELIVERY_NO =new ResultCode(-1004, "配送编号不存在!");

    /**
     * 未查询到发票信息
     */
    public static   ResultCode INVOICE_NOT_FUND =new ResultCode(-1200, "未查询到发票信息!");
    /**
     * 发票未关联账单
     */
    public static   ResultCode INVOICE_UNLINKED_BILL =new ResultCode(-1201, "发票未关联账单!");

    /**
     * 备注信息找不到身份证
     */
    public static   ResultCode NOT_FOUND_IDCARD =new ResultCode(-1301, "备注信息找不到身份证!");

    /**
     * 身份证找不到子账户
     */
    public static   ResultCode NOT_FOUND_SUB_ACCOUNT =new ResultCode(-1302, "身份证找不到子账户!");

    /**
     * 发票记录已写入
     */
    public static   ResultCode INVOICE_RECORD_WRITTEN =new ResultCode(-1303, "发票记录已写入!");

    /**
     * 流水写入失败
     */
    public static   ResultCode INVOICE_WRITE_FAILED =new ResultCode(-1304, "流水写入失败!");


    public static   ResultCode CONSISTENT_CERTIFICATE =new ResultCode(-1400, "三证不合一,不允许排班!!");
    public static   ResultCode ACCIDENT_INSURANCE =new ResultCode(-1401, "意外险即将到期,不允许排班!");
    public static   ResultCode BUSINESS_INSURANCE =new ResultCode(-1402, "商业险即将到期,不允许排班!");
    public static   ResultCode MANDATORY_INSURANCE =new ResultCode(-1403, "交强险即将到期,不允许排班!");
    public static   ResultCode DRIVING_LICENSE =new ResultCode(-1404, "行驶证即将到期,请谨慎排班!");
    public static   ResultCode DRIVER_LICENSE =new ResultCode(-1405, "驾驶证即将到期,请谨慎排班!");

    public static   ResultCode CAPACITY_USED =new ResultCode(-1406, "运力正在配送中,不允许排班!");


    private Integer code;

    private String msg;

    public ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

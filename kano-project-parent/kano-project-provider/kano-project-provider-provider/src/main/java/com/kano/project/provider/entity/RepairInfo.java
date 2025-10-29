package com.kano.project.provider.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (RepairInfo)表实体类
 *
 * @author makejava
 * @since 2025-10-29 14:51:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairInfo extends Model<RepairInfo> {
//id
    private Long id;
//错误类型(0:硬件 1:网络 2:HIS 3:EMR 4:平台 5:其他)
    private Integer type;
//科室类型(0住院1门诊2医技3体检4其他)
    private Integer departmentType;
//保修科室名
    private String departmentName;
//问题描述
    private String troubleDesc;
//处理过程图片，视频地址
    private String dealProcess;
//创建时间
    private Date createTime;
//修改时间
    private Date updatedTime;

    //是否疑难
    private Boolean isHard;




}


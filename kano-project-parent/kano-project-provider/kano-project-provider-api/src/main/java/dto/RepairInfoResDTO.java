package dto;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairInfoResDTO implements Serializable {
    private static final long serialVersionUID = 111111111L;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updatedTime;
    //是否疑难
    private Boolean isHard;
}

package dto;

import com.kano.project.common.model.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairInfoReqDTO extends Page {
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
    //是否疑难
    private Boolean isHard;
}

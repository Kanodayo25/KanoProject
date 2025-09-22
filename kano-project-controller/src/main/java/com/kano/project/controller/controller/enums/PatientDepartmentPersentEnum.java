package com.kano.project.controller.controller.enums;

import lombok.Getter;

@Getter
public enum PatientDepartmentPersentEnum {

    //血透门诊
    HEMODIALYSIS_CLINIC("血透门诊",100.0),

    //新生儿科
    NEONATAL_DEPARTMENT("新生儿科",100.0),

    //肾病内科
    DEPARTMENT_OF_NEPHROLOGY("肾病内科",100.0),

    //镇痛门诊
    ANALGESIA_CLINIC("镇痛门诊",100.0),

    //呼吸与危重症医学科＋老年科
    RESPIRATORY_AND_CRITICAL_CARE("呼吸与危重症医学科＋老年科",99.80),

    //神经内科
    NEUROLOGY_DEPARTMENT("神经内科",99.60),

    //骨一科
    DEPARTMENT_ONE_OF_ORTHOPEDICS("骨一科",99.40),

    //中医科
    CHINESE_MEDICINE("中医科",99.20),

    //感染性疾病科
    INFECTIOUS_DISEASES("感染性疾病科",99.0),

    //产科
    OBSTETRICS("产科",98.80),

    //骨二科
    ORTHOPEDICS_II("骨二科",98.60),

    //急诊医学科
    EMERGENCY_MEDICINE("急诊医学科",98.40),

    //消化内科
    GASTROENTEROLOGY("消化内科",98.20),

    //肝胆外科
    HEPATOBILIARY_SURGERY("肝胆外科",98.0),

    //眼科
    OPHTHALMOLOGY("眼科",97.8),

    //肿瘤内科、血液内科
    TUMOR_BLOOD("肿瘤内科、血液内科",97.6),

    //康复疼痛科
    REHABILITATION_PAIN("康复疼痛科",97.4),

    //儿科
    PEDIATRICS("儿科",97.2),

    //胃肠外科
    GASTROINTESTINAL_SURGERY("胃肠外科",97.0),

    //泌尿外科
    UROLOGY("泌尿外科",96.8),

    //妇科
    GYNECOLOGY("妇科",96.6),

    //神经外科
    NEUROSURGERY("神经外科",96.4),

    //心血管内科
    CARDIOVASCULAR_MEDICINE("心血管内科",96.2),

    //内分泌科
    ENDOCRINOLOG("内分泌科",96.0),

    //口腔科
    DENTISTRY("口腔科",95.8),

    //皮肤科
    DERMATOLOGY("皮肤科",95.6),

    //耳鼻咽喉科
    OTORHINOLARYNGOLOGY("耳鼻咽喉科",95.4);



    private String departmentName;
    private Double percent;

    PatientDepartmentPersentEnum(String departmentName, Double percent) {
        this.departmentName = departmentName;
        this.percent = percent;
    }

    public static PatientDepartmentPersentEnum getEnumByDepartmentName(String departmentName) throws IllegalAccessException {
        for(PatientDepartmentPersentEnum item:PatientDepartmentPersentEnum.values()){
            if(departmentName.equals(item.getDepartmentName())){
                return item;
            }
        }
        throw new IllegalAccessException("PatientDepartmentPersentEnum中"+departmentName+"不存在");
    }

}

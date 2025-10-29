package dubboImplTest;

import com.kano.project.common.Enum.BaseYesOrNoEnum;
import com.kano.project.common.model.PageResult;
import com.kano.project.provider.KanoProjectProviderApplication;
import com.kano.project.provider.dao.UserDaoImpl.RepairInfoDaoImpl;
import dto.RepairInfoReqDTO;
import dto.RepairInfoResDTO;
import objEnum.DepartmentType;
import objEnum.RepairErrorTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanoProjectProviderApplication.class)
public class RepairInfoServiceImplTest {

    @Autowired
    private RepairInfoDaoImpl dao;

    @Test
    public void add(){
        RepairInfoReqDTO reqDTO = new RepairInfoReqDTO();
        reqDTO.setType(RepairErrorTypeEnum.HARDWARE.getCode());
        reqDTO.setDepartmentType(DepartmentType.INPATIENT.getCode());
        reqDTO.setDepartmentName("骨一科");
        reqDTO.setTroubleDesc("打印机故障");
        reqDTO.setDealProcess("https://troubleinf-1301296837.cos.ap-guangzhou.myqcloud.com/infoImage/47de295d-96d4-458c-ad84-0995a043faff.jpg");
        reqDTO.setIsHard(BaseYesOrNoEnum.NO.getCode());
        Boolean b = dao.addRepairInfo(reqDTO);
        System.out.println(b);
    }

    @Test
    public void query(){
        RepairInfoReqDTO reqDTO = new RepairInfoReqDTO();
        reqDTO.setType(RepairErrorTypeEnum.HARDWARE.getCode());
        reqDTO.setDepartmentType(DepartmentType.INPATIENT.getCode());
        reqDTO.setDepartmentName("骨");
        reqDTO.setTroubleDesc("打印机");
        reqDTO.setIsHard(BaseYesOrNoEnum.NO.getCode());
        reqDTO.setPageSize(20L);
        reqDTO.setPage(1L);
        PageResult<RepairInfoResDTO> repairInfoList = dao.getRepairInfoList(reqDTO);
        System.out.println(repairInfoList);
    }

    @Test
    public void update(){
        RepairInfoReqDTO reqDTO = new RepairInfoReqDTO();
        reqDTO.setId(1983442405733363714L);
        reqDTO.setType(RepairErrorTypeEnum.HARDWARE.getCode());
        reqDTO.setDepartmentType(DepartmentType.INPATIENT.getCode());
        reqDTO.setDepartmentName("骨二科");
        reqDTO.setTroubleDesc("打印机故障");
        reqDTO.setDealProcess("https://troubleinf-1301296837.cos.ap-guangzhou.myqcloud.com/infoImage/47de295d-96d4-458c-ad84-0995a043faff.jpg");
        reqDTO.setIsHard(BaseYesOrNoEnum.NO.getCode());
        Boolean b = dao.updateRepairInfo(reqDTO);
        System.out.println(b);
    }


}

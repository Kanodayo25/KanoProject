package com.kano.project.provider.dubboImpl;

import com.kano.project.common.model.PageResult;
import com.kano.project.common.model.Result;
import com.kano.project.provider.dao.RepairInfoDao;
import dto.RepairInfoReqDTO;
import dto.RepairInfoResDTO;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import service.RepairInfoService;

@Service
public class RepairInfoServiceImpl implements RepairInfoService {

    @Autowired
    private RepairInfoDao dao;


    @Override
    public Result<Boolean> addRepairInfo(RepairInfoReqDTO reqDTO) {
        Boolean flag = dao.addRepairInfo(reqDTO);
        return flag ? Result.success(Boolean.TRUE):Result.fail("新增失败，请查看报错日志");
    }

    @Override
    public Result<PageResult<RepairInfoResDTO>> getRepairInfoList(RepairInfoReqDTO reqDTO) {
        return Result.success(dao.getRepairInfoList(reqDTO));
    }

    @Override
    public Result<Boolean> updateRepairInfo(RepairInfoReqDTO reqDTO) {
        Boolean flag = dao.updateRepairInfo(reqDTO);
        return flag ? Result.success(Boolean.TRUE):Result.fail("修改失败，请查看报错日志");
    }

    @Override
    public Result<Boolean> deleteRepairInfo(Long id) {
        Boolean flag = dao.deleteRepairInfo(id);
        return flag ? Result.success(Boolean.TRUE):Result.fail("删除失败，请查看报错日志");
    }
}

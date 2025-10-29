package service;

import com.kano.project.common.model.PageResult;
import com.kano.project.common.model.Result;
import dto.RepairInfoReqDTO;
import dto.RepairInfoResDTO;

public interface RepairInfoService {

    /**
     * 新增
     * @param reqDTO 入参
     * @return 是否
     */
    Result<Boolean> addRepairInfo(RepairInfoReqDTO reqDTO);

    /**
     * 查
     * @param reqDTO 入参
     * @return 分页返回
     */
    Result<PageResult<RepairInfoResDTO>> getRepairInfoList(RepairInfoReqDTO reqDTO);

    /**
     * 改
     * @param reqDTO 入参
     * @return 是否
     */
    Result<Boolean> updateRepairInfo(RepairInfoReqDTO reqDTO);

    /**
     * 删
     * @param id 入参
     * @return 是否
     */
    Result<Boolean> deleteRepairInfo(Long id);
}

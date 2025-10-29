package com.kano.project.provider.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kano.project.common.model.PageResult;
import com.kano.project.common.model.Result;
import com.kano.project.provider.entity.RepairInfo;
import dto.RepairInfoReqDTO;
import dto.RepairInfoResDTO;

/**
 * (RepairInfo)表服务接口
 *
 * @author makejava
 * @since 2025-10-29 14:51:28
 */
public interface RepairInfoDao extends IService<RepairInfo> {

    /**
     * 新增
     * @param reqDTO 入参
     * @return 是否
     */
    Boolean addRepairInfo(RepairInfoReqDTO reqDTO);

    /**
     * 查
     * @param reqDTO 入参
     * @return 分页返回
     */
    PageResult<RepairInfoResDTO> getRepairInfoList(RepairInfoReqDTO reqDTO);

    /**
     * 改
     * @param reqDTO 入参
     * @return 是否
     */
    Boolean updateRepairInfo(RepairInfoReqDTO reqDTO);

    /**
     * 删
     * @param id 入参
     * @return 是否
     */
    Boolean deleteRepairInfo(Long id);

}


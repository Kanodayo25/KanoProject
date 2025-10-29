package com.kano.project.provider.dao.UserDaoImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kano.project.common.model.PageResult;
import com.kano.project.common.utils.DozerUtils;
import com.kano.project.provider.dao.RepairInfoDao;
import com.kano.project.provider.entity.RepairInfo;
import com.kano.project.provider.mapper.RepairInfoMapper;
import dto.RepairInfoReqDTO;
import dto.RepairInfoResDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RepairInfoDaoImpl extends ServiceImpl<RepairInfoMapper, RepairInfo> implements RepairInfoDao {

    @Autowired
    private RepairInfoMapper repairInfoMapper;

    @Override
    public Boolean addRepairInfo(RepairInfoReqDTO reqDTO) {
        RepairInfo repairInfo = DozerUtils.map(reqDTO,RepairInfo.class);
        int insertFlag = repairInfoMapper.insert(repairInfo);
        if(insertFlag <= 0){
            log.error("新增失败，req:"+reqDTO.toString());
            return false;
        }
        return true;
    }

    @Override
    public PageResult<RepairInfoResDTO> getRepairInfoList(RepairInfoReqDTO reqDTO) {
        IPage<RepairInfo> page = new Page<>(reqDTO.getPage(),reqDTO.getPageSize());
        //构建lambda查询
        LambdaQueryWrapper<RepairInfo> queryWrapper = BuildQueryWrapper(reqDTO);
        IPage<RepairInfo> pageResInfo = repairInfoMapper.selectPage(page, queryWrapper);
        return
                new PageResult<>(pageResInfo.getCurrent(),pageResInfo.getSize(),
                        pageResInfo.getTotal(),
                        DozerUtils.mapList(pageResInfo.getRecords(),RepairInfoResDTO.class));
    }

    /**
     * 构建查询参数
     * @param reqDTO
     * @return
     */
    private LambdaQueryWrapper<RepairInfo> BuildQueryWrapper(RepairInfoReqDTO reqDTO) {
        LambdaQueryWrapper<RepairInfo> queryWrapper = new LambdaQueryWrapper<RepairInfo>();
        //错误类型
        if(ObjectUtils.isNotEmpty(reqDTO.getType())){
            queryWrapper.eq(RepairInfo::getType,reqDTO.getType());
        }
        //科室类型
        if (ObjectUtils.isNotEmpty(reqDTO.getDepartmentType())){
            queryWrapper.eq(RepairInfo::getDepartmentType,reqDTO.getDepartmentType());
        }
        //科室名称模糊
        if(ObjectUtils.isNotEmpty(reqDTO.getDepartmentName())){
            queryWrapper.like(RepairInfo::getDepartmentName,reqDTO.getDepartmentName());
        }
        //错误描述模糊
        if(ObjectUtils.isNotEmpty(reqDTO.getTroubleDesc())){
            queryWrapper.like(RepairInfo::getTroubleDesc,reqDTO.getTroubleDesc());
        }
        //是否疑难
        if(ObjectUtils.isNotEmpty(reqDTO.getIsHard())){
            queryWrapper.eq(RepairInfo::getIsHard,reqDTO.getIsHard());
        }
        return queryWrapper;
    }

    @Override
    public Boolean updateRepairInfo(RepairInfoReqDTO reqDTO) {
        RepairInfo repairInfo = DozerUtils.map(reqDTO,RepairInfo.class);
        repairInfo.setUpdatedTime(new Date());
        int updateFlag = repairInfoMapper.updateById(repairInfo);
        if(updateFlag <= 0){
            log.error("修改失败，req:"+reqDTO.toString());
            return false;
        }
        return true;
    }

    @Override
    public Boolean deleteRepairInfo(Long id) {
        int deleteFlag = repairInfoMapper.deleteById(id);
        if(deleteFlag == 0){
            log.error("记录不存在,id:"+id);
            return false;
        }
        return true;
    }
}

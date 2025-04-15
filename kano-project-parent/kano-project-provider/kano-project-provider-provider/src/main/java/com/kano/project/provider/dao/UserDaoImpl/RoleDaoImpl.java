package com.kano.project.provider.dao.UserDaoImpl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kano.project.provider.dao.RoleDao;
import com.kano.project.provider.entity.Role;
import com.kano.project.provider.mapper.RoleMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleDaoImpl extends ServiceImpl<RoleMapper, Role> implements RoleDao {
}

package com.kano.project.provider.dubboImpl;

import cn.dev33.satoken.stp.StpInterface;
import org.apache.dubbo.config.annotation.Service;
import service.RoleService;

import java.util.Collections;
import java.util.List;

/**
 * 系统菜单权限表(Role)表服务实现类
 *
 * @author makejava
 * @since 2025-04-15 16:09:43
 */
@Service
public class RoleServiceImpl implements RoleService, StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return Collections.emptyList();
    }
}


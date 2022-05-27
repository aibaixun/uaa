package com.aibaixun.uaa.service.impl;

import com.aibaixun.uaa.entity.RolePermission;
import com.aibaixun.uaa.mapper.RolePermissionMapper;
import com.aibaixun.uaa.service.IRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

    @Override
    public List<RolePermission> list(List<String> roleIds) {
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(RolePermission::getRoleId,roleIds);
        return list(queryWrapper);
    }

    @Override
    public Boolean delByRoleIds(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)){
            return true;
        }
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermission::getRoleId,roleIds);
        remove(queryWrapper);
        return true;
    }

    @Override
    public Boolean delByPermissionIds(List<String> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)){
            return true;
        }
        LambdaQueryWrapper<RolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermission::getPermissionId,permissionIds);
        remove(queryWrapper);
        return true;
    }
}

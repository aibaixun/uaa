package com.aibaixun.gail.service.impl;

import com.aibaixun.gail.entity.Permission;
import com.aibaixun.gail.entity.RolePermission;
import com.aibaixun.gail.mapper.PermissionMapper;
import com.aibaixun.gail.service.IPermissionService;
import com.aibaixun.gail.service.IRolePermissionService;
import com.aibaixun.gail.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Autowired
    private IRolePermissionService rolePermissionService;

    @Override
    public List<Permission> listByRoleIds(List<String> roleIds) {
        List<Permission> result = new ArrayList<>();
        List<RolePermission> rolePermissions = rolePermissionService.list(roleIds);
        if (!CollectionUtils.isEmpty(rolePermissions)) {
            List<String> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
            result = listByIds(permissionIds);
        }
        return result;
        //return baseMapper.selectPermissionByRoleIds(roleIds);

    }
}

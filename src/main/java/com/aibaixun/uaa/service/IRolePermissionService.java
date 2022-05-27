package com.aibaixun.uaa.service;

import com.aibaixun.uaa.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
public interface IRolePermissionService extends IService<RolePermission> {
    List<RolePermission> list(List<String> roleIds);

    Boolean delByRoleIds(List<String> roleIds);

    Boolean delByPermissionIds(List<String> permissionIds);
}

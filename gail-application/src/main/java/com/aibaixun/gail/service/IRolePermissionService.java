package com.aibaixun.gail.service;

import com.aibaixun.gail.entity.RolePermission;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
}

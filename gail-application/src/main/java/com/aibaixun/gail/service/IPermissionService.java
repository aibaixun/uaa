package com.aibaixun.gail.service;

import com.aibaixun.gail.entity.Permission;
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
public interface IPermissionService extends IService<Permission> {
    List<Permission> listByRoleIds(List<String> roleIds);

}

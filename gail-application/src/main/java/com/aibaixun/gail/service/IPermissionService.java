package com.aibaixun.gail.service;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.gail.entity.App;
import com.aibaixun.gail.entity.Permission;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    IPage<Permission> page(Integer page, Integer pageSize, String name, String appId);

    List<Permission> list(String name, String appId);

    Boolean checkName(String id, String name);

    Boolean checkSave(Permission permission) throws BaseException;

    Boolean checkUpdate(Permission permission) throws BaseException;

    Boolean del(List<String> permissionIds);

    Permission info(String id);
}

package com.aibaixun.uaa.service;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.uaa.entity.Role;
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
public interface IRoleService extends IService<Role> {
    List<Role> listRoleByUserGroup(String groupId);

    IPage<Role> page(Integer page, Integer pageSize, String appId, String name, String currentTenantId);

    List<Role> list(String name, String tenantId);

    Boolean checkName(String id, String name, String currentTenantId);

    Boolean checkUpdate(Role role) throws BaseException;

    Boolean checkSave(Role role) throws BaseException;

    Boolean del(List<String> roleIds);

    Role info(String id);
}

package com.aibaixun.gail.service;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.gail.entity.UserGroup;
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
public interface IUserGroupService extends IService<UserGroup> {

    IPage<UserGroup> page(Integer page, Integer pageSize, String name, String tenantId);
    
    List<UserGroup> list(String name, String tenantId);

    Boolean checkSave(UserGroup group) throws BaseException;

    Boolean del(List<String> groupIds);

    UserGroup info(String id);

    Boolean checkUpdate(UserGroup group) throws BaseException;

    Boolean checkName(String id, String name, String currentTenantId);
}

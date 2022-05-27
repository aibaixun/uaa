package com.aibaixun.uaa.service;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.uaa.entity.User;
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
public interface IUserService extends IService<User> {

    IPage<User> page(Integer page, Integer pageSize, String groupId, String name, String tenantId);

    List<User> list(String name, String currentTenantId);

    Boolean checkName(String id, String name, String currentTenantId);

    Boolean checkSave(User user) throws BaseException;

    Boolean checkUpdate(User user) throws BaseException;

    User info(String id);

    Boolean del(List<String> userIds);
}

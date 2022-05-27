package com.aibaixun.uaa.service;

import com.aibaixun.uaa.entity.AuthLog;
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
public interface IAuthLogService extends IService<AuthLog> {

    Boolean delByUserIds(List<String> userIds);

    IPage<AuthLog> page(Integer page, Integer pageSize, String name, String tenantId);

    List<AuthLog> list(String name, String tenantId);

    Boolean checkSave(AuthLog authLog);

    Boolean checkUpdate(AuthLog authLog);

    Boolean del(List<String> authLogIds);

    AuthLog info(String id);
}

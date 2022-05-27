package com.aibaixun.uaa.service;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.uaa.entity.Tenant;
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
public interface ITenantService extends IService<Tenant> {

    List<String> appIdsBytenantId(String tenantId);

    IPage<Tenant> page(Integer page, Integer pageSize, String name);

    List<Tenant> list(String name);

    Boolean checkName(String id, String name);

    Boolean checkSave(Tenant tenant) throws BaseException;

    Boolean checkUpdate(Tenant tenant) throws BaseException;

    Boolean del(List<String> tenantIds);

    Tenant info(String id);
}

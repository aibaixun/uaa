package com.aibaixun.uaa.service;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.uaa.entity.App;
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
public interface IAppService extends IService<App> {

    IPage<App> page(Integer page, Integer pageSize, String name);

    List<App> list(String name,String tenantId);

    Boolean checkName(String id, String name);

    Boolean checkSave(App app) throws BaseException;

    Boolean checkUpdate(App app) throws BaseException;

    Boolean del(List<String> appIds);

    App info(String id);
}

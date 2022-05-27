package com.aibaixun.uaa.service.impl;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.uaa.entity.App;
import com.aibaixun.uaa.mapper.AppMapper;
import com.aibaixun.uaa.service.IAppService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

    @Override
    public IPage<App> page(Integer page, Integer pageSize, String name) {
        Page<App> pageOf = Page.of(page, pageSize);
        LambdaQueryWrapper<App> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight(App::getName,name);
        }
        queryWrapper.orderByDesc(App::getCreateTime);
        return page(pageOf,queryWrapper);
    }

    @Override
    public List<App> list(String name,String tenantId) {
        LambdaQueryWrapper<App> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight(App::getName,name);
        }
        if (!StringUtils.isEmpty(tenantId)){
            //todo 租户授权的应用
        }
        queryWrapper.orderByDesc(App::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public Boolean checkName(String id, String name) {
        LambdaQueryWrapper<App> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(id)){
            queryWrapper.ne(App::getId, id);
        }
        queryWrapper.eq(App::getName,name);
        queryWrapper.orderByDesc(App::getCreateTime);
        App app = getOne(queryWrapper);
        if (app==null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean checkSave(App app) throws BaseException {
        checkParam(app);
        save(app);
        return true;
    }

    @Override
    public Boolean checkUpdate(App app) throws BaseException {
        checkParam(app);
        saveOrUpdate(app);
        return true;
    }

    @Override
    public Boolean del(List<String> appIds) {
        //todo 删除app与租户关联

        removeByIds(appIds);
        return true;
    }

    @Override
    public App info(String id) {
        return getById(id);
    }

    private Boolean checkParam(App app) throws BaseException {
        if (StringUtils.isEmpty(app.getName())){
            if (StringUtils.isEmpty(app.getName())){
                throw new BaseException("应用名称为空", BaseResultCode.BAD_PARAMS);
            }
        }
        //重名校验
        if (!checkName(app.getId(),app.getName())){
            throw new BaseException("应用名称已经存在", BaseResultCode.BAD_PARAMS);
        }
        return true;
    }
}

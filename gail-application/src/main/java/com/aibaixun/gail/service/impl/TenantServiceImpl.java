package com.aibaixun.gail.service.impl;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.gail.entity.Tenant;
import com.aibaixun.gail.mapper.TenantMapper;
import com.aibaixun.gail.service.ITenantService;
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
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

    @Override
    public List<String> appIdsBytenantId(String tenantId) {
        //todo
        return null;
    }

    @Override
    public IPage<Tenant> page(Integer page, Integer pageSize, String name) {
        Page<Tenant> pageOf = Page.of(page, pageSize);
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight(Tenant::getNickname,name);
        }
        queryWrapper.orderByDesc(Tenant::getCreateTime);
        return page(pageOf,queryWrapper);
    }

    @Override
    public List<Tenant> list(String name) {
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight(Tenant::getNickname,name);
        }
        queryWrapper.orderByDesc(Tenant::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public Boolean checkName(String id, String name) {
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(id)){
            queryWrapper.ne(Tenant::getId, id);
        }
        queryWrapper.eq(Tenant::getNickname,name);
        Tenant tenant = getOne(queryWrapper);
        if (tenant==null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean checkSave(Tenant tenant) throws BaseException {
        checkParam(tenant);
        // todo 初始化租户相关信息
        save(tenant);
        return true;
    }

    @Override
    public Boolean checkUpdate(Tenant tenant) throws BaseException {
        checkParam(tenant);
        // todo 初始化租户相关信息
        saveOrUpdate(tenant);
        return true;
    }

    @Override
    public Boolean del(List<String> tenantIds) {
        //todo 删除校验

        //todo 删除关联信息

        removeByIds(tenantIds);
        return true;
    }

    @Override
    public Tenant info(String id) {
        return getById(id);
    }

    private void checkParam(Tenant tenant) throws BaseException {
        if (StringUtils.isEmpty(tenant.getNickname())){
            throw new BaseException("租户名称为空", BaseResultCode.BAD_PARAMS);
        }
        //重名校验
        if (!checkName(tenant.getId(),tenant.getNickname())){
            throw new BaseException("租户名称已经存在", BaseResultCode.BAD_PARAMS);
        }
    }
}

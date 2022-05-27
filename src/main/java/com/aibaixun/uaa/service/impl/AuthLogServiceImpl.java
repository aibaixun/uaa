package com.aibaixun.uaa.service.impl;

import com.aibaixun.uaa.entity.AuthLog;
import com.aibaixun.uaa.mapper.AuthLogMapper;
import com.aibaixun.uaa.service.IAuthLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class AuthLogServiceImpl extends ServiceImpl<AuthLogMapper, AuthLog> implements IAuthLogService {

    @Override
    public Boolean delByUserIds(List<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)){
            return true;
        }
        LambdaQueryWrapper<AuthLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AuthLog::getUserId,userIds);
        remove(queryWrapper);
        return true;
    }

    @Override
    public IPage<AuthLog> page(Integer page, Integer pageSize, String name, String tenantId) {
        IPage<AuthLog> pageData = baseMapper.page(name, tenantId, Page.of(page, pageSize));
        return pageData;
    }

    @Override
    public List<AuthLog> list(String name, String tenantId) {
        List<AuthLog> listData = baseMapper.list(name, tenantId);
        return listData;
    }

    @Override
    public Boolean checkSave(AuthLog authLog) {
        return save(authLog);
    }

    @Override
    public Boolean checkUpdate(AuthLog authLog) {
        return saveOrUpdate(authLog);
    }

    @Override
    public Boolean del(List<String> authLogIds) {
        return removeByIds(authLogIds);
    }

    @Override
    public AuthLog info(String id) {
        return getById(id);
    }
}

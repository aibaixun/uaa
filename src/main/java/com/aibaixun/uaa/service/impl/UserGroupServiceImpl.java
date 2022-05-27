package com.aibaixun.uaa.service.impl;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.uaa.entity.Role;
import com.aibaixun.uaa.entity.UserGroup;
import com.aibaixun.uaa.mapper.UserGroupMapper;
import com.aibaixun.uaa.service.IUserGroupRoleService;
import com.aibaixun.uaa.service.IUserGroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements IUserGroupService {
    @Autowired
    private IUserGroupRoleService groupRoleService;

    @Override
    public IPage<UserGroup> page(Integer page, Integer pageSize, String name, String tenantId) {
        Page<UserGroup> pagev = Page.of(page,pageSize);
        LambdaQueryWrapper<UserGroup> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight(UserGroup::getName,name);
        }
        if (!StringUtils.isEmpty(tenantId)){
            queryWrapper.likeRight(UserGroup::getTenantId,tenantId);
        }
        queryWrapper.orderByDesc(UserGroup::getCreateTime);
        return page(pagev,queryWrapper);
    }

    @Override
    public List<UserGroup> list(String name, String tenantId) {
        LambdaQueryWrapper<UserGroup> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight(UserGroup::getName,name);
        }
        if (!StringUtils.isEmpty(tenantId)){
            queryWrapper.likeRight(UserGroup::getTenantId,tenantId);
        }
        queryWrapper.orderByDesc(UserGroup::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public Boolean checkSave(UserGroup group) throws BaseException {
        //校验参数
        checkParam(group);
        //保存用户组与角色关联关系
        groupRoleService.saveByGroupId(group.getId(),group.getRoles().stream().map(Role::getId).collect(Collectors.toSet()));
        //保存用户组信息
        save(group);
        return true;
    }

    @Override
    public Boolean del(List<String> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)){
            return true;
        }
        //删除角色关系
        groupRoleService.delByGroupIds(groupIds);
        removeByIds(groupIds);
        return true;
    }

    @Override
    public UserGroup info(String id) {
        return getById(id);
    }

    @Override
    @Transactional
    public Boolean checkUpdate(UserGroup group) throws BaseException {
        //校验参数
        checkParam(group);
        //保存用户组与角色关联关系
        groupRoleService.updateByGroupId(group.getId(),group.getRoles().stream().map(Role::getId).collect(Collectors.toSet()));
        //保存用户组信息
        saveOrUpdate(group);
        return true;
    }

    @Override
    public Boolean checkName(String id, String name, String currentTenantId) {
        LambdaQueryWrapper<UserGroup> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(id)){
            queryWrapper.ne(UserGroup::getId, id);
        }
        queryWrapper.eq(UserGroup::getName,name);

        queryWrapper.eq(UserGroup::getTenantId, currentTenantId);

        queryWrapper.orderByDesc(UserGroup::getCreateTime);
        UserGroup group = getOne(queryWrapper);
        if (group==null){
            return true;
        }else {
            return false;
        }
    }

    private void checkParam(UserGroup group) throws BaseException {
        if (StringUtils.isEmpty(group.getName())){
            throw new BaseException("用户组名称为空", BaseResultCode.BAD_PARAMS);
        }
        if (CollectionUtils.isEmpty(group.getRoles())){
            throw new BaseException("关联角色为空", BaseResultCode.BAD_PARAMS);
        }
        //重名校验
        if (!checkName(group.getId(),group.getName(),group.getTenantId())){
            throw new BaseException("用户组名称已经存在", BaseResultCode.BAD_PARAMS);
        }
    }
}

package com.aibaixun.gail.service.impl;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.gail.entity.App;
import com.aibaixun.gail.entity.Role;
import com.aibaixun.gail.entity.User;
import com.aibaixun.gail.entity.UserGroup;
import com.aibaixun.gail.mapper.RoleMapper;
import com.aibaixun.gail.service.IAppService;
import com.aibaixun.gail.service.IRolePermissionService;
import com.aibaixun.gail.service.IRoleService;
import com.aibaixun.gail.service.IUserGroupRoleService;
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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private IAppService appService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IUserGroupRoleService groupRoleService;

    @Override
    public List<Role> listRoleByUserGroup(String groupId) {
        return baseMapper.selectRoleByUserGroupId(groupId);
    }

    @Override
    public IPage<Role> page(Integer page, Integer pageSize, String appId, String name, String tenantId) {
        return baseMapper.page(appId, name, tenantId, Page.of(page, pageSize));
    }

    @Override
    public List<Role> list(String name, String tenantId) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight(Role::getName, name);
        }
        if (!StringUtils.isEmpty(tenantId)){
            queryWrapper.likeRight(Role::getTenantId,tenantId);
        }
        queryWrapper.orderByDesc(Role::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public Boolean checkName(String id, String name, String currentTenantId) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(id)){
            queryWrapper.ne(Role::getId, id);
        }
        queryWrapper.eq(Role::getName,name);

        queryWrapper.eq(Role::getTenantId, currentTenantId);
        Role role = getOne(queryWrapper);
        if (role==null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean checkUpdate(Role role) throws BaseException {
        checkParam(role);
        save(role);
        return true;
    }

    @Override
    public Boolean checkSave(Role role) throws BaseException {
        checkParam(role);
        save(role);
        return true;
    }

    @Override
    @Transactional
    public Boolean del(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)){
            return true;
        }
        //删除删除角色与用户组关系
        rolePermissionService.delByRoleIds(roleIds);
        //删除角色与权限关系
        groupRoleService.delByRoleIds(roleIds);
        //删除角色本身
        removeByIds(roleIds);
        return true;
    }

    @Override
    public Role info(String id) {
        Role role = getById(id);
        if (role!=null&&role.getAppId()!=null){

            App app = appService.getById(role.getAppId());
            if (app!=null){
                role.setAppName(app.getName());
            }
        }
        return role;
    }

    private void checkParam(Role role) throws BaseException {
        if (StringUtils.isEmpty(role.getName())){
            throw new BaseException("角色名称为空", BaseResultCode.BAD_PARAMS);
        }
        if (StringUtils.isEmpty(role.getAppId())){
            throw new BaseException("角色所属应用为空", BaseResultCode.BAD_PARAMS);
        }
        //重名校验
        if (!checkName(role.getId(),role.getName(),role.getTenantId())){
            throw new BaseException("角色名称已经存在", BaseResultCode.BAD_PARAMS);
        }
    }
}

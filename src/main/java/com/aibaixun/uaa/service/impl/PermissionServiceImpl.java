package com.aibaixun.uaa.service.impl;

import com.aibaixun.basic.exception.BaseException;
import com.aibaixun.basic.result.BaseResultCode;
import com.aibaixun.uaa.entity.Permission;
import com.aibaixun.uaa.mapper.PermissionMapper;
import com.aibaixun.uaa.service.IPermissionService;
import com.aibaixun.uaa.service.IRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Autowired
    private IRolePermissionService rolePermissionService;

    @Override
    public List<Permission> listByRoleIds(List<String> roleIds) {
        /*List<Permission> result = new ArrayList<>();
        List<RolePermission> rolePermissions = rolePermissionService.list(roleIds);
        if (!CollectionUtils.isEmpty(rolePermissions)) {
            List<String> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
            result = listByIds(permissionIds);
        }
        return result;*/
        return baseMapper.selectPermissionByRoleIds(roleIds);

    }

    @Override
    public IPage<Permission> page(Integer page, Integer pageSize, String name, String appId) {
        return baseMapper.page(name,appId, Page.of(page,pageSize));
    }

    @Override
    public List<Permission> list(String name, String appId) {
        return baseMapper.list(name, appId);
    }

    @Override
    public Boolean checkName(String id, String name) {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper();
        if (!StringUtils.isEmpty(id)){
            queryWrapper.ne(Permission::getId, id);
        }
        queryWrapper.eq(Permission::getName,name);
        Permission permission = getOne(queryWrapper);
        if (permission==null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean checkSave(Permission permission) throws BaseException {
        checkParam(permission);
        save(permission);
        return true;
    }

    @Override
    public Boolean checkUpdate(Permission permission) throws BaseException {
        checkParam(permission);
        saveOrUpdate(permission);
        return true;
    }

    @Override
    public Boolean del(List<String> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)){
            return true;
        }
        //删除权限与角色关系
        rolePermissionService.delByPermissionIds(permissionIds);

        removeByIds(permissionIds);
        return true;
    }

    @Override
    public Permission info(String id) {
        return getById(id);
    }

    private void checkParam(Permission permission) throws BaseException {
        if (StringUtils.isEmpty(permission.getName())){
            throw new BaseException("资源名称为空", BaseResultCode.BAD_PARAMS);
        }
        if (StringUtils.isEmpty(permission.getAppId())){
            throw new BaseException("资源所属应用为空", BaseResultCode.BAD_PARAMS);
        }
        if (StringUtils.isEmpty(permission.getResource())){
            throw new BaseException("数据资源不能为空", BaseResultCode.BAD_PARAMS);
        }
        if (StringUtils.isEmpty(permission.getUrl())){
            throw new BaseException("资源地址不能为空", BaseResultCode.BAD_PARAMS);
        }
        if (StringUtils.isEmpty(permission.getView())){
            throw new BaseException("资源路由不能为空", BaseResultCode.BAD_PARAMS);
        }
        //重名校验
        if (!checkName(permission.getId(),permission.getName())){
            throw new BaseException("资源名称已经存在", BaseResultCode.BAD_PARAMS);
        }
    }
}

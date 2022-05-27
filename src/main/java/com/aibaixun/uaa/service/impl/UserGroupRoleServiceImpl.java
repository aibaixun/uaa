package com.aibaixun.uaa.service.impl;

import com.aibaixun.uaa.entity.UserGroupRole;
import com.aibaixun.uaa.mapper.UserGroupRoleMapper;
import com.aibaixun.uaa.service.IUserGroupRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Service
public class UserGroupRoleServiceImpl extends ServiceImpl<UserGroupRoleMapper, UserGroupRole> implements IUserGroupRoleService {

    @Override
    @Transactional
    public Boolean updateByGroupId(String groupId, Set<String> roleIds) {
        //查询已经存在的
        LambdaQueryWrapper<UserGroupRole> query = new LambdaQueryWrapper<>();
        query.eq(UserGroupRole::getUserGroupId, groupId);
        query.in(UserGroupRole::getRoleId, roleIds);
        List<UserGroupRole> existData = list(query);
        //删除没有的关系
        if (existData != null) {
            List<String> delIds = existData.stream().filter(userGroupRole -> {
                return !roleIds.contains(userGroupRole.getRoleId());
            }).map(UserGroupRole::getId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(delIds)) {
                removeByIds(delIds);
            }
        }

        //添加
        List<String> saveRoleIds = null;
        if (existData != null) {
            Set<String> exisRoleIds = existData.stream().map(UserGroupRole::getRoleId).collect(Collectors.toSet());
            saveRoleIds = roleIds.stream().filter(id -> {
                return !exisRoleIds.contains(id);
            }).collect(Collectors.toList());

        }
        if (!CollectionUtils.isEmpty(saveRoleIds)){
            List<UserGroupRole> saves =  new ArrayList<>();
            for (int i = 0; i < saveRoleIds.size(); i++) {
                saves.add(new UserGroupRole(groupId, saveRoleIds.get(i)));
            }
            if (saves.size()>0){
                saveBatch(saves);
            }
        }
        return true;
    }

    @Override
    public Boolean saveByGroupId(String groupId, Set<String> roleIds) {
        List<String> roleIdArr = roleIds.stream().collect(Collectors.toList());
        List<UserGroupRole> saves =  new ArrayList<>();
        for (int i = 0; i < roleIdArr.size(); i++) {
            saves.add(new UserGroupRole(groupId, roleIdArr.get(i)));
        }
        if (saves.size()>0){
            saveBatch(saves);
        }
        return true;
    }

    @Override
    public Boolean delByGroupIds(List<String> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)){
            return true;
        }
        LambdaQueryWrapper<UserGroupRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserGroupRole::getUserGroupId,groupIds);
        remove(queryWrapper);
        return true;
    }

    @Override
    public Boolean delByRoleIds(List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)){
            return true;
        }
        LambdaQueryWrapper<UserGroupRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserGroupRole::getRoleId,roleIds);
        remove(queryWrapper);
        return true;
    }
}

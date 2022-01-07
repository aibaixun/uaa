package com.aibaixun.gail.service.impl;

import com.aibaixun.gail.entity.Role;
import com.aibaixun.gail.mapper.RoleMapper;
import com.aibaixun.gail.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public List<Role> listRoleByUserGroup(String groupId) {
        return baseMapper.selectRoleByUserGroupId(groupId);
    }
}

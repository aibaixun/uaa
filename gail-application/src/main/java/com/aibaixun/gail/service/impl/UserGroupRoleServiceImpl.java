package com.aibaixun.gail.service.impl;

import com.aibaixun.gail.entity.Role;
import com.aibaixun.gail.entity.UserGroupRole;
import com.aibaixun.gail.mapper.UserGroupRoleMapper;
import com.aibaixun.gail.service.IUserGroupRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class UserGroupRoleServiceImpl extends ServiceImpl<UserGroupRoleMapper, UserGroupRole> implements IUserGroupRoleService {

}

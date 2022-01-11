package com.aibaixun.gail.service;

import com.aibaixun.gail.entity.Role;
import com.aibaixun.gail.entity.UserGroupRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
public interface IUserGroupRoleService extends IService<UserGroupRole> {
    Boolean updateByGroupId(String groupId, Set<String> roleIds);
    Boolean saveByGroupId(String groupId, Set<String> roleIds);

    Boolean delByGroupIds(List<String> groupIds);

    Boolean delByRoleIds(List<String> roleIds);
}

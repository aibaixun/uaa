package com.aibaixun.gail.mapper;

import com.aibaixun.gail.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> selectRoleByUserGroupId(Long userGroupId);
}

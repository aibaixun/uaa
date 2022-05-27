package com.aibaixun.uaa.mapper;

import com.aibaixun.uaa.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

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
    List<Role> selectRoleByUserGroupId(String userGroupId);

    IPage<Role> page(@Param("appId") String appId, @Param("name") String name, @Param("tenantId") String tenantId, @Param("page") Page page);
}

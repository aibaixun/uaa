package com.aibaixun.gail.mapper;

import com.aibaixun.gail.entity.Permission;
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
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> selectPermissionByRoleIds(@Param(value = "roleIds") List<String> roleIds);
    IPage<Permission> page(@Param("name") String name, @Param("appId") String appId, @Param("page") Page page);
    List<Permission> list(@Param("name") String name, @Param("appId") String appId);
}

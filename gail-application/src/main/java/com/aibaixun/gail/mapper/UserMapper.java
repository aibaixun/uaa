package com.aibaixun.gail.mapper;

import com.aibaixun.gail.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
public interface UserMapper extends BaseMapper<User> {
    IPage<User> page(@Param("name") String name, @Param("groupId") String groupId, @Param("tenantId") String tenantId, @Param("page") Page page);
}

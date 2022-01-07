package com.aibaixun.gail.service.impl;

import com.aibaixun.gail.entity.AuthLog;
import com.aibaixun.gail.mapper.AuthLogMapper;
import com.aibaixun.gail.service.IAuthLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjhuang
 * @since 2022-01-06
 */
@Service
public class AuthLogServiceImpl extends ServiceImpl<AuthLogMapper, AuthLog> implements IAuthLogService {

}

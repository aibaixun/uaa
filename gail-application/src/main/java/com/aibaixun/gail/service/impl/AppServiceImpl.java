package com.aibaixun.gail.service.impl;

import com.aibaixun.gail.entity.App;
import com.aibaixun.gail.mapper.AppMapper;
import com.aibaixun.gail.service.IAppService;
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
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

}

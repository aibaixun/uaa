package com.aibaixun.gail.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.aibaixun.gail.mapper")
public class MybatisPlusConfig {
}

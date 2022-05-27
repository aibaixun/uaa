package com.aibaixun.uaa.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.aibaixun.uaa.mapper")
public class MybatisPlusConfig {
}

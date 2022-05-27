package com.aibaixun.uaa.support;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class BaseEntityMetaObjectHandler implements MetaObjectHandler {
    private UserHolder userHolder;

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            this.strictInsertFill(metaObject, "createTime", Long.class, Instant.now().toEpochMilli());
            this.strictInsertFill(metaObject, "createBy", String.class, userHolder.getCurrentUserId());
        }catch (RuntimeException e){}
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            this.strictInsertFill(metaObject, "updateTime", Long.class, Instant.now().toEpochMilli());
            this.strictInsertFill(metaObject, "updateBy", String.class, userHolder.getCurrentUserId());
        }catch (RuntimeException e){}
    }

    @Autowired
    public void setUserHolder(UserHolder userHolder) {
        this.userHolder = userHolder;
    }
}

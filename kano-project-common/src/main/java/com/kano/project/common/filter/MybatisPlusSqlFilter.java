package com.kano.project.common.filter;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatisPlus 增改操作，时间相关内容注入
 */
@Slf4j
@Component
public class MybatisPlusSqlFilter implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入填充......");
        this.strictInsertFill(metaObject,"createdTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject,"updatedTime",LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("修改填充......");
        this.strictUpdateFill(metaObject,"updatedTime",LocalDateTime.class, LocalDateTime.now());
    }
}

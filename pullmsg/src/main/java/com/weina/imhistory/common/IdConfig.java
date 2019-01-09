package com.weina.imhistory.common;

import com.sohu.idcenter.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: mayc
 * @Date: 2019/01/09 14:13
 */
@Configuration
public class IdConfig {

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }
}

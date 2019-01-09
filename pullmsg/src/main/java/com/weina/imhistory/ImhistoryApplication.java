package com.weina.imhistory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Description:
 * @Author: mayc
 * @Date: 2019/01/09 11:38
 */
@SpringBootApplication
@EnableTransactionManagement//开启事务管理
@MapperScan("com.weina.*.mapper")
@EnableScheduling
public class ImhistoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImhistoryApplication.class, args);
    }
}

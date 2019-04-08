package com.zq;

import com.zq.shop.web.common.SocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement//开启事务
@MapperScan(value = "com.zq.shop.web.mappers")//mybatis扫描mapper接口
public class ExampleApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(ExampleApplication.class, args);
        context.getBean(SocketServer.class).start();
    }

}

package com.sakura.xdvideo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@MapperScan("com.sakura.xdvideo.mapper")
@EnableTransactionManagement
public class XdvideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(XdvideoApplication.class, args);
    }

}

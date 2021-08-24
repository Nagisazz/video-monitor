package com.nagisazz.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @auther zhushengzhe
 * @date 2021/08/23 14:48
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableScheduling
public class VideoMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoMonitorApplication.class,args);
    }
}

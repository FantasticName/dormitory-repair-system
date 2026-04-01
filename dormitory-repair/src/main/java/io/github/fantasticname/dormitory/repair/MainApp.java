package io.github.fantasticname.dormitory.repair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 宿舍报修系统应用主类
 * 
 * @author FantasticName
 */
@SpringBootApplication
@MapperScan("io.github.fantasticname.dormitory.repair.mapper")
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }

}

package cn.sichu.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.sichu.mybatis.plus.mapper")
public class MybatisPlusIiDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusIiDatasourceApplication.class, args);
    }

}

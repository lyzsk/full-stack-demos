package cn.sichu.mybatis.plus;

import cn.sichu.mybatis.plus.service.ProductService;
import cn.sichu.mybatis.plus.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MybatisPlusIiDatasourceApplicationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @Test
    public void test() {
        System.out.println(userService.getById(1));
        System.out.println(productService.getById(1));
    }

    @Test
    void contextLoads() {
    }
}

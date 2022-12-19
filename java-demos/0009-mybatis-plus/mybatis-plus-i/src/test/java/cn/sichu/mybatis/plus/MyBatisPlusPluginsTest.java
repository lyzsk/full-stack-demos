package cn.sichu.mybatis.plus;

import cn.sichu.mybatis.plus.mapper.ProductMapper;
import cn.sichu.mybatis.plus.mapper.UserMapper;
import cn.sichu.mybatis.plus.pojo.Product;
import cn.sichu.mybatis.plus.pojo.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyBatisPlusPluginsTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testPagination1() {
        // ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 LIMIT ?,?
        // ==> Parameters: 3(Long), 3(Long)
        Page<User> page = new Page<>(2, 3);
        userMapper.selectPage(page, null);
        System.out.println(page);
    }

    @Test
    public void testPageination2() {
        Page<User> page = new Page<>(2, 3);
        userMapper.selectPage(page, null);
        System.out.println(page.getRecords());
        System.out.println(page.getPages());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    @Test
    public void testPageVo() {
        // ==>  Preparing: SELECT COUNT(*) AS total FROM t_user WHERE age > ?
        // ==>  Preparing: select uid,user_name,age,email from t_user where age>? LIMIT ?
        Page<User> page = new Page<>();
        userMapper.selectPageVo(page, 20);
        System.out.println(page);
    }

    @Test
    public void testProduct01() {
        Product productLi = productMapper.selectById(1);
        // 小李查询的商品价格: 100
        // 使用乐观锁插件后: 小王查询的商品价格: 100
        System.out.println("小李查询的商品价格: " + productLi.getPrice());
        Product productWang = productMapper.selectById(1);
        // 小王查询的商品价格: 100
        // 使用乐观锁插件后: 小王查询的商品价格: 100
        System.out.println("小王查询的商品价格: " + productWang.getPrice());
        productLi.setPrice(productLi.getPrice() + 50);
        productMapper.updateById(productLi);
        productWang.setPrice(productWang.getPrice() - 30);
        // productMapper.updateById(productWang);
        int result = productMapper.updateById(productWang);
        if (result == 0) {
            // 操作失败, 重试
            Product productNew = productMapper.selectById(1);
            productNew.setPrice(productNew.getPrice() - 30);
            productMapper.updateById(productNew);
        }

        Product productBoss = productMapper.selectById(1);
        // 老板查询的商品价格: 70
        // 使用乐观锁插件后: 老板查询的商品价格: 150
        // 原因是:
        // 小李的操作:
        // ==>  Preparing: UPDATE t_product SET name=?, price=?, version=? WHERE id=? AND version=?
        // ==> Parameters: product1(String), 150(Integer), 2(Integer), 1(Long), 1(Integer)
        // <==    Updates: 1
        // 小王的操作:
        // ==>  Preparing: UPDATE t_product SET name=?, price=?, version=? WHERE id=? AND version=?
        // ==> Parameters: product1(String), 70(Integer), 2(Integer), 1(Long), 1(Integer)
        // <==    Updates: 0
        // 小王的操作操作时数据库的version+1了
        // 增加if判断小王操作后: 老板查询的商品价格: 120
        System.out.println("老板查询的商品价格: " + productBoss.getPrice());
    }
}
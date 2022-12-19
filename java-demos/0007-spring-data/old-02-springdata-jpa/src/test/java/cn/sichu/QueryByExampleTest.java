package cn.sichu;

import cn.sichu.config.SpringDataJPAConfig;
import cn.sichu.pojo.Customer;
import cn.sichu.repositories.CustomerQueryByExampleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

/**
 * @author sichu
 * @date 2022/12/10
 **/
@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryByExampleTest {

    @Autowired
    private CustomerQueryByExampleRepository repository;

    @Test
    public void testExample() {
        Customer customer = new Customer();
        customer.setName("张三");
        // 通过Example构建查询条件
        Example<Customer> example = Example.of(customer);
        List<Customer> list = new ArrayList<>();
        Iterable<Customer> all = repository.findAll(example);
        all.forEach(list::add);
        System.out.println(list);
    }

    @Test
    public void testExampleMatcher() {
        Customer customer = new Customer();
        customer.setName("三");
        customer.setAddress("JING");
        // 通过匹配器 对条件行为配置
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("name")
            // .withIgnoreCase()
            // .withStringMatcher(ExampleMatcher.StringMatcher.ENDING)
            // .withMatcher("address", m -> m.endsWith())
            .withMatcher("address", endsWith().ignoreCase());
        // 通过Example构建查询条件
        Example<Customer> example = Example.of(customer, matcher);
        List<Customer> list = new ArrayList<>();
        Iterable<Customer> all = repository.findAll(example);
        all.forEach(list::add);
        System.out.println(list);
    }
}

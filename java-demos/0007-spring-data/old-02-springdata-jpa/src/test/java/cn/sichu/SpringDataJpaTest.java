package cn.sichu;

import cn.sichu.config.SpringDataJPAConfig;
import cn.sichu.pojo.Customer;
import cn.sichu.repositories.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author sichu
 * @date 2022/12/09
 **/
// @ContextConfiguration("/spring.xml")
@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testR() {
        Optional<Customer> byId = customerRepository.findById(1L);
        System.out.println(byId.get());
    }

    @Test
    public void testC() {
        Customer customer = new Customer();
        // customer.setId(4L);
        customer.setName("郑十");
        customer.setAddress("郑十地址");
        // 相当于jpa的merge, 没指定id是insert, 指定id是update
        customerRepository.save(customer);
    }

    @Test
    public void testD() {
        Customer customer = new Customer();
        customer.setId(4L);
        customer.setName("王五");
        // Hibernate: select customer0_.cust_id as cust_id1_0_0_, customer0_.cust_address as cust_add2_0_0_, customer0_.cust_name as cust_nam3_0_0_ from cst_customer customer0_ where customer0_.cust_id=?
        // Hibernate: delete from cst_customer where cust_id=?
        // 即使是 detached 游离状态的对象, Spring Data JPA 底层也会先查询再删除
        customerRepository.delete(customer);
    }

    @Test
    public void testFindAllById() {
        Iterable<Customer> itr = customerRepository.findAllById(Arrays.asList(1L, 2L, 6L));
        System.out.println(itr);
    }
}

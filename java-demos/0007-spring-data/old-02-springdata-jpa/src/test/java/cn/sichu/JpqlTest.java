package cn.sichu;

import cn.sichu.config.SpringDataJPAConfig;
import cn.sichu.pojo.Customer;
import cn.sichu.repositories.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/10
 **/
@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class JpqlTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testR() {
        List<Customer> customer = customerRepository.findCustomerByName("张三");
        System.out.println(customer);
    }

    @Test
    public void testU() {
        int result = customerRepository.updateCustomerById("赵六", 3L);
        System.out.println(result);
    }

    @Test
    public void testD() {
        int result = customerRepository.deleteCustomerById(3L);
        System.out.println(result);
    }

    @Test
    public void testC() {
        int result = customerRepository.insertCustomerBySelect(1L);
        System.out.println(result);
    }

    @Test
    public void testRNative() {
        List<Customer> list = customerRepository.nativeFindCustomerByName("张三");
        System.out.println(list);
    }
}

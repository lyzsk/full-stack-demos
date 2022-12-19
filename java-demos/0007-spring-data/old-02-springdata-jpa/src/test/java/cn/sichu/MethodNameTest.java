package cn.sichu;

import cn.sichu.config.SpringDataJPAConfig;
import cn.sichu.pojo.Customer;
import cn.sichu.repositories.CustomerMethodNameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author sichu
 * @date 2022/12/10
 **/
@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MethodNameTest {

    @Autowired
    private CustomerMethodNameRepository repository;

    @Test
    public void testR() {
        List<Customer> list = repository.findByName("张三");
        System.out.println(list);
    }

    @Test
    public void testExistsBy() {
        boolean result = repository.existsByName("王五");
        System.out.println(result);
    }

    @Test
    public void testRemoveBy() {
        if (repository.existsById(6L)) {
            repository.deleteById(6L);
        }
        Optional<Customer> result = repository.findById(6L).isPresent() ? repository.findById(6L) : Optional.empty();
        System.out.println(result);
    }

    @Test
    public void testFindByNameLike() {
        List<Customer> list1 = repository.findByNameLike("张%");
        List<Customer> list2 = repository.findByNameLike("%三");
        System.out.println(list1);
        System.out.println(list2);
    }
}

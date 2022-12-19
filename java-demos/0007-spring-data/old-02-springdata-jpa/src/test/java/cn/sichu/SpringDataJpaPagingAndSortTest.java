package cn.sichu;

import cn.sichu.config.SpringDataJPAConfig;
import cn.sichu.pojo.Customer;
import cn.sichu.repositories.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author sichu
 * @date 2022/12/10
 **/
@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringDataJpaPagingAndSortTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testPaging() {
        Page<Customer> all = customerRepository.findAll(PageRequest.of(0, 2));
        System.out.println(all);
        System.out.println(all.getTotalElements());
        System.out.println(all.getTotalPages());
        System.out.println(all.getContent());
    }

    @Test
    public void testSort() {
        Sort sortById = Sort.by("id").descending();
        Iterable<Customer> all = customerRepository.findAll(sortById);
        System.out.println(all);
    }

    @Test
    public void testSortTypedSafe() {
        Sort.TypedSort<Customer> customerTypedSort = Sort.sort(Customer.class);
        Sort sort =
            customerTypedSort.by(Customer::getId).descending().and(customerTypedSort.by(Customer::getName).ascending());
        Iterable<Customer> all = customerRepository.findAll(sort);
        System.out.println(all);
    }
}

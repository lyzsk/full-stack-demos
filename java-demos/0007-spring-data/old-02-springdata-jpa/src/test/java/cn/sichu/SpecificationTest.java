package cn.sichu;

import cn.sichu.config.SpringDataJPAConfig;
import cn.sichu.pojo.Customer;
import cn.sichu.repositories.CustomerSpecificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sichu
 * @date 2022/12/10
 **/
@ContextConfiguration(classes = SpringDataJPAConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpecificationTest {
    @Autowired
    private CustomerSpecificationRepository repository;

    @Test
    public void testFindAll() {
        List<Customer> all = repository.findAll(new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // root, 可以理解成 from, 获取column
                // query (组合 orderby, where)
                // criteriaBuilder, 可以理解成 where, 设置条件 (< , >, in)
                Path<Long> id = root.get("id");
                Path<String> name = root.get("name");
                Path<String> address = root.get("address");
                Predicate predicate1 = criteriaBuilder.equal(address, "beijing");
                Predicate predicate2 = criteriaBuilder.greaterThan(id, 2L);
                // Hibernate: select customer0_.cust_id as cust_id1_0_, customer0_.cust_address as cust_add2_0_, customer0_.cust_name as cust_nam3_0_ from cst_customer customer0_ where customer0_.cust_address=? and customer0_.cust_id>2
                // [Customer{id=7, name='张三', address='BEIJING'}, Customer{id=9, name='田七', address='beiJING'}]
                // 可以看出这个 equal() 方法忽略了大小写
                Predicate predicateAll1 = criteriaBuilder.and(predicate1, predicate2);
                CriteriaBuilder.In<String> in = criteriaBuilder.in(name).value("张三").value("李四").value("王五");
                Predicate predicateAll2 = criteriaBuilder.and(predicateAll1, in);
                return predicateAll2;
            }
        });
        System.out.println(all);
    }

    @Test
    public void testDynamicFindAll() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("张三, 李四, 田七, 周八");
        customer.setAddress("beijing");
        repository.findAll(new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<Long> id = root.get("id");
                Path<String> name = root.get("name");
                Path<String> address = root.get("address");
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(customer.getAddress())) {
                    list.add(criteriaBuilder.equal(address, "beijing"));
                }
                if (customer.getId() > -1) {
                    list.add(criteriaBuilder.equal(id, 1L));
                }
                if (!StringUtils.isEmpty(customer.getName())) {
                    CriteriaBuilder.In<String> inList = criteriaBuilder.in(name).value("张三").value("李四").value("田七");
                    list.add(inList);
                }
                Predicate predicate = criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
                return predicate;
            }
        });
    }

}

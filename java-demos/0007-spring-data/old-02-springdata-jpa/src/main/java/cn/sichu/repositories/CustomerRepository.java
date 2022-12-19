package cn.sichu.repositories;

import cn.sichu.pojo.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/09
 **/
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    // @Query("FROM Customer where name=?1")
    // List<Customer> findCustomerByName(String name);

    @Query("FROM Customer where name=:name")
    List<Customer> findCustomerByName(@Param("name") String name);

    @Query("UPDATE Customer c set c.name=:name where c.id=:id")
    @Transactional
    @Modifying
    int updateCustomerById(@Param("name") String name, @Param("id") Long id);

    @Query("DELETE FROM Customer c where c.id=?1")
    @Transactional
    @Modifying
    int deleteCustomerById(Long id);

    @Transactional
    @Modifying
    @Query("INSERT INTO Customer(name) SELECT c.name FROM Customer c WHERE c.id=?1")
    int insertCustomerBySelect(Long id);

    @Query(nativeQuery = true, value = "select * from cst_customer where cust_name=:name")
    List<Customer> nativeFindCustomerByName(@Param("name") String name);
}

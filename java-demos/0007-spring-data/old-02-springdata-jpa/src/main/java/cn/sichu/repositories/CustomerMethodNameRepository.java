package cn.sichu.repositories;

import cn.sichu.pojo.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sichu
 * @date 2022/12/10
 **/
public interface CustomerMethodNameRepository extends PagingAndSortingRepository<Customer, Long> {

    List<Customer> findByName(String name);

    boolean existsByName(String name);

    @Override
    @Transactional
    @Modifying
    void deleteById(Long id);

    List<Customer> findByNameLike(String name);
}

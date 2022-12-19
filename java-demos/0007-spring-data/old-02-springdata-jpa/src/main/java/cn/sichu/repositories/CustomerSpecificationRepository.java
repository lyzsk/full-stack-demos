package cn.sichu.repositories;

import cn.sichu.pojo.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author sichu
 * @date 2022/12/10
 **/
public interface CustomerSpecificationRepository
    extends PagingAndSortingRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
}

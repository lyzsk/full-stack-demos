package cn.sichu.repositories;

import cn.sichu.pojo.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * @author sichu
 * @date 2022/12/10
 **/
public interface CustomerQueryByExampleRepository
    extends PagingAndSortingRepository<Customer, Long>, QueryByExampleExecutor<Customer> {
}

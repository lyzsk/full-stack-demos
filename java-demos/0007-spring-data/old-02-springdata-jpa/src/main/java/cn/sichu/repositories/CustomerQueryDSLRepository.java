package cn.sichu.repositories;

import cn.sichu.pojo.Customer;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author sichu
 * @date 2022/12/10
 **/
public interface CustomerQueryDSLRepository
    extends PagingAndSortingRepository<Customer, Long>, QuerydslPredicateExecutor<Customer> {
}

package cn.sichu.demo.repositories;

import cn.sichu.demo.pojo.StudentIdCard;
import org.springframework.data.repository.CrudRepository;

/**
 * @author sichu
 * @date 2022/12/11
 **/
public interface StudentCardRepository extends CrudRepository<StudentIdCard, Long> {
}

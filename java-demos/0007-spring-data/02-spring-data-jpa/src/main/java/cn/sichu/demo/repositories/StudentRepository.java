package cn.sichu.demo.repositories;

import cn.sichu.demo.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sichu
 * @date 2022/12/11
 **/
public interface StudentRepository extends JpaRepository<Student, Long> {
}

package cn.sichu.demo.repositories;

import cn.sichu.demo.pojo.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author sichu
 * @date 2022/12/11
 **/
@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.email=?1")
    Optional<Student> findStudentByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.firstName=?1 AND s.age=?2")
    List<Student> selectStudentWhereNameAndAgeGreaterOrEqual(String firstName, Integer age);

    @Query(nativeQuery = true, value = "SELECT * FROM student WHERE first_name=:firstName AND age>=:age")
    List<Student> selectStudentWhereFirstNameAndAgeGreaterOrEqualNative(@Param("firstName") String firstName,
        @Param("age") Integer age);

    @Transactional
    @Modifying
    @Query("DELETE FROM Student s WHERE s.id=?1")
    int deleteStudentById(Long id);
}

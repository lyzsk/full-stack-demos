package cn.sichu.demo;

import cn.sichu.demo.pojo.Student;
import cn.sichu.demo.repositories.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student maria = new Student("Maria", "Jones", "maria.jones@amigoscode.edu", 21);
            Student ahmed = new Student("Ahmed", "Ali", "ahmed.ali@amigoscode.edu", 18);
            System.out.println("Adding maria and ahmed");
            List<Student> list = new ArrayList<>(Arrays.asList(maria, ahmed));
            studentRepository.saveAll(list);
            System.out.print("Number of students: ");
            System.out.println(studentRepository.count());
            Optional<Student> byId = studentRepository.findById(2L);
            System.out.println(byId.get());
            System.out.println("Select all students");
            List<Student> students = studentRepository.findAll();
            students.forEach(System.out::println);
            System.out.println("Delete maria");
            studentRepository.deleteById(1L);

            System.out.print("Number of students: ");
            System.out.println(studentRepository.count());
        };
    }
}

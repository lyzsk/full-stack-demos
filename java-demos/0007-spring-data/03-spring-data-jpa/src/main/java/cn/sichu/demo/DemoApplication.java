package cn.sichu.demo;

import cn.sichu.demo.pojo.Student;
import cn.sichu.demo.repositories.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student maria = new Student("Maria", "Jones", "maria.jones@amigoscode.edu", 21);

            Student maria2 = new Student("Maria", "Jones", "maria2.jones@amigoscode.edu", 25);

            Student ahmed = new Student("Ahmed", "Ali", "ahmed.ali@amigoscode.edu", 18);

            studentRepository.saveAll(new ArrayList<>(Arrays.asList(maria, maria2, ahmed)));

            studentRepository.findStudentByEmail("ahmed.ali@amigoscode.edu");

            studentRepository.selectStudentWhereFirstNameAndAgeGreaterOrEqualNative("Maria", 21)
                .forEach(System.out::println);

            studentRepository.selectStudentWhereFirstNameAndAgeGreaterOrEqualNative("Maria", 21)
                .forEach(System.out::println);

            System.out.println("Deleting Maria 2");
            System.out.println(studentRepository.deleteStudentById(3L));
        };
    }
}

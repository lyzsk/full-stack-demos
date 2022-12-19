package cn.sichu.demo;

import cn.sichu.demo.pojo.Student;
import cn.sichu.demo.pojo.StudentIdCard;
import cn.sichu.demo.repositories.StudentCardRepository;
import cn.sichu.demo.repositories.StudentRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
        StudentCardRepository studentCardRepository) {
        return args -> {
            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String emailPrefix = faker.cat().name();
            String emailSuffix = faker.food().dish();
            String email = String.format("%s.%s@%s.%s", firstName, lastName, emailPrefix, emailSuffix);
            Student student = new Student(firstName, lastName, email, faker.number().numberBetween(18, 56));
            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);

            studentCardRepository.save(studentIdCard);
            studentRepository.findById(1L).ifPresent(System.out::println);
            studentCardRepository.findById(1L).ifPresent(System.out::println);
            // studentRepository.deleteById(1L);
        };
    }
}

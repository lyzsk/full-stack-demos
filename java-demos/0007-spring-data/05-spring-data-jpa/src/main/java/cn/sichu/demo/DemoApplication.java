package cn.sichu.demo;

import cn.sichu.demo.pojo.Book;
import cn.sichu.demo.pojo.Student;
import cn.sichu.demo.pojo.StudentIdCard;
import cn.sichu.demo.repositories.StudentIdCardRepository;
import cn.sichu.demo.repositories.StudentRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
        StudentIdCardRepository studentIdCardRepository) {
        return args -> {
            Faker faker = new Faker();

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String emailPrefix = faker.app().name();
            List<String> emailSuffixs = new ArrayList<>(Arrays.asList("com", "cn", "edu"));
            String emailSuffix = emailSuffixs.get(new Random().nextInt(emailSuffixs.size()));
            String email = String.format("%s.%s@%s.%s", firstName, lastName, emailPrefix, emailSuffix);
            Student student = new Student(firstName, lastName, email, faker.number().numberBetween(18, 56));
            // Book book1 = new Book(LocalDateTime.now().minusDays(4), faker.book().title());
            // Book book2 = new Book(LocalDateTime.now(), faker.book().title());
            // Book book3 = new Book(LocalDateTime.now().minusYears(1), faker.book().title());
            // List<Book> books = new ArrayList<>(Arrays.asList(book1, book2, book3));
            // student.setBooks(books);
            student.addBook(new Book(LocalDateTime.now().minusDays(4), faker.book().title()));
            student.addBook(new Book(LocalDateTime.now(), faker.book().title()));
            student.addBook(new Book(LocalDateTime.now().minusYears(1), faker.book().title()));

            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);

            student.setStudentIdCard(studentIdCard);

            studentRepository.save(student);

            studentRepository.findById(1L).ifPresent(s -> {
                System.out.println("fetch book lazy...");
                List<Book> list = student.getBooks();
                list.forEach(book -> {
                    System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
                });
            });
            studentIdCardRepository.findById(1L).ifPresent(System.out::println);
            // studentRepository.deleteById(1L);
        };
    }
}

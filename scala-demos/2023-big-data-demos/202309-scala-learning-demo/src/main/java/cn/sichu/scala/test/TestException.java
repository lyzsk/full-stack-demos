package cn.sichu.scala.test;

/**
 * @author sichu
 * @date 2023/10/03
 **/
public class TestException {
    public static void main(String[] args) {
        // 调用一个为null的成员属性或成员方法会产生空指针异常
        UserX user = new UserX();
        // Integer => 自动拆箱(intValue) => int, intValue是成员方法, 所以是编译字节码才能看出问题
        // test(user.age);
    }

    public static void test(int age) {
        System.out.println(age);
    }
}

class UserX {
    public static Integer age;
}
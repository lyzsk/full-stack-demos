package cn.sichu.scala.chapter01;

import java.io.Serializable;

/**
 * @author sichu
 * @date 2023/10/01
 **/
public class User implements Serializable {
    // public static int age = 30;
    public static int age;

    static {
        age = 30;
        System.out.println("user static init...");
    }

    public static String test() {
        // exception
        // int i = 10 / 0;
        return "abc";
    }

    @Override
    public String toString() {
        return "User[" + age + "]";
    }
}

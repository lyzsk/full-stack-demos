package cn.sichu.scala.test;

/**
 * @author sichu
 * @date 2023/10/02
 **/
public class TestString {
    public static void main(String[] args) {
        String s = " a b ";
        System.out.println(":" + s.trim() + "!");
        s.trim();
        System.out.println(":" + s + "!");
        s = s.trim();
        System.out.println(":" + s + "!");
    }
}

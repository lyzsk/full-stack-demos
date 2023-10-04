package cn.sichu.scala.test;

/**
 * @author sichu
 * @date 2023/10/03
 **/
public class TestOperation {
    public static void main(String[] args) {
        String s1 = new String("abc");
        String s2 = new String("abc");
        // 内存地址比较
        System.out.println(s1 == s2);
        // 值的比较
        System.out.println(s1.equals(s2));

        // 100 => int => 自动装箱(Integer.valueOf())
        Integer i1 = 100;
        Integer i2 = 100;
        System.out.println(i1 == i2);
        Integer i3 = 10000;
        Integer i4 = 10000;
        System.out.println(i3 == i4);
        System.out.println(i3.equals(i4));
    }
}

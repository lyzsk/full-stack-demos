package cn.sichu.scala.test;

/**
 * @author sichu
 * @date 2023/10/04
 **/
public class TestStringUtil {
    public static void main(String[] args) {
        System.out.println(isNotEmpty(null));
    }

    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().equals("");
    }
}

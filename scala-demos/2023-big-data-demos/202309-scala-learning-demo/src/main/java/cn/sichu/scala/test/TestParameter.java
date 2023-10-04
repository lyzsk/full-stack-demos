package cn.sichu.scala.test;

/**
 * @author sichu
 * @date 2023/10/04
 **/
public class TestParameter {
    public static void main(String[] args) {
        System.out.println(func1("zhangsan,", "lisi,", "wangwu,", "zhaoliu,"));
    }

    public static String func1(String s1, String... s2) {
        StringBuilder sb = new StringBuilder();
        for (String s : s2) {
            sb.append(s);
        }
        return s1 + sb;
    }
}

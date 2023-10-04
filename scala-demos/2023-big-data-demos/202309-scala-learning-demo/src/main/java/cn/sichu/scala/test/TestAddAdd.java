package cn.sichu.scala.test;

/**
 * @author sichu
 * @date 2023/10/03
 **/
public class TestAddAdd {
    public static void main(String[] args) {
        int i = 0;
        int j = i++;
        System.out.println("i = " + i);
        System.out.println("j = " + j);

        int k = 0;
        k = k++;
        System.out.println("k = " + k);
    }
}

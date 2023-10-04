package cn.sichu.scala.test;

import java.util.List;

/**
 * @author sichu
 * @date 2023/10/04
 **/
public class TestFor {
    public static void main(String[] args) {
        // java.lang.NullPointerException
        List<Object> list = null;
        for (Object obj : list) {
            System.out.println(obj);
        }
    }
}

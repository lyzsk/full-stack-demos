package cn.sichu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
public class RandomStringUtil {
    private static final String salt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static List<String> generateRandomStringList(String prefix, int length, int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            Random random = new Random();
            for (int j = 0; j < length; j++) {
                int idx = (int)(random.nextFloat() * salt.length());
                sb.append(salt.charAt(idx));
            }
            list.add(sb.toString());
        }
        return list;
    }
}

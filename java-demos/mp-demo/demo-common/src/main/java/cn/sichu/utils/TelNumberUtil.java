package cn.sichu.utils;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
public class TelNumberUtil {
    private static final String[] PREFIXX =
        "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    public static int getNum(int start, int end) {
        return (int)(Math.random() * (end - start + 1) + start);
    }

    public static String getChineseMainLandTelNumber() {
        int index = getNum(0, PREFIXX.length - 1);
        String first = PREFIXX[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }
}

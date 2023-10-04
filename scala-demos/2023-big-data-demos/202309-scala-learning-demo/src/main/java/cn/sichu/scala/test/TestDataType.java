package cn.sichu.scala.test;

/**
 * @author sichu
 * @date 2023/10/03
 **/
public class TestDataType {
    public static void main(String[] args) {
        // java 中基本数据类型是没有类型的概念的, 只有精度的概念, 除了char类型
        byte b = 1;
        test(b);

        char ch = 'A' + 1;
        System.out.println(ch);

        // 强制转化, 截取精度, 32位->8位
        int i = 100;
        byte bb = (byte)i;
        System.out.println(bb);

        // 一元运算符不会提升数据类型
        byte aaa = 126;
        byte bbb = 127;
        byte ccc = 125;
        aaa++;
        bbb++;
        byte ccc1 = (byte)(ccc + 1);
        System.out.println(aaa);
        System.out.println(bbb);
        System.out.println(ccc1);
    }

    // public static void test(byte b) {
    //     System.out.println("bbbb");
    // }

    // public static void test(short s) {
    //     System.out.println("ssss");
    // }

    public static void test(char c) {
        System.out.println("cccc");
    }

    public static void test(int i) {
        System.out.println("iiii");
    }
}

package cn.sichu.utils;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
public class NumberUtil {
    /**
     * @param num num
     * @return boolean
     * @author sichu huang
     * @date 2024/09/16
     **/
    public static boolean isOdd(int num) {
        return (num % 2) != 0;
    }

    /**
     * @param num num
     * @return boolean
     * @author sichu huang
     * @date 2024/09/16
     **/
    public static boolean isPrimeNum(int num) {
        double sqrt = Math.sqrt(num);
        if (num < 2) {
            return false;
        }
        if (num == 2 || num == 3) {
            return true;
        }
        if (num % 2 == 0) {
            return false;
        }
        for (int i = 3; i <= sqrt; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param num num
     * @return boolean
     * @author sichu huang
     * @date 2024/09/16
     **/
    public static boolean isHappyNumber(int num) {
        int slow = num;
        int fast = getNext(num);
        while (fast != 1 && slow != fast) {
            slow = getNext(slow);
            fast = getNext(getNext(fast));
        }
        return fast == 1;
    }

    /**
     * @param n n
     * @return int
     * @author sichu huang
     * @date 2024/09/16
     **/
    private static int getNext(int n) {
        int sum = 0;
        while (n > 0) {
            int digit = n % 10;
            n /= 10;
            sum += digit * digit;
        }
        return sum;
    }

    /**
     * @param num num
     * @return boolean
     * @author sichu huang
     * @date 2024/09/16
     **/
    public static boolean isUglyNumber(int num) {
        if (num <= 0) {
            return false;
        }
        int[] factors = new int[] {2, 3, 5};
        for (int factor : factors) {
            while (num % factor == 0) {
                num /= factor;
            }
        }
        return num == 1;
    }
}

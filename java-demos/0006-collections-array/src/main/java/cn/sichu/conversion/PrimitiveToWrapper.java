package cn.sichu.conversion;

import java.util.Arrays;

/**
 * @author sichu
 * @date 2022/11/30
 **/
public class PrimitiveToWrapper {
    public int[] integerArrayTointArray(Integer[] arr) {
        return Arrays.stream(arr).mapToInt(Integer::intValue).toArray();
    }
}

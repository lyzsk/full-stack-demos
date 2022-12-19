package cn.sichu.conversion;

import java.util.Arrays;

/**
 * @author sichu
 * @date 2022/11/30
 **/
public class WrapperToPrimitive {
    public Integer[] intArrayToIntegerArray(int[] arr) {
        return Arrays.stream(arr).boxed().toArray(Integer[]::new);
    }
}

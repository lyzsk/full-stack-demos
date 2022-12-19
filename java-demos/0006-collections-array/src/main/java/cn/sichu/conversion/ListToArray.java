package cn.sichu.conversion;

import java.util.List;

/**
 * @author sichu
 * @date 2022/11/30
 **/
public class ListToArray {
    public Integer[] listToIntegerArray(List<Integer> list) {
        return list.toArray(new Integer[0]);
    }

    public int[] listToIntArray(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).toArray();
    }
}

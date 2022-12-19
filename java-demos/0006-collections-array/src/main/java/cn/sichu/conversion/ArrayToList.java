package cn.sichu.conversion;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sichu
 * @date 2022/11/30
 **/
public class ArrayToList {
    public static void main(String[] args) {
        int[] arr = new int[] {12, 23, 34, 45, 56};
        System.out.println(Arrays.toString(arr));
        Integer[] integers = Arrays.stream(arr).boxed().toArray(Integer[]::new);
        System.out.println(Arrays.toString(integers));
        List<Integer> list = Arrays.asList(integers);
        System.out.println(list);
    }

    /**
     * int[] -> List<Integer>, boxed 装箱, 基本类型转包装类型</>
     *
     * @param arr
     * @return List<Integer> res</>
     */
    public List<Integer> intArrayToList(int[] arr) {
        return Arrays.stream(arr).boxed().collect(Collectors.toList());
    }

    public List<Integer> IntegerArrayToList(Integer[] arr) {
        return Arrays.asList(arr);
    }
}

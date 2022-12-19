package cn.sichu.conversion;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sichu
 * @date 2022/11/30
 **/
public class ArrayToListTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayToListTest.class);

    @Test
    public void testIntArrayToList() {
        int[] arr = new int[] {1, 3, 5, 7, 2, 4, 6, 8};
        ArrayToList arrayToList = new ArrayToList();
        LOGGER.info("int[]: {} \nList<Integer>: {}", arr, arrayToList.intArrayToList(arr));
        LOGGER.debug("int[]: {} \nList<Integer>: {}", arr, arrayToList.intArrayToList(arr));
    }

    @Test
    public void testIntegerArrayToList() {
        Integer[] arr = new Integer[] {1, 2, 3, 4, 5};
        ArrayToList arrayToList = new ArrayToList();
        LOGGER.info("Integer[]: {} \nList<Integer>: {}", arr, arrayToList.IntegerArrayToList(arr));
    }
}

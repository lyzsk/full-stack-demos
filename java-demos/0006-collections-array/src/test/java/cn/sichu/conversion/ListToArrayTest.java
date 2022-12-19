package cn.sichu.conversion;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sichu
 * @date 2022/11/30
 **/
public class ListToArrayTest {
    private static final Logger LOGGER = LoggerFactory.getLogger("ListToArrayTest");

    @Test
    public void testListToIntArray() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 4));
        ListToArray listToArray = new ListToArray();
        int[] res = listToArray.listToIntArray(list);
        LOGGER.info("List<Integer>: {}, type: {} \nint[]: {}, type: [}", list, list.getClass(), res, res.getClass());
        LOGGER.info("res class: {}", res.getClass());
    }

    @Test
    public void testListToIntegerArray() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        ListToArray listToArray = new ListToArray();
        Integer[] res = listToArray.listToIntegerArray(list);
        LOGGER.info("List<Integer>: {} \nInteger[]: {}", list, res);
        LOGGER.info("res type: {}", listToArray.listToIntegerArray(list).getClass());
        LOGGER.info("'==': {}", listToArray.listToIntegerArray(list) == res);
        LOGGER.info("equals: {}", listToArray.listToIntegerArray(list).equals(res));
        LOGGER.info("deepequals: {}", Arrays.equals(listToArray.listToIntegerArray(list), res));
    }
}

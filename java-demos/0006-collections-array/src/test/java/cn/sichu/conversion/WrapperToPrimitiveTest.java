package cn.sichu.conversion;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sichu
 * @date 2022/11/30
 **/
public class WrapperToPrimitiveTest {
    private static final Logger LOGGER = LoggerFactory.getLogger("WrapperToPrimitiveTest");

    @Test
    public void testIntArrayToIntegerArray() {
        int[] arr = new int[] {1, 3, 5, 6};
        WrapperToPrimitive wrapperToPrimitive = new WrapperToPrimitive();
        Object res = wrapperToPrimitive.intArrayToIntegerArray(arr);
        LOGGER.info("int[]: {}, type: {} \nInteger[]: {}, type: {}", arr, arr.getClass(), res, res.getClass());
    }
}

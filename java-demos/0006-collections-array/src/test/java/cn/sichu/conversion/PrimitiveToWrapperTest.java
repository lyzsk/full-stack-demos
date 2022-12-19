package cn.sichu.conversion;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sichu
 * @date 2022/11/30
 **/
public class PrimitiveToWrapperTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrimitiveToWrapperTest.class);

    @Test
    public void testIntegerArrayTointArray() {
        Integer[] arr = new Integer[] {1, 3, 5, 7};
        PrimitiveToWrapper primitiveToWrapper = new PrimitiveToWrapper();
        int[] res = primitiveToWrapper.integerArrayTointArray(arr);
        LOGGER.info("Integer[]: {}, type: {} \nint[]: {}, type: {}", arr, arr.getClass(), res, res.getClass());
    }
}

import cn.sichu.constant.EqpStatus;
import cn.sichu.constant.Gender;
import cn.sichu.constant.KeySkills;
import cn.sichu.constant.Result;
import cn.sichu.entity.Eqp;
import cn.sichu.service.IEqpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sichu huang
 * @date 2024/09/16
 **/
@SpringBootTest(classes = Application.class)
public class TestEqp {
    @Autowired
    IEqpService eqpService;

    @Test
    public void initTable() throws ParseException {
        int result = eqpService.initTable(3, 4, 2);
        System.err.println(result == Result.SUCCESS ? "Success" : "Failed");
    }

    @Test
    public void updateOwner() {
        List<String> list = new ArrayList<>(
            Arrays.asList(KeySkills.COMMUNICATION, KeySkills.PROBELM_SOLVING, KeySkills.TECHNICAL_SAVVY,
                KeySkills.SELF_MOTIVATED, KeySkills.CONFIDENCE, KeySkills.TEAMWORK, KeySkills.FAST_LEARNER));
        int updateCount = eqpService.updateOwner(Gender.FEMALE, 25, list, 2);
        System.err.println(updateCount);
    }

    @Test
    public void selectEqpByStatus() {
        List<Eqp> eqps = eqpService.selectEqpByStatus(EqpStatus.ALARM);
        eqps.forEach(System.err::println);
    }

    @Test
    public void updateEqpByStatus() {
        int updateCount = eqpService.updateEqpByStatus(EqpStatus.ALARM);
        System.err.println(updateCount);
    }
}

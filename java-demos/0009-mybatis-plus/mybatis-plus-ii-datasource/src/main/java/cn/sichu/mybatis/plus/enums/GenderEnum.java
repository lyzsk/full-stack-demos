package cn.sichu.mybatis.plus.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author sichu
 * @date 2022/12/07
 **/
@Getter
public enum GenderEnum {
    /**
     *
     */
    MALE(1, "男"), FEMALE(2, "女");

    @EnumValue
    private final Integer gender;
    private final String genderName;

    GenderEnum(Integer gender, String genderName) {
        this.gender = gender;
        this.genderName = genderName;
    }
}

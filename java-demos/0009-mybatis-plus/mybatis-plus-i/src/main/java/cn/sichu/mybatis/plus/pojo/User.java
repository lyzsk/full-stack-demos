package cn.sichu.mybatis.plus.pojo;

import cn.sichu.mybatis.plus.enums.GenderEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

/**
 * @author sichu
 * @date 2022/12/05
 **/
@Data
// @TableName("t_user")
public class User {
    // @TableId(value = "uid", type = IdType.AUTO)
    @TableId(value = "uid")
    private Long id;

    @TableField("user_name")
    private String name;

    private Integer age;

    private String email;

    @TableLogic
    private Integer isDeleted;

    private GenderEnum gender;
}

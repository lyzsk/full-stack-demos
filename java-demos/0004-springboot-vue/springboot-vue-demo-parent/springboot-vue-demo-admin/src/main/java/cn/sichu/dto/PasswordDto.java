package cn.sichu.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author sichu
 * @date 2022/12/19
 **/
@Data
public class PasswordDto implements Serializable {
    @NotBlank(message = "新密码不能为空")
    private String password;

    @NotBlank(message = "旧密码不能为空")
    private String currentPass;
}

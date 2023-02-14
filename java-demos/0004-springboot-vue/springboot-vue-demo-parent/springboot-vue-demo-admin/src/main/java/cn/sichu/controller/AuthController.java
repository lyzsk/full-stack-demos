package cn.sichu.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.sichu.common.Const;
import cn.sichu.common.Result;
import cn.sichu.entity.User;
import cn.sichu.service.IUserService;
import cn.sichu.utils.RedisUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author sichu
 * @date 2022/12/17
 **/
@RestController
public class AuthController {
    @Autowired
    public HttpServletRequest request;

    @Autowired
    public RedisUtil redisUtil;

    @Autowired
    public Producer producer;

    @Autowired
    IUserService userService;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();
        // 测试用
        key = "aaaaa";
        code = "11111";
        // producer生成captcha
        BufferedImage image = producer.createImage(code);
        // IO传入byte字节码
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        // 固定前缀 + 字节码将转64位编码生成图片
        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        return Result.success(MapUtil.builder().put("token", key).put("captchaImg", base64Img).build());
    }

    @GetMapping("/sys/userInfo")
    public Result userInfo(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return Result.success(MapUtil.builder().put("id", user.getId()).put("username", user.getUsername())
            .put("avatar", user.getAvatar()).put("created", user.getCreated()).map());
    }
}

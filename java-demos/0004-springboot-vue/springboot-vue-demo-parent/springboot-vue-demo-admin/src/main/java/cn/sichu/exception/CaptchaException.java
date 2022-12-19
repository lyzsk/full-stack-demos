package cn.sichu.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author sichu
 * @date 2022/12/17
 **/
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}

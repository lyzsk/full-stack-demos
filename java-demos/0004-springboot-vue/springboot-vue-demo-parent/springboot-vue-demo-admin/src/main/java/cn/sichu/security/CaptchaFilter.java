package cn.sichu.security;

import cn.sichu.common.Const;
import cn.sichu.exception.CaptchaException;
import cn.sichu.utils.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sichu
 * @date 2022/12/17
 **/
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    public RedisUtil redisUtil;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String url = request.getRequestURI();
        if ("/login".equals(url) && request.getMethod().equals("POST")) {
            try {
                validate(request);
            } catch (CaptchaException e) {
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 校验验证码
     *
     * @param request
     */
    private void validate(HttpServletRequest request) {
        String key = request.getParameter("token");
        String code = request.getParameter("code");
        if (StringUtils.isBlank(key) || StringUtils.isBlank(code)) {
            throw new CaptchaException("验证码错误");
        }
        if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
            throw new CaptchaException("验证码错误");
        }
        // 防止验证码重复使用
        // TODO: 应该改成提交表单的时候刷新验证码
        redisUtil.hdel(Const.CAPTCHA_KEY, key);
    }
}

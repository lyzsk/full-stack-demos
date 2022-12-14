package cn.sichu.config;

import cn.sichu.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author sichu
 * @date 2022/12/17
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] URL_WHITELIST = {"/login", "/logout", "/captcha", "/favicon.ico"};

    @Autowired
    public LoginFailureHandler loginFailureHandler;

    @Autowired
    public LoginSuccessHandler loginSuccessHandler;

    @Autowired
    public CaptchaFilter captchaFilter;

    @Autowired
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()

            // ????????????
            .formLogin().successHandler(loginSuccessHandler).failureHandler(loginFailureHandler).and().logout()
            .logoutSuccessHandler(jwtLogoutSuccessHandler)
            // ??????session
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // ??????????????????
            .and().authorizeRequests().antMatchers(URL_WHITELIST).permitAll().anyRequest().authenticated()
            // ???????????????
            .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)
            // ???????????????????????????
            .and().addFilter(jwtAuthenticationFilter())
            .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}

package cn.sichu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author sichu
 * @date 2022/12/18
 **/
@Component
@ConfigurationProperties(prefix = "sichu.jwt")
public class JwtUtils {
    /**
     * 自定义参数, 在yml里配置
     */
    private long expire;
    private String secret;
    /**
     * 给jwt统一自定义名字
     */
    private String header;

    public JwtUtils() {
    }

    public JwtUtils(long expire, String secret, String header) {
        this.expire = expire;
        this.secret = secret;
        this.header = header;
    }

    /**
     * 生成 jwt
     */
    public String generateToken(String username) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);
        return Jwts.builder().setHeaderParam("type", "JWT").setSubject(username).setIssuedAt(nowDate)
            .setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 解析 jwt
     */
    public Claims getClaimByToken(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断 jwt 过期
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    @Override
    public String toString() {
        return "JwtUtils{" + "expire=" + expire + ", secret='" + secret + '\'' + ", header='" + header + '\'' + '}';
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

package io.github.fantasticname.dormitory.repair.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 
 * @author FantasticName
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 生成JWT token
     * 
     * @param userId 用户ID
     * @param role 角色
     * @return token
     */
    public String generateToken(Long userId, Integer role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    /**
     * 解析JWT token
     * 
     * @param token token
     * @return Claims
     */
    public Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从token中获取用户ID
     * 
     * @param token token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从token中获取角色
     * 
     * @param token token
     * @return 角色
     */
    public Integer getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", Integer.class);
    }

    /**
     * 验证token是否有效
     * 
     * @param token token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从HttpServletRequest中获取用户ID
     * 
     * @param request 请求对象
     * @return 用户ID
     */
    public Long getUserIdFromRequest(jakarta.servlet.http.HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        String token = authorization.substring(7);
        if (!validateToken(token)) {
            return null;
        }
        return getUserIdFromToken(token);
    }

}

package io.github.fantasticname.dormitory.repair.interceptor;

import io.github.fantasticname.dormitory.repair.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT认证拦截器
 * 
 * @author FantasticName
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            // 无token，返回401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            Map<String, Object> result = new HashMap<>();
            result.put("code", 401);
            result.put("message", "未授权，请先登录");
            writer.write("{\"code\":401,\"message\":\"未授权，请先登录\"}");
            writer.flush();
            writer.close();
            return false;
        }

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            // token无效，返回401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"message\":\"token无效或已过期\"}");
            writer.flush();
            writer.close();
            return false;
        }

        // 从token中获取用户信息并存储到请求上下文
        Long userId = jwtUtil.getUserIdFromToken(token);
        Integer role = jwtUtil.getRoleFromToken(token);
        request.setAttribute("userId", userId);
        request.setAttribute("role", role);

        return true;
    }
}

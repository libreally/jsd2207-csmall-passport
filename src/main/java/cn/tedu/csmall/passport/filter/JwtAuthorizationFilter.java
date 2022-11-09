package cn.tedu.csmall.passport.filter;

import cn.tedu.csmall.passport.security.LoginPrincipal;
import cn.tedu.csmall.passport.web.JsonResult;
import cn.tedu.csmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * <p>JWT过滤器</p>
 *
 * <p>此JWT的主要作用：</p>
 * <ul>
 *     <li>获取客户端携带的JWT，惯用做法是：客户端应该通过请求头中的Authorization属性来携带JWT</li>
 *     <li>解析客户端携带的JWT，并创建出Authentication对象，存入到SecurityContext中</li>
 * </ul>
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final int JWT_MIN_LENGTH = 113;

    @Value("${csmall.jwt.secret-key}")
    private String secretKey;

    public JwtAuthorizationFilter() {
        log.info("创建过滤器对象：JwtAuthorizationFilter");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtAuthorizationFilter开始执行过滤……");
        // 获取客户端携带的JWT
        String jwt = request.getHeader("Authorization");
        log.debug("获取客户端携带的JWT：{}", jwt);

        // 检查是否获取到了基本有效的JWT
        if (!StringUtils.hasText(jwt) || jwt.length() < JWT_MIN_LENGTH) {
            // 对于无效的JWT，直接放行，交由后续的组件进行处理
            log.debug("获取到的JWT被视为无效，当前过滤器将放行……");
            filterChain.doFilter(request, response);
            return;
        }

        // 尝试解析JWT
        log.debug("获取到的JWT被视为有效，准备解析JWT……");
        response.setContentType("application/json; charset=utf-8");
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (SignatureException e) {
            log.debug("解析JWT时出现SignatureException");
            String message = "非法访问！";
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_SIGNATURE, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonResultString);
            return;
        } catch (MalformedJwtException e) {
            log.debug("解析JWT时出现MalformedJwtException");
            String message = "非法访问！";
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_MALFORMED, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonResultString);
            return;
        } catch (ExpiredJwtException e) {
            log.debug("解析JWT时出现ExpiredJwtException");
            String message = "登录信息已过期，请重新登录！";
            JsonResult<Void> jsonResult = JsonResult.fail(ServiceCode.ERR_JWT_EXPIRED, message);
            String jsonResultString = JSON.toJSONString(jsonResult);
            PrintWriter writer = response.getWriter();
            writer.println(jsonResultString);
            return;
        } catch (Throwable e) {
            log.debug("解析JWT时出现Throwable，需要开发人员在JWT过滤器补充对异常的处理");
            e.printStackTrace();
            String message = "你有异常没有处理，请根据服务器端控制台的信息，补充对此类异常的处理！！！";
            PrintWriter writer = response.getWriter();
            writer.println(message);
            return;
        }

        // 获取JWT中的管理员信息
        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        String authoritiesJsonString = claims.get("authoritiesJsonString", String.class);
        log.debug("从JWT中获取id：{}", id);
        log.debug("从JWT中获取username：{}", username);
        log.debug("从JWT中获取authoritiesJsonString：{}", authoritiesJsonString);

        // 处理权限信息
        List<SimpleGrantedAuthority> authorities
                = JSON.parseArray(authoritiesJsonString, SimpleGrantedAuthority.class);

        // 创建Authentication对象
        LoginPrincipal loginPrincipal = new LoginPrincipal(id, username);
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                loginPrincipal, null, authorities);

        // 将Authentication对象存入到SecurityContext
        log.debug("向SecurityContext中存入认证信息：{}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 过滤器链继续向后传递，即：放行
        log.debug("JWT过滤器执行完毕，放行！");
        filterChain.doFilter(request, response);
    }

}

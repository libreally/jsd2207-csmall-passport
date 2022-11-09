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
import org.springframework.security.core.GrantedAuthority;
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
import java.util.ArrayList;
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

    @Value("${csmall.jwt.secret-key}")
    private String secretKey;
    public static final int JWT_MIN_LENGTH = 113;

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
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.debug("解析JWT时出现ExpiredJwtException异常");
            return;
        } catch (MalformedJwtException e) {
            log.debug("解析JWT时出现MalformedJwtException异常");
            return;
        } catch (SignatureException e) {
            log.debug("解析JWT时出现SignatureException异常");

            ServiceCode serviceCode = ServiceCode.ERR_JWT_SIGNATURE;
            String message = "非法访问";
            JsonResult<Void> jsonResult=JsonResult.fail(serviceCode,message);
            String jsonResultString= JSON.toJSONString(jsonResult);
            response.setContentType("text/html;charset=utf-8");
            PrintWriter printWriter= response.getWriter();
            printWriter.println(jsonResultString);
            return;
        } catch (Throwable e) {
            log.debug("解析JWT时出现Throwable异常,需要开发人员对异常的补充");
            e.printStackTrace();
            return;
        }

        // 获取JWT中的管理员信息
        String username = claims.get("username", String.class);
        Long id=claims.get("id",Long.class);

        // 处理权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("这是一个假权限");
        authorities.add(authority);



        // 创建Authentication对象
        LoginPrincipal loginPrincipal=new LoginPrincipal(id,username);
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

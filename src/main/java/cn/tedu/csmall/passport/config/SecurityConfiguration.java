package cn.tedu.csmall.passport.config;

import cn.tedu.csmall.passport.filter.JwtAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public SecurityConfiguration() {
        log.debug("创建配置类对象：SecurityConfiguration");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 如果不调用父类方法，默认所有请求都不需要通过认证，可以直接访问
        // super.configure(http);

        // 白名单
        String[] urls = {
                "/favicon.ico",
                "/doc.html",
                "/**/*.js",
                "/**/*.css",
                "/swagger-resources",
                "/v2/api-docs",
                "/admins/login"
        };
        http.cors();
        // 将防止伪造跨域攻击的机制禁用
        http.csrf().disable();

        // 提示：关于请求路径的配置，如果同一路径对应多项配置规则，以第1次配置的为准
        http.authorizeRequests() // 管理请求授权
                .mvcMatchers(urls) // 匹配某些路径
                .permitAll() // 直接许可，即可不需要通过认证即可访问
                .anyRequest() // 除了以上配置过的以外的其它所有请求
                .authenticated(); // 要求是“已经通过认证的”

        // 将JWT过滤器添加到Spring Security框架的过滤器链中
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        // 启用登录表单
        // 当未认证时：
        // -- 如果启用了表单，会自动重定向到登录表单
        // -- 如果未启用表单，则会提示403错误
        // http.formLogin();
    }

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

}

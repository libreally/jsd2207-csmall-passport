package cn.tedu.csmall.passport.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全验证配置类
 */
@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public  SecurityConfiguration(){
       log.debug("创建配置类对象:SecurityConfiguration");
   }

   @Bean
   public PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance();//无操作密码编码器,不对密码进行处理,不推荐
           return new BCryptPasswordEncoder();//必须使用的是BCryptPasswordEncoder加密算法
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
   protected void configure(HttpSecurity http) throws Exception{
       //如果不调用父类的方法则默认所有的请求都不需要通过认证,可以直接访问
       //super.configure(http);

       //将防止伪造跨域攻击的机制禁用
       http.csrf().disable();

       //白名单
       String[] urls={
                     "/favicon.ico",
                     "/doc.html",
                     "/**/*.js",
                     "/**/*.css",
                     "/swagger-resources",
                     "/v2/api-docs",
                     "/admins/login"
       };

       //同意路径对应多项配置,以第一个为准
       http.authorizeRequests()//管理请求授权
               .mvcMatchers(urls)//选择匹配路径
               .permitAll()//直接许可,即可不需要通过认证即可访问
               .anyRequest()//除了以上配置的所有请求
               .authenticated();//要求是已经通过认证的
      //启用登录表单
      //当未认证时:  如果启用了表单描绘重定向到登录表单 否则提示403错误
      //http.formLogin();
   }

}

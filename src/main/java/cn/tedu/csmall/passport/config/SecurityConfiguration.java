package cn.tedu.csmall.passport.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全验证配置类
 */
@Slf4j
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public  SecurityConfiguration(){
       log.debug("创建配置类对象:SecurityConfiguration");
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception{
       //如果不调用父类的方法则默认所有的请求都不需要通过认证,可以直接访问
       //super.configure(http);
       http.authorizeRequests()//管理请求授权
               .mvcMatchers("/**")//选择匹配路径
               .authenticated();//要求是已经通过认证的
      //启用登录表单
      //当未认证时:  如果启用了表单描绘重定向到登录表单 否则提示403错误
      http.formLogin();
   }

}

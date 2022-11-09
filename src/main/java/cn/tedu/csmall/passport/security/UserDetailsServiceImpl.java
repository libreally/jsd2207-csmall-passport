package cn.tedu.csmall.passport.security;

import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.pojo.vo.AdminLonginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现spring Security 框架
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("spring Security 框架自动调用UserDetailsService中的loadUserByUsername");
        AdminLonginInfoVO admin = adminMapper.getLoginInfoByUsername(s);
        log.debug("从数据库中根据用户名【{}】查询管理员信息，结果：{}", s, admin);
        if (admin == null) {
            log.debug("没有与用户名【{}】匹配的用户信息，即将向Spring Security框架返回为null", s);
            String message="登录失败,用户名不存在";
            throw new BadCredentialsException(message);
        }

        // 处理权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("这是一个假权限");
        authorities.add(authority);

        AdminDetails adminDetails = new AdminDetails(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getEnable()==1,
                authorities);
        /*UserDetails userDetails = User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .disabled(admin.getEnable() == 0)//账号是否被禁用
                .accountLocked(false)//账号锁定
                .accountExpired(false)//账号是否过期
                .credentialsExpired(false)//凭证锁定机制
                .authorities("权限表示")//账号权限
                .build();*/
        log.debug("即将向Spring Security框架返回adminDetails对象：{}", adminDetails);
        return adminDetails;

    }
}

package cn.tedu.csmall.passport.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

/**
 * 管理员详情类
 */
@Getter
@Setter
@ToString(callSuper=true)//调用父类的方法
@EqualsAndHashCode
public class AdminDetails extends User {

    private Long id;

    public AdminDetails(Long id,String username, String password, boolean enabled,
                        Collection<? extends GrantedAuthority> authorities) {
        super( username, password, enabled,
                true, true, true, authorities);
        this.id=id;
    }
}

package cn.tedu.csmall.passport.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginPrincipal {
    private Long id;
    private String username;
}

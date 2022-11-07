package cn.tedu.csmall.passport.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminLoginDTO implements Serializable {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（原文）
     */
    private String password;
}

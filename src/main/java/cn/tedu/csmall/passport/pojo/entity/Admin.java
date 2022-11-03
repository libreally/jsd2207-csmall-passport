package cn.tedu.csmall.passport.pojo.entity;

import lombok.Data;


import java.time.LocalDateTime;

import java.io.Serializable;

/**
 * 管理员(AmsAdmin)实体类
 *
 * @author makejava
 * @since 2022-11-03 14:22:47
 */
@Data
public class Admin implements Serializable {

    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码（密文）
     */
    private String password;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像URL
     */
    private String avatar;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;
    /**
     * 最后登录IP地址（冗余）
     */
    private String lastLoginIp;
    /**
     * 累计登录次数（冗余）
     */
    private Integer loginCount;
    /**
     * 最后登录时间（冗余）
     */
    private LocalDateTime gmtLastLogin;
    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;
    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;


}


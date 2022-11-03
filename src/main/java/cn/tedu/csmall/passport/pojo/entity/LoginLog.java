package cn.tedu.csmall.passport.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

import java.io.Serializable;

/**
 * 管理员登录日志(AmsLoginLog)实体类
 *
 * @author makejava
 * @since 2022-11-03 14:27:43
 */
@Data
public class LoginLog implements Serializable {

    private Long id;
    /**
     * 管理员id
     */
    private Long adminId;
    /**
     * 管理员用户名（冗余）
     */
    private String username;
    /**
     * 管理员昵称（冗余）
     */
    private String nickname;
    /**
     * 登录IP地址
     */
    private String ip;
    /**
     * 浏览器内核
     */
    private String userAgent;
    /**
     * 登录时间
     */
    private LocalDateTime gmtLogin;
    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;
    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;




}


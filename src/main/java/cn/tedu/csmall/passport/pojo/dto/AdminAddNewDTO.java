package cn.tedu.csmall.passport.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class AdminAddNewDTO implements Serializable {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotNull(message = "添加管理员失败，必须提交用户名！")
    private String username;

    /**
     * 密码（原文）
     */
    @ApiModelProperty(value = "密码（原文）", required = true)
    private String password;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;

    /**
     * 头像URL
     */
    @ApiModelProperty("头像URL")
    private String avatar;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", required = true)
    private String phone;

    /**
     * 电子邮箱
     */
    @ApiModelProperty(value = "电子邮箱", required = true)
    private String email;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String description;

    /**
     * 是否启用，1=启用，0=未启用
     */
    @ApiModelProperty(value = "是否启用", required = true)
    private Integer enable;

    /**
     * 角色id数组
     */
    @ApiModelProperty(value = "角色id数组", required = true)
    private Long[] roleIds;

}

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
    @NotNull(message = "添加管理员失败，必须提交用户名！")
    @ApiModelProperty(value = "用户名",required = true)
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

}

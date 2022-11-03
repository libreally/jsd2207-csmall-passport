package cn.tedu.csmall.passport.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

import java.io.Serializable;

/**
 * 管理员角色关联(AmsAdminRole)实体类
 *
 * @author makejava
 * @since 2022-11-03 14:27:42
 */
@Data
public class AdminRole implements Serializable {

    private Long id;
    /**
     * 管理员id
     */
    private Long adminId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;
    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;

}


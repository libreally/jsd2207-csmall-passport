package cn.tedu.csmall.passport.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

import java.io.Serializable;

/**
 * 角色权限关联(AmsRolePermission)实体类
 *
 * @author makejava
 * @since 2022-11-03 14:27:43
 */
@Data
public class RolePermission implements Serializable {

    private Long id;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 权限id
     */
    private Long permissionId;
    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;
    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;

}


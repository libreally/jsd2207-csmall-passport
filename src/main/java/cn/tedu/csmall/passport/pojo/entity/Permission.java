package cn.tedu.csmall.passport.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

import java.io.Serializable;

/**
 * 权限(AmsPermission)实体类
 *
 * @author makejava
 * @since 2022-11-03 14:27:43
 */
@Data
public class Permission implements Serializable {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
    /**
     * 描述
     */
    private String description;
    /**
     * 自定义排序序号
     */
    private Integer sort;
    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;
    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;

}


package cn.tedu.csmall.passport.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoleListItemVO implements Serializable {
    /**
     * 数据id
     */
    @ApiModelProperty("数据id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 自定义排序序号
     */
    @ApiModelProperty("自定义排序序号")
    private Integer sort;
}

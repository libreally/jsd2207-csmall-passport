package cn.tedu.csmall.passport.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminLonginInfoVO implements Serializable {
    private Long id;
    private String username;
    private String password;
    private Integer enable;
    /**
     * ๆ้ๅ่กจ
     */
    private List<String> permissions;
}

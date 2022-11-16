package cn.tedu.csmall.passport.web;

import cn.tedu.csmall.passport.ex.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
@Component
@Controller
@Service
@Repository
@Configuration
@Data
public class JsonResult<T> implements Serializable {

    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private Integer state;
    /**
     * 操作失败时的描述文本
     */
    @ApiModelProperty("操作失败时的描述文本")
    private String message;
    /**
     * 操作成功时响应的数据
     */
    @ApiModelProperty("操作成功时响应的数据")
    private T data;

    public static JsonResult<Void> ok() {
        return ok(null);
    }

    public static <T> JsonResult<T> ok(T data) {
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.state = ServiceCode.OK.getValue();
        jsonResult.data = data;
        return jsonResult;
    }

    public static JsonResult<Void> fail(ServiceException e) {
        return fail(e.getServiceCode(), e.getMessage());
    }

    public static JsonResult<Void> fail(ServiceCode serviceCode, String message) {
        JsonResult<Void> jsonResult = new JsonResult<>();
        jsonResult.state = serviceCode.getValue();
        jsonResult.message = message;
        return jsonResult;
    }

}
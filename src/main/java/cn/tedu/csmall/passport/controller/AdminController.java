package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListVO;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admins")
@Api(tags = "01. 管理员管理模块")
public class AdminController {
    @Autowired
    private IAdminService iAdminService;

    public AdminController(){
        log.debug("创建控制器对象：AdminController");
    }
    @ApiOperation("添加管理员")
    @ApiOperationSupport(order = 100)
    @PostMapping("/add-new")
    public JsonResult<Void> addNew(@Valid AdminAddNewDTO adminAddNewDTO){
        log.debug("开始处理【添加管理员】的请求，参数：{}", adminAddNewDTO);
        iAdminService.addAdmin(adminAddNewDTO);
        log.debug("添加管理员成功！");
        return JsonResult.ok();
    }
    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 420)
    @GetMapping("")
    public JsonResult<List<AdminListVO>> list() {
        log.debug("开始处理【查询相册列表】的请求，无参数");
        List<AdminListVO> list = iAdminService.list();
        return JsonResult.ok(list);
    }
}

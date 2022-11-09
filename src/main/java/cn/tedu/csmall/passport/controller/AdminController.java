package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListVO;
import cn.tedu.csmall.passport.security.LoginPrincipal;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@Api(tags = "01. 管理员管理模块")
@Validated
@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    IAdminService adminService;

    public AdminController() {
        log.info("创建控制器对象：AdminController");
    }

    // http://localhost:9081/admins/login
    @ApiOperation("管理员登录")
    @ApiOperationSupport(order = 50)
    @PostMapping("/login")
    public JsonResult<String> login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的请求，参数：{}", adminLoginDTO);
        String jwt = adminService.login(adminLoginDTO);
        return JsonResult.ok(jwt);
    }

    // http://localhost:9081/admins/add-new?username=aa&phone=bb&email=cc
    @ApiOperation("添加管理员")
    @ApiOperationSupport(order = 100)
    @PostMapping("/add-new")
    public JsonResult<Void> addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理【添加管理员】的请求，参数：{}", adminAddNewDTO);
        adminService.addAdmin(adminAddNewDTO);
        return JsonResult.ok();
    }

    // http://localhost:8080/admins/9527/delete
    @ApiOperation("根据id删除管理员")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "id", value = "管理员id", required = true, dataType = "long")
    @PostMapping("/{id:[0-9]+}/delete")
    public JsonResult<Void> delete(@Range(min = 1, message = "删除管理员失败，尝试删除的管理员的ID无效！")
                                   @PathVariable Long id) {
        log.debug("开始处理【根据id删除管理员】的请求，参数：{}", id);
        adminService.deleteById(id);
        return JsonResult.ok();
    }

    // http://localhost:8080/admins/9527/enable
    @ApiOperation("启用管理员")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParam(name = "id", value = "管理员id", required = true, dataType = "long")
    @PostMapping("/{id:[0-9]+}/enable")
    public JsonResult<Void> setEnable(@Range(min = 1, message = "启用管理员失败，尝试启用的管理员的ID无效！")
                                      @PathVariable Long id) {
        log.debug("开始处理【启用管理员】的请求，参数：{}", id);
        adminService.setEnable(id);
        return JsonResult.ok();
    }

    // http://localhost:8080/admins/9527/disable
    @ApiOperation("禁用管理员")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParam(name = "id", value = "管理员id", required = true, dataType = "long")
    @PostMapping("/{id:[0-9]+}/disable")
    public JsonResult<Void> setDisable(@Range(min = 1, message = "禁用管理员失败，尝试禁用的管理员的ID无效！")
                                       @PathVariable Long id) {
        log.debug("开始处理【禁用管理员】的请求，参数：{}", id);
        adminService.setDisable(id);
        return JsonResult.ok();
    }

    // http://localhost:9081/admins
    @ApiOperation("查询管理员列表")
    @ApiOperationSupport(order = 420)
    @GetMapping("")
    public JsonResult<List<AdminListVO>> list(
            @ApiIgnore @AuthenticationPrincipal LoginPrincipal loginPrincipal) {
        log.debug("开始处理【查询管理员列表】的请求，无参数");
        log.debug("当前登录的当事人：{}", loginPrincipal);
        List<AdminListVO> list = adminService.list();
        return JsonResult.ok(list);
    }

}
package cn.tedu.csmall.passport.controller;

import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListVO;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        log.debug("开始处理【查询管理员列表】的请求，无参数");
        List<AdminListVO> list = iAdminService.list();
        return JsonResult.ok(list);
    }
    @ApiOperation("删除管理员")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "id", value = "相册id", required = true, dataType = "long")
    @PostMapping("/{id:[0-9]+}/delete")
    public JsonResult<Void> delete(@Range(min = 1, message = "删除管理员失败，尝试删除的管理员的ID无效！")
                                   @PathVariable Long id) {
        log.debug("开始处理【根据id删除管理员】的请求，参数：{}", id);
        iAdminService.deleteById(id);
        return JsonResult.ok();
    }

    @ApiOperation("启用管理员")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParam(name = "id", value = "管理员id", required = true, dataType = "long")
    @PostMapping("/{id:[0-9]+}/enable")
    public JsonResult<Void> setEnable(@Range(min = 1, message = "启用管理员失败，尝试启用的管理员的ID无效！")
                                      @PathVariable Long id) {
        log.debug("开始处理【启用管理员】的请求，参数：{}", id);
        iAdminService.setEnable(id);
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
        iAdminService.setDisable(id);
        return JsonResult.ok();
    }
}

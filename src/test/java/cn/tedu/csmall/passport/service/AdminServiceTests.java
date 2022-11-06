package cn.tedu.csmall.passport.service;

import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class AdminServiceTests {

    @Autowired
    IAdminService service;

    @Test
    void addNew() {
        AdminAddNewDTO adminAddNewDTO = new AdminAddNewDTO();
        adminAddNewDTO.setUsername("wangkejing001");
        adminAddNewDTO.setPassword("123456");
        adminAddNewDTO.setPhone("13800138001");
        adminAddNewDTO.setEmail("wangkejing001@baidu.com");

        try {
            service.addNew(adminAddNewDTO);
            log.debug("添加管理员成功！");
        } catch (ServiceException e) {
            log.debug("{}", e.getMessage());
        }
    }

    @Test
    void delete() {
        Long id = 6L;

        try {
            service.delete(id);
            log.debug("删除管理员成功！");
        } catch (ServiceException e) {
            log.debug("{}", e.getMessage());
        }
    }

    @Test
    void setEnable() {
        Long id = 5L;

        try {
            service.setEnable(id);
            System.out.println("启用管理员成功！");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void setDisable() {
        Long id = 5L;

        try {
            service.setDisable(id);
            System.out.println("禁用管理员成功！");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void list() {
        List<?> list = service.list();
        log.debug("查询列表完成，列表中的数据的数量：{}", list.size());
        for (Object item : list) {
            log.debug("{}", item);
        }
    }

}

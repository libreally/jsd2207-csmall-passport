package cn.tedu.csmall.passport.service;


import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.vo.AdminListVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IAdminService {
    void addAdmin(AdminAddNewDTO adminAddNewDTO);
    List<AdminListVO> list();

}

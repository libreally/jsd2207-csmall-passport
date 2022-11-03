package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.vo.AdminListVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    int insert(Admin admin);
    int countByUsername(String username);
    int countByPhone(String phone);
    int countByEmail(String email);
    List<AdminListVO> list();
}

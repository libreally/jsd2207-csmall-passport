package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.vo.AdminListVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    int insert(Admin admin);
    int insertBatch(List<Admin> admins);
    int deleteById(Long id);
    int deleteByIds(Long[] ids);
    int update(Admin admin);
    int count();
    int countByUsername(String username);
    int countByPhone(String phone);
    int countByEmail(String email);
    AdminStandardVO getStandarId(Long id);
    List<AdminListVO> list();
}

package cn.tedu.csmall.passport.mapper;

import cn.tedu.csmall.passport.pojo.entity.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminMapperTest {
    @Autowired
    AdminMapper adminMapper;
    @Test
    void insert(){
        Admin admin = new Admin();
        admin.setUsername("root1");
        adminMapper.insert(admin);
    }
    @Test
    void countByUsername(){
        adminMapper.countByUsername("root");
    }
    @Test
    void countByPhone(){
        adminMapper.countByPhone("13900139001");
    }
    @Test
    void countByEmail(){
        adminMapper.countByEmail("root@baidu.com");
    }
    @Test
    void list(){
        adminMapper.list();
    }
}

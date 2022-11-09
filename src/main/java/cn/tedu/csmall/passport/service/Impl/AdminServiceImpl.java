package cn.tedu.csmall.passport.service.Impl;


import cn.tedu.csmall.passport.ex.ServiceException;
import cn.tedu.csmall.passport.mapper.AdminMapper;
import cn.tedu.csmall.passport.mapper.AdminRoleMapper;
import cn.tedu.csmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.csmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.csmall.passport.pojo.entity.Admin;
import cn.tedu.csmall.passport.pojo.entity.AdminRole;
import cn.tedu.csmall.passport.pojo.vo.AdminListVO;
import cn.tedu.csmall.passport.pojo.vo.AdminStandardVO;
import cn.tedu.csmall.passport.security.AdminDetails;
import cn.tedu.csmall.passport.service.IAdminService;
import cn.tedu.csmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${csmall.jwt.secret-key}")
    private String secretKey;
    @Value("${csmall.jwt.duration-in-minute}")
    private Long durationInMinute;

    public AdminServiceImpl() {
        log.debug("创建业务对象：AdminServiceImpl");
    }

    @Override
    public String login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理【管理员登录】的业务，参数：{}", adminLoginDTO);
        // 执行认证
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                adminLoginDTO.getUsername(), adminLoginDTO.getPassword());
        Authentication authenticateResult
                = authenticationManager.authenticate(authentication);
        log.debug("认证通过，认证管理器返回：{}", authenticateResult);

        // 从认证结果中获取所需的数据，将用于生成JWT
        Object principal = authenticateResult.getPrincipal();
        log.debug("认证结果中的当事人类型：{}", principal.getClass().getName());
        AdminDetails adminDetails = (AdminDetails) principal;
        String username = adminDetails.getUsername();
        Long id = adminDetails.getId();
        Collection<GrantedAuthority> authorities = adminDetails.getAuthorities();
        String authoritiesJsonString = JSON.toJSONString(authorities);

        // 生成JWT数据时，需要填充装到JWT中的数据
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("username", username);
        claims.put("authoritiesJsonString", authoritiesJsonString);
        log.debug("向JWT中存入id：{}", id);
        log.debug("向JWT中存入username：{}", username);
        log.debug("向JWT中存入authoritiesJsonString：{}", authoritiesJsonString);
        // 以下是生成JWT的固定代码
        Date date = new Date(System.currentTimeMillis() + durationInMinute * 60 * 1000L);
        String jwt = Jwts.builder()
                // Header
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                // Payload
                .setClaims(claims)
                // Signature
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("生成的JWT：{}", jwt);
        return jwt;
    }

    @Override
    public void addAdmin(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理【添加管理员】的业务，参数：{}", adminAddNewDTO);

        log.debug("即将检查用户名是否被占用……");
        {
            // 从参数对象中获取username
            String username = adminAddNewDTO.getUsername();
            // 调用adminMapper的countByUsername()方法执行统计查询
            int count = adminMapper.countByUsername(username);
            // 判断统计结果是否不等于0
            if (count != 0) {
                // 是：抛出异常
                String message = "添加管理员失败，用户名【" + username + "】已经被占用！";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_UNAUTHORIZED, message);
            }
        }

        log.debug("即将检查手机号码是否被占用……");
        {
            // 从参数对象中获取手机号码
            String phone = adminAddNewDTO.getPhone();
            // 调用adminMapper的countByPhone()方法执行统计查询
            int count = adminMapper.countByPhone(phone);
            // 判断统计结果是否不等于0
            if (count != 0) {
                // 是：抛出异常
                String message = "添加管理员失败，手机号码【" + phone + "】已经被占用！";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

        log.debug("即将检查电子邮箱是否被占用……");
        {
            // 从参数对象中获取电子邮箱
            String email = adminAddNewDTO.getEmail();
            // 调用adminMapper的countByEmail()方法执行统计查询
            int count = adminMapper.countByEmail(email);
            // 判断统计结果是否不等于0
            if (count != 0) {
                // 是：抛出异常
                String message = "添加管理员失败，电子邮箱【" + email + "】已经被占用！";
                log.debug(message);
                throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
            }
        }

        // 创建Admin对象
        Admin admin = new Admin();
        // 通过BeanUtils.copyProperties()方法将参数对象的各属性值复制到Admin对象中
        BeanUtils.copyProperties(adminAddNewDTO, admin);
        // TODO 取出密码，进行加密处理，并将密文封装回Admin对象中
        // 补全Admin对象中的属性值：loginCount >>> 0
        admin.setLoginCount(0);
        // 调用adminMapper的insert()方法插入数据
        log.debug("即将插入管理员数据，参数：{}", admin);
        int rows = adminMapper.insert(admin);
        // 判断插入数据的结果是否符合预期
        if (rows != 1) {
            String message = "添加管理员失败，服务器忙，请稍后再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }

        // 插入管理员与角色关联的数据
        Long[] roleIds = adminAddNewDTO.getRoleIds();
        AdminRole[] adminRoles = new AdminRole[roleIds.length];
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < roleIds.length; i++) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(admin.getId());
            adminRole.setRoleId(roleIds[i]);
            adminRole.setGmtCreate(now);
            adminRole.setGmtModified(now);
            adminRoles[i] = adminRole;
        }
        rows = adminRoleMapper.insertBatch(adminRoles);
        if (rows != roleIds.length) {
            String message = "添加管理员失败，服务器忙，请稍后再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }

    @Override
    public List<AdminListVO> list() {
        log.debug("查询管理员列表");
        List<AdminListVO> list = adminMapper.list();
        Iterator<AdminListVO> iterator = list.iterator();
        while (iterator.hasNext()){
            AdminListVO item = iterator.next();
            if (item.getId()==1){
                iterator.remove();
                break;
            }
        }
        return list;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("开始处理【根据id删除管理员】的业务，参数：{}", id);
        //id值为1的管理员不能删除
        if (id==1){
            String message="删除管理员失败。尝试删除的管理员不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        AdminStandardVO standardId = adminMapper.getStandardId(id);
        if (standardId==null){
            String message = "删除管理员失败，尝试访问的数据不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        //删除管理员
        log.debug("即将执行删除，参数：{}", id);
        int rows = adminMapper.deleteById(id);
        /*事务处理*/
        if (rows != 1) {
            String message = "删除管理员失败，服务器忙，请稍后再尝试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }
        //删除管理员与角色的关联数据
        rows = adminRoleMapper.deleteByAdminId(id);
        if (rows < 1) {
            String message = "删除管理员失败，服务器忙，请稍后再尝试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }

    }



    @Override
    public void setEnable(Long id) {
        updateEnableById(id,1);
    }

    @Override
    public void setDisable(Long id) {
        updateEnableById(id,0);
    }
    private void updateEnableById(Long id, Integer enable) {
        String[] tips = {"禁用", "启用"};
        log.debug("开始处理【{}管理员】的业务，参数：{}", tips[enable], id);

        // 判断参数id是否为1
        if (id == 1) {
            String message = tips[enable] + "管理员失败，尝试访问的数据不存在！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        // 检查尝试访问的数据是否存在
        AdminStandardVO queryResult = adminMapper.getStandardId(id);
        if (queryResult == null) {
            String message = tips[enable] + "管理员失败，尝试访问的数据不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        // 判断状态是否冲突（当前已经是目标状态）
        if (queryResult.getEnable().equals(enable)) {
            String message = tips[enable] + "管理员失败，管理员账号已经处于" + tips[enable] + "状态！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_CONFLICT, message);
        }

        // 准备执行更新
        Admin admin = new Admin();
        admin.setId(id);
        admin.setEnable(enable);
        int rows = adminMapper.update(admin);
        if (rows != 1) {
            String message = tips[enable] + "管理员失败，服务器忙，请稍后再次尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERR_UPDATE, message);
        }
    }
}

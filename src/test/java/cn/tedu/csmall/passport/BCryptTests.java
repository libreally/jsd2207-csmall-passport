package cn.tedu.csmall.passport;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密工具
 */
public class BCryptTests {
    @Test
    public void encode() {
        //密码编码器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "123456";
        System.out.println("原文:" + password);
        for (int i = 0; i < 50; i++) {
            String encode = passwordEncoder.encode(password);
            System.out.println("密文 = " + encode);
        }
        /*译码 = $2a$10$CDyBvOEFUebwyLxaIlXqd.Bf7br6Apg6vC8KZkmx4w8DiGDHjgcXe
          译码 = $2a$10$sxcGNMeOFMkmolkkC17EMOCskh4B/srGZq5HYw.Bg/u7Kyno6DCTO
          译码 = $2a$10$mLhCRTSra6QLiUbf9OhSO.WkKotyJq9aauke4JTogQxQZZStxODjC
          译码 = $2a$10$jewz.UqATusR2QVLwAh2neHcTIaLWE0VTEdZMK7ia8Nb58ss6dIei
          译码 = $2a$10$hoxzs19OITie2baZ7fEm2Oaivxhj5arOUZwAr8S/r.iPvxv8GY3oe
          译码 = $2a$10$OhzIQ8WiFQwx2Tx0KdL9a.LRidJ.a5mIzairApTasLwj0pZDXWhDa
          译码 = $2a$10$tvI0yMyCt173WIMTZYIATeRMRyX.IYfJaVjmccnGt4Y228vrQRz7K*/
    }
    @Test
    public void matches(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword="123456";
        String encodedPassword="$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCu6v2XyFDtC4q.jeeGm/TEZyj15C";
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println("原文 = " + rawPassword);
        System.out.println("密文 = " + encodedPassword);
        System.out.println("验证 = " + matches);
    }
}

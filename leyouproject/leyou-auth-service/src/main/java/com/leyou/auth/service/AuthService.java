package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    public String accredit(String username, String password) {

        User user = this.userClient.queryUser(username,password);

        if(user==null){
            return null;
        }

        try {
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

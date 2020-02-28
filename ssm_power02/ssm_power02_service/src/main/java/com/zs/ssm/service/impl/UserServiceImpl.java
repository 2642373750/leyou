package com.zs.ssm.service.impl;


import com.zs.ssm.dao.UserDao;
import com.zs.ssm.pojo.Role;
import com.zs.ssm.pojo.UserInfo;
import com.zs.ssm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo=null;
        try {
            userInfo=userDao.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  User user=new User(userInfo.getUsername(),"{noop}"+userInfo.getPassword(),getAuthority(userInfo.getRoles()));
        User user=new User(userInfo.getUsername(),userInfo.getPassword(),userInfo.getStatus()==0?false:true,true,true,true,getAuthority(userInfo.getRoles()));
        return user;
    }
    public List<SimpleGrantedAuthority> getAuthority(List<Role> list){

        List<SimpleGrantedAuthority> l=new ArrayList<SimpleGrantedAuthority>();
         for(Role r:list) {
            System.out.println(r.getRoleName());
            l.add(new SimpleGrantedAuthority("ROLE_"+r.getRoleName()));
        }

        return l;
    }


    @Override
    public List<UserInfo> findAll() throws Exception {

        return userDao.findAll();
    }

    @Override
    public void save(UserInfo userInfo) throws Exception {
        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        userDao.save(userInfo);
    }

    @Override
    public UserInfo findById(String id) throws Exception {

        return userDao.findById(id);
    }

    @Override
    public List<Role> findUserByIdAndAllRole(String id) throws Exception {
        return userDao.findUserByIdAndAllRole(id);
    }

    @Override
    public void addRoleToUser(String userId, String[] roleIds) throws Exception {
        for(String roleId:roleIds){
            userDao.addRoleToUser(userId,roleId);
        }
    }


}

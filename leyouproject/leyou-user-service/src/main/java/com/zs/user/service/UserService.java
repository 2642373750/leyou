package com.zs.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.pojo.User;
import com.zs.user.mapper.UserMapper;
import com.zs.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static  final String KEY_PREFIX = "user:verify:";

    public void sendVerifyCode(String phone) {

        if(StringUtils.isBlank(phone)){
            return;
        }

        String code = NumberUtils.generateCode(6);

        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);

        //发布消息
        this.amqpTemplate.convertAndSend("leyou.sms.exchange","verifycode.sms",map);
        //保存到redis
        this.redisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
    }

    public Boolean checkUser(String data, Integer type) {
        User record = new User();
        if(type==1){
            record.setUsername(data);
        }else if(type==2){
            record.setPhone(data);
        }else{
            return null;
        }

        return this.userMapper.selectCount(record)==0;
    }

    public void register(User user, String code) {

        String c = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());

        //1.验证码
        if(!StringUtils.equals(code,c)){
            return;
        }
        //2.生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        //3.加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));

        //4.新增用户
        user.setId(null);
        user.setCreated(new Date());
        this.userMapper.insertSelective(user);

        //删除验证码
        this.redisTemplate.delete(KEY_PREFIX + user.getPhone());

    }

    public User queryUser(String username, String password) {
        User recort =new User();
        recort.setUsername(username);
        User user = this.userMapper.selectOne(recort);
        if(user==null){
            return null;
        }
        password = CodecUtils.md5Hex(password,user.getSalt());



        if(StringUtils.equals(password,user.getPassword())){
            return user;
        }
      return null;
    }
}

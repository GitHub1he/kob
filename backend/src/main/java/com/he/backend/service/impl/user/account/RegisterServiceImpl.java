package com.he.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.User;
import com.he.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String,String> map = new HashMap<>();
        if(username == null) {
            map.put("error_message", "用户名为空");
            return map;
        }
        if(password == null || confirmedPassword == null ){
            map.put("error_message", "密码为空");
            return map;
        }
        username = username.trim();
        if(username.length() == 0 ){
            map.put("error_message", "用户名长度为空");
            return map;
        }
        if(username.length() > 10) {
            map.put("error_message", "用户名长度不能大于10");
            return map;
        }
        if(password.length() == 0 || confirmedPassword.length() == 0){
            map.put("error_message", "密码长度为0");
            return map;
        }
        if(password.length() > 16 || confirmedPassword.length() > 16){
            map.put("error_message", "密码长度不能超过16");
            return map;
        }
        if(!password.equals(confirmedPassword)) {
            map.put("error_message", "两次密码不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",username);
        User user = userMapper.selectOne(queryWrapper);
        if(user != null) {
            map.put("error_message","用户名已存在");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://baomidou.com/img/logo.svg";
        User registerUser = new User(null,username,encodedPassword,photo,null);
        userMapper.insert(registerUser);

        map.put("error_message","success");
        return map;
    }
}

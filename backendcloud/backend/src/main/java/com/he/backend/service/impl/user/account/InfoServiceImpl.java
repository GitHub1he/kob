package com.he.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.User;
import com.he.backend.service.impl.utils.UserDetailsImpl;
import com.he.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message" , "success");
        map.put("id" , user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        map.put("followercount", user.getFollowercount().toString());
        return map;
    }

    @Override
    public List<User> userList() {
        List<User> users = userMapper.selectList(null);
        for(User user: users){
            user.setPassword("");
        }
        return users;
    }

    @Override
    public Map<String, String> getInfoById(int user_id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",user_id);
        User user = userMapper.selectOne(queryWrapper);

        Map<String, String> map = new HashMap<>();
        if(user == null) {
            map.put("error_message" , "用户不存在或注销");
            return map;
        }

        map.put("error_message" , "success");
        map.put("id" , user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        map.put("followercount", user.getFollowercount().toString());
        return map;
    }
}

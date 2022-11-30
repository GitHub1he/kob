package com.he.backend.service.impl.user.account;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.User;
import com.he.backend.service.impl.utils.UserDetailsImpl;
import com.he.backend.service.user.account.InfoService;
import com.he.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String, String> getInfo() {
        User user = UserUtil.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message" , "success");
        map.put("id" , user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        map.put("followercount", user.getFollowercount().toString());
        map.put("rating", user.getRating().toString());
        map.put("last_login_time", user.getLastLoginTime().toString());
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
        map.put("rating", user.getRating().toString());
        map.put("last_login_time", user.getLastLoginTime().toString());
        return map;
    }

    @Override
    public JSONObject searchUser(String name, Integer rating, Integer follower_cnt) {
        JSONObject res = new JSONObject();
        if ("".equals(name)) {
            res.put("error_message", "请输入要查找的人名！");
            return res;
        }
        QueryWrapper<User> qwuser = new QueryWrapper<>();
        qwuser.like("name", name).ge("rating", rating).ge("followerCount", follower_cnt);
        List<User> users = userMapper.selectList(qwuser);
        List<JSONObject> items = new ArrayList<>();
        for (User user : users) {
            JSONObject item = new JSONObject();
            item.put("id", user.getId());
            item.put("name", user.getUsername());
            item.put("photo", user.getPhoto());
            item.put("rating", user.getRating());
            item.put("follower_cnt", user.getFollowercount());
            item.put("last_login_time", new SimpleDateFormat("yyyy-MM-dd").format(user.getLastLoginTime()));
            items.add(item);
        }
        if(users.size() == 0) {
            res.put("error_message", "用户不存在");
            return res;
        }
        res.put("search_res", items);
        res.put("error_message", "success");
        return res;
    }
}

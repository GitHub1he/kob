package com.he.backend.service.user.account;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.pojo.User;

import java.util.List;
import java.util.Map;

public interface InfoService {
    Map<String,String> getInfo();
    List<User> userList();
    Map<String,String> getInfoById(int user_id);
    JSONObject searchUser(String name, Integer rating, Integer follower_cnt);
}

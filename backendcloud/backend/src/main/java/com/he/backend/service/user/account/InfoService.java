package com.he.backend.service.user.account;

import com.he.backend.pojo.User;

import java.util.List;
import java.util.Map;

public interface InfoService {
    public Map<String,String> getInfo();
    public List<User> userList();
    public Map<String,String> getInfoById(int user_id);
}

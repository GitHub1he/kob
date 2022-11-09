package com.he.backend.service.user.chat;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

public interface TeamDetailService {
    Map<String, String> groupJoin(Integer teamId, Integer userId);
    JSONObject getGroupUsers(Integer teamId, Integer page);
}

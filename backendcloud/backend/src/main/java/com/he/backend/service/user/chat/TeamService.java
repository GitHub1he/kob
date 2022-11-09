package com.he.backend.service.user.chat;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

public interface TeamService {
    Map<String, String> teamCreate(String name);
    Map<String, String> teamDel(Integer id);
    Map<String, String> teamUpd(Integer id, String name, Integer ownerId);
    JSONObject getTeam(Integer page);
}

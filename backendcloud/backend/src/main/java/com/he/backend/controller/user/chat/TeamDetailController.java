package com.he.backend.controller.user.chat;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.service.user.chat.TeamDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TeamDetailController {
    @Autowired
    private TeamDetailService teamDetailService;

    @PostMapping("/api/team/join/")
    public Map<String, String> groupJoin(@RequestParam Map<String, String> data){
        Integer teamId = Integer.parseInt(data.get("team_id"));
        Integer userId = Integer.parseInt(data.get("user_id"));
        return teamDetailService.groupJoin(teamId, userId);
    }

    @GetMapping("/api/team/getusers/")
    public JSONObject getGroupUsers(@RequestParam Map<String, String> data) {
        Integer teamId = Integer.parseInt(data.get("team_id"));
        Integer page = Integer.parseInt(data.get("page"));
        return teamDetailService.getGroupUsers(teamId, page);
    }
}
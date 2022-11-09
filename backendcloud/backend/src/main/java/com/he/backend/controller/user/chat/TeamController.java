package com.he.backend.controller.user.chat;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.service.user.chat.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping("/api/team/add/")
    public Map<String, String> teamAdd(@RequestParam Map<String, String> data){
        String name = data.get("name");
        return teamService.teamCreate(name);
    }

    @PostMapping("/api/team/del/")
    public Map<String, String> teamDel(@RequestParam Map<String, String> data) {
        Integer id = Integer.parseInt(data.get("id"));
        return teamService.teamDel(id);
    }

    @PostMapping("/api/team/upd/")
    public Map<String, String> teamUpd(@RequestParam Map<String, String> data) {
        Integer id = Integer.parseInt(data.get("id"));
        String name = data.get("name");
        Integer ownerId = Integer.parseInt(data.getOrDefault("owner_id","0"));
        return teamService.teamUpd(id, name, ownerId);
    }

    @GetMapping("/api/team/get/")
    public JSONObject getTeam(@RequestParam Integer page) {
        return teamService.getTeam(page);
    }
}

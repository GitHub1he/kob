package com.he.backend.controller.user.account;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.pojo.User;
import com.he.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/api/user/account/info/")
    public Map<String, String> getInfo(){
        return infoService.getInfo();
    }

    @GetMapping("/api/user/account/userlist/")
    public List<User> userList(){
        return infoService.userList();
    }

    @GetMapping("/api/user/account/info/get/")
    public Map<String, String> getInfoById(@RequestParam Map<String,String> map){
        int user_id = Integer.parseInt(map.get("user_id"));
        return infoService.getInfoById(user_id);
    }
    @GetMapping("/api/user/search/")
    public JSONObject searchUser(@RequestParam Map<String, String> data) {
        String name = data.get("name");
        Integer rating = Integer.valueOf("".equals(data.get("rating")) ? "0" : data.get("rating"));
        Integer follower_cnt = Integer.valueOf("".equals(data.get("follower_cnt")) ? "0" : data.get("follower_cnt"));
        return infoService.searchUser(name, rating, follower_cnt);
    }
}

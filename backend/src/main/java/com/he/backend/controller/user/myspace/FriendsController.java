package com.he.backend.controller.user.myspace;

import com.he.backend.service.user.myspace.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FriendsController {
    @Autowired
    private FriendsService friendsService;

    @PostMapping("/user/myspace/friends/changefollow/")
    public Map<String, String> changeFollow(@RequestParam Map<String,String> map){
        int target_id = Integer.parseInt(map.get("target_id"));
        return friendsService.changeFollow(target_id);
    }

    @GetMapping("/user/myspace/friends/isfollow/")
    public Map<String, String> isFollowed(@RequestParam Map<String,String> map){
        int target_id = Integer.parseInt(map.get("target_id"));
        return friendsService.isFollowed(target_id);
    }

}

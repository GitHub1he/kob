package com.he.backend.controller.user.myspace;

import com.he.backend.pojo.User;
import com.he.backend.service.user.myspace.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    //最好返回map， 返回user到前端不安全
    @GetMapping("/user/myspace/friends/follower/")
    public List<User> myFollower(@RequestParam Map<String, String> map){
        int target_id = Integer.parseInt(map.get("target_id"));
        return friendsService.myFollower(target_id);
    }

    @GetMapping("/user/myspace/friends/focuser/")
    public List<User> myFocuser(@RequestParam Map<String, String> map){
        int follower_id = Integer.parseInt(map.get("follower_id"));
        return friendsService.myFocuser(follower_id);
    }

    @GetMapping("/user/myspace/friends/unfocus/")
    public List<User> myUnFocuser(@RequestParam Map<String, String> map){
        int follower_id = Integer.parseInt(map.get("follower_id"));
        return friendsService.myUnFocuser(follower_id);
    }
}

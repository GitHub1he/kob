package com.he.backend.service.user.myspace;

import com.he.backend.pojo.User;

import java.util.List;
import java.util.Map;

public interface FriendsService {
    public Map<String,String> changeFollow(int target_id);

    public Map<String, String> isFollowed(int target_id);

    List<User> myFollower(int target_id);

    List<User> myFocuser(int follower_id);

    List<User> myUnFocuser(int follower_id);
}

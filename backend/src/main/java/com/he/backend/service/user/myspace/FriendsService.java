package com.he.backend.service.user.myspace;

import java.util.Map;

public interface FriendsService {
    public Map<String,String> changeFollow(int target_id);

    public Map<String, String> isFollowed(int target_id);
}

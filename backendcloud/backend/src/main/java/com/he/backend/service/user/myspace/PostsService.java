package com.he.backend.service.user.myspace;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.pojo.Posts;

import java.util.List;
import java.util.Map;

public interface PostsService {
    JSONObject getList(Integer page);
    Map<String,String> createUserPost(String content, Integer isPrivate);
    List<Posts> getPostByUserid(int user_id);
    Map<String,String> delPostByPostid(int post_id);

    Map<String, String> addlike(Integer postId);
}

package com.he.backend.service.user.myspace;

import com.he.backend.pojo.Posts;

import java.util.List;
import java.util.Map;

public interface PostsService {
    public Map<String,String> createUserPost(String content);
    public List<Posts> getPostByUserid(int user_id);
    public Map<String,String> delPostByPostid(int post_id);
}

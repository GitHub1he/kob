package com.he.backend.controller.user.myspace;

import com.alibaba.fastjson2.JSONObject;
import com.he.backend.pojo.Posts;
import com.he.backend.service.user.myspace.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PostsController {
    @Autowired
    private PostsService postsService;

    @GetMapping("/api/user/myspace/posts/list/")
    public JSONObject getList(@RequestParam Map<String, String> data){
        Integer page = Integer.parseInt(data.get("page"));
        return postsService.getList(page);
    }
    @PostMapping("/api/user/myspace/posts/create/")
    public Map<String,String> createpost(@RequestParam Map<String,String> map){
        String content = map.get("content");
        Integer isPrivate = Integer.valueOf(map.get("isPrivate") == null ? "0" : map.get("isPrivate"));
        return postsService.createUserPost(content, isPrivate);
    }

    @GetMapping("/api/user/myspace/posts/get/")
    public List<Posts> getuserpost(@RequestParam Map<String,String> map){
        int user_id = Integer.parseInt(map.get("user_id"));
        return postsService.getPostByUserid(user_id);
    }

    @DeleteMapping("/api/user/myspace/posts/del/")
    public Map<String, String> deluserpost(@RequestParam Map<String,String> map){
        int post_id = Integer.parseInt(map.get("post_id"));
        return  postsService.delPostByPostid(post_id);
    }

    @PostMapping("/api/user/myspace/posts/addlike/")
    public Map<String, String> addlike(@RequestParam Map<String, String> data){
        Integer postId = Integer.parseInt(data.get("post_id"));
        return postsService.addlike(postId);
    }
}

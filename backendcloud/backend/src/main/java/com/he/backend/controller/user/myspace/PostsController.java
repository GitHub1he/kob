package com.he.backend.controller.user.myspace;

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

    @PostMapping("/api/user/myspace/posts/create/")
    public Map<String,String> createpost(@RequestParam Map<String,String> map){
        String content = map.get("content");
        return postsService.createUserPost(content);
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
}

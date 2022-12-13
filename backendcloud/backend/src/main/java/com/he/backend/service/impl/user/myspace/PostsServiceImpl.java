package com.he.backend.service.impl.user.myspace;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.backend.mapper.PostsMapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.Posts;
import com.he.backend.pojo.User;
import com.he.backend.service.user.myspace.PostsService;
import com.he.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostsServiceImpl implements PostsService {
    @Autowired
    private PostsMapper postsMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        JSONObject res = new JSONObject();
        IPage<Posts> iPage = new Page<>(page , 10);
        QueryWrapper<Posts> postsQW = new QueryWrapper<>();
        postsQW.eq("is_private", 1).orderByDesc("id");
        List<Posts> posts = postsMapper.selectPage(iPage, postsQW).getRecords();
        List<JSONObject> items = new ArrayList<>();
        for (Posts post : posts) {
            JSONObject item = new JSONObject();
            item.put("id", post.getId());
            item.put("userid", post.getUserId());
            item.put("post", post.getPost());
            item.put("likes", post.getLikes());
            item.put("createtime", post.getCreatetime());
            User user = userMapper.selectById(post.getUserId());
            item.put("username",user.getUsername());
            item.put("userphoto", user.getPhoto());
            items.add(item);
        }
        Long posts_count = postsMapper.selectCount(postsQW);
        res.put("posts", items);
        res.put("posts_count", posts_count);
        return res;
    }

    @Override
    public Map<String, String> createUserPost(String content , Integer isPrivate) {
        User user = UserUtil.getUser();

        Date now = new Date();
        postsMapper.insert(new Posts(null,user.getId(),content,null,isPrivate,now,now));

        Map<String, String> map = new HashMap<>();
        map.put("error_message","success");
        return map;
    }

    @Override
    public List<Posts> getPostByUserid(int user_id) {
        User user = UserUtil.getUser();

        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user_id);

        return postsMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, String> delPostByPostid(int post_id) {
        User user = UserUtil.getUser();

        Map<String, String> map = new HashMap<>();
        Posts posts = postsMapper.selectById(post_id);
        if(posts == null || !posts.getUserId().equals(user.getId())){
            map.put("error_message","帖子不存在或已被删除");
        }

        postsMapper.deleteById(post_id);

        map.put("error_message","success");
        return map;
    }

    @Override
    public Map<String, String> addlike(Integer postId) {
        UpdateWrapper<Posts> postsUpdateWrapper = new UpdateWrapper<>();
        postsUpdateWrapper.eq("id", postId).setSql( "likes = likes + 1");
        postsMapper.update(null, postsUpdateWrapper);
        Map<String, String> res = new HashMap<>();
        res.put("error_message", "success");
        return res;
    }
}

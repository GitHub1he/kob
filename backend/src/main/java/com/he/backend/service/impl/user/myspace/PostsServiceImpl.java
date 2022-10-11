package com.he.backend.service.impl.user.myspace;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.backend.mapper.PostsMapper;
import com.he.backend.pojo.Posts;
import com.he.backend.pojo.User;
import com.he.backend.service.impl.utils.UserDetailsImpl;
import com.he.backend.service.user.myspace.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostsServiceImpl implements PostsService {
    @Autowired
    private PostsMapper postsMapper;

    @Override
    public Map<String, String> createUserPost(String content) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        postsMapper.insert(new Posts(null,user.getId(),content));

        Map<String, String> map = new HashMap<>();
        map.put("error_message","success");
        return map;
    }

    @Override
    public List<Posts> getPostByUserid(int user_id) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user_id);

        List<Posts> posts = postsMapper.selectList(queryWrapper);
        return posts;
    }

    @Override
    public Map<String, String> delPostByPostid(int post_id) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        postsMapper.deleteById(post_id);

        Map<String, String> map = new HashMap<>();
        map.put("error_message","success");
        return map;
    }
}

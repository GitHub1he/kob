package com.he.backend.service.impl.user.myspace;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.backend.mapper.FriendsMapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.Friends;
import com.he.backend.pojo.User;
import com.he.backend.service.impl.utils.UserDetailsImpl;
import com.he.backend.service.user.myspace.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FriendsServiceImpl implements FriendsService {
    @Autowired
    private FriendsMapper friendsMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> changeFollow(int target_id) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id",target_id).eq("follower_id",user.getId());
        Friends friends = friendsMapper.selectOne(queryWrapper);
        if(friends == null) {
            friendsMapper.insert(new Friends(null,target_id,user.getId()));

            //target_id用户的粉丝数加一
            User needUpdateUser = userMapper.selectById(target_id);
            needUpdateUser.setFollowercount(needUpdateUser.getFollowercount() + 1);
            userMapper.updateById(needUpdateUser);
        }else {
            friendsMapper.deleteById(friends);

            //target_id用户的粉丝数减一
            User needUpdateUser = userMapper.selectById(target_id);
            needUpdateUser.setFollowercount(needUpdateUser.getFollowercount() - 1);
            userMapper.updateById(needUpdateUser);
        }
        Map<String, String> map = new HashMap<>();
        map.put("error_message","success");
        return map;
    }

    @Override
    public Map<String, String> isFollowed(int target_id) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id",target_id)
                .eq("follower_id",user.getId());
        Friends friends = friendsMapper.selectOne(queryWrapper);

        Map<String,String> map = new HashMap<>();
        if(friends == null){
            map.put("state","unfollow");
        }else {
            map.put("state","follow");
        }
        map.put("error_message","success");
        return map;
    }
}

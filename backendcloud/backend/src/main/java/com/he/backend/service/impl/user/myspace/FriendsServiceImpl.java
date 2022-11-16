package com.he.backend.service.impl.user.myspace;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.he.backend.mapper.ChatMapper;
import com.he.backend.mapper.FriendsMapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.Chat;
import com.he.backend.pojo.Friends;
import com.he.backend.pojo.User;
import com.he.backend.service.impl.utils.UserDetailsImpl;
import com.he.backend.service.user.myspace.FriendsService;
import com.he.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FriendsServiceImpl implements FriendsService {
    @Autowired
    private FriendsMapper friendsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChatMapper chatMapper;

    @Override
    public Map<String, String> changeFollow(int target_id) {
        User user = UserUtil.getUser();

        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id",target_id).eq("follower_id",user.getId());
        Friends friends = friendsMapper.selectOne(queryWrapper);
        if(friends == null) {
            friendsMapper.insert(new Friends(null,target_id,user.getId()));

            //target_id用户的粉丝数加一
            User needUpdateUser = userMapper.selectById(target_id);
            needUpdateUser.setFollowercount(needUpdateUser.getFollowercount() + 1);
            userMapper.updateById(needUpdateUser);
            //发送消息
            Date now = new Date();
            chatMapper.insert(new Chat(null, user.getId(), target_id, "和我聊天，不要不识好歹！", null, now, now));
        }else {
            friendsMapper.deleteById(friends);

            //target_id用户的粉丝数减一
            User needUpdateUser = userMapper.selectById(target_id);
            needUpdateUser.setFollowercount(needUpdateUser.getFollowercount() - 1);
            userMapper.updateById(needUpdateUser);
            QueryWrapper<Chat> chatQueryWrapper = new QueryWrapper<>();
            chatQueryWrapper.eq("sender_id", user.getId()).eq("receiver_id", target_id)
                    .or().eq("sender_id", target_id).eq("receiver_id", user.getId())
                    .orderByAsc("id");
            chatMapper.delete(chatQueryWrapper);
        }
        Map<String, String> map = new HashMap<>();
        map.put("error_message","success");
        return map;
    }

    @Override
    public Map<String, String> isFollowed(int target_id) {
        User user = UserUtil.getUser();

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

    @Override
    public List<User> myFollower(int target_id) {
        List<User> list = new ArrayList<>();
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_id", target_id);
        List<Friends> friends = friendsMapper.selectList(queryWrapper);
        for (Friends friend : friends) {
            Integer userid = friend.getFollowerId();
            list.add(userMapper.selectById(userid));
        }

        for (User user : list) {
            user.setPassword("");
        }
        return list;
    }

    @Override
    public List<User> myFocuser(int follower_id) {
        List<User> list = new ArrayList<>();
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", follower_id);
        List<Friends> friends = friendsMapper.selectList(queryWrapper);
        for (Friends friend : friends) {
            Integer userid = friend.getTargetId();

            list.add(userMapper.selectById(userid));
        }
        for (User user : list) {
            user.setPassword("");
        }
        return list;
    }

    @Override
    public List<User> myUnFocuser(int follower_id) {
        String sql = "select target_id from friends where follower_id = " +follower_id;

        List<User> users = userMapper.selectList(new QueryWrapper<User>()
                .notInSql("id", sql));
        for (User user : users) {
            user.setPassword("");
        }
        return users;
    }


}

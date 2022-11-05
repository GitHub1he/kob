package com.he.backend.service.impl.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.User;
import com.he.backend.service.ranklist.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankListServiceImpl implements RankListService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer page) {
        JSONObject res = new JSONObject();

        IPage<User> userIPage = new Page<>(page, 5);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users = userMapper.selectPage(userIPage,queryWrapper).getRecords();
        for (User user : users)
            user.setPassword("");
        res.put("users", users);
        res.put("users_count", userMapper.selectCount(null));
        return res;
    }
}

package com.he.backend.service.impl.user.chat;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.backend.mapper.TeamDetailMapper;
import com.he.backend.mapper.TeamMapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.Teams;
import com.he.backend.pojo.TeamsDetail;
import com.he.backend.pojo.User;
import com.he.backend.service.user.chat.TeamDetailService;
import com.he.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamDetailServiceImpl implements TeamDetailService {
    @Autowired
    private TeamDetailMapper teamDetailMapper;
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String, String> groupJoin(Integer teamId, Integer userId) {
        Map<String, String> res = new HashMap<>();
        Teams teams = teamMapper.selectById(teamId);
        User user = UserUtil.getUser();

        if(teams != null && user != null && Objects.equals(user.getId(), teams.getOwnerId()) || Objects.equals(user.getId(), userId)) {
            QueryWrapper<TeamsDetail> tdqueryWrapper = new QueryWrapper<>();
            tdqueryWrapper.eq("team_id", teamId).eq("user_id", userId);
            List<TeamsDetail> teamsDetails = teamDetailMapper.selectList(tdqueryWrapper);
            Date now = new Date();

            if(teamsDetails.size() == 0) { //进组
                TeamsDetail teamsDetail = new TeamsDetail(null, teamId, userId, now);
                teamDetailMapper.insert(teamsDetail);

                Teams newteam = teamMapper.selectById(teamId);
                newteam.setModifytime(now);
                newteam.setPersonCount(newteam.getPersonCount() + 1);
                teamMapper.updateById(newteam);
            }else {
                for (TeamsDetail teamsDetail : teamsDetails) {
                    teamDetailMapper.deleteById(teamsDetail);
                    Teams newteam = teamMapper.selectById(teamId);
                    newteam.setModifytime(now);
                    newteam.setPersonCount(newteam.getPersonCount() - 1);
                    teamMapper.updateById(newteam);
                }
            }
        }else {
            res.put("error_message", "非法访问！！！");
            return res;
        }

        res.put("error_message", "success");
        return res;
    }

    @Override
    public JSONObject getGroupUsers(Integer teamId, Integer page) {
        JSONObject res = new JSONObject();
        IPage<TeamsDetail> teamsDetailIPage = new Page<>(page, 10);

        QueryWrapper<TeamsDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        List<TeamsDetail> details = teamDetailMapper.selectPage(teamsDetailIPage,queryWrapper).getRecords();

        List<User> users = new LinkedList<>();
        for (TeamsDetail detail : details) {
            Integer userId = detail.getUserId();
            User person = userMapper.selectById(userId);
            if(person != null) {
                person.setPassword("");
                users.add(person);
            }
        }

        Integer userCount = teamMapper.selectById(teamId).getPersonCount();
        res.put("users", users);
        res.put("users_count", userCount);
        return res;
    }
}

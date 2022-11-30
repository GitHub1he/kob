package com.he.backend.service.impl.user.chat;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.backend.mapper.TeamDetailMapper;
import com.he.backend.mapper.TeamMapper;
import com.he.backend.pojo.Teams;
import com.he.backend.pojo.TeamsDetail;
import com.he.backend.pojo.User;
import com.he.backend.service.user.chat.TeamService;
import com.he.backend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private TeamDetailMapper teamDetailMapper;

    @Override
    public Map<String, String> teamCreate(String name) {
        Map<String, String> res = new HashMap<>();
        User user = UserUtil.getUser();
        Integer ownerId = user.getId();
        Date now = new Date();

        QueryWrapper<Teams> teamsQueryWrapper = new QueryWrapper<>();
        teamsQueryWrapper.eq("owner_id", ownerId);
        List<Teams> teams = teamMapper.selectList(teamsQueryWrapper);
        if(teams.size() >= 5) {
            res.put("error_message", "每人最多创建5个群！！！");
            return res;
        }
        teamsQueryWrapper.eq("name", name);
        teams = teamMapper.selectList(teamsQueryWrapper);
        if(teams.size() != 0){
            res.put("error_message", "群组已存在！");
            return res;
        }

        Teams newteam = new Teams(null, name,null, ownerId, 1,now,now);
        teamMapper.insert(newteam);

        List<Teams> teamsList = teamMapper.selectList(teamsQueryWrapper);
        for (Teams group : teamsList) {
            TeamsDetail teamsDetail = new TeamsDetail(null, group.getId(), ownerId, now);
            teamDetailMapper.insert(teamsDetail);
        }
        res.put("error_message", "success");
        return res;
    }

    @Override
    public Map<String, String> teamDel(Integer id) {
        Map<String, String> res = new HashMap<>();
        User user = UserUtil.getUser();

        Teams teams = teamMapper.selectById(id);
        if(teams == null || !Objects.equals(teams.getOwnerId(), user.getId())){
            res.put("error_message", "组不存在或没有权限解散组");
            return res;
        }
        teamMapper.deleteById(teams);

        QueryWrapper<TeamsDetail> teamsDetailQueryWrapper = new QueryWrapper<>();
        teamsDetailQueryWrapper.eq("team_id", id);
        teamDetailMapper.delete(teamsDetailQueryWrapper);
        res.put("error_message", "success");
        return res;
    }

    @Override
    public Map<String, String> teamUpd(Integer id, String name, Integer ownerId) {
        Map<String, String> res = new HashMap<>();
        User user = UserUtil.getUser();

        Teams teams = teamMapper.selectById(id);
        if(teams == null || !Objects.equals(teams.getOwnerId(), user.getId())){
            res.put("error_message", "组不存在或没有权限修改组");
            return res;
        }
        if(name == null || "".equals(name)) name = teams.getName();
        if(ownerId == 0) ownerId = teams.getOwnerId();
        Date now = new Date();
        Teams newteam = new Teams(id, name, null, ownerId, teams.getPersonCount(), teams.getCreatetime(), now);
        teamMapper.updateById(newteam);
        res.put("error_message", "success");
        return res;
    }

    @Override
    public JSONObject getTeam(Integer page) {
        JSONObject res = new JSONObject();
        IPage<Teams> groupIPage = new Page<>(page, 10);
        QueryWrapper<Teams> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("modifytime");
        List<Teams> teams = teamMapper.selectPage(groupIPage, queryWrapper).getRecords();
        Integer teams_count = Math.toIntExact(teamMapper.selectCount(null));
        res.put("teams", teams);
        res.put("teams_count", teams_count);
        return res;
    }

    @Override
    public JSONObject searchTeam(String name) {
        JSONObject res = new JSONObject();
        if ("".equals(name)) {
            res.put("error_message", "请输入要查找的群名！");
            return res;
        }
        QueryWrapper<Teams> qwteams = new QueryWrapper<>();
        qwteams.like("name", name);
        List<Teams> teams = teamMapper.selectList(qwteams);
        if(teams.size() == 0) {
            res.put("error_message", "该群不存在");
            return res;
        }
        res.put("search_res", teams);
        res.put("error_message", "success");
        return res;
    }
}

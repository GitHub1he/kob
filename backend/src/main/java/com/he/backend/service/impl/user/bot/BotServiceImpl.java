package com.he.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.he.backend.mapper.BotMapper;
import com.he.backend.pojo.Bot;
import com.he.backend.pojo.User;
import com.he.backend.service.impl.utils.UserDetailsImpl;
import com.he.backend.service.user.bot.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotServiceImpl implements BotService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> botAdd(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        String title = data.get("title");
        String description = data.get("descriptioin");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

        if(title == null || title.length() == 0){
            map.put("error_message","标题不能为空");
            return map;
        }
        if(title.length() > 30){
            map.put("error_message", "标题长度不能大于30");
            return map;
        }
        if(description != null && description.length() > 300){
            map.put("error_message", "描述长度不能大于300");
            return map;
        }
        if(description == null || description.length() == 0){
            description = "这个用户是懒蛋,什么都没有留下~~";
        }
        if(content == null || content.length() == 0) {
            map.put("error_message", "代码不能为空");
            return map;
        }
        if(content.length()> 10000){
            map.put("error_message", "代码长度不能超过10000");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(null,user.getId(),title,description,content,null,now,now);

        botMapper.insert(bot);

        map.put("error_message","success");
        return map;
    }

    @Override
    public Map<String, String> botDel(String bot_id) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        Map<String, String> map = new HashMap<>();
        Bot bot = botMapper.selectById(bot_id);
        if(bot == null || bot.getUserId() != user.getId()){
            map.put("error_message","bot不存在或已被删除");
            return map;
        }

        botMapper.deleteById(bot_id);

        map.put("error_message","success");
        return map;
    }

    @Override
    public Map<String, String> botUpd(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        String bot_id = data.get("bot_id");
        String title = data.get("title");
        String description = data.get("descriptioin");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();
        Bot bot = botMapper.selectById(bot_id);
        if(bot == null || bot.getUserId() != user.getId()){
            map.put("error_message","bot不存在或已被删除");
            return map;
        }
        if(title == null || title.length() == 0) title = bot.getTitle();
        if(description == null || description.length() == 0) description = bot.getDescription();
        if(content == null || content.length() == 0) content = bot.getContent();

        Date now = new Date();
        Bot newbot = new Bot(null, user.getId(), title, description, content, bot.getRating(), bot.getCreatetime(), now);

        UpdateWrapper<Bot> botUpdateWrapper = new UpdateWrapper<>();
        botUpdateWrapper.eq("id",bot_id);
        botMapper.update(newbot,botUpdateWrapper);

        map.put("error_message","success");
        return map;
    }

    @Override
    public List<Bot> botGetList() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        return botMapper.selectList(queryWrapper);
    }
}

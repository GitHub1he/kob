package com.he.backend.controller.user.bot;

import com.he.backend.pojo.Bot;
import com.he.backend.service.user.bot.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BotController {
    @Autowired
    private BotService botService;

    @PostMapping("/api/user/bot/add/")
    public Map<String, String> botAdd(@RequestParam Map<String, String> data){
        return botService.botAdd(data);
    }

    @DeleteMapping("/api/user/bot/del/")
    public Map<String, String> botDel(@RequestParam Map<String, String> data){
        String bot_id = data.get("bot_id");
        return botService.botDel(bot_id);
    }

    @PostMapping("/api/user/bot/update/")
    public Map<String, String> botUpd(@RequestParam Map<String, String> data) {
        return botService.botUpd(data);
    }

    @GetMapping("/api/user/bot/getlist/")
    public List<Bot> getBotList(){
        return botService.botGetList();
    }
}

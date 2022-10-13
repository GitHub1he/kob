package com.he.backend.service.user.bot;

import com.he.backend.pojo.Bot;

import java.util.List;
import java.util.Map;

public interface BotService {
    Map<String, String> botAdd(Map<String, String> data);
    Map<String, String> botDel(String bot_id);
    Map<String, String> botUpd(Map<String, String> data);
    List<Bot> botGetList();
}

package com.he.botrunningsystem.service.impl;

import com.he.botrunningsystem.service.BotRunningService;
import com.he.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botPool = new BotPool();
    @Override
    public String addBot(Integer userId, String botCode, String input) {
        botPool.addBot(userId, botCode, input);
        return "add bot success";
    }
}

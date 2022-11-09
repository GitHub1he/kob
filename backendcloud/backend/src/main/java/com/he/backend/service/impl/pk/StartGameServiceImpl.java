package com.he.backend.service.impl.pk;

import com.he.backend.consumerpk.PkWebSocketServer;
import com.he.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId,Integer bId, Integer bBotId) {
        System.out.println("start game : " + aId + " " + bId);
        PkWebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "start game success";
    }
}

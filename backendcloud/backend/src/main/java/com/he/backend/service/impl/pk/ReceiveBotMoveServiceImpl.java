package com.he.backend.service.impl.pk;

import com.he.backend.consumer.WebSocketServer;
import com.he.backend.consumer.utils.Game;
import com.he.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receiveBotMove : " + userId + " " + direction);
        if(WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if(game != null) {
                if(game.getPlayerA().getId().equals(userId)){
                    game.setNextStepA(direction);
                }else if(game.getPlayerB().getId().equals(userId)){
                    game.setNextStepB(direction);
                }
            }
        }

        return "receive bot move success";
    }
}

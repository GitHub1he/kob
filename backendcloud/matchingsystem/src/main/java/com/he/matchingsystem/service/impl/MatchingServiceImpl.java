package com.he.matchingsystem.service.impl;

import com.he.matchingsystem.service.MatchingService;
import com.he.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("add player: " + userId + " " + rating);
        matchingPool.addPlayer(userId, rating);
        return "add player success";
    }

    @Override
    public String remove(Integer userId) {
        System.out.println("remove player" + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}

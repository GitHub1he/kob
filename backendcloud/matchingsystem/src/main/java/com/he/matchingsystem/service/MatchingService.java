package com.he.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer rating, Integer botId) ;
    String remove(Integer userId);
}

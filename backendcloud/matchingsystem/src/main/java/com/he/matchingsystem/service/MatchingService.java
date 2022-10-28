package com.he.matchingsystem.service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer rating) ;
    String remove(Integer userId);
}

package com.he.chatSystem.controller;

import com.he.matchingsystem.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class MatchingController {
    @Autowired

    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data){

    }

    @PostMapping("/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data){

    }
}

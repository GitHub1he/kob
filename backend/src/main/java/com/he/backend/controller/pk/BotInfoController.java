package com.he.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/pk/")
public class BotInfoController {

    @RequestMapping("getuserinfo/")
    public Map<String,String> getBotInfo(){
        System.out.println("1");
        Map<String,String> map = new LinkedHashMap<>();
        map.put("name","黄恩");
        map.put("age","18");
        return map;
    }
}

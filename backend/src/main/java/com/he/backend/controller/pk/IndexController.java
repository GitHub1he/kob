package com.he.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pk/")
public class IndexController {
    @RequestMapping("index/")
    public String index(){
        return "pk/index.html";
    }
}

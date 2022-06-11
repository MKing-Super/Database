package pers.mk.activemq.second.controller;

import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class ActiveMqController {

    public static String receiveMessage = "";

    @GetMapping("/second-receive")
    public String test(Model model) {
        model.addAttribute("result", receiveMessage);
        return "/index";
    }

}

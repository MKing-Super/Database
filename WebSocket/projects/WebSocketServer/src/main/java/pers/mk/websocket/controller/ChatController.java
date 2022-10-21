package pers.mk.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.mk.websocket.util.UserContant;

/**
 * @Description: TODO
 * @Author: kun.ma
 * @Date: 2022/10/21 14:04
 */
@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatController {


    @GetMapping("/login")
    @ResponseBody
    public String login(String userName){
        return "ee";
    }

}

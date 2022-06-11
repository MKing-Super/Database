package pers.mk.activemq.first.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.jms.*;


@Controller
public class ActiveMqController {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "queueDestination")
    private Destination destination;

    @GetMapping("/text-message")
    public String test(String textMessage, Model model){
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage mkmk = session.createTextMessage(textMessage);
                return mkmk;
            }
        });
        model.addAttribute("result","发送成功~");
        return "/index";
    }

}

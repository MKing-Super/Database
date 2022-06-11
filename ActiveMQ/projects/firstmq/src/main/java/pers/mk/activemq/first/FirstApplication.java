package pers.mk.activemq.first;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:/config/*.xml"})
public class FirstApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(FirstApplication.class, args);
        Object queueDestination = run.getBean("queueDestination");
        System.out.println(queueDestination);
    }

}

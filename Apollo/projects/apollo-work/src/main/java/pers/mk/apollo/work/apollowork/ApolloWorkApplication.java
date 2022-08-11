package pers.mk.apollo.work.apollowork;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ApolloWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApolloWorkApplication.class, args);
        log.info(">>>>>>>>>>>>>>>>>>>> ApolloWorkApplication started~");
    }

}

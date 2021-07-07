package com.lyf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableTransactionManagement
@EnableKafka
@SpringBootApplication
public class SecondsKillDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondsKillDemoApplication.class, args);
    }

}

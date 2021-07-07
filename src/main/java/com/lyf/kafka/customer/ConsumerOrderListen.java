package com.lyf.kafka.customer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lyf.model.Stock;
import com.lyf.service.StockOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ConsumerOrderListen {

    private Gson gson = new GsonBuilder().create();

    @Autowired
    private StockOrderService orderService;

    @KafkaListener(topics = "SECONDS-KILL-TOPIC")
    public void listen(ConsumerRecord<String, String> record)  {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        // Object -> String
        String message = (String) kafkaMessage.get();
        // 反序列化
        Stock stock = gson.fromJson((String) message, Stock.class);

        try {
            // 创建订单
            orderService.consumerTopicToCreateOrderWithKafka(stock);
        }catch (Exception e){
            log.info("kafka消费:"+e.getMessage());
        }

    }
}

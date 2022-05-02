package com.prabhakar.pubsubwithboot.controller;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PubSubDemoController {

    String message;
    @GetMapping("getMessage")
    public String getMessage(){
        return "Message from GCP "+message;
    }

    @Bean
    public PubSubInboundChannelAdapter messageAdapter(
            @Qualifier("pubsubInputChannel") MessageChannel inputChannel, PubSubTemplate template){
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(
                template, "projects/gcp-learning-342413/subscriptions/my-topic-sub");
        adapter.setOutputChannel(inputChannel);
        return adapter;
    }

    @Bean
    MessageChannel pubsubInputChannel(){
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void receiveMessage(String payload){
        this.message = payload;
        System.out.println(payload);
    }
}

package com.payoneer.workers.mailer.mailworkerservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ServerConfiguration {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("job-exchange");
    }

    @Bean
    public Queue queue() {
        return new Queue("response-queue", false, false, false);
    }

    @Bean
    public Binding binding(DirectExchange directExchange) {
        return BindingBuilder.bind(queue())
                .to(directExchange)
                .with("response-queue");
    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

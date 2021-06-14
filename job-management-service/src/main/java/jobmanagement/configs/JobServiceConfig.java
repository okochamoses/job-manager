package jobmanagement.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.context.annotation.Configuration
public class JobServiceConfig {
    public static final String DIRECT_EXCHANGE = "job-exchange";
    public static final String RESPONSE_QUEUE = "response-queue";
    @Value("${queue.workers}")
    private String[] workers;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(RabbitTemplate rabbitTemplate) {
        return new AsyncRabbitTemplate(rabbitTemplate);
    }

    @Bean
    public Declarables declarables() {
        var bindings = getBindings();
        var responseBinding = BindingBuilder.bind(new Queue(RESPONSE_QUEUE, false, false, false)).to(directExchange()).with(RESPONSE_QUEUE);
        bindings.add(responseBinding);

        return new Declarables(bindings);
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        ConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);
        queues().values().forEach(amqpAdmin::declareQueue);
        return amqpAdmin;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(); //single threaded by default
    }

    public Map<String, Queue> queues() {
        Map<String, Queue> qs = new HashMap<>();
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 100);

        List<String> exchangeList = new ArrayList<>(Arrays.asList(workers));
        exchangeList.forEach(exchange -> qs.put(exchange, new Queue(exchange, false, false, false, args)));

        qs.put(RESPONSE_QUEUE, new Queue(RESPONSE_QUEUE, false, false, false));

        return qs;
    }

    private List<Declarable> getBindings() {
        return Arrays.stream(workers).map(key -> BindingBuilder.bind(
                this.queues().get(key)).
                to(directExchange()).with(key)).collect(Collectors.toList());
    }

}

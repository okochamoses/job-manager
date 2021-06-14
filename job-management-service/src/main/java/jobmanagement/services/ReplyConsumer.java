package jobmanagement.services;


import com.payoneer.jobmanagement.data.models.ServiceResponse;
import jobmanagement.repositories.JobRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static jobmanagement.configs.JobServiceConfig.RESPONSE_QUEUE;

@Component
public class ReplyConsumer {
    private final JobRepository jobRepository;

    public ReplyConsumer(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @RabbitListener(queues = RESPONSE_QUEUE, concurrency = "10")
    public void receive(ServiceResponse serviceResponse, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        jobRepository.findById(correlationId).subscribe(data -> {
            data.setStatus(serviceResponse.getCode());
            jobRepository.save(data).subscribe();
        });
    }
}

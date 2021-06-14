package jobmanagement.services;

import com.payoneer.jobmanagement.data.enums.Status;
import jobmanagement.models.Job;
import jobmanagement.models.JobDto;
import jobmanagement.repositories.JobRepository;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static jobmanagement.configs.JobServiceConfig.DIRECT_EXCHANGE;
import static jobmanagement.configs.JobServiceConfig.RESPONSE_QUEUE;

@Service
public class QueueService {
    private final RabbitTemplate rabbitTemplate;
    private final JobRepository jobRepository;

    public QueueService(RabbitTemplate rabbitTemplate, JobRepository jobRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.jobRepository = jobRepository;
    }

    public void queueJob(JobDto jobDto) {
        Job<Object> job = new Job<>();
        job.setName(jobDto.getName());
        job.setData(jobDto.getData());
        job.setStatus(Status.QUEUED);
        job.setPriority(jobDto.getPriority());

        var savedJob = jobRepository.save(job).block();

        rabbitTemplate.convertAndSend(
                DIRECT_EXCHANGE,
                job.getName(),
                job.getData(),
                getMessagePostProcessor(savedJob)
        );

        job.setStatus(Status.RUNNING);
        jobRepository.save(job).subscribe();
    }

    private MessagePostProcessor getMessagePostProcessor(Job<Object> savedJob) {
        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setReplyTo(RESPONSE_QUEUE);
            messageProperties.setCorrelationId(savedJob.getId());
            messageProperties.setPriority(savedJob.getPriority());
            return message;
        };
    }
}

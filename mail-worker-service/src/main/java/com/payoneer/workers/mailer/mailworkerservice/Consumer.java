package com.payoneer.workers.mailer.mailworkerservice;

import com.payoneer.jobmanagement.data.enums.Status;
import com.payoneer.jobmanagement.data.models.ServiceResponse;
import com.payoneer.workers.mailer.mailworkerservice.models.MailWorkerTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class Consumer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "mail", concurrency = "10")
    public ServiceResponse receive(MailWorkerTemplate mailWorkerTemplate) {
        ServiceResponse response;
        try {
            // SEND EMAIL LOGIC
            log.info("Sending email to " + mailWorkerTemplate.getRecipient());
            log.info(mailWorkerTemplate.toString());

            response = new ServiceResponse(Status.SUCCESS, null);
        } catch (Exception e) {
            response = new ServiceResponse(Status.FAILED, e.getMessage());
        }
        return response;
    }
}

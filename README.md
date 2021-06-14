### INTRO
The main problems I looked to solve in this project was the ability to scale the job-service and workers horizontally and provide optimum durability from poor network io and server crashes/restarts.

### Running the service
```mvn package```

```java -jar ./job-management-service/target/job-management-service-0.0.1-SNAPSHOT.jar```

```java -jar ./mail-worker-service/target/mail-worker-service-0.0.1-SNAPSHOT.jar```

You can reach the server via 
```
Method: POST 

URL: http://localhost:8080/job
{
    "name": "mail",
    "schedule": {
        "cron": "*/5 * * * * MON-FRI",
        "timeZone": "Australia/Darwin"
    },
    "priority": 10,
    "data": {
        "sender": "okocha.chukwutem@gmail.com",
        "recipient": "test@email.com",
        "message": "This is a test message",
        "isHtml": false
    }
}
```

##FLOW
- There is a job service which can receive jobs from an external service via REST, AMQP, or any other protocol.
- The service connects to different workers through rabbit mq via a Queue and Routing Key specific to that type if worker. The server does not need any acknowledgement from the worker queue.
- The worker processes the job and sends a message through the "response-queue" channel and any available `job-service` picks it up and updates the state of the job. This process is stateless, so any job-service instance can get the response from any worker instance

#### FLEXIBILITY
Any string defined in the `queue.workers` property in the job-service `application.properties` file automatically generates a queue and routing-key for that string.
This helps in you not needing to refactor the job service to create new job types and processes. 
You can instead, create a standalone service that listens to that queue name.

 #### RELIABILITY
 Jobs always get their status returned from the worker processing them.
 
 #### INTERNAL CONSISTENCY
 
Job status' are stored on a mongodb database and updated according to the current state of the job in the system

#### PRIORITY
A field for priority can be populated from 1 - 100 to give the Job higher or lower precedence. This will bw handled by rabbitmq

#### SCHEDULING
A `scheduledJob` collection is available on the DB to store jobs that have theie scheduled fields populated. Scheduling uses a cron string and a time zone passed from the external service delegating the task. This scheduled job is then saved to the DB and put in a `taskSchedule`. 

If the job-service is restarted, all the scheduled jobs are pulled from the DB and stored in put in task schedules again that run according to the cron.


### Conclusion
Time spent for architecture: 1 day

Time spent for implementation: 2 days



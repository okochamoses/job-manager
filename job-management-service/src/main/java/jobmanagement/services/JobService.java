package jobmanagement.services;

import jobmanagement.models.JobDto;
import jobmanagement.models.ScheduledJob;
import jobmanagement.repositories.JobRepository;
import jobmanagement.repositories.ScheduledJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.ZoneId;

@Service
public class JobService {
    private final QueueService queueService;
    private final JobRepository jobRepository;
    private final ScheduledJobRepository scheduledJobRepository;
    @Autowired
    private TaskScheduler executor;

    @PostConstruct
    void init() {
        scheduledJobRepository.findByScheduleIsNotNull()
                .subscribe(data -> this.scheduleJob(new JobDto(data.getName(), data.getSchedule(), data.getData(), data.getPriority())));
    }

    public JobService(QueueService queueService, JobRepository jobRepository, ScheduledJobRepository scheduledJobRepository) {
        this.queueService = queueService;
        this.jobRepository = jobRepository;
        this.scheduledJobRepository = scheduledJobRepository;
    }

    public void createJob(JobDto jobDto) {
        if (jobDto.getSchedule() != null && jobDto.getSchedule().getCron() != null) {
            saveScheduledJob(jobDto);
            return;
        }

        queueService.queueJob(jobDto);
    }

    private void saveScheduledJob(JobDto jobDto) {
        ScheduledJob<Object> job = new ScheduledJob<>();
        job.setName(jobDto.getName());
        job.setSchedule(jobDto.getSchedule());
        job.setData(jobDto.getData());
        job.setPriority(jobDto.getPriority());

        scheduledJobRepository.save(job).subscribe();

        scheduleJob(jobDto);
    }

    private void scheduleJob(JobDto jobDto) {
        Runnable task = () -> queueService.queueJob(jobDto);
        executor.schedule(
                task,
                new CronTrigger(jobDto.getSchedule().getCron(),
                        ZoneId.of(jobDto.getSchedule().getTimeZone()))
        );
    }
}

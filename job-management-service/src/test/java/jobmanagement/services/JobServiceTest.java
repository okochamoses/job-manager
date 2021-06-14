package jobmanagement.services;

import jobmanagement.models.JobDto;
import jobmanagement.models.Schedule;
import jobmanagement.models.ScheduledJob;
import jobmanagement.repositories.JobRepository;
import jobmanagement.repositories.ScheduledJobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private ScheduledJobRepository scheduledJobRepository;
    @Mock
    private QueueService queueService;
    @Mock
    private TaskScheduler taskScheduler;
    @InjectMocks
    private JobService jobService;


    @Test
    void unscheduled_jobs_are_queued() {

        jobService.createJob(new JobDto());

        verify(queueService, times(1)).queueJob(any(JobDto.class));
    }

    @Test
    void scheduled_jobs_are_saved_to_scheduledjob_db() {
        JobDto jobDto = new JobDto();
        jobDto.setData("");
        jobDto.setPriority(1);
        jobDto.setName("mail");
        jobDto.setSchedule(new Schedule("*/5 * * * * MON-FRI", "Australia/Darwin"));

        when(scheduledJobRepository.save(any(ScheduledJob.class))).thenReturn(Mono.just(new ScheduledJob<>()));

        jobService.createJob(jobDto);

        verify(scheduledJobRepository, times(1)).save(any(ScheduledJob.class));
    }
}
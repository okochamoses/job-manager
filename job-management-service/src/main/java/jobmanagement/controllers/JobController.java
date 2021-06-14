package jobmanagement.controllers;

import jobmanagement.models.JobDto;
import jobmanagement.services.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<Void> createJob(@RequestBody JobDto jobDto) {
        this.jobService.createJob(jobDto);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
}

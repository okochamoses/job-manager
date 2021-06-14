package jobmanagement.repositories;

import jobmanagement.models.ScheduledJob;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ScheduledJobRepository extends ReactiveMongoRepository<ScheduledJob, String> {
    Flux<ScheduledJob<Object>> findByScheduleIsNotNull();
}

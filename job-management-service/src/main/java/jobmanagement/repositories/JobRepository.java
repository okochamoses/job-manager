package jobmanagement.repositories;

import jobmanagement.models.Job;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends ReactiveMongoRepository<Job, String> {
}

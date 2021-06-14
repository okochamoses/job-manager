package jobmanagement.models;

import com.payoneer.jobmanagement.data.enums.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job<T> extends GenericJob<T> {
    @Id
    private String id;
    private Status status;
}


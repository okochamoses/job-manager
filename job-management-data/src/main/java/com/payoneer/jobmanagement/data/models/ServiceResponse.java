package com.payoneer.jobmanagement.data.models;

import com.payoneer.jobmanagement.data.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceResponse {
    private Status code;
    private String message;
}

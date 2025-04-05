package com.hipradeep.userservices.payload;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

    private String message;

    private boolean successStatus;

    private HttpStatus status;
}

package com.grs.helpdeskmodule.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class Response <T>{
    private HttpStatus status;
    private String message;
    private T data;
}

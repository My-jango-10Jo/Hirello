package com.sparta.hirello.secondary.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {

    private int statusCode;
    private String msg;

}

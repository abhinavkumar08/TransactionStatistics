package com.n26.transaction.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Date timestamp;
    private String message;
    private String details;

}

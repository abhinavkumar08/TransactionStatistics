package com.n26.transaction.model;

import lombok.*;
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private String amount;
    private String timestamp;
}

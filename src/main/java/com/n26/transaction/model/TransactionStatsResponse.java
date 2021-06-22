package com.n26.transaction.model;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatsResponse {
    private String sum;
    private String avg;
    private String max;
    private String min;
    private Long count;
}

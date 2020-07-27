package com.tech.challenge.controller.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLicensedMatchesResponse {
    private Long matchId;
    private String startDate;
    private String playerA;
    private String playerB;
    private String summary;
}

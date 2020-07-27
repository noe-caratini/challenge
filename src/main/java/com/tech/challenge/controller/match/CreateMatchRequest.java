package com.tech.challenge.controller.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMatchRequest {
    private Long tournamentId;
    private String startDate;
    private String playerA;
    private String playerB;
}

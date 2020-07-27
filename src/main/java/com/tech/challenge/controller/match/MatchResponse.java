package com.tech.challenge.controller.match;

import com.tech.challenge.model.Match;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponse {
    private Long id;
    private String startDate;
    private String playerA;
    private String playerB;

    public static MatchResponse from(Match match) {
        return new MatchResponse(
                match.getId(),
                match.getStartDate().toString(),
                match.getPlayerA(),
                match.getPlayerB());
    }
}

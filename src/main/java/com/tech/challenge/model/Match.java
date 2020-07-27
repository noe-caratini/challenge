package com.tech.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_tournament_id")
    @JsonIgnore
    private Tournament parentTournament;

    private Instant startDate;
    private String playerA;
    private String playerB;

    public Match(Tournament parentTournament, Instant startDate, String playerA, String playerB) {
        this.parentTournament = parentTournament;
        this.startDate = startDate;
        this.playerA = playerA;
        this.playerB = playerB;
    }
}

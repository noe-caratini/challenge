package com.tech.challenge.service;

import com.tech.challenge.controller.customer.request.SummaryType;
import com.tech.challenge.dao.MatchRepository;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.Tournament;
import com.tech.challenge.service.exception.TournamentNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    private MatchRepository matchRepository;
    private TournamentService tournamentService;

    public MatchService(MatchRepository matchRepository, TournamentService tournamentService) {
        this.matchRepository = matchRepository;
        this.tournamentService = tournamentService;
    }

    public Match save(Long parentTournamentId, Instant startTime, String playerA, String playerB) throws TournamentNotFoundException {
        Tournament tournament = tournamentService.findById(parentTournamentId)
                .orElseThrow(() -> new TournamentNotFoundException("No tournament found for id " + parentTournamentId));

        Match newMatch = new Match(tournament, startTime, playerA, playerB);

        return matchRepository.save(newMatch);
    }

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public List<Match> findAllById(Iterable<Long> ids) {
        return matchRepository.findAllById(ids);
    }

    public Optional<Match> findById(Long id) {
        return matchRepository.findById(id);
    }

    public String getSummaryFor(Match match, SummaryType type, Instant refTime) {
        StringBuilder summary = new StringBuilder();

        if (type == SummaryType.AVB || type == SummaryType.AVB_TIME) {
            summary.append(match.getPlayerA())
                    .append(" vs ")
                    .append(match.getPlayerB());
        }

        if (type == SummaryType.AVB_TIME) {
            if (refTime.isBefore(match.getStartDate())) {
                summary.append(" starts in ")
                        .append(refTime.until(match.getStartDate(), ChronoUnit.MINUTES))
                        .append(" minutes");
            } else {
                summary.append(", started ")
                        .append(match.getStartDate().until(refTime, ChronoUnit.MINUTES))
                        .append(" minutes ago");
            }
        }

        return summary.toString();
    }
}

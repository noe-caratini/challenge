package com.tech.challenge.service;

import com.tech.challenge.TestData;
import com.tech.challenge.controller.customer.request.SummaryType;
import com.tech.challenge.dao.MatchRepository;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private TournamentService tournamentService;

    private MatchService matchService;

    @BeforeEach
    public void setUp() {
        matchService = new MatchService(matchRepository, tournamentService);
    }

    @Test
    public void saveMatchShouldCallRepositoryAndReturnSavedEntity() throws Exception {
        Tournament tournament = TestData.generateTournament(10L);
        when(tournamentService.findById(tournament.getId())).thenReturn(Optional.of(tournament));

        Match match = TestData.generateMatch(14L, tournament);
        when(matchRepository.save(any())).thenReturn(match);

        Match result = matchService.save(tournament.getId(), match.getStartDate(), match.getPlayerA(), match.getPlayerB());

        assertThat(result.getId()).isEqualTo(match.getId());
        assertThat(result.getParentTournament()).isEqualToComparingFieldByField(tournament);
        assertThat(result.getStartDate()).isEqualTo(match.getStartDate());
        assertThat(result.getPlayerA()).isEqualTo(match.getPlayerA());
        assertThat(result.getPlayerB()).isEqualTo(match.getPlayerB());
    }

    @Test
    public void saveMatchShouldThrowExceptionIdTournamentNotFound() {
        assertThatThrownBy(() -> matchService.save(10L, Instant.now(), "A", "B"))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void findAllShouldReturnListOfAllMatches() {
        Tournament tournament = TestData.generateTournament(1L);
        Match match1 = TestData.generateMatch(1L, tournament);
        Match match2 = TestData.generateMatch(2L, tournament);

        when(matchRepository.findAll()).thenReturn(newArrayList(match1, match2));

        List<Match> result = matchService.findAll();

        assertThat(result).containsExactlyInAnyOrder(match1, match2);
    }

    @Test
    public void findAllByIdShouldReturnListOfMatches() {
        Tournament tournament = TestData.generateTournament(1L);
        Match match1 = TestData.generateMatch(1L, tournament);
        Match match2 = TestData.generateMatch(2L, tournament);
        Match match3 = TestData.generateMatch(3L, tournament);

        List<Long> idsToFind = newArrayList(1L, 2L);

        when(matchRepository.findAllById(idsToFind)).thenReturn(newArrayList(match1, match2));

        List<Match> result = matchService.findAllById(idsToFind);

        assertThat(result).containsExactlyInAnyOrder(match1, match2);
        assertThat(result).doesNotContain(match3);
    }

    @Test
    public void findByIdShouldReturnCorrectMatch() {
        Tournament tournament = TestData.generateTournament(1L);
        Match match1 = TestData.generateMatch(1L, tournament);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match1));

        Optional<Match> result = matchService.findById(1L);

        assertThat(result).isNotEmpty();
        assertThat(result).contains(match1);
    }

    @Test
    public void summaryShouldBeBlankIfSummaryTypeIsNull() {
        Tournament tournament = TestData.generateTournament(1L);
        Match match = TestData.generateMatch(1L, tournament);

        String result = matchService.getSummaryFor(match, null, Instant.now());

        assertThat(result).isBlank();
    }

    @Test
    public void summaryShouldBeCorrectForAVBType() {
        Tournament tournament = TestData.generateTournament(1L);
        Match match = TestData.generateMatch(1L, tournament);

        String result = matchService.getSummaryFor(match, SummaryType.AVB, Instant.now());

        assertThat(result).isEqualTo(match.getPlayerA() + " vs " + match.getPlayerB());
    }

    @Test
    public void summaryShouldBeCorrectForAVBTimeTypeInFuture() {
        Instant now = Instant.now();
        Instant in2Minutes = now.plus(2, ChronoUnit.MINUTES);

        Tournament tournament = TestData.generateTournament(1L);
        Match match = TestData.generateMatch(1L, tournament);
        match.setStartDate(in2Minutes);

        String result = matchService.getSummaryFor(match, SummaryType.AVB_TIME, now);

        assertThat(result).isEqualTo(match.getPlayerA() + " vs " + match.getPlayerB() + " starts in " + now.until(in2Minutes, ChronoUnit.MINUTES) + " minutes");
    }

    @Test
    public void summaryShouldBeCorrectForAVBTimeTypeInPast() {
        Instant now = Instant.now();
        Instant twoMinutesAgo = now.minus(2, ChronoUnit.MINUTES);

        Tournament tournament = TestData.generateTournament(1L);
        Match match = TestData.generateMatch(1L, tournament);
        match.setStartDate(twoMinutesAgo);

        String result = matchService.getSummaryFor(match, SummaryType.AVB_TIME, now);

        assertThat(result).isEqualTo(match.getPlayerA() + " vs " + match.getPlayerB() + ", started " + twoMinutesAgo.until(now, ChronoUnit.MINUTES) + " minutes ago");
    }
}
package com.tech.challenge.service;

import com.tech.challenge.TestData;
import com.tech.challenge.dao.TournamentRepository;
import com.tech.challenge.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    private TournamentService tournamentService;

    @BeforeEach
    public void setUp() {
        tournamentService = new TournamentService(tournamentRepository);
    }

    @Test
    public void saveTournamentShouldCallRepositoryAndReturnSavedEntity() {
        Tournament tournament = TestData.generateTournament(10L);

        when(tournamentRepository.save(argThat(t -> t.getName().equals(tournament.getName()))))
                .thenReturn(tournament);

        Tournament result = tournamentService.save(tournament.getName());

        assertThat(result.getId()).isEqualTo(tournament.getId());
        assertThat(result.getName()).isEqualTo(tournament.getName());
    }

    @Test
    public void findAllShouldReturnListOfAllTournaments() {
        Tournament tournament1 = TestData.generateTournament(1L);
        Tournament tournament2 = TestData.generateTournament(2L);

        when(tournamentRepository.findAll()).thenReturn(newArrayList(tournament1, tournament2));

        List<Tournament> result = tournamentService.findAll();

        assertThat(result).containsExactlyInAnyOrder(tournament1, tournament2);
    }

    @Test
    public void findAllByIdShouldReturnListOfTournaments() {
        Tournament tournament1 = TestData.generateTournament(1L);
        Tournament tournament2 = TestData.generateTournament(2L);
        Tournament tournament3 = TestData.generateTournament(3L);
        List<Long> idsToFind = newArrayList(1L, 2L);

        when(tournamentRepository.findAllById(idsToFind)).thenReturn(newArrayList(tournament1, tournament2));

        List<Tournament> result = tournamentService.findAllById(idsToFind);

        assertThat(result).containsExactlyInAnyOrder(tournament1, tournament2);
        assertThat(result).doesNotContain(tournament3);
    }

    @Test
    public void findByIdShouldReturnCorrectTournament() {
        Tournament tournament1 = TestData.generateTournament(1L);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament1));

        Optional<Tournament> result = tournamentService.findById(1L);

        assertThat(result).isNotEmpty();
        assertThat(result).contains(tournament1);
    }
}
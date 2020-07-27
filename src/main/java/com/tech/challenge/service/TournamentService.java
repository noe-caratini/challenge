package com.tech.challenge.service;

import com.tech.challenge.dao.TournamentRepository;
import com.tech.challenge.model.Tournament;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    private TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public Tournament save(String name) {
        Tournament newTournament = new Tournament(name);
        return tournamentRepository.save(newTournament);
    }

    public List<Tournament> findAll() {
        return tournamentRepository.findAll();
    }

    public List<Tournament> findAllById(Iterable<Long> ids) {
        return tournamentRepository.findAllById(ids);
    }

    public Optional<Tournament> findById(Long id) {
        return tournamentRepository.findById(id);
    }
}

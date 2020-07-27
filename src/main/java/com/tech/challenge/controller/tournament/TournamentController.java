package com.tech.challenge.controller.tournament;

import com.tech.challenge.service.TournamentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournament")
public class TournamentController {

    private TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping("/save")
    public ResponseEntity saveTournament(@RequestBody CreateTournamentRequest request) {
        if (StringUtils.isBlank(request.getName())) {
            return ResponseEntity.badRequest().build();
        }

        tournamentService.save(request.getName());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity listTournaments() {
        return ResponseEntity.ok(tournamentService.findAll());
    }
}

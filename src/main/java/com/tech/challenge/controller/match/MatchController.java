package com.tech.challenge.controller.match;

import com.tech.challenge.service.MatchService;
import com.tech.challenge.service.exception.TournamentNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/match")
public class MatchController {

    private MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/save")
    public ResponseEntity saveMatch(@RequestBody CreateMatchRequest request) {
        if (request.getTournamentId() == null ||
                StringUtils.isBlank(request.getStartDate()) ||
                StringUtils.isBlank(request.getPlayerA()) ||
                StringUtils.isBlank(request.getPlayerB())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Instant startDate = OffsetDateTime.parse(request.getStartDate()).toInstant();

            matchService.save(request.getTournamentId(),
                    startDate,
                    request.getPlayerA(),
                    request.getPlayerB());

            return ResponseEntity.noContent().build();
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (TournamentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity listMatches() {
        return ResponseEntity.ok(matchService.findAll().stream().map(MatchResponse::from).collect(toList()));
    }
}

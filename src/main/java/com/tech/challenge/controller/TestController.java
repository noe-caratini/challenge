package com.tech.challenge.controller;

import com.tech.challenge.model.Customer;
import com.tech.challenge.model.License;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.Tournament;
import com.tech.challenge.model.licensetype.LicenseType;
import com.tech.challenge.service.CustomerService;
import com.tech.challenge.service.LicenseService;
import com.tech.challenge.service.MatchService;
import com.tech.challenge.service.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/test")
public class TestController {

    private final CustomerService customerService;
    private final TournamentService tournamentService;
    private final MatchService matchService;
    private final LicenseService licenseService;

    public TestController(CustomerService customerService,
                          TournamentService tournamentService,
                          MatchService matchService,
                          LicenseService licenseService) {
        this.customerService = customerService;
        this.tournamentService = tournamentService;
        this.matchService = matchService;
        this.licenseService = licenseService;
    }

    @GetMapping
    @RequestMapping("/setup")
    public ResponseEntity setup() throws Exception {
        Customer customer1 = customerService.save("Customer 1");
        Customer customer2 = customerService.save("Customer 2");
        Customer customer3 = customerService.save("Customer 3");

        Tournament tournament1 = tournamentService.save("Tournament 1");
        Tournament tournament2 = tournamentService.save("Tournament 2");
        Tournament tournament3 = tournamentService.save("Tournament 3");

        Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Match match1 = matchService.save(tournament1.getId(), now.minus(10, ChronoUnit.MINUTES), "Player 1A", "Player 1B");
        Match match2 = matchService.save(tournament1.getId(), now.plus(10, ChronoUnit.MINUTES), "Player 2A", "Player 2B");
        Match match3 = matchService.save(tournament1.getId(), now.plus(60, ChronoUnit.MINUTES), "Player 3A", "Player 3B");

        Match match4 = matchService.save(tournament2.getId(), now.minus(15, ChronoUnit.MINUTES), "Player 4A", "Player 4B");
        Match match5 = matchService.save(tournament2.getId(), now.plus(15, ChronoUnit.MINUTES), "Player 5A", "Player 5B");

        Match match6 = matchService.save(tournament3.getId(), now.minus(8, ChronoUnit.MINUTES), "Player 6A", "Player 6B");
        Match match7 = matchService.save(tournament3.getId(), now.plus(8, ChronoUnit.MINUTES), "Player 7A", "Player 7B");

        License license1 = licenseService.save(customer1.getId(), LicenseType.MATCH, match2.getId());
        License license2 = licenseService.save(customer1.getId(), LicenseType.MATCH, match5.getId());
        License license3 = licenseService.save(customer1.getId(), LicenseType.MATCH, match6.getId());

        License license4 = licenseService.save(customer2.getId(), LicenseType.TOURNAMENT, tournament1.getId());
        License license5 = licenseService.save(customer2.getId(), LicenseType.TOURNAMENT, tournament3.getId());

        License license6 = licenseService.save(customer3.getId(), LicenseType.MATCH, match1.getId());
        License license7 = licenseService.save(customer3.getId(), LicenseType.MATCH, match3.getId());
        License license8 = licenseService.save(customer3.getId(), LicenseType.MATCH, match4.getId());
        License license9 = licenseService.save(customer3.getId(), LicenseType.TOURNAMENT, tournament3.getId());

        return ResponseEntity.ok().build();
    }
}

package com.tech.challenge;

import com.tech.challenge.model.Customer;
import com.tech.challenge.model.License;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.Tournament;
import com.tech.challenge.model.licensetype.LicenseType;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TestData {

    public static Tournament generateTournament(Long id) {
        Tournament tournament = new Tournament("Tournament " + id);
        tournament.setId(id);

        return tournament;
    }

    public static Match generateMatch(Long id, Tournament tournament) {
        Match match = new Match(tournament, Instant.now().truncatedTo(ChronoUnit.SECONDS).plus(2, ChronoUnit.DAYS), "Player A", "Player B");
        match.setId(id);

        return match;
    }

    public static Customer generateCustomer(Long id) {
        Customer customer = new Customer("Customer " + id);
        customer.setId(id);

        return customer;
    }

    public static License generateLicenseForMatch(Long id, Customer customer, Match match) {
        License license = new License(customer, LicenseType.MATCH, match.getId());
        license.setId(id);

        return license;
    }

    public static License generateLicenseForTournament(Long id, Customer customer, Tournament tournament) {
        License license = new License(customer, LicenseType.TOURNAMENT, tournament.getId());
        license.setId(id);

        return license;
    }
}

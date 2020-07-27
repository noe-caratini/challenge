package com.tech.challenge.service;

import com.tech.challenge.TestData;
import com.tech.challenge.dao.LicenseRepository;
import com.tech.challenge.model.Customer;
import com.tech.challenge.model.License;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.Tournament;
import com.tech.challenge.model.licensetype.LicenseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LicenseServiceTest {

    @Mock
    private LicenseRepository licenseRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private TournamentService tournamentService;
    @Mock
    private MatchService matchService;

    private LicenseService licenseService;

    @BeforeEach
    public void setUp() {
        licenseService = new LicenseService(licenseRepository, customerService, tournamentService, matchService);
    }

    @Test
    public void saveLicenseForMatchShouldCallRepositoryAndReturnSavedEntity() throws Exception {
        Customer customer = TestData.generateCustomer(20L);
        Tournament tournament = TestData.generateTournament(10L);
        Match match = TestData.generateMatch(30L, tournament);
        License license = TestData.generateLicenseForMatch(50L, customer, match);

        when(customerService.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(matchService.findById(match.getId())).thenReturn(Optional.of(match));

        when(licenseRepository.save(any())).thenReturn(license);

        License result = licenseService.save(customer.getId(), LicenseType.MATCH, match.getId());

        assertThat(result.getId()).isEqualTo(license.getId());
        assertThat(result.getCustomer()).isEqualToComparingFieldByField(customer);
        assertThat(result.getType()).isEqualTo(LicenseType.MATCH);
        assertThat(result.getContentId()).isEqualTo(match.getId());
    }

    @Test
    public void saveLicenseForMatchShouldThrowExceptionIfMatchNotFound() {
        assertThatThrownBy(() -> licenseService.save(10L, LicenseType.MATCH, 20L))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void saveLicenseForTournamentShouldCallRepositoryAndReturnSavedEntity() throws Exception {
        Customer customer = TestData.generateCustomer(20L);
        Tournament tournament = TestData.generateTournament(10L);
        License license = TestData.generateLicenseForTournament(50L, customer, tournament);

        when(customerService.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(tournamentService.findById(tournament.getId())).thenReturn(Optional.of(tournament));

        when(licenseRepository.save(any())).thenReturn(license);

        License result = licenseService.save(customer.getId(), LicenseType.TOURNAMENT, tournament.getId());

        assertThat(result.getId()).isEqualTo(license.getId());
        assertThat(result.getCustomer()).isEqualToComparingFieldByField(customer);
        assertThat(result.getType()).isEqualTo(LicenseType.TOURNAMENT);
        assertThat(result.getContentId()).isEqualTo(tournament.getId());
    }

    @Test
    public void saveLicenseForTournamentShouldThrowExceptionIfTournamentNotFound() {
        assertThatThrownBy(() -> licenseService.save(10L, LicenseType.MATCH, 20L))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void saveLicenseShouldThrowExceptionIfLicenseTypeInvalid() {
        assertThatThrownBy(() -> licenseService.save(10L, null, 20L))
                .isInstanceOf(Exception.class);
    }
}
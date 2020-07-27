package com.tech.challenge.service;

import com.tech.challenge.TestData;
import com.tech.challenge.dao.CustomerRepository;
import com.tech.challenge.model.Customer;
import com.tech.challenge.model.License;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TournamentService tournamentService;
    @Mock
    private MatchService matchService;

    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        customerService = new CustomerService(customerRepository, tournamentService, matchService);
    }

    @Test
    public void saveCustomerShouldCallRepositoryAndReturnSavedEntity() {
        Customer customer = TestData.generateCustomer(20L);
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.save(customer.getName());

        assertThat(result.getId()).isEqualTo(customer.getId());
        assertThat(result.getName()).isEqualTo(customer.getName());
    }

    @Test
    public void findAllShouldReturnListOfAllCustomers() {
        Customer customer1 = TestData.generateCustomer(1L);
        Customer customer2 = TestData.generateCustomer(2L);

        when(customerRepository.findAll()).thenReturn(newArrayList(customer1, customer2));

        List<Customer> result = customerService.findAll();

        assertThat(result).containsExactlyInAnyOrder(customer1, customer2);
    }

    @Test
    public void findByIdShouldReturnCorrectCustomer() {
        Customer customer1 = TestData.generateCustomer(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        Optional<Customer> result = customerService.findById(1L);

        assertThat(result).isNotEmpty();
        assertThat(result).contains(customer1);
    }

    @Test
    public void findAllLicensedMatchesShouldThrowExceptionIfCustomerNotFound() {
        when(customerRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findAllLicensedMatches(1L))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void findAllLicensedMatchesShouldReturnCorrectMatchesForMatchLicenses() throws Exception {
        Tournament tournament = TestData.generateTournament(1L);
        Match match1 = TestData.generateMatch(11L, tournament);
        Match match2 = TestData.generateMatch(12L, tournament);
        Match match3 = TestData.generateMatch(13L, tournament);

        Customer customer = TestData.generateCustomer(1L);

        License license1 = TestData.generateLicenseForMatch(101L, customer, match1);
        License license2 = TestData.generateLicenseForMatch(102L, customer, match2);

        customer.setLicenses(newArrayList(license1, license2));

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(matchService.findAllById(newHashSet(match1.getId(), match2.getId()))).thenReturn(newArrayList(match1, match2));

        List<Match> result = customerService.findAllLicensedMatches(customer.getId());

        assertThat(result).containsExactlyInAnyOrder(match1, match2);
        assertThat(result).doesNotContain(match3);
    }

    @Test
    public void findAllLicensedMatchesShouldReturnCorrectMatchesForTournamentLicenses() throws Exception {
        Tournament tournament1 = TestData.generateTournament(1L);
        Match match1 = TestData.generateMatch(11L, tournament1);
        Match match2 = TestData.generateMatch(12L, tournament1);
        tournament1.setMatches(newArrayList(match1, match2));

        Tournament tournament2 = TestData.generateTournament(2L);
        Match match3 = TestData.generateMatch(21L, tournament2);

        Customer customer = TestData.generateCustomer(1L);
        License license1 = TestData.generateLicenseForTournament(101L, customer, tournament1);

        customer.setLicenses(newArrayList(license1));

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(tournamentService.findAllById(newHashSet(tournament1.getId()))).thenReturn(newArrayList(tournament1));
        when(matchService.findAllById(newHashSet(match1.getId(), match2.getId()))).thenReturn(newArrayList(match1, match2));

        List<Match> result = customerService.findAllLicensedMatches(customer.getId());

        assertThat(result).containsExactlyInAnyOrder(match1, match2);
        assertThat(result).doesNotContain(match3);
    }

    @Test
    public void findAllLicensedMatchesShouldReturnCorrectMatchesForMixedLicenses() throws Exception {
        Tournament tournament1 = TestData.generateTournament(1L);
        Match match1 = TestData.generateMatch(11L, tournament1);
        Match match2 = TestData.generateMatch(12L, tournament1);
        tournament1.setMatches(newArrayList(match1, match2));

        Tournament tournament2 = TestData.generateTournament(2L);
        Match match3 = TestData.generateMatch(21L, tournament2);
        Match match4 = TestData.generateMatch(22L, tournament2);
        tournament2.setMatches(newArrayList(match3, match4));

        Customer customer = TestData.generateCustomer(1L);
        License license1 = TestData.generateLicenseForTournament(101L, customer, tournament1);
        License license2 = TestData.generateLicenseForMatch(102L, customer, match4);

        customer.setLicenses(newArrayList(license1, license2));

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(tournamentService.findAllById(newHashSet(tournament1.getId()))).thenReturn(newArrayList(tournament1));
        when(matchService.findAllById(newHashSet(match1.getId(), match2.getId(), match4.getId()))).thenReturn(newArrayList(match1, match2, match4));

        List<Match> result = customerService.findAllLicensedMatches(customer.getId());

        assertThat(result).containsExactlyInAnyOrder(match1, match2, match4);
        assertThat(result).doesNotContain(match3);
    }
}
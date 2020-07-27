package com.tech.challenge.service;

import com.tech.challenge.dao.CustomerRepository;
import com.tech.challenge.model.Customer;
import com.tech.challenge.model.License;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.licensetype.LicenseType;
import com.tech.challenge.service.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TournamentService tournamentService;
    private final MatchService matchService;

    public CustomerService(CustomerRepository customerRepository,
                           TournamentService tournamentService,
                           MatchService matchService) {
        this.customerRepository = customerRepository;
        this.tournamentService = tournamentService;
        this.matchService = matchService;
    }

    public Customer save(String customerName) {
        Customer customer = new Customer(customerName);

        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Match> findAllLicensedMatches(Long customerId) throws CustomerNotFoundException {
        Customer customer = findById(customerId).orElseThrow(() -> new CustomerNotFoundException("No customer found for id " + customerId));

        Set<Long> licensedMatchesIds = customer.getLicenses().stream()
                .filter(license -> license.getType() == LicenseType.MATCH)
                .mapToLong(License::getContentId)
                .boxed()
                .collect(toSet());

        Set<Long> licensedTournamentIds = customer.getLicenses().stream()
                .filter(license -> license.getType() == LicenseType.TOURNAMENT)
                .mapToLong(License::getContentId)
                .boxed()
                .collect(toSet());

        Set<Long> licensedTournamentMatchesIds = tournamentService.findAllById(licensedTournamentIds).stream()
                .flatMap(tournament -> tournament.getMatches().stream().mapToLong(Match::getId).boxed())
                .collect(toSet());

        licensedMatchesIds.addAll(licensedTournamentMatchesIds);

        return matchService.findAllById(licensedMatchesIds);
    }
}

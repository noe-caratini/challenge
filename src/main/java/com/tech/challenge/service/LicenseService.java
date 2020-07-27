package com.tech.challenge.service;

import com.tech.challenge.dao.LicenseRepository;
import com.tech.challenge.model.Customer;
import com.tech.challenge.model.License;
import com.tech.challenge.model.licensetype.LicenseType;
import com.tech.challenge.service.exception.CustomerNotFoundException;
import com.tech.challenge.service.exception.MatchNotFoundException;
import com.tech.challenge.service.exception.NotFoundException;
import com.tech.challenge.service.exception.TournamentNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {
    private final LicenseRepository licenseRepository;
    private final CustomerService customerService;
    private final TournamentService tournamentService;
    private final MatchService matchService;

    public LicenseService(LicenseRepository licenseRepository,
                          CustomerService customerService,
                          TournamentService tournamentService,
                          MatchService matchService) {
        this.licenseRepository = licenseRepository;
        this.customerService = customerService;
        this.tournamentService = tournamentService;
        this.matchService = matchService;
    }

    public License save(Long customerId, LicenseType licenseType, Long licensedContentId) throws IllegalArgumentException, NotFoundException {
        Customer customer = customerService.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("No customer found for id " + customerId));

        switch (licenseType) {
            case MATCH:
                matchService.findById(licensedContentId).orElseThrow(() -> new MatchNotFoundException("No match found for id " + licensedContentId));
                break;
            case TOURNAMENT:
                tournamentService.findById(licensedContentId).orElseThrow(() -> new TournamentNotFoundException("No tournament found for id " + licensedContentId));
                break;
            default:
                throw new IllegalArgumentException("Invalid license type: " + licenseType.name());
        }

        License newLicense = new License(customer, licenseType, licensedContentId);

        return licenseRepository.save(newLicense);
    }
}

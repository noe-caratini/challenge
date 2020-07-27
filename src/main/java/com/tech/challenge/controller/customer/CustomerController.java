package com.tech.challenge.controller.customer;

import com.tech.challenge.controller.customer.request.CreateCustomerRequest;
import com.tech.challenge.controller.customer.request.SummaryType;
import com.tech.challenge.model.Customer;
import com.tech.challenge.model.Match;
import com.tech.challenge.service.CustomerService;
import com.tech.challenge.service.MatchService;
import com.tech.challenge.service.exception.CustomerNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final MatchService matchService;

    public CustomerController(CustomerService customerService, MatchService matchService) {
        this.customerService = customerService;
        this.matchService = matchService;
    }

    @PostMapping("/save")
    public ResponseEntity postSaveCustomer(@RequestBody CreateCustomerRequest request) {
        if (StringUtils.isBlank(request.getName())) {
            return ResponseEntity.badRequest().build();
        }

        customerService.save(request.getName());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public List<Customer> getCustomerList() {
        return customerService.findAll();
    }

    @GetMapping("/{customerId}/licenses/matches")
    public ResponseEntity getLicensedMatches(@PathVariable Long customerId,
                                             @RequestParam(required = false) String summaryType) {
        try {
            List<Match> licensedMatches = customerService.findAllLicensedMatches(customerId);

            List<CustomerLicensedMatchesResponse> response = licensedMatches.stream()
                    .map(match -> new CustomerLicensedMatchesResponse(
                            match.getId(),
                            match.getStartDate().toString(),
                            match.getPlayerA(),
                            match.getPlayerB(),
                            matchService.getSummaryFor(match, SummaryType.fromString(summaryType), Instant.now())
                    ))
                    .collect(toList());

            return ResponseEntity.ok(response);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

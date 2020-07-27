package com.tech.challenge.controller.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.challenge.TestData;
import com.tech.challenge.controller.customer.request.CreateCustomerRequest;
import com.tech.challenge.model.Customer;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.Tournament;
import com.tech.challenge.service.CustomerService;
import com.tech.challenge.service.MatchService;
import com.tech.challenge.service.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    private static final String CUSTOMER_NAME = "Some customer";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;
    @MockBean
    private MatchService matchService;

    @Test
    public void shouldReturn204OnCustomerCreated() throws Exception {
        MockHttpServletRequestBuilder builder = post("/customer/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateCustomerRequest(CUSTOMER_NAME)));

        this.mockMvc.perform(builder)
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(customerService).save(CUSTOMER_NAME);
    }

    @Test
    public void shouldReturn400WhenCustomerNameIsNull() throws Exception {
        String customerName = null;

        MockHttpServletRequestBuilder builder = post("/customer/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateCustomerRequest(customerName)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenCustomerNameIsEmpty() throws Exception {
        String customerName = "   ";

        MockHttpServletRequestBuilder builder = post("/customer/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateCustomerRequest(customerName)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn200WithListOfCustomers() throws Exception {
        Customer customer1 = TestData.generateCustomer(1L);
        Customer customer2 = TestData.generateCustomer(2L);
        List<Customer> customerList = newArrayList(customer1, customer2);

        when(customerService.findAll()).thenReturn(customerList);

        MockHttpServletRequestBuilder builder = get("/customer/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerList)))
                .andDo(print());
    }

    @Test
    public void shouldReturn200WithEmptyListIfNoCustomers() throws Exception {
        MockHttpServletRequestBuilder builder = get("/customer/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());

        verify(customerService).findAll();
    }

    @Test
    public void shouldReturn200WithListOfCustomersLicensedMatches() throws Exception {
        Tournament tournament1 = TestData.generateTournament(1L);
        Match match1 = TestData.generateMatch(11L, tournament1);
        Match match2 = TestData.generateMatch(12L, tournament1);

        Tournament tournament2 = TestData.generateTournament(2L);
        Match match3 = TestData.generateMatch(21L, tournament2);

        Customer customer = TestData.generateCustomer(1L);

        List<Match> matchList = newArrayList(match1, match2, match3);
        when(customerService.findAllLicensedMatches(customer.getId())).thenReturn(matchList);
        when(matchService.getSummaryFor(any(), any(), any())).thenReturn("");

        MockHttpServletRequestBuilder builder = get("/customer/" + customer.getId() + "/licenses/matches")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        List<CustomerLicensedMatchesResponse> expectedResponse = matchList.stream()
                .map(match -> new CustomerLicensedMatchesResponse(
                        match.getId(),
                        match.getStartDate().toString(),
                        match.getPlayerA(),
                        match.getPlayerB(),
                        ""
                ))
                .collect(toList());

        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andDo(print());
    }

    @Test
    public void shouldReturn404WhenCustomerNotFound() throws Exception {
        when(customerService.findAllLicensedMatches(any())).thenThrow(new CustomerNotFoundException("Didn't find this guy"));

        MockHttpServletRequestBuilder builder = get("/customer/" + 1L + "/licenses/matches")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        this.mockMvc.perform(builder)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenCustomerIdIsNull() throws Exception {
        MockHttpServletRequestBuilder builder = get("/customer/" + null + "/licenses/matches")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
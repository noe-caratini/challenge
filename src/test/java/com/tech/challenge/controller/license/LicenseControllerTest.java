package com.tech.challenge.controller.license;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.challenge.model.licensetype.LicenseType;
import com.tech.challenge.service.LicenseService;
import com.tech.challenge.service.exception.MatchNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LicenseController.class)
public class LicenseControllerTest {
    private static final Long CUSTOMER_ID = 1L;
    private static final Long CONTENT_ID = 10L;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LicenseService licenseService;

    @Test
    public void shouldReturn204OnLicenseCreated() throws Exception {
        MockHttpServletRequestBuilder builder = post("/license/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new RegisterLicenseRequest(CUSTOMER_ID, LicenseType.MATCH.getCode(), CONTENT_ID)));

        this.mockMvc.perform(builder)
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(licenseService).save(CUSTOMER_ID, LicenseType.MATCH, CONTENT_ID);
    }

    @Test
    public void shouldReturn404WhenContentNotFound() throws Exception {
        when(licenseService.save(any(), any(), any())).thenThrow(new MatchNotFoundException("Content not found"));

        MockHttpServletRequestBuilder builder = post("/license/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new RegisterLicenseRequest(CUSTOMER_ID, LicenseType.MATCH.getCode(), CONTENT_ID)));

        this.mockMvc.perform(builder)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenCustomerIdIsNull() throws Exception {
        Long customerId = null;

        MockHttpServletRequestBuilder builder = post("/license/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new RegisterLicenseRequest(customerId, LicenseType.MATCH.getCode(), CONTENT_ID)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenContentIdIsNull() throws Exception {
        Long contentId = null;

        MockHttpServletRequestBuilder builder = post("/license/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new RegisterLicenseRequest(CUSTOMER_ID, LicenseType.MATCH.getCode(), contentId)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenLicenseTypeIsNull() throws Exception {
        String licenseType = null;

        MockHttpServletRequestBuilder builder = post("/license/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new RegisterLicenseRequest(CUSTOMER_ID, licenseType, CONTENT_ID)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenLicenseTypeIsBlank() throws Exception {
        String licenseType = "   ";

        MockHttpServletRequestBuilder builder = post("/license/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new RegisterLicenseRequest(CUSTOMER_ID, licenseType, CONTENT_ID)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenLicenseTypeIsInvalid() throws Exception {
        String licenseType = "not a valid license type";

        MockHttpServletRequestBuilder builder = post("/license/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new RegisterLicenseRequest(CUSTOMER_ID, licenseType, CONTENT_ID)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
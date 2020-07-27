package com.tech.challenge.controller.tournament;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.challenge.TestData;
import com.tech.challenge.model.Tournament;
import com.tech.challenge.service.TournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentController.class)
public class TournamentControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    @Test
    public void shouldReturn204OnTournamentCreated() throws Exception {
        String tournamentName = "WSL Championship Tour";

        MockHttpServletRequestBuilder builder = post("/tournament/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateTournamentRequest(tournamentName)));

        this.mockMvc.perform(builder)
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(tournamentService).save(tournamentName);
    }

    @Test
    public void shouldReturn400WhenTournamentNameIsNullOrEmpty() throws Exception {
        String tournamentName = null;

        MockHttpServletRequestBuilder builder = post("/tournament/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateTournamentRequest(tournamentName)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenTournamentNameIsEmpty() throws Exception {
        String tournamentName = "   ";

        MockHttpServletRequestBuilder builder = post("/tournament/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateTournamentRequest(tournamentName)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn200WithListOfTournaments() throws Exception {
        Tournament tournament1 = TestData.generateTournament(1L);
        Tournament tournament2 = TestData.generateTournament(2L);
        List<Tournament> tournamentList = newArrayList(tournament1, tournament2);

        when(tournamentService.findAll()).thenReturn(tournamentList);

        MockHttpServletRequestBuilder builder = get("/tournament/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tournamentList)))
                .andDo(print());
    }

    @Test
    public void shouldReturn200WithEmptyListIfNoCustomers() throws Exception {
        MockHttpServletRequestBuilder builder = get("/tournament/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());

        verify(tournamentService).findAll();
    }
}
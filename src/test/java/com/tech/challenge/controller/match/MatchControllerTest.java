package com.tech.challenge.controller.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.challenge.TestData;
import com.tech.challenge.model.Match;
import com.tech.challenge.model.Tournament;
import com.tech.challenge.service.MatchService;
import com.tech.challenge.service.exception.TournamentNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
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

@WebMvcTest(MatchController.class)
public class MatchControllerTest {
    private static final OffsetDateTime START_DATE = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusDays(2);
    private static final Tournament tournament = TestData.generateTournament(10L);
    private static final Match match = TestData.generateMatch(14L, tournament);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @Test
    public void shouldReturn204OnMatchCreated() throws Exception {
        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), START_DATE.toString(), match.getPlayerA(), match.getPlayerB())));

        this.mockMvc.perform(builder)
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(matchService).save(tournament.getId(), START_DATE.toInstant(), match.getPlayerA(), match.getPlayerB());
    }

    @Test
    public void shouldReturn404WhenTournamentNotFound() throws Exception {
        when(matchService.save(any(), any(), any(), any())).thenThrow(new TournamentNotFoundException("Doesn't exist"));

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), START_DATE.toString(), match.getPlayerA(), match.getPlayerB())));

        this.mockMvc.perform(builder)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenTournamentIdIsNull() throws Exception {
        Long tournamentId = null;

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournamentId, START_DATE.toString(), match.getPlayerA(), match.getPlayerB())));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenStartDateIsNull() throws Exception {
        String startDate = null;

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), startDate, match.getPlayerA(), match.getPlayerB())));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenStartDateIsEmpty() throws Exception {
        String startDate = "   ";

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), startDate, match.getPlayerA(), match.getPlayerB())));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenStartDateIsInvalid() throws Exception {
        String startDate = "definitely not a date";

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), startDate, match.getPlayerA(), match.getPlayerB())));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenPlayerAIsNull() throws Exception {
        String playerA = null;

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), START_DATE.toString(), playerA, match.getPlayerB())));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenPlayerAIsEmpty() throws Exception {
        String playerA = "   ";

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), START_DATE.toString(), playerA, match.getPlayerB())));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenPlayerBIsNull() throws Exception {
        String playerB = null;

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), START_DATE.toString(), match.getPlayerA(), playerB)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn400WhenPlayerBIsEmpty() throws Exception {
        String playerB = "   ";

        MockHttpServletRequestBuilder builder = post("/match/save")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(new CreateMatchRequest(tournament.getId(), START_DATE.toString(), match.getPlayerA(), playerB)));

        this.mockMvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void shouldReturn200WithListOfMatches() throws Exception {
        Tournament tournament = TestData.generateTournament(1L);
        Match match1 = TestData.generateMatch(1L, tournament);
        Match match2 = TestData.generateMatch(2L, tournament);
        List<Match> matchList = newArrayList(match1, match2);

        when(matchService.findAll()).thenReturn(matchList);

        MockHttpServletRequestBuilder builder = get("/match/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        List<MatchResponse> expectedResponse = matchList.stream().map(MatchResponse::from).collect(toList());
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andDo(print());
    }

    @Test
    public void shouldReturn200WithEmptyListIfNoMatches() throws Exception {
        MockHttpServletRequestBuilder builder = get("/match/list")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());

        verify(matchService).findAll();
    }
}
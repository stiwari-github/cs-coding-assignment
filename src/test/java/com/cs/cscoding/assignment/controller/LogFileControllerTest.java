package com.cs.cscoding.assignment.controller;

import com.cs.cscoding.assignment.entity.LogEvent;
import com.cs.cscoding.assignment.service.LogFileService;
import com.cs.cscoding.assignment.utils.TestDataUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogFileControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private LogFileService logFileService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldParseAndSaveLogEvent() throws Exception {
        doNothing().when(logFileService).saveLogEvents(TestDataUtils.getEventData());
        MvcResult result = mockMvc.perform(
                post("/logs/save-events?filePath=src/test/resources/log-file.txt")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(asyncDispatch(result)).andExpect(status().isOk())
                .andExpect(content().string("Log events have been saved successfully"))
                .andReturn();
    }

    @Test
    public void shouldReturnLogEventsWithAlert() throws Exception {
        LogEvent logEvent = LogEvent.builder().id("1").host(null).type(null).alert(true).duration(2L).build();
        List<LogEvent> events = Stream.of(logEvent).collect(Collectors.toList());
        CompletableFuture<List<LogEvent>> logEvents = CompletableFuture.completedFuture(events);
        when(logFileService.getLogsWithAlert()).thenReturn(logEvents);
        mockMvc.perform(
                get("/logs/logs-with-alert"))
                .andExpect(status().isOk())
                .andReturn();
    }
}

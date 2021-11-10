package com.cs.cscoding.assignment.service;

import com.cs.cscoding.assignment.entity.LogEvent;
import com.cs.cscoding.assignment.model.LogFileEvents;
import com.cs.cscoding.assignment.repository.LogFileRepository;
import com.cs.cscoding.assignment.utils.TestDataUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class LogFileServiceTest {

    @InjectMocks
    private LogFileService logFileService;

    @Mock
    private LogFileRepository logFileRepository;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(logFileService, "alertThreshold", 4L);
    }

    @Test
    public void shouldSaveLogEvents() {
        LogEvent logEvent = LogEvent.builder().id("1").alert(true).duration(2L).host(null).type(null).build();
        when(logFileRepository.save(any(LogEvent.class))).thenReturn(logEvent);
        logFileService.saveLogEvents(TestDataUtils.getEventData());
        verify(logFileRepository, times(2)).save(any(LogEvent.class));
    }

    @Test
    public void shouldNotSaveLogEventIfEventIsNotFinished() {
        LogEvent logEvent = LogEvent.builder().id("1").alert(true).duration(2L).host(null).type(null).build();
        logFileService.saveLogEvents(Stream.of(LogFileEvents.builder().id("1").host(null).status("STARTED").timestamp(12345L).type(null).build()).collect(Collectors.toList()));
        verify(logFileRepository, times(0)).save(logEvent);
    }

    @Test
    public void shouldReturnLogsWithAlertTrue() throws ExecutionException, InterruptedException {
        LogEvent logEvent = LogEvent.builder().id("1").alert(true).duration(2L).host(null).type(null).build();
        when(logFileRepository.findByAlert(Boolean.TRUE)).thenReturn(Stream.of(logEvent).collect(Collectors.toList()));
        CompletableFuture<List<LogEvent>> logEvents = logFileService.getLogsWithAlert();
        verify(logFileRepository, times(1)).findByAlert(Boolean.TRUE);
        assertEquals(1, logEvents.get().size());
        assertTrue(logEvents.get().get(0).getAlert());
    }
}

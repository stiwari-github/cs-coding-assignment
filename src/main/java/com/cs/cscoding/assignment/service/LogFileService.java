package com.cs.cscoding.assignment.service;

import com.cs.cscoding.assignment.entity.LogEvent;
import com.cs.cscoding.assignment.model.LogFileEvents;
import com.cs.cscoding.assignment.repository.LogFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class LogFileService {

    Logger logger = LoggerFactory.getLogger(LogFileService.class);

    @Autowired
    private LogFileRepository logFileRepository;

    @Value("${alert.threshold}")
    private Long alertThreshold;

    public void saveLogEvents(List<LogFileEvents> logEvents) {

        Set<String> uniqueEventIds = logEvents.stream().map(LogFileEvents::getId).collect(Collectors.toSet());
        Map<String, Long> startTimestamp = uniqueEventIds.stream()
                .map(e -> logEvents.stream()
                        .filter(l -> l.getId().equalsIgnoreCase(e) && l.getStatus().equals("STARTED"))
                        .findFirst()
                        .get())
                .collect(Collectors.toMap(LogFileEvents::getId, LogFileEvents::getTimestamp));
        Map<String, Long> stopTimestamp = uniqueEventIds.stream()
                .map(e -> logEvents.stream()
                        .filter(l -> l.getId().equalsIgnoreCase(e) && l.getStatus().equals("FINISHED"))
                        .findFirst()
                        .orElseGet(() -> LogFileEvents.builder().id(e).timestamp(0L).build()))
                .collect(Collectors.toMap(LogFileEvents::getId, LogFileEvents::getTimestamp));

        uniqueEventIds.forEach(e -> logEvents.stream()
                .filter(l -> l.getId().equalsIgnoreCase(e))
                .findFirst()
                .ifPresent(fe -> saveLogEventsTodB(fe, startTimestamp, stopTimestamp)));
    }

    @Transactional
    void saveLogEventsTodB(LogFileEvents logFileEvent, Map<String, Long> startTimestamp, Map<String, Long> stopTimestamp) {
        if (stopTimestamp.get(logFileEvent.getId()) > 0) {
            Long timeDif = stopTimestamp.get(logFileEvent.getId()) - startTimestamp.get(logFileEvent.getId());
            LogEvent logEvent = LogEvent.builder()
                    .id(logFileEvent.getId())
                    .duration(timeDif)
                    .alert(timeDif > alertThreshold)
                    .host(logFileEvent.getHost())
                    .type(logFileEvent.getType())
                    .build();
            logger.info("Saving log event {} ", logEvent);
            logFileRepository.save(logEvent);
        }
    }

    public CompletableFuture<List<LogEvent>> getLogsWithAlert() {
        return CompletableFuture.completedFuture(logFileRepository.findByAlert(Boolean.TRUE));
    }

    public CompletableFuture<List<LogEvent>> getAllEvents() {
        return CompletableFuture.completedFuture(logFileRepository.findAll());
    }
}

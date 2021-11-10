package com.cs.cscoding.assignment.controller;

import com.cs.cscoding.assignment.entity.LogEvent;
import com.cs.cscoding.assignment.model.LogFileEvents;
import com.cs.cscoding.assignment.service.LogFileService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/logs")
@RestController
public class LogFileController {

    Logger logger = LoggerFactory.getLogger(LogFileController.class);

    @Autowired
    private LogFileService logFileService;

    @PostMapping("/save-events")
    public CompletableFuture<ResponseEntity<String>> parseAndSaveLogEvents(@RequestParam String filePath) {
        return CompletableFuture.runAsync(() -> {
            List<LogFileEvents> logEvents = extractJsonFromTextFile(filePath);
            logFileService.saveLogEvents(logEvents);
        }).thenApply((Void body) -> ResponseEntity.ok().body("Log events have been saved successfully"))
                .exceptionally(ex -> ResponseEntity.internalServerError().body("Error in saving log events :" + ex.getMessage()));
    }

    @GetMapping("/logs-with-alert")
    public CompletableFuture<List<LogEvent>> getLogsWithAlert() {
        return logFileService.getLogsWithAlert();
    }

    @GetMapping("/all-events")
    public CompletableFuture<List<LogEvent>> getAllLogEvents() {
        return logFileService.getAllEvents();
    }

    private List<LogFileEvents> extractJsonFromTextFile(final String filePath) {
        logger.info("Parsing file located at {} ", filePath);
        final List<LogFileEvents> logFileEventsList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                final LogFileEvents logFileEvent = mapper.readValue(line, new TypeReference<LogFileEvents>() {
                });
                logFileEventsList.add(logFileEvent);
            }
            logger.info("Parsed total {} log events", logFileEventsList.size());
            return logFileEventsList;
        } catch (IOException e) {
            logger.error("Failed to parse Text file ", e);
        }
        return logFileEventsList;
    }
}

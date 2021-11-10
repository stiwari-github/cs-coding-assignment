package com.cs.cscoding.assignment.utils;

import com.cs.cscoding.assignment.model.LogFileEvents;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataUtils {

    public static List<LogFileEvents> getEventData() {
        LogFileEvents logFileEvents1 = LogFileEvents.builder().id("1").host(null).status("STARTED").timestamp(12345L).type(null).build();
        LogFileEvents logFileEvents2 = LogFileEvents.builder().id("1").host(null).status("FINISHED").timestamp(12353L).type(null).build();
        LogFileEvents logFileEvents3 = LogFileEvents.builder().id("2").host("local").status("STARTED").timestamp(12345L).type("APP").build();
        LogFileEvents logFileEvents4 = LogFileEvents.builder().id("2").host("local").status("FINISHED").timestamp(12346L).type("APP").build();
        LogFileEvents logFileEvents5 = LogFileEvents.builder().id("3").host(null).status("STARTED").timestamp(12345L).type(null).build();

        return Stream.of(logFileEvents1, logFileEvents2, logFileEvents3, logFileEvents4, logFileEvents5).collect(Collectors.toList());
    }
}

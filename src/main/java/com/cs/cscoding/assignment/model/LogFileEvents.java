package com.cs.cscoding.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogFileEvents {
    private String id;
    private Long timestamp;
    private String type;
    private String host;
    private String status;
}

package com.cs.cscoding.assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "LOG_EVENTS")
@AllArgsConstructor
@NoArgsConstructor
public class LogEvent {
    @Id
    private String id;
    private Long duration;
    private String type;
    private String host;
    private Boolean alert;
}

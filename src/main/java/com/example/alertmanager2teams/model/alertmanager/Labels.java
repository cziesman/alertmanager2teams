package com.example.alertmanager2teams.model.alertmanager;

import lombok.Data;

@Data
public class Labels {

    private String alertname;

    private String instance;

    private String job;

    private String monitor;

    private String severity;

}

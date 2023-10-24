package com.example.alertmanager2teams.model.alertmanager;

import lombok.Data;

@Data
public class CommonLabels {

    private String alertname;

    private String monitor;

    private String severity;

}

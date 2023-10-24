package com.example.alertmanager2teams.model.alertmanager;

import java.util.List;

import lombok.Data;

@Data
public class Alert {

    private String version;

    private String groupKey;

    private String status;

    private String receiver;

    private GroupLabels groupLabels;

    private CommonLabels commonLabels;

    private CommonAnnotations commonAnnotations;

    private String externalURL;

    private List<AlertDetails> alerts;

}

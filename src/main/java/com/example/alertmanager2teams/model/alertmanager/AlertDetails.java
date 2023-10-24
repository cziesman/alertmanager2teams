package com.example.alertmanager2teams.model.alertmanager;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class AlertDetails {

    private Labels labels;

    private Annotations annotations;

    private OffsetDateTime startsAt;

    private OffsetDateTime endsAt;

}

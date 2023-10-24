package com.example.alertmanager2teams.model.teams;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Section {

    private String activityTitle;

    private String activitySubtitle;

    private String activityImage;

    private List<Fact> facts = new ArrayList<>();

    private boolean markdown = false;

}

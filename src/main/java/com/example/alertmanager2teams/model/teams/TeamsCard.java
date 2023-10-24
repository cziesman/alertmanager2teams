package com.example.alertmanager2teams.model.teams;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeamsCard {

    @JsonProperty("@type")
    private String type;

    @JsonProperty("@context")
    private String context;

    private String themeColor;

    private String summary;

    private List<Section> sections = new ArrayList<>();

}

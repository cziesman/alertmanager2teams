package com.example.alertmanager2teams.processor;

import com.example.alertmanager2teams.config.WebhookConfig;
import com.example.alertmanager2teams.model.alertmanager.Alert;
import com.example.alertmanager2teams.model.alertmanager.AlertDetails;
import com.example.alertmanager2teams.model.alertmanager.Annotations;
import com.example.alertmanager2teams.model.alertmanager.Labels;
import com.example.alertmanager2teams.model.teams.Fact;
import com.example.alertmanager2teams.model.teams.Section;
import com.example.alertmanager2teams.model.teams.TeamsCard;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TeamsCardConverter implements Processor {

    private static final String ALERT_NAME = "Alert Name";

    private static final String INSTANCE = "Instance";

    private static final String JOB = "Job";

    private static final String MONITOR = "Monitor";

    private static final String SEVERITY = "Severity";

    private static final String TIMESTAMP = "Timestamp";

    private static final String MS_TEAMS_CHANNEL = "ms.teams.channel";

    @Value("${teams.card.context}")
    private String context;

    @Value("${teams.card.themeColor}")
    private String themeColor;

    @Value("${teams.card.type}")
    private String type;

    @Autowired
    private WebhookConfig webhookConfig;

    @Override
    public void process(Exchange exchange) {

        Alert alert = exchange.getIn().getBody(Alert.class);

        TeamsCard teamsCard = new TeamsCard();

        teamsCard.setSummary(String.format("%s(%s)", alert.getCommonAnnotations().getSummary(), alert.getStatus()));
        teamsCard.setContext(context);
        teamsCard.setThemeColor(themeColor);
        teamsCard.setType(type);

        for (AlertDetails alertDetails : alert.getAlerts()) {

            Section section = new Section();

            Annotations annotations = alertDetails.getAnnotations();

            section.setActivityTitle(annotations.getSummary());
            section.setActivitySubtitle(annotations.getDescription());

            Labels labels = alertDetails.getLabels();

            Fact fact = new Fact();
            fact.setName(ALERT_NAME);
            fact.setValue(labels.getAlertname());
            section.getFacts().add(fact);

            fact = new Fact();
            fact.setName(INSTANCE);
            fact.setValue(labels.getInstance());
            section.getFacts().add(fact);

            fact = new Fact();
            fact.setName(JOB);
            fact.setValue(labels.getJob());
            section.getFacts().add(fact);

            fact = new Fact();
            fact.setName(MONITOR);
            fact.setValue(labels.getMonitor());
            section.getFacts().add(fact);

            fact = new Fact();
            fact.setName(SEVERITY);
            fact.setValue(labels.getSeverity());
            section.getFacts().add(fact);

            fact = new Fact();
            fact.setName(TIMESTAMP);
            fact.setValue(alertDetails.getStartsAt().toString());
            section.getFacts().add(fact);

            teamsCard.getSections().add(section);
        }

        exchange.getIn().setBody(teamsCard);
        String channelUrl = webhookConfig.lookupAlert(alert.getGroupLabels().getAlertname());
        exchange.getIn().setHeader(MS_TEAMS_CHANNEL, channelUrl);
    }

}

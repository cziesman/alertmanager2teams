package com.example.alertmanager2teams.route;

import com.example.alertmanager2teams.config.WebhookConfig;
import com.example.alertmanager2teams.model.alertmanager.Alert;
import com.example.alertmanager2teams.processor.JsonBeautifier;
import com.example.alertmanager2teams.processor.TeamsCardConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AlertManagerToTeamsRoute extends RouteBuilder {

    private static final String MS_TEAMS_CHANNEL = "ms.teams.channel";

    @Autowired
    private JsonBeautifier jsonBeautifier;

    @Autowired
    private TeamsCardConverter teamsCardConverter;

    @Autowired
    private WebhookConfig webhookConfig;


    @Override
    public void configure() {

        LOG.info("{}", webhookConfig);

        LOG.info("{}", webhookConfig.lookupAlert("high_memory_load"));
        LOG.info("{}", webhookConfig.lookupAlert("some_other_alert"));
        LOG.info("{}", webhookConfig.lookupAlert("yet_another_alert"));

        restConfiguration()
                .component("undertow")
                .contextPath("/teams-adapter")
                .port(8080);

        rest("/alert")
                .post()
                .routeId("/alert")
                .to("direct:adapter")
                .responseMessage();

        from("direct:adapter")
                .routeId("direct-adapter")
                .to("seda:adapter?waitForTaskToComplete=Never")
                .setBody(simple(""));

        from("seda:adapter")
                .routeId("seda-adapter")
                .convertBodyTo(String.class)
                .process(jsonBeautifier)
                .log(LoggingLevel.DEBUG, "\n${body}")
                .unmarshal().json(JsonLibrary.Jackson, Alert.class)
                .process(teamsCardConverter)
                .marshal().json()
                .recipientList(header(MS_TEAMS_CHANNEL));
    }

}

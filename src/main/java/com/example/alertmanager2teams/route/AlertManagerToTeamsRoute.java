package com.example.alertmanager2teams.route;

import com.example.alertmanager2teams.model.alertmanager.Alert;
import com.example.alertmanager2teams.processor.JsonBeautifier;
import com.example.alertmanager2teams.processor.TeamsCardConverter;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertManagerToTeamsRoute extends RouteBuilder {

    @Autowired
    private JsonBeautifier jsonBeautifier;

    @Autowired
    private TeamsCardConverter teamsCardConverter;

    @Override
    public void configure() {

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
                .to("{{teams.webhook.url}}");
    }

}

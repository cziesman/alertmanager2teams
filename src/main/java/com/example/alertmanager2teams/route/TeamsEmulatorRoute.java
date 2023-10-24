package com.example.alertmanager2teams.route;

import com.example.alertmanager2teams.model.teams.TeamsCard;
import com.example.alertmanager2teams.processor.JsonBeautifier;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * Provides an emulator for a Teams webhook endpoint. This class should be removed if this application
 * is used in a production environment.
 */
@Component
public class TeamsEmulatorRoute extends RouteBuilder {

    @Autowired
    private JsonBeautifier jsonBeautifier;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .component("undertow")
                .contextPath("/teams-adapter")
                .port(8080);

        rest("/teams")
                .post()
                .routeId("/teams")
                .to("direct:teams");

        from("direct:teams")
                .routeId("direct-teams")
                .convertBodyTo(String.class)
                .process(jsonBeautifier)
                .log(LoggingLevel.DEBUG, "\n${body}")
                .unmarshal().json(JsonLibrary.Jackson, TeamsCard.class)
                .log(LoggingLevel.DEBUG, "\n${body}")
                .setBody(simple(""));
    }

}
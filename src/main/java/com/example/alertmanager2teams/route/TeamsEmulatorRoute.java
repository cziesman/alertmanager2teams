package com.example.alertmanager2teams.route;

import com.example.alertmanager2teams.model.teams.TeamsCard;
import com.example.alertmanager2teams.processor.JsonBeautifier;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * Provides an emulator for a Teams webhook endpoint. This class can be removed if this application
 * is used in a production environment.
 */
@Component
public class TeamsEmulatorRoute extends RouteBuilder {

    @Autowired
    private JsonBeautifier jsonBeautifier;

    @Override
    public void configure() {

        restConfiguration()
                .bindingMode(RestBindingMode.json);

        rest("/first-alert")
                .post()
                .routeId("/first-alert")
                .to("direct:teams");

        rest("/second-alert")
                .post()
                .routeId("/second-alert")
                .to("direct:teams");

        rest("/other-alert")
                .post()
                .routeId("/other-alert")
                .to("direct:teams");

        from("direct:teams")
                .routeId("direct-teams")
                .marshal().json()
                .process(jsonBeautifier)
                .log(LoggingLevel.DEBUG, "\n${body}")
                .unmarshal().json(JsonLibrary.Jackson, TeamsCard.class)
                .setBody(simple(""));
    }

}

package com.example.alertmanager2teams.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonBeautifier implements Processor {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void process(Exchange exchange) throws Exception {

        String json = exchange.getIn().getBody(String.class);
        Object object = mapper.readValue(json, Object.class);
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        exchange.getIn().setBody(json);
    }

}

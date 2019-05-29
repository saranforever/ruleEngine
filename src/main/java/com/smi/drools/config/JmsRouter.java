package com.smi.drools.config;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smi.drools.controller.TestController;
import com.smi.drools.model.CustomerDocument;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JmsRouter extends RouteBuilder{
	
	@Autowired
	private TestController testController;

	@Override
	public void configure() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		from("{{input.queue}}")
		//     .log(LoggingLevel.DEBUG, log, "New message received")
		.process(exchange -> {
			String convertedMessage = exchange.getMessage().getBody().toString();
			CustomerDocument doc = mapper.readValue(convertedMessage, CustomerDocument.class);
			exchange.getMessage().setBody(testController.testDocumentContext(doc).toString());
		})
		.to("{{output.queue}}")
		//        .log(LoggingLevel.DEBUG, log, "Message sent to the other queue")
		.end();
	}
}

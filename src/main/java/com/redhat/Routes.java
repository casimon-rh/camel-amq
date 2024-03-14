package com.redhat;

// import org.apache.camel.Exchange;
// import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.jms.JMSSecurityException;

/**
 * Routes
 */
@ApplicationScoped
public class Routes extends RouteBuilder {

  @ConfigProperty(name="custom-project.name")
  String project;

  @Override
  public void configure() throws Exception {
    restConfiguration().bindingMode(RestBindingMode.json).port(8080);

    rest("/fruits/")
        .post("/newfruit").type(Fruit.class).to("direct:newFruit");

    from("direct:newFruit").marshal().json(JsonLibrary.Jackson)
        .doTry()
        .toD("activemq:queue:"+project+"?exchangePattern=InOnly")
          .transform(constant("Sent!"))
        .doCatch(JMSSecurityException.class)
          .transform(constant("Unauthorized!"))
        .doCatch(Exception.class)
          .to("log:DEBUG?showHeaders=true")
        .end();

    from("activemq:queue:"+project).unmarshal(new JacksonDataFormat(Fruit.class))
        .to("log:DEBUG?showHeaders=true");

    // from("timer://mytime?fixedRate=true&period=5000")
    //     .process(new Processor() {
    //       public void process(Exchange exchange) throws Exception {
    //         Fruit testFruit = new Fruit("Banana", 10);
    //         exchange.getIn().setBody(testFruit);
    //       }
    //     }).to("direct:newFruit");
  }
}
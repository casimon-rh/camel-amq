package com.redhat;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Routes
 */
public class Routes extends RouteBuilder {
  private String FRUITCLASS = Fruit.class.getName();
  private String JPAPREFIX = "jpa://" + FRUITCLASS + "?";

  @Override
  public void configure() throws Exception {
    restConfiguration().bindingMode(RestBindingMode.json).port(8080);

    rest("/fruits/")
        .get("/").to("direct:fruitList")
        .get("/{id}").to("direct:fruitDetail")
        .post("/newfruit").type(Fruit.class).to("direct:newFruit");

    from("direct:fruitList").to(JPAPREFIX + "namedQuery=findAll");
    from("direct:fruitDetail")
      .toD(JPAPREFIX + "query=select b from " + FRUITCLASS+ " b where b.id= ${header.id}");

    from("direct:newFruit").marshal().json(JsonLibrary.Jackson)
        .toD("activemq:queue:myqueue").transform(constant("Sent!"));


    from("activemq:queue:myqueue").unmarshal(new JacksonDataFormat(Fruit.class))
      .to("jpa:" + FRUITCLASS + "?usePersist=true")
      .to("log:DEBUG?showHeaders=true");
  }
}
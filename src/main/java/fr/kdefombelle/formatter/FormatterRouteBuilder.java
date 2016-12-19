package fr.kdefombelle.formatter;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author kdefombelle
 */
@Component
public class FormatterRouteBuilder extends RouteBuilder{

    @Autowired
    private FreemarkerXmlModelCreator freemarkerXmlModelCreator = new FreemarkerXmlModelCreator();

    @Override
    public void configure() throws Exception {
        from("file:///Users/ka/Documents/work/dev/code/formatter/src/test/resources/?charset=UTF-8&noop=true")
                   .routeId("Formatter")
                .log("Received order ${body}").bean(freemarkerXmlModelCreator)
                .to("freemarker:fr/kdefombelle/formatter/test.ftl??contentCache=false")
                .log("Received order ${body}").to("mock:result");
    }
}

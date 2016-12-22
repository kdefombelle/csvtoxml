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
        from("file://{{input.folder}}?charset=UTF-8&noop=true")
                   .routeId("Formatter")
                //.log("Received order ${body}")
                .bean(freemarkerXmlModelCreator)
                .to("freemarker:{{template.file}}??contentCache=false")
                .log("Received order \n${body}")
                //.simple(headers[CamelFileName])
                .to("file:{{output.folder}}?charset=UTF-8&fileExist=Append") //Override
                .to("mock:result");
    }
}

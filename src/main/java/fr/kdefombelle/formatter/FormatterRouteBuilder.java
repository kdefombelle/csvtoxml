package fr.kdefombelle.formatter;

import org.apache.camel.LoggingLevel;
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
        from("file://{{input.folder}}?charset=UTF-8&noop=true").to("seda:processing").routeId("RouteReadFiles");
        
        from("seda:processing").threads(5).routeId("RouteFormatter")
                .log("Processing file [${in.header.CamelFileName}]")
                .choice()
	            	.when(simple("'xml' == '{{input.type}}'"))
	                	.bean(freemarkerXmlModelCreator).id("ModelCreator")
		                .to("freemarker:{{template.file}}")
		                .choice()
		                	.when(simple("'append' == '{{output.mode}}'"))
		                		.log(LoggingLevel.DEBUG,"Formatter configured in [{{output.mode}}] mode")
			                	.setHeader("CamelOverruleFileName", simple("report.{{output.extension}}"))
				                .to("file:{{output.folder}}?charset=UTF-8&fileExist=Append")
				            .when(simple("'override' == '{{output.mode}}'"))
			                	.log(LoggingLevel.DEBUG,"Formatter configured in [{{output.mode}}] mode")
			                	.setHeader("CamelOverruleFileName", simple("${headers.tradeId}.{{output.extension}}"))
			                	.log("TradeId [${in.header.TradeId}]")
			                	.to("file:{{output.folder}}?charset=UTF-8&fileExist=Override")
			        		.otherwise().throwException(new IllegalStateException("output mode not supported"))
			            .to("mock:result")
        			.otherwise().throwException(new IllegalStateException("input.type not supported"));

    }
}

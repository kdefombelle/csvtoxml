package fr.kdefombelle.integration.csvtoxml;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author kdefombelle
 */
@Component
public class CsvToXmlRouteBuilder extends RouteBuilder{

	public static String ROUTE_READ_INPUT_CSV="RouteReadCsvFiles";
	public static String ROUTE_CSV_TO_XML="RouteCsvToxml";
	
    @Override
    public void configure() throws Exception {
        onException(Exception.class).log("Exception CSV");
    	from("file://{{csv.input.folder}}?charset=UTF-8&noop=true")
    	.routeId(ROUTE_READ_INPUT_CSV)
    	.log("File [${in.header.CamelFileName}] read")
    	.to("direct:csvToxml");
    	
		from("direct:csvToxml")
		.routeId(ROUTE_CSV_TO_XML)
		.split(body().tokenize("\n")).streaming()
		.unmarshal().csv()
		.log("CSV line as list [${body}]");
    }
}

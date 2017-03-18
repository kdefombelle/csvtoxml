package fr.kdefombelle.integration.formatter;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author kdefombelle
 */
@Component
public class CsvToXmlRouteBuilder extends RouteBuilder{

	public static String ROUTE_READ_INPUT_CSV="RouteReadCsvFiles";
	public static String ROUTE_CSV_TO_XML="RouteCsvToxml";
	
    //@Autowired
    //private FreemarkerXmlModelCreator freemarkerXmlModelCreator = new FreemarkerXmlModelCreator();
    static volatile int counter=0;
    
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
		.process(new Processor(){
			@Override
			public void process(Exchange exchange) throws Exception {
				log.info("CSV line {} as list [${body}]",++counter);
			}
		});
    }
}

package fr.kdefombelle.integration.csvtoxml;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.kdefombelle.junit.FileResources;
import fr.kdefombelle.junit.FileResourcesRule;

@RunWith(CamelSpringRunner.class)
// @BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration({ "classpath:META-INF/spring/spring.xml" })
//@UseAdviceWith instructs test to not start camel context as some advise should be placed first
//not needed in this program as camel context is not autostarted.
public class CsvToXmlTest {

	@Rule
	public FileResourcesRule files = new FileResourcesRule();

	@Autowired
	private CamelContext camelContext;

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

//	private NotifyBuilder notify;

	@Before
	public void before() throws Exception {
		camelContext.getRouteDefinition(CsvToXmlRouteBuilder.ROUTE_READ_INPUT_CSV).adviceWith(camelContext, new AdviceWithRouteBuilder() {
			@Override
			public void configure() throws Exception {
				weaveAddLast().to("mock:result");
			}
		});
	}

	@Test
	@FileResources(files = "csv/test.csv")
	public void testSendMatchingMessage() throws Exception {
		String csv = files.read(0);
		resultEndpoint.expectedMessageCount(1);
//		NotifyBuildernotify = new NotifyBuilder(camelContext).fromRoute(FormatterRouteBuilder.ROUTE_FORMATTER)
//				.whenAnyDoneMatches(Builder.header(Exchange.OVERRULE_FILE_NAME).contains("202292HM")).create();
		camelContext.startRoute(CsvToXmlRouteBuilder.ROUTE_READ_INPUT_CSV);
		camelContext.startRoute(CsvToXmlRouteBuilder.ROUTE_CSV_TO_XML);
		template.sendBodyAndHeader("direct:csvToxml", csv, Exchange.FILE_NAME, "test.csv");

//		assertTrue(notify.matches());
		resultEndpoint.assertIsSatisfied();
	}

}

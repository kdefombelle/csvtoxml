package fr.kdefombelle.formatter;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kdefombelle
 */
public class Formatter {

    private Logger logger = LoggerFactory.getLogger(Formatter.class);

    @Autowired
    private CamelContext camelContext;

    public void start(){
        logger.debug("{} starting isCamelAutoStartup {}",Formatter.class.getSimpleName(), camelContext.isAutoStartup());
        try {
//        	System.setProperty(Exchange.DEFAULT_CHARSET_PROPERTY, "UTF-8");
        	camelContext.start();
        	camelContext.startAllRoutes();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void stop(){
    	logger.debug("{} stopping", Formatter.class.getSimpleName());
    	try {
			camelContext.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

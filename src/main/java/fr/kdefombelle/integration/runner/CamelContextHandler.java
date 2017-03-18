package fr.kdefombelle.integration.runner;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author kdefombelle
 */
public class CamelContextHandler {

    private Logger logger = LoggerFactory.getLogger(CamelContextHandler.class);

    @Autowired
    private CamelContext camelContext;

    public void start(){
        logger.debug("{} starting isCamelAutoStartup {}",CamelContextHandler.class.getSimpleName(), camelContext.isAutoStartup());
        try {
        	camelContext.start();
        	camelContext.startAllRoutes();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void stop(){
    	logger.debug("{} stopping", CamelContextHandler.class.getSimpleName());
    	try {
			camelContext.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

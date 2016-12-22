package fr.kdefombelle.formatter;

import org.apache.camel.CamelContext;
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
        logger.debug("Formatter starting isCamelAutoStartup {}",camelContext.isAutoStartup());
//        try {
//        	camelContext.start();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//			e.printStackTrace();
//		}
    }
}

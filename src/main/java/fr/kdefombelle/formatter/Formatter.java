package fr.kdefombelle.formatter;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author kdefombelle
 */
public class Formatter {

    private Logger logger = LoggerFactory.getLogger(Formatter.class);

    @Autowired
    private CamelContext camelContext;

    public void start(){
        logger.debug(""+camelContext.isAutoStartup());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

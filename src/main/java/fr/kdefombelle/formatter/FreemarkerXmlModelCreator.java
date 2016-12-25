package fr.kdefombelle.formatter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.freemarker.FreemarkerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author kdefombelle
 */

public class FreemarkerXmlModelCreator {

    private Logger logger = LoggerFactory.getLogger(FreemarkerXmlModelCreator.class);

    @Handler
    public void process(Exchange in) {
        GenericFile<File> o = in.getIn().getBody(GenericFile.class);
        logger.info(""+o);
        Map<String, Object> root = new HashMap<>();
        try {
            root.put("xml", freemarker.ext.dom.NodeModel.parse(o.getFile()));
            root.put("request", in.getIn());
            root.put("headers", in.getIn().getHeaders());
            //root.put("body", in.getIn().getBody());
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        in.getIn().setHeader(FreemarkerConstants.FREEMARKER_DATA_MODEL, root);
    }
}

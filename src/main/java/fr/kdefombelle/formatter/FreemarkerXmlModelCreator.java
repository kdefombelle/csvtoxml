package fr.kdefombelle.formatter;

import freemarker.ext.dom.NodeModel;
import freemarker.template.TemplateModelException;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.component.file.GenericFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kdefombelle
 */

public class FreemarkerXmlModelCreator {

    private Logger logger = LoggerFactory.getLogger(FreemarkerXmlModelCreator.class);

    @Handler
    public void process(Exchange in) {
        GenericFile o = in.getIn().getBody(GenericFile.class);
        logger.info(""+o);
        Map root = new HashMap();
        try {
            root.put("xml", freemarker.ext.dom.NodeModel.parse((File)o.getFile()));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        in.getIn().setHeader("CamelFreemarkerDataModel", root);
//        try {
//            String tradeId = ((NodeModel) ((NodeModel) root.get("xml")).get("/TradeList/*/TradeId")).getNode().getFirstChild().getTextContent();
//            in.getIn().setHeader("tradeId", tradeId);
//            logger.info(""+tradeId);
//        } catch (TemplateModelException e) {
//            e.printStackTrace();
//        }

        in.getIn().setBody(o);
    }
}

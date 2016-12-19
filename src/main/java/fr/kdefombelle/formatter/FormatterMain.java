package fr.kdefombelle.formatter;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author kdefombelle
 */
@Configuration
@ComponentScan
public class FormatterMain
{

    public static void main( String[] args )
    {
        System.out.println( "FormatterMain Start!" );
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
        context.getBean(Formatter.class).start();
        System.out.println( "FormatterMain Stop!" );
    }
}

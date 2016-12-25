package fr.kdefombelle.formatter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.camel.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.kdefombelle.jmx.JmxConstants;
import fr.kdefombelle.jmx.JmxUrlBuilder;
import fr.kdefombelle.jmx.ObjectNameFactory;
import fr.kdefombelle.jmx.server.Agent;
import fr.kdefombelle.jmx.server.SimpleAgent;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * @author kdefombelle
 */
@Configuration
@ComponentScan
public class FormatterRunner implements FormatterRunnerMBean {

	/** The logger used by this class. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatterRunner.class);

	private static final String OPT_HELP = "help";
	private static final String OPT_HELP_SHORT = "h";
	private static final String OPT_JMX_PORT = "port";
	private static final String OPT_JMX_PORT_SHORT = "p";
	private static final String OPT_JMX_HOST = "host";
	private static final String OPT_JMX_HOST_SHORT = "hostname";
	private static final String OPT_COMMAND = "command";
	private static final String OPT_COMMAND_SHORT = "c";

	private static final String JMX_DOMAIN = "murex-sg";
	private final CountDownLatch shutdownLatch = new CountDownLatch(1);
	// private final String objectName;
	private Agent agent;
	private ApplicationContext context;

	private FormatterRunner(OptionSet options) throws Throwable {
		String host = getHost(options);
		String port = getPort(options);
		String jmxUrl = new JmxUrlBuilder().setHost(host).setRmiRegistryPort(port).build();
		LOGGER.debug("JMX URL created {}", jmxUrl);
		agent = new SimpleAgent(jmxUrl, new HashMap<>(), new ObjectNameFactory(JMX_DOMAIN));
		agent.registerMBean(this, "formatter", port);
		LOGGER.info("Initializing formatter {}:{}", host, port);
	}

	private static String getHost(OptionSet options) throws UnknownHostException {
		return getFirstNotNull((String) options.valueOf("host"), InetAddress.getLocalHost().getHostName(), "localhost");
	}

	private static String getHost() {
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return host;
	}

	private static String getPort(OptionSet options) throws UnknownHostException {
		return getFirstNotNull((String) options.valueOf("port"), "1617");
	}

	private static String getFirstNotNull(String value, String... values) {
		if (value != null)
			return value;
		for (String v : values)
			if (v != null)
				return v;
		throw new IllegalArgumentException("None of the provided values is not null");
	}

	private static String getObjectName(String port) {
		return JMX_DOMAIN + ":formatter=" + port + "," + JmxConstants.KEY_TYPE + "=formatter";
	}

	private void awaitShutdown() throws InterruptedException {
		shutdownLatch.await();
	}

	public void startJmxConnector() {
		agent.startJmxConnector();
	}

	public static void main(String[] args) throws Throwable {
		OptionParser parser = new OptionParser();
		List<String> helpOptions = Arrays.asList(OPT_HELP, OPT_HELP_SHORT);
		OptionSpec<?> help = parser.acceptsAll(helpOptions, "print this help message").withOptionalArg();

		List<String> jmxPortOptions = Arrays.asList(OPT_JMX_PORT, OPT_JMX_PORT_SHORT);
		OptionSpec<String> port = parser.acceptsAll(jmxPortOptions, "jmx port").withOptionalArg().ofType(String.class);

		List<String> commandOptions = Arrays.asList(OPT_COMMAND, OPT_COMMAND_SHORT);
		OptionSpec<String> commandOption = parser.acceptsAll(commandOptions, "command start/stop").withRequiredArg()
				.ofType(String.class);

		List<String> hostOptions = Arrays.asList(OPT_JMX_HOST, OPT_JMX_HOST_SHORT);
		OptionSpec<String> hostOption = parser.acceptsAll(hostOptions, "host name").withRequiredArg()
				.ofType(String.class);

		OptionSet options = parser.parse(args);
		LOGGER.debug("Parsing options...{}", options.asMap());

		if (options.has(help)) {
			// print help and exit
			try {
				parser.printHelpOn(System.out);
				return;
			} catch (IOException e) {
				LOGGER.error(FormatterRunner.class.getClass().getSimpleName() + " execution failed:", e);
				return;
			}
		}

		String command = options.valueOf(commandOption);
		if ("start".equals(command)) {
			startCommand(options);
		} else if ("stop".equals(command)) {
			stopCommand(options);
		} else {
			throw new IllegalStateException("unknown command");
		}

	}

	public static void startCommand(OptionSet options) throws Throwable {
		LOGGER.info("Starting Formatter");
		FormatterRunner formaterRunner = new FormatterRunner(options);
		long startTime = System.currentTimeMillis();
		String port = getPort(options);
		LocateRegistry.createRegistry(Integer.parseInt(port));
		formaterRunner.startJmxConnector();

		invokeRemoteOperation("start", getPort(options));

		LOGGER.info("Formatter started in {}", TimeUtils.printDuration(System.currentTimeMillis() - startTime));
		formaterRunner.awaitShutdown();
	}

	public static void stopCommand(OptionSet options) throws Throwable {
		LOGGER.info("Stopping Formatter");
		long startTime = System.currentTimeMillis();
		invokeRemoteOperation("stop", getPort(options));
		//TODO: ensure stop is completed to handle properly camel context auto startup false
		LOGGER.info("Formatter stopped in {}", TimeUtils.printDuration(System.currentTimeMillis() - startTime));
	}

	private static void invokeRemoteOperation(String operation, String port) throws IOException, JMException {
		invokeRemoteOperation(operation, port, null, null);
	}

	private static Object invokeRemoteOperation(String operation, String port, Object[] params, String[] signature)
			throws IOException, JMException {
		JMXServiceURL jmxUrl = new JmxUrlBuilder().setHost(getHost()).setRmiRegistryPort(port).buildUrl();
		JMXConnector jmxc = JMXConnectorFactory.connect(jmxUrl, null);
		MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
		LOGGER.debug("Object name {}", getObjectName(port));
		ObjectName runnerObjectName = new ObjectNameFactory(JMX_DOMAIN).createObjectName("formatter", port);
		return mbsc.invoke(runnerObjectName, operation, params, signature);
	}

	@Override
	public void start() {
		context = new ClassPathXmlApplicationContext("META-INF/spring/spring.xml");
		Formatter formatter = context.getBean(Formatter.class);
		formatter.start();
	}

	@Override
	public void stop() {
		try {
			Formatter formatter = context.getBean(Formatter.class);
			formatter.stop();
			agent.stopJmxConnector();
		} finally {
			shutdownLatch.countDown();
		}
	}
}

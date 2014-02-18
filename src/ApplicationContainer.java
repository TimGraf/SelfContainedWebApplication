import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.applicationcontainer.appserver.ApplicationServer;
import org.applicationcontainer.client.ApplicationClient;
import org.applicationcontainer.dbserver.ApplicationDatabaseServer;

public class ApplicationContainer {
	private static final String CONFIG_FILE     = "./resources/config.properties";
	private static final String LOG4J_FILE      = "./resources/log4j.properties";	
	private static final String APP_SERVER_PORT = "appServerPort";
	private static final String APP_FILE_NAME   = "appFileName";
	private static final String APP_CONTEXT     = "appContext";
	private static final String APP_URL         = "appUrl";
	private static final String DB_SERVER_PORT  = "dbServerPort";
	private static final String DB_USER         = "dbUser";
	private static final String DB_PASSWORD     = "dbPassword"; 
	private static final String DB_FILE_PATH    = "dbFilePath";
	private static final String XUL_RUNNER_HOME = "xulRunnerHome";
	
	static {
		PropertyConfigurator.configure(LOG4J_FILE);
	}
	
	private Log        log  = LogFactory.getLog(ApplicationContainer.class);
	private Properties prop = new Properties();
	
	private String                        workingDirectory;
	private ApplicationDatabaseServer    databaseServer;
	private ApplicationServer applcationServer;

	public static void main(String[] args) {
		new ApplicationContainer();
    }
	
	public ApplicationContainer() {
		try {
			//
			workingDirectory = new File (".").getCanonicalPath();
			//
			prop.load(new FileInputStream(CONFIG_FILE));
			//
			int    appServerPort = Integer.parseInt(prop.getProperty(APP_SERVER_PORT));
			String appFileName   = prop.getProperty(APP_FILE_NAME);
			String appContext    = prop.getProperty(APP_CONTEXT);
			String appUrl        = prop.getProperty(APP_URL);
			//
			int    dbServerPort = Integer.parseInt(prop.getProperty(DB_SERVER_PORT));
			String dbUser       = prop.getProperty(DB_USER);
			String dbPassword   = prop.getProperty(DB_PASSWORD);
			String dbFilePath   = prop.getProperty(DB_FILE_PATH);
			//
			String xulRunnerHome = workingDirectory + prop.getProperty(XUL_RUNNER_HOME);
			//
			applcationServer = new ApplicationServer(appServerPort, appFileName, appContext);
			databaseServer   = new ApplicationDatabaseServer(dbServerPort, dbUser, dbPassword, dbFilePath);
			//
			log.debug("Starting application server.");
			applcationServer.startServer();
			//
			log.debug("Starting database server.");
			databaseServer.startServer();
			//
			while (!applcationServer.serverRunning() || !databaseServer.serverRunning()) {
				log.debug("Waiting for servers to start ...");
			}
			
			new ApplicationClient(xulRunnerHome, appUrl);
		} catch (IOException ex) {
			log.error(ex);
		}
	}
}

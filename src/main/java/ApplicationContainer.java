import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.applicationcontainer.appserver.ApplicationServer;
import org.applicationcontainer.client.ApplicationClient;
import org.applicationcontainer.dbserver.ApplicationDatabaseServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationContainer {
	private static final String CONFIG_FILE     = "/src/main/resources/config.properties";
	private static final String LOG4J_FILE      = "/src/main/resources/log4j.properties";
	private static final String APP_SERVER_PORT = "appServerPort";
	private static final String APP_FILE_NAME   = "appFileName";
	private static final String APP_CONTEXT     = "appContext";
	private static final String APP_URL         = "appUrl";
	private static final String DB_SERVER_PORT  = "dbServerPort";
	private static final String DB_USER         = "dbUser";
	private static final String DB_PASSWORD     = "dbPassword"; 
	private static final String DB_FILE_PATH    = "dbFilePath";
	private static final String XUL_RUNNER_HOME = "xulRunnerHome";

    private static String appDirectory;

    static {

        try {
            appDirectory = new File(".").getCanonicalPath();
        } catch (IOException e) {
            appDirectory = "./";
        }

        PropertyConfigurator.configure(appDirectory + LOG4J_FILE);
    }
	
	private Log        applicationLog        = LogFactory.getLog(ApplicationContainer.class);
	private Properties applicationProperties = new Properties();

	private ApplicationDatabaseServer databaseServer;
	private ApplicationServer         applicationServer;

	public static void main(String[] args) {
		new ApplicationContainer();
    }
	
	public ApplicationContainer() {
		try {
            initApplicationConfiguration();
            attachShutDownHook();
            createApplicationServer();
            createDatabaseServer();
            startDatabaseServer();
            startApplicationServer();
            createApplicationWebClient();
		} catch (Exception ex) {
			applicationLog.error(ex);
		}
	}

    private void initApplicationConfiguration() throws IOException {
        applicationProperties.load(new FileInputStream(appDirectory + CONFIG_FILE));
    }

    private void createApplicationServer() {
        int    appServerPort = Integer.parseInt(applicationProperties.getProperty(APP_SERVER_PORT));
        String appFileName   = appDirectory + applicationProperties.getProperty(APP_FILE_NAME);
        String appContext    = applicationProperties.getProperty(APP_CONTEXT);

        applicationServer = new ApplicationServer(appServerPort, appFileName, appContext);
    }

    private void createDatabaseServer() throws IOException {
        int    dbServerPort = Integer.parseInt(applicationProperties.getProperty(DB_SERVER_PORT));
        String dbUser       = applicationProperties.getProperty(DB_USER);
        String dbPassword   = applicationProperties.getProperty(DB_PASSWORD);
        String dbFilePath   = appDirectory + applicationProperties.getProperty(DB_FILE_PATH);

        databaseServer   = new ApplicationDatabaseServer(dbServerPort, dbUser, dbPassword, dbFilePath);
    }

    private void startApplicationServer() throws InterruptedException {
        applicationLog.debug("Starting application server.");
        applicationServer.startServer();

        while (!applicationServer.isRunning()) {
            applicationLog.debug("Waiting for application server to start ...");
            Thread.sleep(1000);
        }
    }

    private void startDatabaseServer() throws InterruptedException {
        applicationLog.debug("Starting database server.");
        databaseServer.startServer();

        while (!databaseServer.isRunning()) {
            applicationLog.debug("Waiting for database server to start ...");
            Thread.sleep(1000);
        }
    }

    private void createApplicationWebClient() throws IOException {
        String appUrl        = applicationProperties.getProperty(APP_URL);
        String xulRunnerHome = appDirectory + applicationProperties.getProperty(XUL_RUNNER_HOME);

        new ApplicationClient(xulRunnerHome, appUrl);
    }

    public void attachShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {

                if (databaseServer != null) {
                    databaseServer.stopServer();
                }

                if (applicationServer != null) {
                    applicationServer.stopServer();
                }
            }
        });
    }
}

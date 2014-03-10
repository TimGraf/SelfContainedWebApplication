package org.applicationcontainer.dbserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by timothygraf on 3/9/14.
 */
@RunWith(JUnit4.class)
public class ApplicationDatabaseServerTest {

    private ApplicationDatabaseServer databaseServer;

    @Before
    public void setUp() throws IOException {
        String appDirectory = new File(".").getCanonicalPath();
        String dbFilePath = appDirectory + "/src/main/resources/application.db4o";

        databaseServer = new ApplicationDatabaseServer(9090, "user", "password", dbFilePath);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateDataServerSuccessful() throws IOException {
        assertNotNull("assert database server object is not null", databaseServer);
    }

    @Test
    public void testIsRunningAfterStarting() throws IOException, InterruptedException {
        databaseServer.startServer();
        Thread.sleep(1000);
        assertTrue("asssert that the call to isRunning is true after server is started, and waiting for 1 second", databaseServer.isRunning());
        databaseServer.stopServer();
    }

    @Test
    public void testIsNotRunningAfterStopping() throws IOException, InterruptedException {
        databaseServer.startServer();
        Thread.sleep(1000);
        databaseServer.stopServer();
        Thread.sleep(1000);
        assertFalse("asssert that the call to isRunning is false after server is started, waiting for 1 second, then shut down", databaseServer.isRunning());
    }
}

package org.applicationcontainer.appserver;

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
public class ApplicationServerTest {
    private ApplicationServer applicationServer;

    @Before
    public void setUp() throws IOException {
        String appDirectory = new File(".").getCanonicalPath();
        String appFileName = appDirectory + "/src/main/resources/sample.war";

        applicationServer = new ApplicationServer(9080, appFileName, "/");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateApplicationServerSuccessful() {
        assertNotNull("assert application server object is not null", applicationServer);
    }

    @Test
    public void testIsRunningAfterStarting() throws InterruptedException {
        applicationServer.startServer();
        Thread.sleep(1000);
        assertTrue("asssert that the call to isRunning is true after server is started, and waiting for 1 second", applicationServer.isRunning());
        applicationServer.stopServer();
    }

    @Test
    public void testIsNotRunningAfterStopping() throws InterruptedException {
        applicationServer.startServer();
        Thread.sleep(1000);
        applicationServer.stopServer();
        Thread.sleep(1000);
        assertFalse("asssert that the call to isRunning is false after server is started, waiting for 1 second, then shut down", applicationServer.isRunning());
    }
}

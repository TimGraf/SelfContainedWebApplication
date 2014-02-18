package org.applicationcontainer.appserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class ApplicationServer {
	private Log log  = LogFactory.getLog(ApplicationServer.class);
	
	private Server        server;
	private WebAppContext webApp;
	
	public ApplicationServer(int port, String webApp, String contextPath) {
        this.server = new Server(port);
        this.webApp = new WebAppContext(webApp, contextPath);

		this.server.setHandler(this.webApp);
	}
	
	public void startServer() {
		log.debug("Start application server.");
		
		try {
			server.start();
		} catch (Exception e) {
			log.error(e);
			// XXX Wrap this in an appropriate exception
		}
	}
	
	public void stopServer() {
		log.debug("Stop application server.");
		
		try {
			server.stop();
		} catch (Exception e) {
			log.error(e);
			// XXX Wrap this in an appropriate exception
		}
	}
	
	public boolean serverRunning() {
		
		if (server == null) {
			return false;
		} else {
			return server.isRunning();
		}
	}
}

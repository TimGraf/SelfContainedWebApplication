package org.applicationcontainer.dbserver;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectServer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.reflect.jdk.JdkReflector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

public class ApplicationDatabaseServer {
	private Log log  = LogFactory.getLog(ApplicationDatabaseServer.class);
	//
	private ObjectServer server;	
	private String       dbFileName;
	private String       user;
	private String       password;
	private int          port; 
	
	public ApplicationDatabaseServer(int port, String user, String password, String dbFilePath) throws IOException {
		this.port       = port;
		this.user       = user;
		this.password   = password;
		this.dbFileName = new File(dbFilePath).getCanonicalPath();
		
    	EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
    	
    	configuration.common().reflectWith(new JdkReflector(Thread.currentThread().getContextClassLoader()));
    }
	    
    public void startServer() {
    	log.debug("Start database server.");
    	
		new Thread() {
			public void run() {
				server = Db4oClientServer.openServer(dbFileName, port);
				
				server.grantAccess(user, password);
			}
		}.start();
    }
    
    public void stopServer() {
    	log.debug("Stop database server.");
    	server.close();
        server = null;
    }
    
    public boolean isRunning() {
    	return (server != null);
    }
}

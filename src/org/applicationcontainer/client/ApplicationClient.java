package org.applicationcontainer.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.browser.IMozillaWindow.VisibilityMode;
import org.mozilla.browser.MozillaConfig;
import org.mozilla.browser.MozillaPanel;

public class ApplicationClient {
	private Log log  = LogFactory.getLog(ApplicationClient.class);
	//
	private JFrame frame;
	
	public ApplicationClient(String xulRunnerHome, String appUrl) {
		// Maybe create a panel instead and hide some controls
		MozillaConfig.setXULRunnerHome(new File(xulRunnerHome));
	    
		frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
	    
        // Hide the tool bar and status bar on the moz swing panel
	    final MozillaPanel moz = new MozillaPanel(VisibilityMode.FORCED_HIDDEN, VisibilityMode.FORCED_HIDDEN);
        moz.load(appUrl);
        moz.setMinimumSize(new Dimension(800, 600));
        
        frame.getContentPane().add(moz, BorderLayout.CENTER);
        frame.setVisible(true);
	}
}

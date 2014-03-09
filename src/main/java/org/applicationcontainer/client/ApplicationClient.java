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
	private JFrame       appFrame;
    private MozillaPanel mozPanel;
	
	public ApplicationClient(String xulRunnerHome, String appUrl) {
        createMozillaPanel(xulRunnerHome, appUrl);
        createAppFrame();
	}

    private void createMozillaPanel(String xulRunnerHome, String appUrl) {
        MozillaConfig.setXULRunnerHome(new File(xulRunnerHome));

        mozPanel = new MozillaPanel(VisibilityMode.FORCED_HIDDEN, VisibilityMode.FORCED_HIDDEN);
        mozPanel.load(appUrl);
        mozPanel.setMinimumSize(new Dimension(800, 600));
    }

    private void createAppFrame() {
        appFrame = new JFrame();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(800, 600);

        appFrame.getContentPane().add(mozPanel, BorderLayout.CENTER);
        appFrame.setVisible(true);
    }
}

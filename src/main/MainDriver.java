package main;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import main.gui.GUIFront;
import main.util.Constants;

public class MainDriver {
	
	/**
	 * Main driver to initialize map information and startup the Main GUI
	 * @param args Command-Line arguments required of a main function
	 */
	public static void main(String[] args){		
		File[] localMapList = new File(Constants.LOCAL_MAP_PATH).listFiles(); // gets a list of localmap filename
		
		// Launches the main application
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new GUIFront(localMapList.length, localMapList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}

}

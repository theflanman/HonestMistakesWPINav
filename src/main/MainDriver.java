package main;

import java.awt.EventQueue;
import java.io.File;

public class MainDriver {
	
	/**
	 * Main driver to initalize map information and startup the Main GUI
	 * @param args Command-Line arguments required of a main function
	 */
	public static void main(String[] args){
		
		System.out.println("before making a new local map");
		
		
		File[] localMapList = new File("src/localmaps").listFiles(); // gets a list of localmap filenames
		
		// Launches the main application
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainGUI(localMapList.length, localMapList);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}

}

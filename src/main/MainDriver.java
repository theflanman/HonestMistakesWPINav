package main;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

public class MainDriver {
	
	/**
	 * Main driver to initalize map information and startup the Main GUI
	 * @param args Command-Line arguments required of a main function
	 */
	public static void main(String[] args){
		
		System.out.println("before making a new local map");
		
		
		/*
		// tmp stuff developer tool should do
		LocalMap local = new LocalMap();
		local.setMapID(30);
		local.setMapImage("map1cc_test.jpg");
		
		ArrayList<MapNode> tmpNodes = new ArrayList<MapNode>();
		tmpNodes.add(new MapNode(300.0f, 300.0f));
		tmpNodes.add(new MapNode(350.0f, 350.0f));
		tmpNodes.add(new MapNode(400.0f, 400.0f));
		
		local.setMapNodes(tmpNodes);
		System.out.println("before saving");
		local.saveMap("map1cc_test");
		System.out.println("after saving");
		*/
		
		File[] localMapList = new File("src/localmaps").listFiles(); // gets a list of localmap filenames
		for(File f : localMapList){
			System.out.println(f.getName());
		}
		
		// Launches the main application
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println("before GUI");
					MainGUI frame = new MainGUI(localMapList.length, localMapList);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}

}

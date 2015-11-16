package main;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import main.gui.MainGUI;

public class MainDriver {
	
	/**
	 * Main driver to initalize map information and startup the Main GUI
	 * @param args Command-Line arguments required of a main function
	 */
	public static void main(String[] args){		
		
		File[] localMapList = new File("src/localmaps").listFiles(); // gets a list of localmap filenames
		
		/*
		// Test data to try and run AStar
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		MapNode node1 = new MapNode(100.0, 100.0, 1.0);
		MapNode node2 = new MapNode(200.0, 200.0, 1.0);
		MapNode node3 = new MapNode(500.0, 200.0, 1.0);
		
		node1.addNeighbor(node2);
		node2.addNeighbor(node1);
		node2.addNeighbor(node3);
		node3.addNeighbor(node2);
		
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		
		LocalMap tmp = new LocalMap("downstairsCC.jpg", nodes);
		tmp.setMapNodes(nodes);
		tmp.saveMap("downstairsCC");
		*/
		
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

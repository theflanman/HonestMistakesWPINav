package main;

import java.io.Serializable;
import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class GlobalMap implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<MapNode> mapNodes;
	private LocalMap[] localMaps;
	private MapNode[] path;
	
	public void generateStepByStep() {
		
	}
	
	public void navigate(MapNode startNode, MapNode endNode) {
		
	}
	
	/**
	 * Sets the list of LocalMaps to the given input
	 * @param localMaps A list of LocalMaps that has been initialized from MainGUI
	 */
	public void setLocalMaps(LocalMap[] localMaps){
		this.localMaps = localMaps;
	}

	public void setMapNodes(ArrayList<MapNode> allNodes) {
		this.mapNodes = allNodes;
	}
	
}

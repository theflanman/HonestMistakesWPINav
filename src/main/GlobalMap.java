package main;


import java.io.Serializable;
import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class GlobalMap implements Serializable {

	private ArrayList<MapNode> mapNodes;
	private ArrayList<LocalMap> localMaps;
	private ArrayList<MapNode> path;
	
	public GlobalMap(){
		this.mapNodes = new ArrayList<MapNode>();
		this.localMaps = new ArrayList<LocalMap>();
		this.path = new ArrayList<MapNode>();
	}
	
	public void navigate(MapNode startNode, MapNode endNode) {
		
	}
	
	/**
	 * Sets the list of LocalMaps to the given input
	 * @param localMaps A list of LocalMaps that has been initialized from MainGUI
	 */
	
	 
	 public void addToMapNodes(MapNode node){
		mapNodes.add(node);
	}

	public ArrayList<MapNode> getMapNodes() {
		return mapNodes;
	}
	 
	public void setLocalMaps(ArrayList<LocalMap> localMaps){
		this.localMaps = localMaps;
	}

	public void setMapNodes(ArrayList<MapNode> mapNodes) {
		this.mapNodes = mapNodes;
	}
	
	
}

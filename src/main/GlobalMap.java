package main;

import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class GlobalMap {

	private ArrayList<MapNode> mapNodes;
	private LocalMap[] localMaps;
	private MapNode[] path;
	
	public GlobalMap(){
		this.mapNodes = new ArrayList<MapNode>();
		this.localMaps = new LocalMap[100];
		this.path = new MapNode[1000];
	}
	
	public void generateStepByStep() {
		
	}
	
	public void navigate(MapNode startNode, MapNode endNode) {
		
	}
	
	public void addToMapNodes(MapNode node){
		mapNodes.add(node);
	}

	public ArrayList<MapNode> getMapNodes() {
		return mapNodes;
	}

	public void setMapNodes(ArrayList<MapNode> mapNodes) {
		this.mapNodes = mapNodes;
	}
	
	
	
}

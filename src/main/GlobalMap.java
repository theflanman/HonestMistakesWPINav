package main;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GlobalMap implements Serializable {

	private ArrayList<MapNode> mapNodes, chosenNodes, allNodes;
	private ArrayList<LocalMap> localMaps;
	private ArrayList<MapNode> path;
	private MapNode startNode, endNode;
	private ArrayList<MapNode> middleNodes;
	
	public GlobalMap(){
		this.mapNodes = new ArrayList<MapNode>();
		this.localMaps = new ArrayList<LocalMap>();
		this.path = new ArrayList<MapNode>();
		this.chosenNodes = new ArrayList<MapNode>();
		this.startNode = null;
		this.endNode = null;
		this.middleNodes = new ArrayList<MapNode>();
	}
		
	/**
	 * Sets the list of LocalMaps to the given input
	 * @param localMaps A list of LocalMaps that has been initialized from GUIFront
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
	
	public ArrayList<LocalMap> getLocalMaps(){
		return this.localMaps;
	}

	public void setMapNodes(ArrayList<MapNode> mapNodes) {
		this.mapNodes = mapNodes;
	}
	public void setChosenNodes(ArrayList<MapNode> chosen){
		this.chosenNodes = chosen;
	}
	public ArrayList<MapNode> getChosenNodes(){
		return this.chosenNodes;
	}
	
	public void setStartNode(MapNode start){
		this.startNode = start;
	}
	public void setEndNode(MapNode end){
		this.endNode = end;
	}
	public MapNode getStartNode(){
		return this.startNode;
	}
	public MapNode getEndNode(){
		return this.endNode;
	}
	public ArrayList<MapNode> getMiddleNodes(){
		return this.middleNodes;
	}
	
	public void addToMiddleNodes(MapNode node){
		this.middleNodes.add(node);
	}
	
	public ArrayList<MapNode> getAllNodes() {
		return allNodes;
	}

	public void setAllNodes(ArrayList<MapNode> allNodes) {
		this.allNodes = allNodes;
	}
	
}

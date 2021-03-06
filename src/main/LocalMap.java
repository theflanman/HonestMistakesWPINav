package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import main.util.Constants;
import main.util.GeneralUtil;
import main.util.YamlParser;

@SuppressWarnings("serial")
public class LocalMap implements Serializable{

	private ArrayList<MapNode> mapNodes;
	private MapNode startNode;
	private MapNode endNode;
	private ArrayList<MapNode> chosenNodes = new ArrayList<MapNode>();
	private ArrayList<MapNode> middleNodes = new ArrayList<MapNode>();
	
	private String mapImageName;
	private int mapID;
	private GlobalMap globalMap;
	private double mapScale; // unit is feet/pixel
	
	private double transformAngle; //Angle required to transform (rotate) the local coordinate to match the global coordinate system
	//coordinate offset required to transform (translate) the local coordinates to match the global coordinate system
	private double xOffset;
	private double yOffset;
	private double zHeight;
	
	// constructor
	public LocalMap(String mapImageName, ArrayList<MapNode> mapNodes){
		this.mapImageName = mapImageName;
		this.mapNodes = mapNodes;
		this.startNode = null;
		this.endNode = null;
		
		YamlParser yamlParser = new YamlParser(new String[]{Constants.DATA_PATH});
		
		HashMap<String, Double> argList = yamlParser.getArgList();
		if(argList.size() > 0){
			
			String mapImagepng = GeneralUtil.removeExtension(this.mapImageName);
			String[] s = mapImagepng.split("/");
			mapImagepng = s[s.length-1];
			mapImagepng = mapImagepng + ".png";

			this.mapScale = argList.get("scale-"+ mapImagepng); // gets the zoomRatio based on the associated mapImageName
			this.transformAngle = argList.get("angle-"+ mapImagepng);//gets the transformation angle based on the associated mapImageName
			this.xOffset = argList.get("xOffset-"+ mapImagepng);//gets the x coordinate offset based on the associated mapImageName
			this.yOffset = argList.get("yOffset-"+ mapImagepng);//gets the y coordinate offset based on the associated mapImageName
			this.zHeight = argList.get("zHeight-"+ mapImagepng);//gets the height based on what floor the current local map is on
		}

	}
	
	public MapNode getStartNode() {
		return startNode;
	}

	public void setStartNode(MapNode start) {
		this.startNode = start;
	}

	public MapNode getEndNode() {
		return endNode;
	}

	public void setEndNode(MapNode end) {
		this.endNode = end;
	}

	public ArrayList<MapNode> getChosenNodes() {
		return chosenNodes;
	}

	public void setChosenNodes(ArrayList<MapNode> chosenNodes) {
		this.chosenNodes = chosenNodes;
	}

	public ArrayList<MapNode> getMiddleNodes() {
		return middleNodes;
	}

	public void setMiddleNodes(ArrayList<MapNode> middleNodes) {
		this.middleNodes = middleNodes;
	}
	
	
	public void addNode(double xPos, double yPos) {
		MapNode node = new MapNode();
		node.setXPos(xPos);
		node.setYPos(yPos);
		globalMap.addToMapNodes(node); // add this node to the only GlobalMap
	}
	
	public void removeNode(int nodeID) {
		
	}
	
	/**
	 * @author Andrew Petit
	 * 
	 * @description adds a link between a pair of nodes
	 */
	public void linkNodes(String nodeID1, String nodeID2) {
		MapNode mapNode1 = null;
		MapNode mapNode2 = null;
		
		//Unless nodeID's definitely correspond to an idex of an array we need to actually check every node in the list to get the node
		for (MapNode mapNode: this.mapNodes){
			if (mapNode.getNodeID().equals(nodeID1))
				mapNode1 = mapNode; 
			else if (mapNode.getNodeID().equals(nodeID2))
				mapNode2 = mapNode;
		}
		
		if(mapNode1.equals(null) || mapNode2.equals(null)){
			// TODO return new exception as both nodes need to exist in the local maps list of nodes to link the nodes
		}
		if (Arrays.asList(mapNode1.getNeighbors()).contains(mapNode2) && Arrays.asList(mapNode2.getNeighbors()).contains(mapNode1)){
			// TODO if the link is already established do nothing
		} 
		else if ((Arrays.asList(mapNode1.getNeighbors()).contains(mapNode2) && !Arrays.asList(mapNode2.getNeighbors()).contains(mapNode1)) || (Arrays.asList(mapNode2.getNeighbors()).contains(mapNode1) && !Arrays.asList(mapNode1.getNeighbors()).contains(mapNode2))){
			// TODO if a node is a neighbor to another node, but that other node is not a neighbor to the initial node
			//need to return some error value as map nodes should not appear in neighbors without a link
		} 
		else { 
			// add the link between the two nodes as conditions are met to warrant the change
			mapNode1.addNeighbor(mapNode2);
			mapNode2.addNeighbor(mapNode1);
		}
	}
	
	public void delinkNodes(int nodeID1, int nodeID2) {
		MapNode mapNode1 = null;
		MapNode mapNode2 = null;
		for (MapNode mapNode: this.mapNodes){
			if (mapNode.getNodeID().equals(nodeID1))
				mapNode1 = mapNode; 
			else if (mapNode.getNodeID().equals(nodeID2))
				mapNode2 = mapNode;
		}
		if(mapNode1.equals(null) || mapNode2.equals(null)){
			// TODO return new exception as both nodes need to exist in the local maps list of nodes to delink the nodes
		} 
		else if (Arrays.asList(mapNode1.getNeighbors()).contains(mapNode2) && Arrays.asList(mapNode1.getNeighbors()).contains(mapNode1)){
			//remove the link between the two nodes
			mapNode1.deleteNeighborLink(mapNode2);
			mapNode2.deleteNeighborLink(mapNode1);
			
		} 
		else {
			// TODO error in code - one node cannot be a neighbor without the other node being a neighbor return exception
		}
	}
	
	/**
	 * function to transform the coordinate system (in feet) of all nodes in a local map to match global coordinate system
	 */
	public void transformCoordinates(){
		double xPrime; 
		double yPrime; 
		
		for(MapNode aNode:this.getMapNodes()){
			//the x and y position in feet and the offsets need to be in feet as well
			//rotate the x and y coordinates to match the global coordinate system 
			xPrime = aNode.getXFeet()*Math.cos(aNode.getLocalMap().getTransformAngle()) - aNode.getYFeet()*Math.sin(aNode.getLocalMap().getTransformAngle());
			yPrime = aNode.getYFeet()*Math.cos(aNode.getLocalMap().getTransformAngle()) + aNode.getXFeet()*Math.sin(aNode.getLocalMap().getTransformAngle());
			
			//translate the x and y coordinates of the local map to resemble its location in the global map 
			xPrime = xPrime + aNode.getLocalMap().getXOffset();
			yPrime = yPrime + aNode.getLocalMap().getYOffset();
			
			//set the new transformed coordinates
			aNode.setXFeet(xPrime);
			aNode.setYFeet(yPrime);
		}
	}
	
	public int getMapID() {
		return mapID;
	}

	public void setMapID(int mapID) {
		this.mapID = mapID;
	}
		
	public void setMapNodes(ArrayList<MapNode> nodes){
		this.mapNodes = nodes;
	}
	
	public ArrayList<MapNode> getMapNodes(){
		return this.mapNodes;
	}

	public double getMapScale() {
		return mapScale;
	}

	public void setMapScale(double mapScale) {
		this.mapScale = mapScale;
	}

	public GlobalMap getGlobalMap() {
		return globalMap;
	}

	public void setGlobalMap(GlobalMap globalMap) {
		this.globalMap = globalMap;
	}

	public String getMapImageName() {
		return mapImageName;
	}

	public void setMapImageName(String mapImageName) {
		this.mapImageName = mapImageName;
	}
	public double getTransformAngle(){
		return transformAngle;
	}
	public double getXOffset(){
		return xOffset;
	}
	public double getYOffset(){
		return yOffset;
	}
	public double getZHeight(){
		return zHeight;
	}
}
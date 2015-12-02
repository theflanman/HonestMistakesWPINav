package main.gui;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import main.AStar;
import main.LocalMap;
import main.MapNode;
import main.StepByStep;
import main.gui.GUIFront.TweenPanel;
import main.util.Constants;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */
@SuppressWarnings("serial")
public class GUIBack implements Serializable {
	
	private LocalMap localMap;
	private ArrayList<MapNode> path;
	
	/**
	 * Constructor: Initializes Backend fields to the default map to be loaded.
	 * TODO: Change to Campus Map when it is complete
	 */
	public GUIBack(String defaultMapImage, ArrayList<MapNode> points){
		this.localMap = new LocalMap(defaultMapImage, points);
		this.path = new ArrayList<MapNode>();
	}
		
	/**
	 * loads a LocalMap into this class
	 * 
	 * @param fileName is name of file that stores the object data (requires an extension)
	 */
	public void loadLocalMap(String fileName) {
		
		if(! fileName.contains(".localmap")){
			System.out.println("DevGUIBack.loadMap(fileName) requires a .localmap file as input...exiting");
			System.exit(1);
		}
		
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(Constants.LOCAL_MAP_PATH + "/" + fileName);
			ObjectInputStream objIn = new ObjectInputStream(fileIn);
			this.localMap = (LocalMap) objIn.readObject();
			
			objIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**@author Andrew Petit
	 * 
	 * @description basically used by drawLine function in mainGui to draw a line between two nodes one at a time
	 * 
	 */
	public ArrayList<double[]> getCoordinates(ArrayList<MapNode> mapNodes){
		ArrayList<double[]>coordinates = new ArrayList<double[]>(); 
		for(MapNode mapNode : mapNodes){
			double x = mapNode.getXPos();
			double y = mapNode.getYPos();
			double[] pairs = {x, y};
			coordinates.add(pairs);
		}
		return coordinates;
	}
	
	/**@author Andrew Petit
	 * @description just gets the distance so mainGui can display it on a label  -- will need to be updated when I understand what score to use for this value
	 * @return String - this is necessary to allow MainGui to push the distance to a label
	 */
	
	public int getDistance(ArrayList<MapNode> mapNodes) {
		StepByStep getDistance = new StepByStep(mapNodes);
		int distance = getDistance.calculateTotalDistance();
		return distance;
	}
	/**
	 * @return ArrayList<String> - this is necessary to allow GUIFront to convert the strings in the array into rows of the column
	 */
	//honestly think we should role with the commented-out code, and get rid of generateStepByStep from Global -- Need someone's opinion though
	public ArrayList<String> displayStepByStep(ArrayList<MapNode> mapNodes) {
		StepByStep directions = new StepByStep(mapNodes);
		ArrayList<String> print = directions.printDirection();
		
		return print;
	}
	
	public void selectNodesForNavigation() {
		
	}
	
	/**@author Andrew Petit
	 * 
	 * @description runs the astar algorithm on the start and end nodes selected by the user
	 */
	public ArrayList<MapNode> runAStar(MapNode start, MapNode end) {
		//initiate a new astar class with the starting node and ending node of local map 
		MapNode[] aStarMap = {start, end};
		AStar astar = new AStar(aStarMap);
		astar.runAlgorithm();
		
		return astar.reconstructPath();
	}
	//this needs to be added as a way of reseting the path to have no nodes in it...
	/**
	 * @description when GUIFront calls it, this function removes all nodes from the path of nodes 
	 */
	public void removePath(ArrayList<MapNode> mapNodes) {
		mapNodes.clear();
	}
	
	public ArrayList<ArrayList<MapNode>> getMeRoutes(MapNode start, MapNode end){
		ArrayList<ArrayList<MapNode>> routes = new ArrayList<ArrayList<MapNode>>();
		ArrayList<MapNode> route = new ArrayList<MapNode>();
		ArrayList<MapNode> globalNodes = this.runAStar(start, end);
		MapNode start1 = null;
		MapNode end1 = null;
		int j = 0;
		for(int i = 0; i < globalNodes.size() - 1; i++){
			if (j == 0) {
				start1 = globalNodes.get(i);
				j++;
			} 
			if (globalNodes.get(i).getLocalMap() != globalNodes.get(i + 1).getLocalMap()){
				end1 = globalNodes.get(i);
				route = this.runAStar(start, end);
				routes.add(route);
				start1 = null;
				end1 = null;
				j = 0;
			}
		}
		return routes;
	}
		
	/**
	 * @author Andrew Petit
	 * @description Essentially enables a user to press anywhere on the map
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	public MapNode findNearestNode(double xPos, double yPos){
		MapNode start = new MapNode(xPos, yPos, 0);
		MapNode temp = null;
		//need to initialize with an extremely large unobtainable number - or find a better solution
		double distance = 10000000000000000000000000000000000000000000000000000000000000000000000000.0;
		for (MapNode mapnode : this.localMap.getMapNodes()){ //for all nodes in localmaps' nodes -- this will be changed to global map nodes when that is finished, and then do distance formula to find the nearest node
			 if (distance > (double) Math.sqrt((Math.pow(xPos - mapnode.getXPos(), 2)) + (Math.pow(yPos - mapnode.getYPos(), 2)))){
				 distance = (double) Math.sqrt((Math.pow(xPos - mapnode.getXPos(), 2)) + (Math.pow(yPos - mapnode.getYPos(), 2)));
				 temp = mapnode;
			 }
		}
		if (distance == 0){
			return temp;
		} else {
			//this will change to check to make sure the neighbor is valid
			start.addNeighbor(temp); //add the new nodes link with the closest node
			temp.addNeighbor(start); //add the new node as a neighbor to the closest node
			return start;
		}
	}
	
	public MapNode findNearestAttributedNode(String nodeAttribute, MapNode startNode){
		MapNode temp = null; //initialize a new node
		double distance = 10000000000000000000000000000000000000000000000000000000000000000000000000.0; //need to set distance to a value that is unattainable
		for (MapNode mapnode : this.localMap.getMapNodes()){ //for all nodes in localmaps nodes -- this will be changed to global map nodes when that is finished
			if (mapnode.getAttributes().getType().toString().equals(nodeAttribute)){ //if this is true do distance formula to find the closest node that has that attribute
				 if (distance > (double) Math.sqrt((Math.pow(startNode.getXPos() - mapnode.getXPos(), 2)) + (Math.pow(startNode.getYPos() - mapnode.getYPos(), 2)))){
					 distance = (double) Math.sqrt((Math.pow(startNode.getXPos() - mapnode.getXPos(), 2)) + (Math.pow(startNode.getYPos() - mapnode.getYPos(), 2)));
					 temp = mapnode;//set temp to this node value
				 }
			}
		}
		return temp;
	}

	public LocalMap getLocalMap() {
		return localMap;
	}
	
	public void setLocalMap(LocalMap localMap) {
		this.localMap = localMap;
	}
	public ArrayList<MapNode> getPath(){
		return this.path;
	}
	public void setPath(ArrayList<MapNode> path){
		this.path = path;
	}
}



package main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */
@SuppressWarnings("serial")
public class GUIBackend implements Serializable {
	
	private LocalMap localMap;
	private ArrayList<MapNode> path;
	private MapNode startNode;
	private MapNode endNode;
	
	/**
	 * Constructor: Initializes Backend fields to the default map to be loaded.
	 * TODO: Change to Campus Map when it is complete
	 */
	public GUIBackend(String defaultMapImage){
		this.localMap = new LocalMap(defaultMapImage);
		this.path = new ArrayList<MapNode>();
		this.startNode = null;
		this.endNode = null;
	}
		
	/**
	 * loads a LocalMap into this class
	 * 
	 * @param fileName is name of file that stores the object data (requires an extension)
	 */
	public void loadLocalMap(String fileName) {
		
		if(! fileName.contains(".localmap")){
			System.out.println("DeveloperGUIBackend.loadMap(fileName) requires a .localmap file as input...exiting");
			System.exit(1);
		}
		
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream("src/localmaps/" + fileName);
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
	
	public ArrayList<double[]> getCoordinates(){
		ArrayList<MapNode> mapNodes = this.path;
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
	
	public String getDistance() {
		StepByStep getDistance = new StepByStep(this.path);
		double distance = getDistance.calculateTotalDistance();
		return "Distance to your destination in feet:" + Double.toString(distance);
	}
	/**
	 * @return ArrayList<String> - this is necessary to allow MainGUI to convert the strings in the array into rows of the column
	 */
	//honestly think we should role with the commented-out code, and get rid of generateStepByStep from Global -- Need someone's opinion though
	public ArrayList<String> displayStepByStep() {
		//StepByStep directions = new StepByStep(this.path);
		ArrayList<String> print = this.localMap.getGlobalMap().generateStepByStep();
		return print;
	}
	
	public void selectNodesForNavigation() {
		
	}
	
	/**@author Andrew Petit
	 * 
	 * @description runs the astar algorithm on the start and end nodes selected by the user
	 */
	public void runAStar() {
		//initiate a new astar class with the starting node and ending node of local map 
		MapNode[] aStarMap = {this.startNode, this.endNode};
		AStar astar = new AStar(aStarMap);
		astar.runAlgorithm();
	}
	//this needs to be added as a way of reseting the path to have no nodes in it...
	/**
	 * @description when MainGUI calls it, this function removes all nodes from the path of nodes 
	 */
	public void removePath() {
		for(MapNode mapnode : this.path){
			path.remove(mapnode);
		}
	}

	public LocalMap getLocalMap() {
		return localMap;
	}

	public void setLocalMap(LocalMap localMap) {
		this.localMap = localMap;
	}
	
	public void setStartNode(MapNode startNode){
		this.startNode = startNode;
	}
	public void setEndNode(MapNode endNode){
		this.endNode = endNode;
	}
	public MapNode getStartNode(){
		return this.startNode;
	}
	public MapNode getEndNode(){
		return this.endNode;
	}
	public ArrayList<MapNode> getPath(){
		return this.path;
	}
}

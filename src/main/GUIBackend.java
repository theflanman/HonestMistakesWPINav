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
	private MapNode[] path;
	private MapNode startNode;
	private MapNode endNode;
	
	/**
	 * Constructor: Initializes Backend fields to the default map to be loaded.
	 * TODO: Change to Campus Map when it is complete
	 */
	public GUIBackend(){
		this.localMap = new LocalMap();
		this.path = null;
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
	 * @description basically used by drawpath function in mainGui as it needs the list of ordered pairs for each map node
	 * @return ArrayList<float[]> - the array of coordinate pairs
	 */
	
	public ArrayList<float[]> getPathOnMapInfo() {
		ArrayList<MapNode> mapNodes = this.localMap.getMapNodes();
		ArrayList<float[]>coordinates = new ArrayList<float[]>(); 
		for(MapNode mapNode : mapNodes){
			float x = mapNode.getX();
			float y = mapNode.getY();
			float[] pairs = {x, y};
			coordinates.add(pairs);
		}
		return coordinates;
	}
	
	/**@author Andrew Petit
	 * @description just gets the distance so mainGui can display it on a label  
	 * @return
	 */
	
	public float getDistance() {
		float distance = this.startNode.calcDistance(this.endNode);
		return distance;
	}
	
	public void displayStepByStep() {
		
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
	

}

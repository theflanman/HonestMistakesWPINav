package main;
/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class MapNode {
	
	private float xPos;
	private float yPos;
	private float zPos;
	private int nodeID;
	private MapNode[] neighbors;
	private float fScore;
	private float gScore;
	private float hScore;
	private MapNode cameFrom;
	private GlobalMap globalMap;
	private LocalMap localMap;
	//private Attributes attributes
	
	/**
	 * @return the cameFrom
	 */
	public MapNode getCameFrom() {
		return cameFrom;
	}
	/**
	 * 
	 * @return the x position
	 */
	public float getXPos(){
		return xPos;	
	}
	
	/**
	 * 
	 * @return the y position
	 */
	public float getYPos(){
		return yPos;	
	}

	public float calcDistance(MapNode toNode) {
		
		return 0;
		
	}
	
	public void addNeighbor(MapNode node) {
		
	}

}

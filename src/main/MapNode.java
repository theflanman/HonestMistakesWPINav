package main;

import java.io.Serializable;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class MapNode implements Serializable{
	
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
	
	// temporary constructor
	public MapNode(float xPos, float yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public float calcDistance(MapNode toNode) {
		
		return 0;
		
	}
	
	public void addNeighbor(MapNode node) {
		
	}
	
	public float getX(){
		return this.xPos;
	}
	public float getY(){
		return this.yPos;
	}

}

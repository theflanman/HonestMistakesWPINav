package main;

import java.io.Serializable;
import java.util.LinkedList;

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
	private LinkedList<MapNode> neighbors;
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
	/**@author Andrew Petit
	 * @param node
	 */
	public void addNeighbor(MapNode node) {
		this.neighbors.add(node);
	}
	/**@author Andrew Petit
	 * @param node
	 */
	public void deleteNeighborLink(MapNode node){
		this.neighbors.remove(node);
	}
	
	public float getX(){
		return this.xPos;
	}
	public float getY(){
		return this.yPos;
	}
	public int getNodeID(){
		return this.nodeID;
	}
	public LinkedList<MapNode> getNeighbors(){
		return this.neighbors;
	}

}

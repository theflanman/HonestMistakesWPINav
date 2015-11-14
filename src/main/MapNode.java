package main;

import java.io.Serializable;
import java.util.LinkedList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class MapNode implements Serializable{
	
	private double xPos;
	private double yPos;
	private double zPos;
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
	
	public MapNode(double xPos, double yPos, double zPos){
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
	}
	
	public float calcDistance(MapNode toNode) {
		
		return 0;
		
	}
	
	public void addNeighbor(MapNode node) {
		this.neighbors.add(node);
	}

	public void deleteNeighborLink(MapNode node){
		this.neighbors.remove(node);
	}
	
	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getzPos() {
		return zPos;
	}

	public void setzPos(double zPos) {
		this.zPos = zPos;
	}

	public int getNodeID(){
		return this.nodeID;
	}
	public LinkedList<MapNode> getNeighbors(){
		return this.neighbors;
	}

}

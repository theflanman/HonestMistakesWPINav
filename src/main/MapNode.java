package main;

import java.io.Serializable;
import java.util.ArrayList;
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
	private ArrayList<MapNode> neighbors;
	private double fScore;
	private double gScore;
	private double hScore;
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
	
	public double aStarHeuristic(MapNode toNode) {
		
		double dist = (double) Math.sqrt(Math.pow((xPos - toNode.getxPos()),2) + Math.pow(yPos - toNode.getyPos(),2)) + Math.abs(zPos - toNode.getzPos());
		
		return dist;
		
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
	
	public void setGScore(double distance) {
		gScore = distance;
	}

	public void setHScore(double distance) {
		hScore = distance;	
	}

	public ArrayList<MapNode> getNeighbors() {
		return neighbors;
	}

	public double getGScore() {
		return gScore;
	}

	public void setCameFrom(MapNode current) {
		cameFrom = current;
	}

	public double getFScore() {
		return fScore;
	}

	public MapNode getCameFrom() {
		return cameFrom;
	}

	public void calcFScore() {
		fScore = gScore + hScore;		
	}

}

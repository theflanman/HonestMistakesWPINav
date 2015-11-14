package main;

import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class MapNode {
	
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
	
	
	public double getXPos() {
		return this.xPos;
	}
	
	public void setXPos(double xPos) {
		this.xPos = xPos;
	}
	
	public double getYPos() {
		return this.yPos;
	}
	
	public void setYPos(double yPos) {
		this.yPos = yPos;
	}
	
	public MapNode getCameFrom() {
		return cameFrom;
	}
	
	public void setCameFrom(MapNode cameFrom){
		this.cameFrom = cameFrom;		
	}
	
	/**
	 * @author Nick Gigliotti
	 * 
	 * @param the node to calculate the distance
	 * @return int distance between nodes
	 */
	public int calcDistance(MapNode toNode) {
		double distance = 0;
		double distanceXLeg = toNode.getXPos() - this.getXPos();
		double distanceYLeg = toNode.getYPos() - this.getYPos();
		
		distance = Math.sqrt((distanceXLeg * distanceXLeg) + (distanceYLeg * distanceYLeg));
		distance = Math.round(distance);
		return (int)distance;
	}
		
	/**
	 * @author Rayan Alsoby
	 * 
	 * function to calculate the angle needed to turn from the current to be facing the target node 
	 * @param nextNode the target node to turn towards
	 * @return the angle needed to turn in order to be facing the next node
	 * 90 degrees is left, 270 degrees is right, 180 degrees is forward, 0 degrees is backwards (hope we don't get the last one)
	 */
	public double calculateAngle(MapNode nextNode) {
		MapNode currentNode = this;
		MapNode previousNode = currentNode.getCameFrom();
		double prevX = previousNode.getXPos();
		double prevY = previousNode.getYPos();
		double currentX = currentNode.getXPos();
		double currentY = currentNode.getYPos();
		double nextX = nextNode.getXPos();
		double nextY = nextNode.getYPos();
		
	    double angle1 = Math.atan2(prevY - currentY, prevX - currentX);
	    double angle2 = Math.atan2(nextY - currentY, nextX - currentX);
		
	    double radAngle;
		 radAngle = angle1 - angle2;
		 
		 double resultAngle = Math.toDegrees(radAngle);
			if (resultAngle < 0){
				resultAngle += 360;
			}
			return resultAngle;
	}
	
	public void addNeighbor(MapNode node) {
		
	}
	

}

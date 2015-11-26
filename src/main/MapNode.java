package main;

import java.io.Serializable;
import java.util.ArrayList;


public class MapNode implements Serializable{
	//coordinates in pixels
	private double xPos;
	private double yPos;
	//coordinates in feet
	private double xFeet;
	private double yFeet;
	private double zFeet;
	private int nodeID;
	private ArrayList<MapNode> neighbors;
	private double fScore;
	private double gScore;
	private double hScore;
	private MapNode cameFrom;
	private GlobalMap globalMap;
	private LocalMap localMap;
	private String nodeName;
	//private Attributes attributes
	
	// default constructor
	public MapNode(){}
	
	// constructor
	public MapNode(double newX, double newY, LocalMap aLocalMap) {
		xPos = newX;
		yPos = newY;
		localMap = aLocalMap;
		xFeet = xPos * localMap.getMapScale();
		yFeet = yPos * localMap.getMapScale();
		zFeet = localMap.getZHeight();
		neighbors = new ArrayList<MapNode>();
		fScore = -1;
		gScore = -1;
		hScore = -1;
		cameFrom = null;
	}
	
	public void addNeighbor(MapNode node) {
		neighbors.add(node);
	}
	
	public void removeNeighbor(MapNode node) {
		neighbors.remove(node);
	}
	
	public double aStarHeuristic(MapNode toNode) {
		double dist = (double) Math.sqrt(Math.pow((xFeet - toNode.getXFeet()),2) + Math.pow(yFeet - toNode.getYFeet(),2)) + Math.abs(zFeet - toNode.getZFeet());
		
		return dist;
	}

	public void deleteNeighborLink(MapNode node){
		this.neighbors.remove(node);
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

	/**
	 * @author Nick Gigliotti
	 * 
	 * @param the node to calculate the distance
	 * @return int distance between nodes
	 */
	public int calcDistance(MapNode toNode) {
		double distance = 0;
		double distanceXLeg = (toNode.getXPos() - this.getXPos());
		double distanceYLeg = (toNode.getYPos() - this.getYPos());
	
		distance = (Math.sqrt((distanceXLeg * distanceXLeg) + (distanceYLeg * distanceYLeg)));
		distance = Math.round(distance);
		return (int)distance;
	}

	public ArrayList<MapNode> getNeighbors() {
		return neighbors;
	}
	
	public double getXPos() {
		return xPos;
	}
	public void setXPos(double xPos) {
		this.xPos = xPos;
	}

	public double getYPos() {
		return yPos;
	}
	
	
	public void setYPos(double yPos) {
		this.yPos = yPos;
	}

	public String getnodeName() {
		return nodeName;
	}
	public void setxPos(double pos) {
		xPos = pos;
	}
	public void setyPos(double pos) {
		yPos = pos;
	}
	public void setnodeName(String name) {
		nodeName = name;
	}
	public int getID(){
		return nodeID;
	}
	
	public MapNode getCameFrom() {
		return cameFrom;
	}
	
	public void setCameFrom(MapNode cameFrom){
		this.cameFrom = cameFrom;		
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

	public double getGScore() {
		return gScore;
	}

	public double getFScore() {
		return fScore;
	}

	public void calcFScore() {
		fScore = gScore + hScore;		
	}
	public void setLocalMap(LocalMap localMap){
		this.localMap = localMap;
	}
	public LocalMap getLocalMap(){
		return localMap;
	}
	public void setXFeet(double xFeet){
		this.xFeet = xFeet;
	}
	public void setYFeet(double yFeet){
		this.yFeet = yFeet;
	}
	public double getXFeet(){
		return xFeet;
	}
	public double getYFeet(){
		return yFeet;
	}
	public void setZFeet(double zHeight){
		zFeet = zHeight;
	}
	public double getZFeet(){
		return zFeet;
	}

}
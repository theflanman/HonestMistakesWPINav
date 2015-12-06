package main;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MapNode implements Serializable{

	private double xPos;
	private double yPos;
	private double zPos;

	private double xFeet;
	private double yFeet;
	private double zFeet;

	private String nodeID;
	private ArrayList<MapNode> neighbors;
	private ArrayList<String> crossMapNeighbors;
	private double fScore;
	private double gScore;
	private double hScore;
	private MapNode cameFrom;
	private GlobalMap globalMap;
	private LocalMap localMap;
	private Attributes attributes;

	// default constructor
	public MapNode(){
		xPos = -1.0;
		yPos = -1.0;
		zPos = -1.0;
		xFeet = -1.0;
		yFeet = -1.0;
		zFeet = -1.0;

		neighbors = new ArrayList<MapNode>();
		crossMapNeighbors = new ArrayList<String>();
		attributes = new Attributes();
		fScore = -1;
		gScore = -1;
		hScore = -1;
		cameFrom = null;
	}

	// constructor
	public MapNode(LocalMap localMap){
		neighbors = new ArrayList<MapNode>();
		localMap = this.localMap;
		attributes = new Attributes();
		fScore = -1;
		gScore = -1;
		hScore = -1;
		cameFrom = null;
		this.localMap = localMap;
	}

	public MapNode(double newX, double newY, LocalMap aLocalMap) {
		xPos = newX;
		yPos = newY;
		zPos = 0.0;

		xFeet = xPos * aLocalMap.getMapScale();
		yFeet = yPos * aLocalMap.getMapScale();
		zFeet = aLocalMap.getZHeight();
		neighbors = new ArrayList<MapNode>();
		crossMapNeighbors = new ArrayList<String>();
		attributes = new Attributes();
		fScore = -1;
		gScore = -1;
		hScore = -1;
		cameFrom = null;
		attributes = new Attributes();

		this.localMap = aLocalMap;		
	}

	public void runTransform(){
		double xPrime = this.getXFeet()*Math.cos(this.getLocalMap().getTransformAngle()) - this.getYFeet()*Math.sin(this.getLocalMap().getTransformAngle());
		double yPrime = this.getYFeet()*Math.cos(this.getLocalMap().getTransformAngle()) + this.getXFeet()*Math.sin(this.getLocalMap().getTransformAngle());

		// translate the x and y coordinates of the local map to resemble its location in the global map 
		xPrime = xPrime + this.getLocalMap().getXOffset();
		yPrime = yPrime + this.getLocalMap().getYOffset();

		// set the new transformed coordinates
		this.setXFeet(xPrime);
		this.setYFeet(yPrime);
	}

	public LocalMap getLocalMap() {
		return localMap;
	}

	public void setLocalMap(LocalMap localMap) {
		this.localMap = localMap;
	}

	public void setCrossMapNeighbors(ArrayList<String> s){
		this.crossMapNeighbors = s;
	}

	public ArrayList<String> getCrossMapNeighbors(){
		return this.crossMapNeighbors;
	}

	public void addNeighbor(MapNode node) {
		neighbors.add(node);
	}

	public void removeNeighbor(MapNode node) {
		neighbors.remove(node);
		for(int i = 0; i < this.crossMapNeighbors.size(); i++){
			if(node.getNodeID().equals(this.crossMapNeighbors.get(i))){
				this.crossMapNeighbors.remove(i);
			}
		}
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

		double prevX = previousNode.getXFeet();
		double prevY = previousNode.getYFeet();
		double currentX = currentNode.getXFeet();
		double currentY = currentNode.getYFeet();
		double nextX = nextNode.getXFeet();
		double nextY = nextNode.getYFeet();

		double angle1 = Math.atan2(prevY - currentY, prevX - currentX);
		double angle2 = Math.atan2(nextY - currentY, nextX - currentX);

		double radAngle;
		radAngle = angle1 - angle2;

		double resultAngle = Math.toDegrees(radAngle);
		if (resultAngle < 0)
			resultAngle += 360;

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
		double distanceXLeg = (toNode.getXFeet() - this.getXFeet());
		double distanceYLeg = (toNode.getYFeet() - this.getYFeet());

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

	public double getZPos() {
		return zPos;
	}
	public void setZPos(double zPos) {
		this.zPos = zPos;
	}
	public void setxPos(double pos) {
		xPos = pos;
	}
	public void setyPos(double pos) {
		yPos = pos;
	}

	public String getID(){
		return nodeID;
	}

	public MapNode getCameFrom() {
		return cameFrom;
	}

	public void setCameFrom(MapNode cameFrom){
		this.cameFrom = cameFrom;		
	}

	public String getNodeID(){
		return this.nodeID;
	}
	public void setNodeID(String id){
		this.nodeID = id;
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

	public Attributes getAttributes() {
		return this.attributes;
	}

	public void setDefaultAttributes(Attributes defaultAttributes) {
		Attributes attributes = this.getAttributes();
		attributes.setStairs(defaultAttributes.isStairs());
		attributes.setPOI(defaultAttributes.isPOI());
		attributes.setBikeable(defaultAttributes.isBikeable());
		attributes.setHandicapped(defaultAttributes.isHandicapped());
		attributes.setOutside(defaultAttributes.isOutside());
		attributes.setType(defaultAttributes.getType());
	}

	public void setAttributes(Attributes a) {
		this.attributes = a;
	}

}

package main;
/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class MapNode {
	
	private double xPos;
	private double yPos;
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
	
	public double calcDistance(MapNode toNode) {
		double distance = 0;
		double distanceXLeg = toNode.getXPos() - this.getXPos();
		double distanceYLeg = toNode.getYPos() - this.getYPos();
		
		distance = Math.sqrt((distanceXLeg * distanceXLeg) + (distanceYLeg * distanceYLeg));
		
		return distance;
	}
	
	public void addNeighbor(MapNode node) {
		
	}

}

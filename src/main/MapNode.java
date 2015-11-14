package main;
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
	private MapNode[] neighbors;
	private float fScore;
	private float gScore;
	private float hScore;
	private MapNode cameFrom;
	private GlobalMap globalMap;
	private LocalMap localMap;
	//private Attributes attributes
	
	public MapNode(double xPos, double yPos, double zPos){
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
	}
	
	public float calcDistance(MapNode toNode) {
		
		return 0;
		
	}
	
	public void addNeighbor(MapNode node) {
		
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
	
	

}

import java.util.ArrayList;

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
	private String nodeName;
	//private Attributes attributes
	
	public double calcDistance(MapNode toNode) {
		
		return 0;
		
	}
	
	public void addNeighbor(MapNode node) {
		
		neighbors.add(node);
		
	}
	
	public void removeNeighbor(MapNode node) {
		neighbors.remove(node);
	}
	
	public ArrayList<MapNode> getNeighbors() {
		return neighbors;
	}
	

	public MapNode(double newX, double newY, double newZ, int newID) {
		
		xPos = newX;
		yPos = newY;
		zPos = newZ;
		nodeID = newID;
		//globalMap = newGlobalMap;
		//localMap = newLocalMap;
		
		neighbors = new ArrayList<MapNode>();
		fScore = -1;
		gScore = -1;
		hScore = -1;
		cameFrom = null;
		
		
	}
	
	public double getXPos() {
		return xPos;
	}
	
	public double getYPos() {
		return yPos;
	}
	public double getZPos() {
		return zPos;
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

}
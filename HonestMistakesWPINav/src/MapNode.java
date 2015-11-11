public class MapNode {
	
	private float xPos;
	private float yPos;
	private float zPos;
	private int nodeID;
	private MapNode[] neighbors;
	private float fScore;
	private float gScore;
	private float hScore;
	private MapNode cameFrom;
//	private GlobalMap globalMap;
//	private LocalMap localMap;
	private String nodeName;
	//private Attributes attributes
	
	public float calcDistance(MapNode toNode) {
		
		return 0;
		
	}
	
	public void addNeighbor(MapNode node) {
		
	}
	
	public MapNode(int x, int y, int count) {
		xPos = (float) x;
		yPos = (float) y;
		nodeID = count;
	}
	
	public float getxPos() {
		return xPos;
	}
	
	public float getyPos() {
		return yPos;
	}
	public float getzPos() {
		return zPos;
	}
	public String getnodeName() {
		return nodeName;
	}
	public void setxPos(float pos) {
		xPos = pos;
	}
	public void setyPos(float pos) {
		yPos = pos;
	}
	public void setnodeName(String name) {
		nodeName = name;
	}
	public int getID(){
		return nodeID;
	}
	public String getNeighborsString() {
		String result = "";
		for(int i=0; i < neighbors.length; i++) {
			result += neighbors[i].getnodeName();
		}
		return result;
	}
}
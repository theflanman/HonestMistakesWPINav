package main;
/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class AStar {
	
	private MapNode startNode;
	private MapNode endNode;
	
	/**
	 * @author Connor Flanigan
	 * 
	 * @param nodes
	 */
	public AStar(MapNode[] nodes) {
		startNode = nodes[0];
		endNode = nodes[1];
	}
	
	/**
	 * @author Connor Flanigan
	 * 
	 * @return true if a valid path exists, false otherwise
	 */
	public boolean runAlgorithm() {
		
		return false;
		
	}
	
	/**
	 * 
	 * @return the list of nodes that describes the most efficient path from the start to the end
	 */
	public MapNode[] reconstructPath() {
		
		//create a linkedlist of MapNodes starting with the endNode
		
		return null;
	}

}

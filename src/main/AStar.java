package main;

import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class AStar {
	
	private MapNode startNode;
	private ArrayList<MapNode> endNode;
	private ArrayList<ArrayList<MapNode>> waypoints;
	private AStar parent;
	private AStar child;
	private ArrayList<MapNode> path;
	
	/**
	 * @author Connor Flanigan
	 * 
	 * @param nodes
	 */
	public AStar(MapNode[] nodes) {
		startNode = nodes[0];
		endNode = new ArrayList<MapNode>();
		endNode.set(0, nodes[1]);
		
		nullifyNodes(startNode);
		
		parent = null;
		child = null;
		path = null;
		waypoints = null;
		      
	}
	
	
	public AStar(ArrayList<ArrayList<MapNode>> wayPoints) throws AStarException {
		
		//handle invalid cases
		if (wayPoints.size() < 2) { 
			throw new AStarException("list too short, size is " + Integer.toString(wayPoints.size()));
		} if (wayPoints.get(0).size() > 1) {
			throw new AStarException("too many start nodes, size is" + Integer.toString(wayPoints.get(0).size()));
		} else {

			startNode = wayPoints.get(0).get(0);
			endNode = wayPoints.get(0);
			this.waypoints = wayPoints;
			
			nullifyNodes(startNode);
			
			parent = null;
			child = null;
			path = null;
			
		}
		
	}
	
	public AStar(ArrayList<ArrayList<MapNode>> wayPoints, AStar aStar) throws AStarException {
		
		//handle invalid cases
		if (wayPoints.size() < 2) { 
			throw new AStarException("list too short, size is " + Integer.toString(wayPoints.size()));
		} if (wayPoints.get(0).size() > 1) {
			throw new AStarException("too many start nodes, size is" + Integer.toString(wayPoints.get(0).size()));
		} else {

			startNode = wayPoints.get(0).get(0);
			endNode = wayPoints.get(0);
			this.waypoints = wayPoints;
			
			nullifyNodes(startNode);
			
			parent = aStar;
			child = null;
			path = null;
			
		}
		
	}


	/**
	 * @author Connor Flanigan
	 * 
	 * @return true if a valid path exists, false otherwise
	 * @throws AStarException 
	 * 
	 * @description Find the most efficient route in the graph by updating the cameFrom members in the path
	 */
	public boolean runAlgorithm() throws AStarException {
		
		
		//initialize the various sets
		ArrayList<MapNode> closedSet = new ArrayList<MapNode>();
		ArrayList<MapNode> openSet = new ArrayList<MapNode>();	
		
		//initialize the scores of the start node
		startNode.setGScore(0);
		startNode.setHScore(startNode.aStarHeuristic(endNode));
		startNode.calcFScore();
		
		//add the startNode to the open set
		openSet.add(startNode);
		
		//while the open set contains nodes, there may be a valid solution
		while (openSet.size() > 0) {
			
			//the node currently being processed is the node popped from the open set
			MapNode current = openSet.get(0);
			openSet.remove(0);
			
			//if the current node is the end node, return true
			if (endNode.contains(current)) {
				
				this.reconstructPath();
				
				if (waypoints.size() > 2) {
					
					waypoints.remove(0);
					waypoints.get(0).set(0, current);
					endNode.set(0, current);
					
					path = this.reconstructPath();
					
					child = new AStar(waypoints, this);
					
				}
				
				return true;
			}
			
			//add the current node to the closed set
			closedSet.add(current);
			
			ArrayList<MapNode> neighbors = current.getNeighbors();
			
			//process every neighbor of the current node
			for (int i = 0; i < neighbors.size(); i++) {
				
				//process the neighbor at the current index
				MapNode neighbor = neighbors.get(i);
				
				
				if (closedSet.contains(neighbor)) {
					continue;
				}
				
				//distance from the best path to the current node plus the distance to the neighbor
				double tentativeGScore = current.getGScore() + current.aStarHeuristic(neighbor);
				
				//add an unexplored node to the open set if it hasn't been explored or...
				if (!openSet.contains(neighbor)) {
					openSet.add(neighbor);
				} else if (tentativeGScore >= neighbor.getGScore()) {  //do not process the neighbor if this isn't a better path
					continue;
				}
				
				//update the neighbor
				neighbor.setCameFrom(current);
				neighbor.setGScore(tentativeGScore);
				neighbor.calcFScore();
				
				openSet = this.sortNodes(openSet);
				
			}
			
		}
		
		//if the open set is empty, there is no valid solution
		return false;
		
	}
	
	/**@author Connor Flanigan
	 * 
	 * @return the list of nodes that describes the most efficient path from the start to the end
	 */
	public ArrayList<MapNode> reconstructPath() {
		
		if (this.path == null) {	
		
			//start the empty path
			ArrayList<MapNode> path = new ArrayList<MapNode>();
			
			//we begin at the end
			MapNode current = endNode.get(0);
			
			//while the current node came from another node (is not the start)
			while (current.getCameFrom() != null) {
				
				//add the current node to the path and update the current node
				path.add(0,current);
				current = current.getCameFrom();
				
			}
			
			path.add(0, startNode); // start doesn't seem to be included ?
			// did someone add the above comment?  -CJF
			
			this.path = path;
			
		} 
		
		if (this.child != null) {
			this.path.addAll(this.child.reconstructPath());
		}
		
		return this.path;
		
	}
	
	/**
	 * @author Connor Flanigan
	 * @param openSet 
	 * 
	 * @return a linked list of the open set sorted based on fScore
	 */
	private ArrayList<MapNode> sortNodes(ArrayList<MapNode> openSet) {
		
		//bubble sort the open set
		for (int i = openSet.size(); i > 0; i--) {
			for (int j = 0; j < i-1; j++) {
				
				if (openSet.get(j).getFScore() > openSet.get(j+1).getFScore()) {
					
					MapNode swapA = openSet.get(j);
					MapNode swapB = openSet.get(j+1);
					
					openSet.set(j, swapB);
					openSet.set(j+1, swapA);
					
				}
				
			}
		}
		
		return openSet;
		
	}
	
	private void nullifyNodes (MapNode node) {
		
		ArrayList<MapNode> gNodes = node.getGlobalMap().getMapNodes();
		
		for (int i = 0; i < gNodes.size(); i++) {
			gNodes.get(i).setCameFrom(null);
		}
		
	}


}

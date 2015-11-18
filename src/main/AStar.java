package main;

import java.util.ArrayList;

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
		for (MapNode node : nodes){
			node.setCameFrom(null);
		}
		      
	}
	
	/**
	 * @author Connor Flanigan
	 * 
	 * @return true if a valid path exists, false otherwise
	 * 
	 * @description Find the most efficient route in the graph by updating the cameFrom members in the path
	 */
	public boolean runAlgorithm() {
		
		
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
			if (current == endNode) {
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
		
		//start the empty path
		ArrayList<MapNode> path = new ArrayList<MapNode>();
		
		//we begin at the end
		MapNode current = endNode;
		
		//while the current node came from another node (is not the start)
		while (current.getCameFrom() != null) {
			
			//add the current node to the path and update the current node
			path.add(0,current);
			current = current.getCameFrom();
			
		}
		path.add(0, startNode); // start doesn't seem to be included ?
		
		return path;
		
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


}

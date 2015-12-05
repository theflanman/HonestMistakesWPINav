package main;

import java.util.ArrayList;

/*
 * This class contains methods regarding the StepByStep directions
 * 
 */
public class StepByStep {
	
	private ArrayList<MapNode> pathNodes;
	private int stepNumber;
	
	
	public StepByStep(ArrayList<MapNode> pathNodes){
		this.pathNodes = pathNodes;
		this.stepNumber = 1;
	}
	
	/*
	 * @ author Nick Gigliotti
	 * 
	 * @param node 1
	 * @param node 2
	 * 
	 * @ return double of the total distance for the directions
	 */
	public int calculateTotalDistance() {
		int totalDistance = 0;
		MapNode node1 = null;
		MapNode node2 = null;
		for(int i = 0; i < pathNodes.size() - 1; i++){
			node1 = pathNodes.get(i);
			node2 = pathNodes.get(i+1);
			totalDistance += node1.calcDistance(node2);
		}
		return totalDistance;
	}
	
	
	/**
	 * @author Rayan Alsoby
	 * @author Nick Gigliotti
	 * 
	 * @return list of instruction for the step by step navigation in as one string for each instruction
	 */
	public ArrayList<String> printDirection() {
		ArrayList<String> stepList = new ArrayList<String>();
		
		
		double angle;
		String turn = "";
		int distance = 0;
		int i;
		int j;
		int stepNumber = 1;
		String direction = "";
		
		// Case where there is only 1 node in the route
		if (pathNodes.size() == 1) {
			turn ="Welcome to the era of navigation, you have reached your destination.";
			stepList.add(turn);
			
		} else {
			for (i = 0; i <= (pathNodes.size() - 1); i++) {
				
				// First node in the path
				// TODO Make the first direction have a direction
				if (i == 0) {
					distance = pathNodes.get(i).calcDistance(pathNodes.get(i + 1));
					turn = String.format("%d. Welcome to the era of Navigation, move %d feet.", stepNumber, distance);
					stepList.add(turn);
					stepNumber ++;
					
				// Last node in the route
				} else if (i == (pathNodes.size() - 1)) {
					turn = String.format("%d. You have arrived at your destination", stepNumber);
					stepList.add(turn);
				} else {
					distance = pathNodes.get(i).calcDistance(pathNodes.get(i + 1));
					angle = pathNodes.get(i).calculateAngle(pathNodes.get(i + 1));
					
					// if the node is stairs
					if (pathNodes.get(i).getAttributes().isStairs() && pathNodes.get(i + 1).getAttributes().isStairs()) {
						// If going upstairs
						// TODO Make direction stairs work
						/*
						if () {
							direction = "up";
						} else {
							direction = "down";
						}
						*/
							
						turn = String.format("%d. Walk %s the stairs for %d feet", stepNumber, direction, distance);
						stepList.add(turn);
						stepNumber ++;
					}
					
					// case of going straight
					if (190 > angle && angle > 170) {
						for (j = i; j <= (pathNodes.size() - 2); j++) {
							angle = pathNodes.get(j).calculateAngle(pathNodes.get(j + 1));
							if (190 > angle && angle > 170) {
								i ++;
								distance +=  pathNodes.get(j).calcDistance(pathNodes.get(j + 1));
							} else {
								break ;
							}
							turn = String.format("%d. Continue straight for %d feet", stepNumber, distance);
							stepList.add(turn);
							stepNumber ++;
						}
						
					} else {
						if (170 >= angle && angle >= 110) {
							direction = "slight right";
						}
						if (110 > angle && angle > 70) {
							direction = "right";
						}
						if (70 >= angle && angle >= 20) {
							direction = "sharp right";
						}
						if (20 > angle || angle > 340) {
							direction = "back";
						}
						if (340 >= angle && angle >= 290) {
							direction = "sharp left";
						}
						if (290 > angle && angle > 250) {
							direction = "left";
						}
						if (250 >= angle && angle >= 190) {
							direction = "slight left";
						}
						turn = String.format("%d. Turn %s, and continue for %d feet", stepNumber, direction,  distance);
						stepList.add(turn);
						stepNumber ++;
					}	
				}
			}
		}
		return stepList;
	}
}
package main;

import java.util.ArrayList;

/*
 * This class contains methods regarding the StepByStep directions
 * 
 */
public class StepByStep {
	private ArrayList<MapNode> pathNodes;

	public StepByStep(ArrayList<MapNode> pathNodes) {
		this.pathNodes = pathNodes;
	}

	/*
	 * @ author Nick Gigliotti
	 * 
	 * @param node 1
	 * 
	 * @param node 2
	 * 
	 * @ return double of the total distance for the directions
	 */
	public int calculateTotalDistance() {
		// Initialize variable
		int totalDistance = 0;
		MapNode node1 = null;
		MapNode node2 = null;

		// Iterate over every node in the list of nodes to sum distance
		for (int i = 0; i < pathNodes.size() - 1; i++) {
			node1 = pathNodes.get(i);
			node2 = pathNodes.get(i + 1);
			totalDistance += node1.calcDistance(node2);
		}
		return totalDistance;
	}


	/**
	 * @author Nick Gigliotti
	 * 
	 * @return list of MapNode
	 */
	public ArrayList<MapNode> cleanUpPath() {
		// Initialize list of nodes to return
		ArrayList<MapNode> mapNodes = new ArrayList<MapNode>();

		int i;
		int j;
		double angle;

		// Iterates through each node in the path
		for (i = 0; i <= (pathNodes.size() - 1); i++) {

			// Adds First
			if (i == 0) {
				mapNodes.add(pathNodes.get(i));
			}

			// Adds Last
			else if (i == pathNodes.size() - 1) {
				mapNodes.add(pathNodes.get(i));
				break;
			}

			else {
				for (j = i; j <= (pathNodes.size() - 2); j++) {
					angle = pathNodes.get(j).calculateAngle(pathNodes.get(j + 1));

					// If direction is not going straight
					if (190 > angle && angle > 170) {
						if (pathNodes.get(i).getAttributes().isStairs() || pathNodes.get(i).getAttributes().getType().equals("door")) {
							break;
						}
						else {
							i ++;
						}
					}
					else {
						break ;
					}
				}
				mapNodes.add(pathNodes.get(i));
			}
		}
		pathNodes = mapNodes;
		return mapNodes;
	}


	/**
	 * @author Rayan Alsoby
	 * @author Nick Gigliotti
	 * 
	 * @return list of instruction for the step by step navigation in as one
	 *         string for each instruction
	 */
	public ArrayList<String> printDirection() {
		// Initialize list of strings to return
		ArrayList<String> stepList = new ArrayList<String>();

		// Initialize variables 
		double angle;
		int distance = 0;
		String turn = "";
		String direction = "";
		int stepNumber = 1;
		char floorNum1;
		char floorNum2;
		String temp = "";
		int i;
		
		// Removes the nodes that are unnecessary
		// Will be taken out after being implemented elsewhere
		this.cleanUpPath();

		// Case where there is only 1 node in the route
		if (pathNodes.size() == 1) {
			turn = "Welcome to the era of navigation, you have arrived at your destination.";
			stepList.add(turn);
		} 
		else {
			// Iterates through each node in the path
			for (i = 0; i <= (pathNodes.size() - 1); i++) {

				// First node in the path
				if (i == 0) {
					// TODO Add in a starting reference direction.
					turn = String.format("Welcome to the era of Navigation.", stepNumber);
					stepList.add(turn);
					
					// If the first node has an official name adds the name to the directions
					if (! pathNodes.get(i).getAttributes().getOfficialName().isEmpty()) {
						turn = pathNodes.get(i).getAttributes().getOfficialName();
						stepList.add(turn);
					}
				}
				else {
					distance = pathNodes.get(i-1).calcDistance(pathNodes.get(i));

					// Last node in the route
					if (i == (pathNodes.size() - 1)) {
						turn = String.format("%d. Walk %d feet, then you will arrive at your destination.", stepNumber, distance);
						stepList.add(turn);
						
						// If the last node has an official name adds the name to the directions
						if (! pathNodes.get(i).getAttributes().getOfficialName().isEmpty()) {
							turn = pathNodes.get(i).getAttributes().getOfficialName();
							stepList.add(turn);
						}
					} 
					else {

						// if the current node and next node is stairs
						if (pathNodes.get(i).getAttributes().isStairs()) {

							if (pathNodes.get(i - 1).getAttributes().isStairs()) {

								// Finds the floor for each direction
								temp = pathNodes.get(i - 1).getNodeID().split("_")[0];
								floorNum1 = temp.charAt(temp.length() - 1);
								temp = pathNodes.get(i).getNodeID().split("_")[0];
								floorNum2 = temp.charAt(temp.length() - 1);

								// Going upstairs
								if (floorNum1 < floorNum2) { 
									direction = "up";
								} 

								// Going downstairs
								else { 
									direction ="down";
								}

								// Adds this stairs step to list
								turn = String.format("%d. Walk %s the stairs to floor %c.", stepNumber, direction, floorNum2);
								stepList.add(turn);
								stepNumber++;
							}
							else {
								turn = String.format("%d. Continue walking %s feet to the stairs.", stepNumber, distance);
								stepList.add(turn);
								stepNumber++;
							}
						}

						else {
							// calculates angle for the current turn
							angle = pathNodes.get(i).calculateAngle(pathNodes.get(i + 1));

							// case of going straight
							if (190 > angle && angle > 170) {

								// If you are going into a building
								if (pathNodes.get(i - 1).getAttributes().getType().equals("door")
										&& pathNodes.get(i).getAttributes().getType().equals("door")) {
									turn = String.format("%d. Walk %d feet, then continue into building.", stepNumber);
									stepList.add(turn);
									stepNumber++;
								}

								// Not going into building, this should never happen
								else {
									turn = String.format("%d. Walk %d feet, then continue straight.", stepNumber,
											distance);
									stepList.add(turn);
									stepNumber++;
								}
							} 

							// Turn angle possibilities and words associated
							else {
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

								// Adds this turn step to list
								turn = String.format("%d. Walk %d feet, then turn %s.", stepNumber, distance, direction);
								stepList.add(turn);
								stepNumber++;
							}
						}
					}
				}
			}
		}
		return stepList;
	}
}

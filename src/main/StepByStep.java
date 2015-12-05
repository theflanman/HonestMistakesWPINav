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
		int totalDistance = 0;
		MapNode node1 = null;
		MapNode node2 = null;
		for (int i = 0; i < pathNodes.size() - 1; i++) {
			node1 = pathNodes.get(i);
			node2 = pathNodes.get(i + 1);
			totalDistance += node1.calcDistance(node2);
		}
		return totalDistance;
	}

	/**
	 * @author Rayan Alsoby
	 * @author Nick Gigliotti
	 * 
	 * @return list of instruction for the step by step navigation in as one
	 *         string for each instruction
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
		int extraIncr = 0;

		// Case where there is only 1 node in the route
		if (pathNodes.size() == 1) {
			turn = "Welcome to the era of navigation, you have arrived at your destination.";
			stepList.add(turn);

		} else {
			for (i = 0; i <= (pathNodes.size() - 1); i++) {
				
				// First node in the path
				// TODO Make the first direction have a direction
				if (i == 0) {
					turn = String.format("%d. Welcome to the era of Navigation, head %s.", stepNumber,
							direction);
					stepList.add(turn);
					stepNumber++;

				}  else {
					distance = pathNodes.get(i-1).calcDistance(pathNodes.get(i));
					for (j = i; j <= (pathNodes.size() - 2); j++) {
						angle = pathNodes.get(j).calculateAngle(pathNodes.get(j + 1));
						if (190 > angle && angle > 170) {
							distance += pathNodes.get(j).calcDistance(pathNodes.get(j + 1));
							i ++;
						} else {
							break;
						}
					}

					// Last node in the route
					if (i == (pathNodes.size() - 1)) {
						turn = String.format("%d. In %d feet, you will arrive at your destination.", stepNumber, distance);
						stepList.add(turn);

					} else {
						// if the node is stairs
						if (pathNodes.get(i -1).getAttributes().isStairs()
								&& pathNodes.get(i).getAttributes().isStairs()) {
							// If going upstairs
							// TODO Make direction stairs work
							/*
							 * if () { direction = "up"; } else { direction =
							 * "down"; }
							 */

							turn = String.format("%d. Walk %s the stairs for %d feet.", stepNumber, direction, distance);
							stepList.add(turn);
							stepNumber++;

						} else {
							angle = pathNodes.get(i).calculateAngle(pathNodes.get(i + 1));

							// case of going straight
							if (190 > angle && angle > 170) {
								
								// If you are going into a building
								if (pathNodes.get(i - 1).getAttributes().getType().equals("door")
										&& pathNodes.get(i).getAttributes().getType().equals("door")) {
									turn = String.format("%d. In %d feet, continue into building.", stepNumber);
									stepList.add(turn);
									stepNumber++;

								} else {
									turn = String.format("%d. In %d feet, continue straight.", stepNumber,
											distance);
									stepList.add(turn);
									stepNumber++;
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
								turn = String.format("%d. In %d feet, turn %s.", stepNumber, distance, direction
										);
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

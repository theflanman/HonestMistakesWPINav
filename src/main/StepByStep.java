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
	 * 
	 * @return list of instruction for the step by step navigation in as one string for each instruction
	 */
	public ArrayList<String> printDirection() {
		ArrayList<String> stepList = new ArrayList<String>();
		double angle;
		String turnVar = "";
		int distanceVar = 0;
		
		String firstStep = String.format("%d. Welcome to the era of navigation, move %d feet.", stepNumber, distanceVar);
		String midStepTurn = String.format("%d. Turn %s, and continue for %d feet.", stepNumber, turnVar, distanceVar);
		String midStepNoTurn = String.format("%d. Continue for %d feet.", stepNumber, distanceVar);
		String lastStep = "You have reached your destination.";
		
		int i;
		if (pathNodes.size() == 1) {
			turnVar = "Welcome to the era of navigation, you have reached your destination.";
		} else {
			for (i = 0; i <= (pathNodes.size() - 1); i++) {

				if (i == 0) {
					distanceVar = pathNodes.get(i).calcDistance(pathNodes.get(i + 1));
					firstStep = String.format("%d. Welcome to the era of navigation, move %d feet.", stepNumber,
							distanceVar);
					stepNumber++;
					stepList.add(firstStep);
				}

				else if (i == (pathNodes.size() - 1)) {
					stepList.add("" + (stepList.size() + 1) + ". " + lastStep);
				}

				else {
					distanceVar = pathNodes.get(i).calcDistance(pathNodes.get(i + 1));
					angle = pathNodes.get(i).calculateAngle(pathNodes.get(i + 1));

					// case of going straight
					if (200 > angle && angle > 160) {
						midStepNoTurn = String.format("%d. Continue for %d feet.", stepNumber, distanceVar);
						stepNumber++;
						stepList.add(midStepNoTurn);
					}
					// printing different statements for different angles
					else {
						if (160 >= angle && angle >= 110) {
							turnVar = "slight right";
						}
						if (110 > angle && angle > 70) {
							turnVar = "right";
						}
						if (70 >= angle && angle >= 20) {
							turnVar = "hard right";
						}
						if (20 > angle || angle > 340) {
							turnVar = "back";
						}
						if (340 >= angle && angle >= 290) {
							turnVar = "hard left";
						}
						if (290 > angle && angle > 250) {
							turnVar = "left";
						}
						if (250 >= angle && angle >= 200) {
							turnVar = "slight left";
						}
						midStepTurn = String.format("%d. Turn %s, and continue for %d feet.", stepNumber, turnVar,
								distanceVar);
						stepNumber++;
						stepList.add(midStepTurn);
					}
				}
			}
		} // end of loop
		return stepList;
	}
	
	public void advanceStep() { 
	}
}
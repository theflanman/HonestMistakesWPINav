package main;


import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class StepbyStep {

	// TODO make sure the datatypes are correct
	private ArrayList<MapNode> pathNodes;
	//private int currentPathNodeIndex;
	

	public StepbyStep(ArrayList<MapNode> pathNodes){
		this.pathNodes = pathNodes;
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
		double totalDistance = 0;
		MapNode node1 = null;
		MapNode node2 = null;
		for(int i = 0; i < pathNodes.size() - 1; i++){
			node1 = pathNodes.get(i);
			node2 = pathNodes.get(i+1);
			totalDistance += node1.calcDistance(node2);
		}
		return (int) totalDistance;
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

		String firstStep = String.format("Welcome to the era of navigation, move %d feet.", distanceVar);
		String midStepTurn = String.format("Turn %s, and continue for %d feet.", turnVar, distanceVar);
		String midStepNoTurn = String.format("Continue for %d feet.", distanceVar);
		String lastStep = "You have reached your destination.";

		int i;
		for (i = 0; i <= (pathNodes.size()-1) ; i++) {
			
			if (i == 0) {
				distanceVar = pathNodes.get(i).calcDistance(pathNodes.get(i + 1));
				firstStep = String.format("Welcome to the era of navigation, move %d feet.", distanceVar);
				stepList.add(firstStep);
			} 
			
			else if (i == (pathNodes.size()-1)) {
				stepList.add(lastStep);
			} 
			
			else {
				distanceVar = pathNodes.get(i).calcDistance(pathNodes.get(i + 1));
				angle = pathNodes.get(i).calculateAngle(pathNodes.get(i + 1));
				
				//case of going straight
				if (200 > angle && angle > 160) {
					midStepNoTurn = String.format("Continue for %d feet.", distanceVar);
					stepList.add(midStepNoTurn);
				}
				// printing different statements for different angles
				else{
					if (160 >= angle && angle >= 110) {
						turnVar = "slight left";
					}
					if (110 > angle && angle > 70) {
						turnVar = "left";
					}
					if (70 >= angle && angle >= 20) {
						turnVar = "hard left";
					}
					if (20 > angle || angle > 340) {
						turnVar = "Back";
					}
					if (340 >= angle && angle >= 290) {
						turnVar = "hard right";
					}
					if (290 > angle && angle > 250) {
						turnVar = "right";
					}
					if (250 >= angle && angle >= 200) {
						turnVar = "slight right";
					}
					midStepTurn = String.format("Turn %s, and continue for %d feet.", turnVar, distanceVar);
					stepList.add(midStepTurn);
				}
			}

		}//end of loop
		return stepList;
	}
	
	public void advanceStep() { //not necessary for this revision 
	}
	
}


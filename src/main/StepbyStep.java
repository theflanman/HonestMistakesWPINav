package main;

import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class StepbyStep {

	// TODO make sure the datatypes are correct
	private MapNode[] instructionList;
	private int currentInstructionIndex;
	
	/**
	 * function to calculate the angle needed to turn from the current to be facing the target node 
	 * @param currentNode the current map node to make the turn on
	 * @param nextNode the target node to turn towards
	 * @return the angle needed to turn in order to be facing the next node
	 * 90 degrees is left, 270 degrees is right, 180 degrees is forward, 0 degrees is backwards (hope we don't get the last one)
	 */
	public double calculateAngle(MapNode currentNode, MapNode nextNode) {
		
		MapNode previousNode = currentNode.getCameFrom();
		float prevX = previousNode.getXPos();
		float prevY = previousNode.getYPos();
		float currentX = currentNode.getXPos();
		float currentY = currentNode.getYPos();
		float nextX = nextNode.getXPos();
		float nextY = nextNode.getYPos();
		
	    double angle1 = Math.atan2(prevY - currentY, prevX - currentX);
	    double angle2 = Math.atan2(nextY - currentY, nextX - currentX);
		
	    double radAngle;
		 radAngle = angle1 - angle2;
		 
		 double resultAngle = Math.toDegrees(radAngle);
			if (resultAngle < 0){
				resultAngle += 360;
			}
			return resultAngle;
	}
	
	public void calculateDistance() {
		
	}
	
	/**
	 * 
	 * @param nodes list of map nodes representing a path starting from the last step
	 * @return list of instruction for the step by step navigation in as one string for each instruction
	 */
	public ArrayList <String> printDirection(ArrayList<MapNode>  nodes) {
		ArrayList <String> stepList = new ArrayList <String>();
		double angle;
		String turnVar = "";
		float distanceVar = 0;
		
		String firstStep = String.format("Welcome to the era of navigation, move %f feet.", distanceVar);
		String midStepTurn = String.format("Turn %s, and continue for %f feet.", turnVar,distanceVar);
		String midStepNoTurn = String.format("Continue for %f feet.", distanceVar);
		String lastStep = "You have reached your destination.";
		
		int i;
		//length might be changed to the size of the arraylist if MapNode array is changed to arraylist
		for(i = nodes.size(); i >= 0; i--){
			if (i == nodes.size()){
				distanceVar = nodes.get(i).calcDistance(nodes.get(i-1));
				stepList.add(firstStep);
			}
			else if(i == 0){
				stepList.add(lastStep);
			}
			else{
				distanceVar = nodes.get(i).calcDistance(nodes.get(i-1));
				angle =  calculateAngle(nodes.get(i), nodes.get(i-1));
				//printing different statements for different angles 
				if (200 > angle && angle > 160){
					stepList.add(midStepNoTurn);
				}
				if (160 >= angle && angle >= 110){
					turnVar = "slight left";
					stepList.add(midStepTurn);
				}
				if (110 > angle && angle > 70){
					turnVar = "left";
					stepList.add(midStepTurn);
				}
				if (70 >= angle && angle >= 20 ){
					turnVar = "hard left";
					stepList.add(midStepTurn);
				}
				if (20 > angle && angle > 340 ){
					turnVar = "Back";
					stepList.add(midStepTurn);
				}
				if (340 >= angle && angle >= 290){
					turnVar = "hard right";
					stepList.add(midStepTurn);
				}
				if (290 > angle && angle > 250){
					turnVar = "right";
					stepList.add(midStepTurn);
				}
				if (250 >= angle && angle >= 200){
					turnVar = "slight right";
					stepList.add(midStepTurn);
				}
			}
			
		}
		
		return stepList;
	}
	
	public void advanceStep() {
		
	}
	
}

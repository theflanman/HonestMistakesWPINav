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
	public double calculateTotalDistance() {
		double totalDistance = 0;
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
	 * 
	 * @param nodes list of map nodes representing a path starting from the last step
	 * @return list of instruction for the step by step navigation in as one string for each instruction
	 */
	public ArrayList <String> printDirection() {
		ArrayList <String> stepList = new ArrayList <String>();
		double angle;
		String turnVar = "";
		double distanceVar = 0;
		
		String firstStep = String.format("Welcome to the era of navigation, move %f feet.", distanceVar);
		String midStepTurn = String.format("Turn %s, and continue for %f feet.", turnVar,distanceVar);
		String midStepNoTurn = String.format("Continue for %f feet.", distanceVar);
		String lastStep = "You have reached your destination.";
		
		int i;
		//length might be changed to the size of the arraylist if MapNode array is changed to arraylist
		for(i = pathNodes.size(); i >= 0; i--){
			if (i == pathNodes.size()){
				distanceVar = pathNodes.get(i).calcDistance(pathNodes.get(i-1));
				stepList.add(firstStep);
			}
			else if(i == 0){
				stepList.add(lastStep);
			}
			else{
				distanceVar = pathNodes.get(i).calcDistance(pathNodes.get(i-1));
				angle =  pathNodes.get(i).calculateAngle(pathNodes.get(i-1));
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
	public void advanceStep() { //not necessary for this revision 
	}
	
}


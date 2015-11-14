package main;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class StepbyStep {

	// TODO make sure the datatypes are correct
	private MapNode[] instructionList;
	private int currentInstructionIndex;
	
	public StepbyStep(MapNode[] instructionList){
		this.instructionList = instructionList;
	}
	
	public void calculateAngle() {
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
		for(int i = 0; i < instructionList.length - 1; i++){
			node1 = instructionList[i];
			node2 = instructionList[i+1];
			totalDistance += node1.calcDistance(node2);
		}
		return totalDistance;
	}
	
	public void printDirection() {
	}
	
	public void advanceStep() { //not necessary for this revision 
	}
	
}


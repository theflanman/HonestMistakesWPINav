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
	 * @ return float distance between nodes
	 */
	public double calculateDistance(MapNode node1, MapNode node2) {
		return node1.calcDistance(node2);
	}
	
	public void printDirection() {
	}
	
	public void advanceStep() { //not necessary for this revision 
	}
	
}


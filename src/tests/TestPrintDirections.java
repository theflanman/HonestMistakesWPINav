package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import main.MapNode;
import main.StepByStep;

public class TestPrintDirections extends TestCase{
	
	public void testPrintDirection(){
		MapNode node1 = new MapNode();
		node1.setXPos(3);
		node1.setYPos(1);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode();
		node2.setXPos(0);
		node2.setYPos(2);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode();
		node3.setXPos(1);
		node3.setYPos(5);
		node3.setCameFrom(node2);
		
		MapNode node4 = new MapNode();
		node4.setXPos(7);
		node4.setYPos(-3);
		node4.setCameFrom(node3);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		instructionList1.add(node4);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("Welcome to the era of navigation, move 3 feet.");
		resultSteps.add("Turn right, and continue for 3 feet.");
		resultSteps.add("Turn hard right, and continue for 10 feet.");
		resultSteps.add("You have reached your destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
}

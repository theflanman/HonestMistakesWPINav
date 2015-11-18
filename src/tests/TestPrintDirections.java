package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import main.MapNode;
import main.StepByStep;

public class TestPrintDirections extends TestCase{
	
	public void testDirectionRightAndHardRight(){
		MapNode node1 = new MapNode(3, 1, 0);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode(0, 2, 0);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode(1, 5, 0);
		node3.setCameFrom(node2);
		
		MapNode node4 = new MapNode(7, -3, 0);
		node4.setCameFrom(node3);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		instructionList1.add(node4);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Welcome to the era \nof navigation, move \n3 feet.");
		resultSteps.add("2. Turn left, and \ncontinue for \n3 feet.");
		resultSteps.add("3. Turn hard left, and \ncontinue for \n10 feet.");
		resultSteps.add("4. You have reached \nyour destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
	
	public void testDirectionsContinueStraight(){
		MapNode node1 = new MapNode(3, 1, 0);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode(3, 2, 0);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode(3, 5, 0);
		node3.setCameFrom(node2);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Welcome to the era \nof navigation, move \n1 feet.");
		resultSteps.add("2. Continue for 3 feet.");
		resultSteps.add("3. You have reached \nyour destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
	
	public void testDirectionsBackAndLeft(){
		MapNode node1 = new MapNode(10, -4, 0);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode(2, -4, 0);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode(6, -4, 0);
		node3.setCameFrom(node2);
		
		MapNode node4 = new MapNode(6, 0, 0);
		node4.setCameFrom(node3);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		instructionList1.add(node4);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Welcome to the era \nof navigation, move \n8 feet.");
		resultSteps.add("2. Turn back, and \ncontinue for \n4 feet.");
		resultSteps.add("3. Turn right, and \ncontinue for \n4 feet.");
		resultSteps.add("4. You have reached \nyour destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
	
	public void testDirectionsComplete(){
		MapNode node1 = new MapNode(-1, 1, 0);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode(2, 5, 0);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode(2, 0, 0);
		node3.setCameFrom(node2);
		
		MapNode node4 = new MapNode(0, -3, 0);
		node4.setCameFrom(node3);
		
		MapNode node5 = new MapNode(-2, -6, 0);
		node5.setCameFrom(node4);
		
		MapNode node6 = new MapNode(0, -3, 0);
		node6.setCameFrom(node5);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		instructionList1.add(node4);
		instructionList1.add(node5);
		instructionList1.add(node6);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Welcome to the era \nof navigation, move \n5 feet.");
		resultSteps.add("2. Turn hard left, and \ncontinue for \n5 feet.");
		resultSteps.add("3. Turn slight left, and \ncontinue for \n4 feet.");
		resultSteps.add("4. Continue for 4 feet.");
		resultSteps.add("5. Turn back, and \ncontinue for \n4 feet.");
		resultSteps.add("6. You have reached \nyour destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
}

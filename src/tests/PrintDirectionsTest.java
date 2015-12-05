package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import main.LocalMap;
import main.MapNode;
import main.StepByStep;

public class PrintDirectionsTest extends TestCase{
	
	LocalMap localMap1 = new LocalMap("sample.jpg", null);
	
	public void testDirection1(){
		MapNode node1 = new MapNode(3, 1, localMap1);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode(3, 2, localMap1);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode(3, 6, localMap1);
		node3.setCameFrom(node2);
		
		MapNode node4 = new MapNode(3, 7, localMap1);
		node4.setCameFrom(node3);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		instructionList1.add(node4);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Welcome to the era of Navigation, move 6 feet.");
		resultSteps.add("2. You have arrived at your destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}

	public void testDirections2(){
		MapNode node1 = new MapNode(3, 1, localMap1);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode(3, 2, localMap1);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode(3, 5, localMap1);
		node3.setCameFrom(node2);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Welcome to the era of Navigation, move 4 feet.");
		resultSteps.add("2. You have arrived at your destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
	
	public void testDirections3(){
		MapNode node1 = new MapNode(10, -4, localMap1);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode(2, -4, localMap1);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode(6, -4, localMap1);
		node3.setCameFrom(node2);
		
		MapNode node4 = new MapNode(6, 0, localMap1);
		node4.setCameFrom(node3);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		instructionList1.add(node4);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Welcome to the era of Navigation, move 8 feet.");
		resultSteps.add("2. Turn back, and continue for 4 feet.");
		resultSteps.add("3. Turn right, and continue for 4 feet.");
		resultSteps.add("4. You have arrived at your destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
	
	public void testDirections4(){
		MapNode node1 = new MapNode(-1, 1, localMap1);
		node1.setCameFrom(null);
		
		MapNode node2 = new MapNode(2, 5, localMap1);
		node2.setCameFrom(node1);
		
		MapNode node3 = new MapNode(2, 0, localMap1);
		node3.setCameFrom(node2);
		
		MapNode node4 = new MapNode(0, -3, localMap1);
		node4.setCameFrom(node3);
		
		MapNode node5 = new MapNode(-2, -6, localMap1);
		node5.setCameFrom(node4);
		
		MapNode node6 = new MapNode(0, -3, localMap1);
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
		resultSteps.add("1. Welcome to the era of Navigation, move 5 feet.");
		resultSteps.add("2. Turn sharp left, and continue for 5 feet.");
		resultSteps.add("3. Turn slight left, and continue for 8 feet.");
		resultSteps.add("4. Turn back, and continue for 4 feet.");
		resultSteps.add("5. You have arrived at your destination.");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
}

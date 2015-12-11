package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import main.Attributes;
import main.LocalMap;
import main.MapNode;
import main.StepByStep;

public class PrintDirectionsTest extends TestCase {
	
	LocalMap localMap1 = new LocalMap("sample.png", null);
	
	public void testCleanUp1() {
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
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		
		ArrayList<MapNode> resultPath = new ArrayList<MapNode>();
		resultPath.add(node1);
		resultPath.add(node4);
		
		assertEquals(stepByStep1.cleanUpPath(), resultPath);
	}
	
	public void testCleanUp2(){
		MapNode node1 = new MapNode(3, 1, localMap1);
		node1.setCameFrom(null);
		
		Attributes atr2 = new Attributes();
		atr2.setStairs(true);
		
		MapNode node2 = new MapNode(3, 2, localMap1);
		node2.setCameFrom(node1);
		node2.setAttributes(atr2);
		node2.setNodeID("AK3_59");
		
		Attributes atr3 = new Attributes();
		atr3.setStairs(true);
		
		MapNode node3 = new MapNode(3, 6, localMap1);
		node3.setCameFrom(node2);
		node3.setAttributes(atr3);
		node3.setNodeID("AK1_4");
		
		Attributes atr4 = new Attributes();
		atr4.setStairs(true);
		
		MapNode node4 = new MapNode(3, 7, localMap1);
		node4.setCameFrom(node3);
		node4.setAttributes(atr4);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		instructionList1.add(node4);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		
		ArrayList<MapNode> resultPath = new ArrayList<MapNode>();
		resultPath.add(node1);
		resultPath.add(node2);
		resultPath.add(node3);
		resultPath.add(node4);
		

		assertEquals(stepByStep1.cleanUpPath(), resultPath);
	}
	
	public void testDirection1() {
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
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		stepByStep1.cleanUpPath();
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Walk 6 feet, then you will arrive at your final destination. ENDHERE");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}

	public void testDirections2() {
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
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		stepByStep1.cleanUpPath();
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Walk 4 feet, then you will arrive at your final destination. ENDHERE");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
	
	public void testDirections3() {
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
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		stepByStep1.cleanUpPath();
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Walk 8 feet, then turn back.");
		resultSteps.add("2. Walk 4 feet, then turn right.");
		resultSteps.add("3. Walk 4 feet, then you will arrive at your final destination. ENDHERE");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
	
	public void testDirections4() {
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
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		stepByStep1.cleanUpPath();
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Walk 5 feet, then turn sharp left.");
		resultSteps.add("2. Walk 5 feet, then turn slight left.");
		resultSteps.add("3. Walk 7 feet, then turn back.");
		resultSteps.add("4. Walk 4 feet, then you will arrive at your final destination. ENDHERE");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
	
	public void testDirection5(){
		MapNode node1 = new MapNode(3, 1, localMap1);
		node1.setCameFrom(null);
		
		Attributes atr2 = new Attributes();
		atr2.setStairs(true);
		
		MapNode node2 = new MapNode(3, 2, localMap1);
		node2.setCameFrom(node1);
		node2.setAttributes(atr2);
		node2.setNodeID("AK3_59");
		
		Attributes atr3 = new Attributes();
		atr3.setStairs(true);
		
		MapNode node3 = new MapNode(3, 6, localMap1);
		node3.setCameFrom(node2);
		node3.setAttributes(atr3);
		node3.setNodeID("AK1_4");
		
		Attributes atr4 = new Attributes();
		atr4.setStairs(true);
		
		MapNode node4 = new MapNode(3, 7, localMap1);
		node4.setCameFrom(node3);
		node4.setAttributes(atr4);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		
		instructionList1.add(node1);
		instructionList1.add(node2);
		instructionList1.add(node3);
		instructionList1.add(node4);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		stepByStep1.cleanUpPath();
		
		ArrayList<String> resultSteps = new ArrayList<String>();
		resultSteps.add("1. Continue walking 1 feet to the stairs.");
		resultSteps.add("2. Walk down the stairs to floor 1.");
		resultSteps.add("3. Walk 1 feet, then you will arrive at your final destination. ENDHERE");

		assertEquals(stepByStep1.printDirection(), resultSteps);
	}
}

package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import main.LocalMap;
import main.MapNode;
import main.StepByStep;

public class DistanceTests extends TestCase{
	
	LocalMap localMap1 = new LocalMap("sample.png", null);
	
	public void testNodeDistanceInts() {
		MapNode node1 = new MapNode(5, 3, localMap1);
		MapNode node2 = new MapNode(1, 0, localMap1);
		
		assertEquals(node1.calcDistance(node2), 5); 
	}
	
	public void testNodeDistanceNegative() {
		MapNode node1 = new MapNode(-2, 8, localMap1);
		MapNode node2 = new MapNode(1, -4, localMap1);
		
		assertEquals(node1.calcDistance(node2), 12); 
	}
	
	public void testNodeDistanceDecimal() {
		MapNode node1 = new MapNode(3.2, 1.4, localMap1);
		MapNode node2 = new MapNode(.5, 2.1, localMap1);
		
		assertEquals(node1.calcDistance(node2), 3); 
	}
	
	public void testTotalDistance() {
		MapNode node1 = new MapNode(3, 1, localMap1);
		MapNode node2 = new MapNode(0, 2, localMap1);
		MapNode node3 = new MapNode(1, 5, localMap1);
		MapNode node4 = new MapNode(7, -3, localMap1);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		instructionList1.add(node4);
		instructionList1.add(node3);
		instructionList1.add(node2);
		instructionList1.add(node1);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		
		assertEquals(stepByStep1.calculateTotalDistance(), 16);
	}
	
	public void testTotalDistanceSingleDistance() {
		MapNode node1 = new MapNode(3, 1, localMap1);
		MapNode node2 = new MapNode(0, 2, localMap1);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		instructionList1.add(node2);
		instructionList1.add(node1);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1, false);
		
		assertEquals(stepByStep1.calculateTotalDistance(), 3);
	}
}

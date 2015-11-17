package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import main.MapNode;
import main.StepByStep;

public class DistanceTests extends TestCase{
	public void testNodeDistanceInts() {
		MapNode node1 = new MapNode(5, 3, 0);
		MapNode node2 = new MapNode(1, 0, 0);
		
		assertEquals(node1.calcDistance(node2), 5); 
	}
	
	public void testNodeDistanceNegative() {
		MapNode node1 = new MapNode(-2, 8, 0);
		MapNode node2 = new MapNode(1, -4, 0);
		
		assertEquals(node1.calcDistance(node2), 12); 
	}
	
	public void testNodeDistanceDecimal() {
		MapNode node1 = new MapNode(3.2, 1.4, 0);
		MapNode node2 = new MapNode(.5, 2.1, 0);
		
		assertEquals(node1.calcDistance(node2), 3); 
	}
	
	public void testTotalDistance() {
		MapNode node1 = new MapNode(3, 1, 0);
		MapNode node2 = new MapNode(0, 2, 0);
		MapNode node3 = new MapNode(1, 5, 0);
		MapNode node4 = new MapNode(7, -3, 0);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		instructionList1.add(node4);
		instructionList1.add(node3);
		instructionList1.add(node2);
		instructionList1.add(node1);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		assertEquals(stepByStep1.calculateTotalDistance(), 16);
	}
	
	public void testTotalDistanceSingleDistance() {
		MapNode node1 = new MapNode(3, 1, 0);
		MapNode node2 = new MapNode(0, 2, 0);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		instructionList1.add(node2);
		instructionList1.add(node1);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		assertEquals(stepByStep1.calculateTotalDistance(), 3);
	}
}

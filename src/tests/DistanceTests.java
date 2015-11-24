package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import main.LocalMap;
import main.MapNode;
import main.StepByStep;

public class DistanceTests extends TestCase{
	public void testNodeDistanceInts() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("sample.jpg", nodes);
		MapNode node1 = new MapNode(5, 3, local);
		MapNode node2 = new MapNode(1, 0, local);
		
		assertEquals(node1.calcDistance(node2), 5); 
	}
	
	public void testNodeDistanceNegative() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("sample.jpg", nodes);
		MapNode node1 = new MapNode(-2, 8, local);
		MapNode node2 = new MapNode(1, -4, local);
		
		assertEquals(node1.calcDistance(node2), 12); 
	}
	
	public void testNodeDistanceDecimal() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("sample.jpg", nodes);
		MapNode node1 = new MapNode(3.2, 1.4, local);
		MapNode node2 = new MapNode(.5, 2.1, local);
		
		assertEquals(node1.calcDistance(node2), 3); 
	}
	
	public void testTotalDistance() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("sample.jpg", nodes);
		MapNode node1 = new MapNode(3, 1, local);
		MapNode node2 = new MapNode(0, 2, local);
		MapNode node3 = new MapNode(1, 5, local);
		MapNode node4 = new MapNode(7, -3, local);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		instructionList1.add(node4);
		instructionList1.add(node3);
		instructionList1.add(node2);
		instructionList1.add(node1);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		assertEquals(stepByStep1.calculateTotalDistance(), 16);
	}
	
	public void testTotalDistanceSingleDistance() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("sample.jpg", nodes);
		MapNode node1 = new MapNode(3, 1, local);
		MapNode node2 = new MapNode(0, 2, local);
		
		ArrayList<MapNode> instructionList1 = new ArrayList<MapNode>();
		instructionList1.add(node2);
		instructionList1.add(node1);
		
		StepByStep stepByStep1 = new StepByStep(instructionList1);
		
		assertEquals(stepByStep1.calculateTotalDistance(), 3);
	}
}

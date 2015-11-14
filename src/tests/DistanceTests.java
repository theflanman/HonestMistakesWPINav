package tests;

import junit.framework.TestCase;
import main.MapNode;
import main.StepbyStep;

public class DistanceTests extends TestCase{
	public void testNodeDistanceInts() {
		MapNode node1 = new MapNode();
		node1.setXPos(5);
		node1.setYPos(3);
		
		MapNode node2 = new MapNode();
		node2.setXPos(1);
		node2.setYPos(0);
		
		assertEquals(node1.calcDistance(node2), Math.sqrt(25)); 
	}
	
	public void testNodeDistanceNegative() {
		MapNode node1 = new MapNode();
		node1.setXPos(-2);
		node1.setYPos(8);
		
		MapNode node2 = new MapNode();
		node2.setXPos(1);
		node2.setYPos(-4);
		
		assertEquals(node1.calcDistance(node2), Math.sqrt(153)); 
	}
	
	public void testNodeDistanceDecimal() {
		MapNode node1 = new MapNode();
		node1.setXPos(3.2);
		node1.setYPos(1.4);
		
		MapNode node2 = new MapNode();
		node2.setXPos(.5);
		node2.setYPos(2.1);
		
		assertEquals(node1.calcDistance(node2), Math.sqrt(7.78)); 
	}
	
	/*
	 * public void testTotalDistance() {
	 
		MapNode node1 = new MapNode();
		node1.setXPos(3);
		node1.setYPos(1);
		
		MapNode node2 = new MapNode();
		node2.setXPos(0);
		node2.setYPos(2);
		
		MapNode node3 = new MapNode();
		node3.setXPos(1);
		node3.setYPos(5);
		
		MapNode node4 = new MapNode();
		node4.setXPos(7);
		node4.setYPos(-3);
		
		MapNode[] instructionList1 = {node1, node2, node3, node4};
		
		StepbyStep stepByStep1 = new StepbyStep(instructionList1);
		
		assertEquals(stepByStep1.calculateTotalDistance() = 10);
	}
	 */
}

package tests;

import junit.framework.TestCase;
import main.MapNode;

public class AngleTests extends TestCase {
	public void test270() {
		MapNode node1 = new MapNode(3, 1, 0);
		MapNode node2 = new MapNode(3, 2, 0);
		node2.setCameFrom(node1);
		MapNode node3 = new MapNode(5, 2, 0);

		assertEquals(node2.calculateAngle(node3), 270.0);
	}

	public void test180() {
		MapNode node1 = new MapNode(10, 5, 0);
		MapNode node2 = new MapNode(10, 9, 0);
		node2.setCameFrom(node1);
		MapNode node3 = new MapNode(10, 15, 0);

		assertEquals(node2.calculateAngle(node3), 180.0);
	}

	public void test0() {
		MapNode node1 = new MapNode(-5, 2, 0);
		MapNode node2 = new MapNode(-7, 2, 0);
		node2.setCameFrom(node1);
		MapNode node3 = new MapNode(0, 2, 0);

		assertEquals(node2.calculateAngle(node3), 0.0);
	}

	public void testIntermediate() {
		MapNode node1 = new MapNode(0, 2, 0);
		MapNode node2 = new MapNode(0, 0, 0);
		node2.setCameFrom(node1);
		MapNode node3 = new MapNode(1, 1, 0);

		assertEquals(node2.calculateAngle(node3), 45.0);
	}
}

package tests;

import java.util.ArrayList;

import junit.framework.TestCase;
import main.LocalMap;
import main.MapNode;

public class AngleTests extends TestCase {
	public void test270() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("downstairsCC.jpg", nodes);
		MapNode node1 = new MapNode(3, 1, local);
		MapNode node2 = new MapNode(3, 2, local);
		node2.setCameFrom(node1);
		MapNode node3 = new MapNode(5, 2, local);
	/*	node1.setXFeet(1.0);
		node2.setXFeet(1.0);
		node3.setXFeet(1.0);
		node1.setYFeet(1.0);
		node2.setYFeet(1.0);
		node3.setYFeet(1.0);
*/
		assertEquals(node2.calculateAngle(node3), 270.0);
	}

	public void test180() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("sample.jpg", nodes);
		MapNode node1 = new MapNode(10, 5, local);
		MapNode node2 = new MapNode(10, 9, local);
		node2.setCameFrom(node1);
		MapNode node3 = new MapNode(10, 15, local);

		assertEquals(node2.calculateAngle(node3), 180.0);
	}

	public void test0() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("sample.jpg", nodes);
		MapNode node1 = new MapNode(-5, 2, local);
		MapNode node2 = new MapNode(-7, 2, local);
		node2.setCameFrom(node1);
		MapNode node3 = new MapNode(0, 2, local);

		assertEquals(node2.calculateAngle(node3), 0.0);
	}

	public void testIntermediate() {
		ArrayList<MapNode> nodes = new ArrayList<MapNode>();
		LocalMap local = new LocalMap("sample.jpg", nodes);
		MapNode node1 = new MapNode(0, 2, local);
		MapNode node2 = new MapNode(0, 0, local);
		node2.setCameFrom(node1);
		MapNode node3 = new MapNode(1, 1, local);
		
		assertEquals(node2.calculateAngle(node3), 45.0);
	}
}

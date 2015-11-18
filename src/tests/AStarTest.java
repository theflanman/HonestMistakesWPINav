package tests;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import main.AStar;
import main.LocalMap;
import main.MapNode;

public class AStarTest {

	@Test
	public void test() {
		
		//          n4------n5
		//          |     /
		//          n3  /
		//        / |
		//      /   |
		//    /     |
		// n1-------n2
		MapNode n1 = new MapNode(0,0,0);
		MapNode n2 = new MapNode(3,0,0);
		MapNode n3 = new MapNode(3,4,0);
		MapNode n4 = new MapNode(3,6,0);
		MapNode n5 = new MapNode(6,6,0);
		
		//node 1 = start
		//node 5 = finish
		
		n1.addNeighbor(n2);
		n1.addNeighbor(n3);
		
		n2.addNeighbor(n1);
		n2.addNeighbor(n3);
		
		n3.addNeighbor(n1);
		n3.addNeighbor(n2);
		n3.addNeighbor(n4);
		n3.addNeighbor(n5);
		
		n4.addNeighbor(n3);
		n4.addNeighbor(n5);
		
		n5.addNeighbor(n3);
		n5.addNeighbor(n4);
		
		//put these into a node list, like a LMap would have
		ArrayList<MapNode> nodelist = new ArrayList<MapNode>();
		nodelist.add(n1);
		nodelist.add(n2);
		nodelist.add(n3);
		nodelist.add(n4);
		nodelist.add(n5);
		
		//set start and finish to [0] and [1]
		MapNode[] startAndFinish = new MapNode[2];
		startAndFinish[0]=n1;
		startAndFinish[1]=n5;
		
		//correct path is n1->n3->n4
		AStar testAStar = new AStar(startAndFinish);
		testAStar.runAlgorithm();
		ArrayList<MapNode> path = testAStar.reconstructPath();
		
		//make sure the path size is correct and that the steps along the path are correct.
		assertEquals(path.size(), 3);
		assertEquals(path.get(0), n1);
		assertEquals(path.get(1), n3);
		assertEquals(path.get(2), n5);
		
	}

}

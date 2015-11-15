package tests;

import java.util.ArrayList;

import main.DeveloperGUIBackend;
import main.GUIBackend;
import main.GlobalMap;
import main.LocalMap;
import main.MapNode;
import junit.framework.TestCase;

public class LocalMapTests extends TestCase {

	public void testConstructor(){
		LocalMap localMap = new LocalMap("downstairsCC.jpg");
		
		assertEquals(.107979, localMap.getMapScale());
	}
	
	public void testAddNode(){
		LocalMap localMap = new LocalMap("downstairsCC.jpg");
		localMap.setGlobalMap(new GlobalMap());
		
		localMap.addNode(1.0, 2.0, 3.0);
		
		ArrayList<MapNode> nodes = localMap.getGlobalMap().getMapNodes();
		assertEquals(3.0, nodes.get(0).getZPos());
	}

}

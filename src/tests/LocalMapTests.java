package tests;

import java.util.ArrayList;

import main.GlobalMap;
import main.LocalMap;
import main.MapNode;
import main.gui.DeveloperGUIBackend;
import main.gui.GUIBackend;
import junit.framework.TestCase;

public class LocalMapTests extends TestCase {

	public void testConstructor(){
		LocalMap localMap = new LocalMap("downstairsCC.jpg", null);
		
		assertEquals(.107979, localMap.getMapScale());
	}
	
	public void testAddNode(){
		LocalMap localMap = new LocalMap("downstairsCC.jpg", null);
		localMap.setGlobalMap(new GlobalMap());
		
		localMap.addNode(1.0, 2.0, 3.0);
		
		ArrayList<MapNode> nodes = localMap.getGlobalMap().getMapNodes();
		assertEquals(3.0, nodes.get(0).getZPos());
	}

}

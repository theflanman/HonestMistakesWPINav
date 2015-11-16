package tests;

import main.LocalMap;
import main.MapNode;
import main.gui.DeveloperGUIBackend;
import main.gui.GUIBackend;
import junit.framework.TestCase;

public class LoadSaveMapTests extends TestCase {

	public void testDeveloperGUIBackend(){
		LocalMap localMap1 = new LocalMap("map1", null);
		localMap1.setMapID(1); // give it some random data
		
		DeveloperGUIBackend devGUIBack1 = new DeveloperGUIBackend(localMap1);
		
		devGUIBack1.saveMap(); // save map to a file
		
		DeveloperGUIBackend devGUIBack2 = new DeveloperGUIBackend(null);

		devGUIBack2.loadMap("src/localmaps/map1.localmap"); // get map from file
		
		assertEquals(devGUIBack2.getLocalMap().getMapID(), 1); // check to see if the data is the same
	}
	
	public void testGUIBackend(){
		LocalMap localMap1 = new LocalMap("map1", null);
		localMap1.setMapID(2); // give it some random data
		
		DeveloperGUIBackend devGUIBack1 = new DeveloperGUIBackend(localMap1);
		
		devGUIBack1.saveMap(); // save map to a file
		
		GUIBackend guiBack1 = new GUIBackend("map1.png", null);

		guiBack1.loadLocalMap("map1.localmap"); // load the local map
		
		assertEquals(guiBack1.getLocalMap().getMapID(), 2); // check to see if the data are the same		
	}
	
	public void testLocalMap(){
		LocalMap localMap1 = new LocalMap("map1", null);
		localMap1.setMapID(3);
		
		localMap1.saveMap("map1");
		
		GUIBackend guiBack1 = new GUIBackend("map1.png", null);

		guiBack1.loadLocalMap("map1.localmap"); // load the local map
		
		assertEquals(guiBack1.getLocalMap().getMapID(), 3); // check to see if the data are the same
	}

}

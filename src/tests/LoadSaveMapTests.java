package tests;

import main.DeveloperGUIBackend;
import main.GUIBackend;
import main.LocalMap;
import junit.framework.TestCase;

public class LoadSaveMapTests extends TestCase {

	public void testDeveloperGUIBackend(){
		DeveloperGUIBackend devGUIBack1 = new DeveloperGUIBackend();
		LocalMap localMap1 = new LocalMap("a");
		localMap1.setMapID(1); // give it some random data
		
		devGUIBack1.setLocalMap(localMap1); // set LocalMap for DeveloperGUIBackend
		
		devGUIBack1.saveMap("map1"); // save map to a file
		
		DeveloperGUIBackend devGUIBack2 = new DeveloperGUIBackend();

		devGUIBack2.loadMap("map1.localmap"); // get map from file
		
		assertEquals(devGUIBack2.getLocalMap().getMapID(), 1); // check to see if the data is the same
	}
	
	public void testGUIBackend(){
		DeveloperGUIBackend devGUIBack1 = new DeveloperGUIBackend();
		LocalMap localMap1 = new LocalMap("a");
		localMap1.setMapID(2); // give it some random data
		
		devGUIBack1.setLocalMap(localMap1); // set LocalMap for DeveloperGUIBackend
		
		devGUIBack1.saveMap("map1"); // save map to a file
		
		GUIBackend guiBack1 = new GUIBackend();

		guiBack1.loadLocalMap("map1.localmap"); // load the local map
		
		assertEquals(guiBack1.getLocalMap().getMapID(), 2); // check to see if the data are the same		
	}
	
	public void testLocalMap(){
		LocalMap localMap1 = new LocalMap("a");
		localMap1.setMapID(3);
		
		localMap1.saveMap("map1");
		
		GUIBackend guiBack1 = new GUIBackend();

		guiBack1.loadLocalMap("map1.localmap"); // load the local map
		
		assertEquals(guiBack1.getLocalMap().getMapID(), 3); // check to see if the data are the same
	}

}

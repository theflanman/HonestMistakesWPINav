package tests;

import main.LocalMap;
import main.gui.DeveloperGUIBackend;
import main.gui.GUIBackend;

import java.io.File;

import junit.framework.TestCase;
public class LoadSaveMapTests extends TestCase {
	
	String path = new String("src/localmaps/map1.localmap");
	LocalMap localMap1 = new LocalMap("sample.jpg", null);
	File file = new File(path);

	public void testDeveloperGUIBackend(){
		localMap1.setMapID(1); // give it some random data
		
		DeveloperGUIBackend devGUIBack1 = new DeveloperGUIBackend(localMap1);
		
		devGUIBack1.saveMap(); // save map to a file
		
		DeveloperGUIBackend devGUIBack2 = new DeveloperGUIBackend(null);

		devGUIBack2.loadMap(path); // get map from file
		
		assertEquals(devGUIBack2.getLocalMap().getMapID(), 1); // check to see if the data is the same
		
		file.delete();
	}
	
	public void testGUIBackend(){
		localMap1.setMapID(2); // give it some random data
		
		DeveloperGUIBackend devGUIBack1 = new DeveloperGUIBackend(localMap1);
		
		devGUIBack1.saveMap(); // save map to a file
		
		GUIBackend guiBack1 = new GUIBackend("sample.jpg", null);

		guiBack1.loadLocalMap("map1.localmap"); // load the local map
		
		assertEquals(guiBack1.getLocalMap().getMapID(), 2); // check to see if the data are the same	
		
		file.delete();
	}
	
	public void testLocalMap(){
		localMap1.setMapID(3);
		
		localMap1.saveMap("map1");
		
		GUIBackend guiBack1 = new GUIBackend("sample.jpg", null);

		guiBack1.loadLocalMap("map1.localmap"); // load the local map
		
		assertEquals(guiBack1.getLocalMap().getMapID(), 3); // check to see if the data are the same
		
		file.delete();
	}
}

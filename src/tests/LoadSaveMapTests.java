package tests;

import main.LocalMap;
import main.gui.DevGUIBack;
import main.gui.GUIBack;

import java.io.File;

import junit.framework.TestCase;
public class LoadSaveMapTests extends TestCase {
	
	String path = new String("src/data/localmaps/sample.localmap");
	LocalMap sample = new LocalMap("sample.jpg", null);
	File file = new File(path);

	public void testDeveloperGUIBackend(){
		sample.setMapID(1); // give it some random data
		
		DevGUIBack devGUIBack1 = new DevGUIBack(sample);
		
		devGUIBack1.saveMap(); // save map to a file
		
		DevGUIBack devGUIBack2 = new DevGUIBack(null);

		devGUIBack2.loadMap(path); // get map from file
		
		assertEquals(devGUIBack2.getLocalMap().getMapID(), 1); // check to see if the data is the same
		
		file.delete();
	}
	
	public void testGUIBackend(){
		sample.setMapID(2); // give it some random data
		
		DevGUIBack devGUIBack1 = new DevGUIBack(sample);
		
		devGUIBack1.saveMap(); // save map to a file
		
		GUIBack guiBack1 = new GUIBack("sample.jpg", null);

		guiBack1.loadLocalMap("sample.localmap"); // load the local map
		
		assertEquals(guiBack1.getLocalMap().getMapID(), 2); // check to see if the data are the same	
		
		file.delete();
	}
	
	/*public void testLocalMap(){
		sample.setMapID(3);
		
		GUIBack guiBack1 = new GUIBack("sample.jpg", null);

		guiBack1.loadLocalMap("sample.localmap"); // load the local map
		
		assertEquals(3, guiBack1.getLocalMap().getMapID()); // check to see if the data are the same
		
		file.delete();
	}
	*/
}

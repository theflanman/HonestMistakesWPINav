package main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class GUIBackend implements Serializable {
	
	private LocalMap localMap;
	private MapNode[] path;
	private MapNode startNode;
	private MapNode endNode;
		
	/**
	 * loads a LocalMap into this class
	 * 
	 * @param fileName is name of file that stores the object data (requires an extension)
	 */
	public void loadLocalMap(String fileName) {
		
		if(! fileName.contains(".localmap")){
			System.out.println("DeveloperGUIBackend.loadMap(fileName) requires a .localmap file as input...exiting");
			System.exit(1);
		}
		
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(fileName);
			ObjectInputStream objIn = new ObjectInputStream(fileIn);
			this.localMap = (LocalMap) objIn.readObject();

			objIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void drawPathOnMap() {
		
	}
	
	public void displayStepByStep() {
		
	}
	
	public void selectNodesForNavigation() {
		
	}
	
	public void runAStar() {
		
	}

	public LocalMap getLocalMap() {
		return localMap;
	}

	public void setLocalMap(LocalMap localMap) {
		this.localMap = localMap;
	}
	
	

}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class DeveloperGUIBackend {
	
	private LocalMap localMap;
	
	public void loadMapImage() {
		
	}
	
	public void addMapNode() {
		
	}
	
	public void removeMapNode() {
		
	}
	
	public void linkNode(MapNode n1, MapNode n2) {
		
	}
	
	public void delinkNode(MapNode n1, MapNode n2) {
		
	}
	
	/**
	 * 
	 * @param fileName is name of file that stores the object data without the extension
	 */
	public void saveMap(String fileName) {
		
		if(! fileName.contains(".")){
			System.out.println("The input for DeveloperGUIBackend.saveMap(fileName) should not have an extension or period...exiting");
			System.exit(1);
		}
		
		fileName = fileName.concat(".localmap");
		
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			objOut.writeObject(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * loads a LocalMap into this class
	 * 
	 * @param fileName is name of file that stores the object data (requires an extension)
	 */
	public void loadMap(String fileName) {
		
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
	
	public void selectNode() {
		
	}

}

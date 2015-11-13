package main;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

@SuppressWarnings("serial")
public class LocalMap implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<MapNode> mapNodes;
	private String mapImage;
	private float mapScale;
	private int mapID;
	private GlobalMap globalMap;
	
	/**
	 * saves this map into a file with a .localmap extension
	 * 
	 * @param fileName is name of file that stores the object data without the extension
	 */
	public void saveMap(String fileName) {		
		if(fileName.contains(".")){
			System.out.println("The input for DeveloperGUIBackend.saveMap(fileName) should not have an extension or period...exiting");
			System.exit(1);
		}
		
		fileName = fileName.concat(".localmap");
		
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("src/localmaps/" + fileName);
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			objOut.writeObject(this);
			
			objOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addNode() {
		
	}
	
	public void removeNode(int nodeID) {
		
	}
	
	public void linkNodes(int nodeID1, int nodeID2) {
		
	}
	
	public void delinkNodes(int nodeID1, int nodeID2) {
		
	}

	public int getMapID() {
		return mapID;
	}

	public void setMapID(int mapID) {
		this.mapID = mapID;
	}
	
	public void setMapImage(String imageName){
		this.mapImage = imageName;
	}
	public String getMapImage(){
		return this.mapImage;
	}
	
	public void setMapNodes(ArrayList<MapNode> nodes){
		this.mapNodes = nodes;
	}
	
	public ArrayList<MapNode> getMapNodes(){
		return this.mapNodes;
	}
	
	

}

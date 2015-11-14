package main;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import main.util.YamlParser;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

@SuppressWarnings("serial")
public class LocalMap implements Serializable{
	
	private MapNode[] mapNodes;
	private String mapImage;
	private double mapScale; // unit is feet/pixel
	private int mapID;
	private GlobalMap globalMap;
	
	// constructor
	public LocalMap(String mapImage){
		this.mapImage = mapImage;
		
		YamlParser yamlParser = new YamlParser(new String[]{"src/data/mapScales.yml"});
		
		HashMap<String, Double> argList = yamlParser.getArgList();
		if(argList.size() > 0)
			this.mapScale = argList.get(this.mapImage); // gets the scale based on the mapImage
	}
	
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
			fileOut = new FileOutputStream(fileName);
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			objOut.writeObject(this);
			
			objOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addNode(double xPos, double yPos, double zPos) {
		MapNode node = new MapNode(xPos, yPos, zPos);
		
		globalMap.addToMapNodes(node); // add this node to the only GlobalMap
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

	public double getMapScale() {
		return mapScale;
	}

	public void setMapScale(double mapScale) {
		this.mapScale = mapScale;
	}

	public GlobalMap getGlobalMap() {
		return globalMap;
	}

	public void setGlobalMap(GlobalMap globalMap) {
		this.globalMap = globalMap;
	}
	
	

}

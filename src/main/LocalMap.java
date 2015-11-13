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
	
	/**
	 * @author Andrew Petit
	 * 
	 * @description adds a link between a pair of nodes
	 */
	public void linkNodes(int nodeID1, int nodeID2) {
		//Unless nodeID's definitely correspond to an idex of an array we need to actually check every node in the list to get the node
		for (MapNode mapNode: this.mapNodes){
			if (mapNode.nodeID == nodeID1) {
				MapNode mapNode1 = mapNode;
			} else if (mapNode.nodeID == nodeID2){
				MapNode mapNode2 = mapNode;
			}
		}
		//if the link is already established do nothing 
		if (mapNode1.getNeighbors.contains(mapNode2) && mapNode1.getNeighbors.contains(mapNode1)){
		} //if a node is a neighbor to another node, but that other node is not a neighbor to the initial node 
		else if ((mapNode1.getNeighbors.contains(mapNode2) && !mapNode2.getNeighbors.contains(mapNode1)) || (mapNode2.getNeighbors.contains(mapNode1) && !mapNode1.neighbors.contains(mapNode2)){
			//need to return some error value as map nodes should not appear in neighbors without a link
		} else { // add the link between the two nodes
			mapNode1.addNeighbor(mapNode2);
			mapNode2.addNeighbor(mapNode1);
		}
	}
	
	public void delinkNodes(int nodeID1, int nodeID2) {
		/*for (MapNode mapNode: this.mapNodes){
			if (mapNode.nodeID == nodeID1){
				MapNode mapNode1 = mapNode;
			} else if (mapNode.nodeID == nodeID2){
				MapNode mapNode2 = mapNode;
			}
		}
		mapNode1.neighbors.*/
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

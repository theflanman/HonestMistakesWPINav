package main.gui;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.AStar;
import main.Attributes;
import main.LocalMap;
import main.LocationType;
import main.MapNode;
import main.StepByStep;


@SuppressWarnings("serial")
public class GUIBack implements Serializable {
	
	private LocalMap localMap;
	private ArrayList<MapNode> path;
	private MapNode startNode;
	private ArrayList<MapNode> middleNodes;
	private MapNode endNode;
	
	
	/**
	 * Constructor: Initializes Backend fields to the default map to be loaded.
	 * TODO: Change to Campus Map when it is complete
	 */
	public GUIBack(){
		String defaultMapImage = "Complete_Campus_Map.JPG";
		this.localMap = new LocalMap(defaultMapImage, null);
		this.path = new ArrayList<MapNode>();
		this.startNode = null;
		this.middleNodes = new ArrayList<MapNode>();
		this.endNode = null;
	}
		
	/**
	 * loads a LocalMap into this class
	 * 
	 * @param fileName is name of file that stores the object data (requires an extension)
	 */
	public void loadLocalMap(String fileName){
		ArrayList<MapNode> loadedNodes = new ArrayList<MapNode>();
		ArrayList<ArrayList<Integer>> neighborNodes = new ArrayList<ArrayList<Integer>>();
		
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(fileName);
			Element doc = dom.getDocumentElement();
			
			//first pass, just setup arrays of map nodes and neighbor array
			NodeList xmlNodeList = doc.getElementsByTagName("Node");
			for (int i = 0; i < xmlNodeList.getLength(); ++i){
				ArrayList<Integer> newList = new ArrayList<Integer>();
				MapNode newMapNode = new MapNode();
				
				loadedNodes.add(newMapNode);
				neighborNodes.add(newList);
			}
			
			//second pass, store all map node data except neighbor linking
			for (int i = 0; i < xmlNodeList.getLength(); ++i){
				//get the first xml node from the root node
				Element currentNode = (Element) xmlNodeList.item(i);
				
				//extract these node variables (ID, x, y, z);
				String nodeID = currentNode.getElementsByTagName("NodeID").item(0).getTextContent();
				String xPos = currentNode.getElementsByTagName("XPos").item(0).getTextContent();
				String yPos = currentNode.getElementsByTagName("YPos").item(0).getTextContent();
				String zPos = currentNode.getElementsByTagName("ZPos").item(0).getTextContent();
				//store the nodes in the array list of nodes
				loadedNodes.get(i).setNodeID(nodeID);
				loadedNodes.get(i).setXPos(Double.parseDouble(xPos));
				loadedNodes.get(i).setYPos(Double.parseDouble(yPos));
				loadedNodes.get(i).setZPos(Double.parseDouble(zPos));
				//debug print
				
				/*
				System.out.println(loadedNodes.get(i).getNodeID());
				System.out.println(loadedNodes.get(i).getXPos());
				System.out.println(loadedNodes.get(i).getYPos());
				System.out.println(loadedNodes.get(i).getZPos());
				*/
				
				//get the neighbor values and store those node ID's in the neighbor nodes arraylist
				Element neighborCheck = ((Element)currentNode.getElementsByTagName("Neighbors").item(0));
				if(!neighborCheck.getTextContent().equals("none")){
					NodeList neighborList = neighborCheck.getElementsByTagName("Neighbor");
					//System.out.println(neighborList3.getLength());
					for(int j = 0; j < neighborList.getLength(); ++j){
						Element neighbor = (Element) neighborList.item(j);
						neighborNodes.get(i).add(Integer.parseInt(neighbor.getTextContent().trim()));
						System.out.println("    " + neighbor.getTextContent().trim());
					}
				}
				else{
					//no neighbors, nothing to add
					System.out.println("No Neighbors");
				}
				
				//extract the attribute values and store these
				Element attributes = ((Element)currentNode.getElementsByTagName("Attributes").item(0));
				String officialName = attributes.getElementsByTagName("OfficialName").item(0).getTextContent();
				String bikeable = attributes.getElementsByTagName("Bikeable").item(0).getTextContent();
				String handicapped = attributes.getElementsByTagName("Handicapped").item(0).getTextContent();
				String stairs = attributes.getElementsByTagName("Stairs").item(0).getTextContent();
				String outside = attributes.getElementsByTagName("Outside").item(0).getTextContent();
				String poi = attributes.getElementsByTagName("POI").item(0).getTextContent();
				String type = attributes.getElementsByTagName("Type").item(0).getTextContent();
				
				
				//set the attributes in the array list
				
				Attributes attr = new Attributes();
				attr.setOfficialName(officialName);
				attr.setBikeable(Boolean.parseBoolean(bikeable));
				attr.setHandicapped(Boolean.parseBoolean(handicapped));
				attr.setPOI(Boolean.parseBoolean(poi));
				attr.setStairs(Boolean.parseBoolean(stairs));
				attr.setType(LocationType.parseType(type));
				
				loadedNodes.get(i).setAttributes(attr);
				
				//debug print
				/*
				System.out.println(officialName);
				System.out.println(bikeable);
				System.out.println(handicapped);
				System.out.println(stairs);
				System.out.println(outside);
				System.out.println(poi);
				System.out.println(type);
				*/
	
			}
			
			//third pass; link nodes
			System.out.println("About to do the third pass");
			for (int i = 0; i < xmlNodeList.getLength(); i++){
				//loop through the list of neighbor associations to do assignment for each one
				for (int j = 0; j < neighborNodes.get(i).size(); j++){
					
					int neighborID = neighborNodes.get(i).get(j);
					//need to get the node associated with this ID
					for(MapNode potentialNode : loadedNodes){
						if(potentialNode.getNodeID().equals(neighborID)){
						
							MapNode currentNode = loadedNodes.get(i);
							currentNode.addNeighbor(potentialNode);
							//currentNode.getNeighbors().add(potentialNode);
						}
					}//end inner for		
				}//end middle for
			}//end outer for
			
			this.localMap = new LocalMap(fileName, loadedNodes);
			
		} catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
		
	}
	/**@author Andrew Petit
	 * 
	 * @description basically used by drawLine function in mainGui to draw a line between two nodes one at a time
	 * 
	 */
	
	public ArrayList<double[]> getCoordinates(){
		ArrayList<MapNode> mapNodes = this.path;
		ArrayList<double[]>coordinates = new ArrayList<double[]>(); 
		for(MapNode mapNode : mapNodes){
			double x = mapNode.getXPos();
			double y = mapNode.getYPos();
			double[] pairs = {x, y};
			coordinates.add(pairs);
		}
		return coordinates;
	}
	
	/**@author Andrew Petit
	 * @description just gets the distance so mainGui can display it on a label  -- will need to be updated when I understand what score to use for this value
	 * @return String - this is necessary to allow MainGui to push the distance to a label
	 */
	
	public String getDistance() {
		StepByStep getDistance = new StepByStep(this.path);
		int distance = getDistance.calculateTotalDistance();
		return "Distance in feet:" + distance;
	}
	/**
	 * @return ArrayList<String> - this is necessary to allow GUIFront to convert the strings in the array into rows of the column
	 */
	//honestly think we should role with the commented-out code, and get rid of generateStepByStep from Global -- Need someone's opinion though
	public ArrayList<String> displayStepByStep() {
		StepByStep directions = new StepByStep(this.path);
		ArrayList<String> print = directions.printDirection();
		
		return print;
	}
	
	public void selectNodesForNavigation() {
		
	}
	
	/**@author Andrew Petit
	 * 
	 * @description runs the astar algorithm on the start and end nodes selected by the user
	 */
	public ArrayList<MapNode> runAStar() {
		//initiate a new astar class with the starting node and ending node of local map 
		MapNode[] aStarMap = {this.startNode, this.endNode};
		AStar astar = new AStar(aStarMap);
		astar.runAlgorithm();
		
		return astar.reconstructPath();
	}
	//this needs to be added as a way of reseting the path to have no nodes in it...
	/**
	 * @description when GUIFront calls it, this function removes all nodes from the path of nodes 
	 */
	public void removePath() {
		this.path.clear();
	}
	
	
	/**
	 * @author Andrew Petit
	 * @description Essentially enables a user to press anywhere on the map
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	public MapNode findNearestNode(double xPos, double yPos){
		MapNode start = new MapNode(xPos, yPos, 0);
		MapNode temp = null;
		//need to initialize with an extremely large unobtainable number - or find a better solution
		double distance = 10000000000000000000000000000000000000000000000000000000000000000000000000.0;
		for (MapNode mapnode : this.localMap.getMapNodes()){ //for all nodes in localmaps' nodes -- this will be changed to global map nodes when that is finished, and then do distance formula to find the nearest node
			 if (distance > (double) Math.sqrt((Math.pow(xPos - mapnode.getXPos(), 2)) + (Math.pow(yPos - mapnode.getYPos(), 2)))){
				 distance = (double) Math.sqrt((Math.pow(xPos - mapnode.getXPos(), 2)) + (Math.pow(yPos - mapnode.getYPos(), 2)));
				 temp = mapnode;
			 }
		}
		//this will change to check to make sure the neighbor is valid
		start.addNeighbor(temp); //add the new nodes link with the closest node
		temp.addNeighbor(start); //add the new node as a neighbor to the closest node
		return start;
	}
	
	public MapNode findNearestAttributedNode(String nodeAttribute, MapNode startNode){
		MapNode temp = null; //initialize a new node
		double distance = 10000000000000000000000000000000000000000000000000000000000000000000000000.0; //need to set distance to a value that is unattainable
		for (MapNode mapnode : this.localMap.getMapNodes()){ //for all nodes in localmaps nodes -- this will be changed to global map nodes when that is finished
			if (mapnode.getAttributes().getType().toString().equals(nodeAttribute)){ //if this is true do distance formula to find the closest node that has that attribute
				 if (distance > (double) Math.sqrt((Math.pow(startNode.getXPos() - mapnode.getXPos(), 2)) + (Math.pow(startNode.getYPos() - mapnode.getYPos(), 2)))){
					 distance = (double) Math.sqrt((Math.pow(startNode.getXPos() - mapnode.getXPos(), 2)) + (Math.pow(startNode.getYPos() - mapnode.getYPos(), 2)));
					 temp = mapnode;//set temp to this node value
				 }
			}
		}
		return temp;
	}

	public LocalMap getLocalMap() {
		return localMap;
	}

	public void setLocalMap(LocalMap localMap) {
		this.localMap = localMap;
	}
	
	public void setStartNode(MapNode startNode){
		this.startNode = startNode;
	}
	public void setEndNode(MapNode endNode){
		this.endNode = endNode;
	}
	public MapNode getStartNode(){
		return this.startNode;
	}
	public MapNode getEndNode(){
		return this.endNode;
	}
	
	public void addToMiddleNodes(MapNode node){
		this.middleNodes.add(node);
	}

	public ArrayList<MapNode> getPath(){
		return this.path;
	}
	public void setPath(ArrayList<MapNode> path){
		this.path = path;
	}
}

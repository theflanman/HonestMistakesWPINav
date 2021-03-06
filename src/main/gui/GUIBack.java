package main.gui;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.memetix.mst.language.Language;

import main.AStar;
import main.Attributes;
import main.LocalMap;
import main.Types;
import main.MapNode;
import main.StepByStep;
import main.util.Constants;
import main.util.GeneralUtil;

@SuppressWarnings("serial")
public class GUIBack implements Serializable {
	private LocalMap localMap;
	private ArrayList<MapNode> path;
	public boolean splashFlag = false;//becomes true when done loading maps

	/**
	 * Constructor: Initializes Backend fields to the default map to be loaded.
	 */
	public GUIBack(){
		String defaultMapImage = "CCM.png";
		this.localMap = new LocalMap(defaultMapImage, null);
		this.path = new ArrayList<MapNode>();
		}

	/**
	 * Takes an array of filenames of local maps to be loaded
	 * The following processing is then done to make the maps work:
	 *   Each map its nodes populate with no links.  No linking is done
	 *   until every map has had its nodes loaded
	 * Linking is then done for every map and can be done across maps.
	 * @param fileName
	 */
	public ArrayList<LocalMap> loadLocalMaps(String fileNames[]){

		//iterate through each file name
		ArrayList<ArrayList<ArrayList<String>>> allNeighborList = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<LocalMap> localMapList = new ArrayList<LocalMap>();

		for(String fileName: fileNames){
			//find exclusively the file name
			String fileParts[] = fileName.split("/");
			String mapAppend = fileParts[fileParts.length-1];
			mapAppend = GeneralUtil.removeExtension(mapAppend) + "_";
			//setup an array list of nodes for the local map and an array list of strings for linking
			
			ArrayList<MapNode> loadedNodes = new ArrayList<MapNode>();
			ArrayList<ArrayList<String>> neighborNodes = new ArrayList<ArrayList<String>>();
			Document dom;

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				String xmlFileName = GeneralUtil.removeExtension(fileName) + ".localmap";
				dom = db.parse(Constants.LOCAL_MAP_PATH + "/" + xmlFileName);
				Element doc = dom.getDocumentElement();

				//first pass, just setup arrays of map nodes and neighbor array
				NodeList xmlNodeList = doc.getElementsByTagName("Node");
				for (int i = 0; i < xmlNodeList.getLength(); ++i){
					ArrayList<String> newList = new ArrayList<String>();
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
					String xFeet = currentNode.getElementsByTagName("XFeet").item(0).getTextContent();
					String yFeet = currentNode.getElementsByTagName("YFeet").item(0).getTextContent();
					String zFeet = currentNode.getElementsByTagName("ZFeet").item(0).getTextContent();
					//store the nodes in the array list of nodes
					loadedNodes.get(i).setNodeID(nodeID);
					loadedNodes.get(i).setXPos(Double.parseDouble(xPos));
					loadedNodes.get(i).setYPos(Double.parseDouble(yPos));
					loadedNodes.get(i).setXFeet(Double.parseDouble(xFeet));
					loadedNodes.get(i).setYFeet(Double.parseDouble(yFeet));
					loadedNodes.get(i).setZFeet(Double.parseDouble(zFeet));

					//get the neighbor values and store those node ID's in the neighbor nodes arraylist
					Element neighborCheck = ((Element)currentNode.getElementsByTagName("Neighbors").item(0));
					if(!neighborCheck.getTextContent().equals("none")){
						NodeList neighborList = neighborCheck.getElementsByTagName("Neighbor");
						for(int j = 0; j < neighborList.getLength(); ++j){
							Element neighbor = (Element) neighborList.item(j);
							neighborNodes.get(i).add(neighbor.getTextContent().trim());
						}
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
					//ArrayList<String> aliases = ((ArrayList<String>)currentNode.getElementsByTagName("Aliases").item(0)); 
					//String alias = aliases.getElementsByTagName("Alias").item(0).getTextContent();
					//System.out.println(aliases);

					//set the attributes in the array list
					Attributes attr = new Attributes();
					attr.setOfficialName(officialName);
					attr.setBikeable(Boolean.parseBoolean(bikeable));
					attr.setHandicapped(Boolean.parseBoolean(handicapped));
					attr.setStairs(Boolean.parseBoolean(stairs));
					attr.setOutside(Boolean.parseBoolean(outside));
					attr.setPOI(Boolean.parseBoolean(poi));
					attr.setType(Types.parseType(type));
					//String [] str = alias.split(",");
					
					//ArrayList<String> strs = new ArrayList<String>(Arrays.asList(str));
					//attr.setAliases(strs);
					

					loadedNodes.get(i).setAttributes(attr);

				}//end second for
			} catch (ParserConfigurationException pce) {
				System.out.println(pce.getMessage());
			} catch (SAXException se) {
				System.out.println(se.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}

			//store all this data so far, nodes still need to be linked.
			LocalMap tempMap = new LocalMap(fileName, loadedNodes);
			for(MapNode anode: loadedNodes){
				anode.setLocalMap(tempMap);
			}

			localMapList.add(tempMap);
			allNeighborList.add(neighborNodes);

		}//end file name for

		//Stop here for now to get some info to ensure the loading is functioning correctly.
		//drop all of the map nodes into a single list.
		ArrayList<MapNode> completeNodeList = new ArrayList<MapNode>();
		for(LocalMap lmap : localMapList){ //for each map
			for(MapNode node : lmap.getMapNodes()){ //for each node on this map
				completeNodeList.add(node);
			}
		}

		//time to loop through each local map and link its nodes to itself and other local maps
		for(int i = 0; i< localMapList.size(); i++){

			//this is the local map that we're currently linking
			LocalMap currentLocalMap = localMapList.get(i);
			//this is the number of nodes we have to look a for neighbors
			int numNodes = currentLocalMap.getMapNodes().size();
			ArrayList<MapNode> currentMapsNodes = currentLocalMap.getMapNodes();
			//this list of neighbor nodes contains a list of all the neighbor relations for this map.
			ArrayList<ArrayList<String>> neighborNodes = allNeighborList.get(i);

			//go through each node on this local map
			for(int j = 0; j < numNodes; j++){
				//this is the list of nodes that need to be linked to this node.
				ArrayList<String> thisNodesNeighbors = neighborNodes.get(j);
				int numNeighbors = thisNodesNeighbors.size();
				
				//for each node that needs to be linked
				for(int k = 0; k < numNeighbors; k++){
					
					//get the id of the node to be linked
					String nodeIDToBeLinked = thisNodesNeighbors.get(k);

					//find that node in the collection of nodes
					for(MapNode potentialNode : completeNodeList){
						
						//if there is a match, link the nodes.
						if(potentialNode.getNodeID().equals(nodeIDToBeLinked)){
							MapNode currentNode = currentMapsNodes.get(j);
							currentNode.addNeighbor(potentialNode);
						}
					}
				}
			}

		}

	    splashFlag = true;
		return localMapList; 
	}
	
	/**@author Andrew Petit
	 * 
	 * @description basically used by drawLine function in mainGui to draw a line between two nodes one at a time
	 * 
	 */
	public ArrayList<double[]> getCoordinates(ArrayList<MapNode> mapNodes){
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
	public int getDistance(ArrayList<MapNode> mapNodes, boolean isLastWaypoint) {
		StepByStep getDistance = new StepByStep(mapNodes, isLastWaypoint);
		int distance = getDistance.calculateTotalDistance();
		return distance;
	}
	
	/**
	 * @return ArrayList<String> - this is necessary to allow GUIFront to convert the strings in the array into rows of the column
	 */
	public ArrayList<String> displayStepByStep(ArrayList<MapNode> mapNodes, boolean isLastWaypoint, Language language) {
		StepByStep directions = new StepByStep(mapNodes, isLastWaypoint);
		ArrayList<String> print = directions.printDirection(language);
		return print;
	}

	/**@author Andrew Petit
	 * 
	 * @description runs the astar algorithm on the start and end nodes selected by the user
	 */
	public ArrayList<MapNode> runAStar(MapNode start, MapNode end) {
		//include method from step by step to clear nodes 
		//initiate a new astar class with the starting node and ending node of local map 
		MapNode[] aStarMap = {start, end};
		AStar astar = new AStar(aStarMap);
		astar.runAlgorithm();
		
		StepByStep steps = new StepByStep(astar.reconstructPath(), false);
		return steps.cleanUpPath();
	}

	/**
	 * @description when GUIFront calls it, this function removes all nodes from the path of nodes 
	 */
	public void removePath(ArrayList<MapNode> mapNodes) {
		mapNodes.clear();
	}
	
	/**
	 * @author Dominic B, Nick G, Andrew P
	 * @param start
	 * @param end
	 * @param globalmap
	 * @return
	 */
	public ArrayList<ArrayList<MapNode>> getMeRoutes(MapNode start, MapNode end){
		ArrayList<ArrayList<MapNode>> routes = new ArrayList<ArrayList<MapNode>>();
		ArrayList<MapNode> route = new ArrayList<MapNode>();
		ArrayList<MapNode> globalNodes = this.runAStar(start, end);

		for (int i = 0; i < globalNodes.size(); i++) {
			//if this is the first time through, no nodes have been added immediately add this to a new route
			if (i == 0) {
				route.add(globalNodes.get(i));
				StepByStep temp = new StepByStep(route, false);
				route = temp.cleanUpPath();
				globalNodes.get(i).getLocalMap().setStartNode(globalNodes.get(i));
			}
			
			//this isn't the first node and it isn't the last node
			else if (i != globalNodes.size()-1){
				//if this is on the same local map as the last one...
				if(globalNodes.get(i).getLocalMap().equals(globalNodes.get(i-1).getLocalMap())){
					route.add(globalNodes.get(i));
					StepByStep temp = new StepByStep(route, false);
					route = temp.cleanUpPath();
				}
				
				//time to move onto a new route, add this one to routes and clear it
				else{
					globalNodes.get(i-1).getLocalMap().setEndNode(globalNodes.get(i-1));
					routes.add(route);
					route = new ArrayList<MapNode>();
					route.add(globalNodes.get(i));
					StepByStep temp = new StepByStep(route, false);
					route = temp.cleanUpPath();
				}
			}//end else if
			else {
				globalNodes.get(i-1).getLocalMap().setEndNode(globalNodes.get(i-1));
				route.add(globalNodes.get(i));
				StepByStep temp = new StepByStep(route, false);
				route = temp.cleanUpPath();
				routes.add(route);
			}

		}

		return routes;
	}

	/**
	 * @author Andrew Petit
	 * @description Essentially enables a user to press anywhere on the map
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	public MapNode findNearestNode(double xPos, double yPos, LocalMap localmap){
		MapNode start = new MapNode(xPos, yPos, localmap);
		

		start.setNodeID("temp" + Double.toString(xPos));
		MapNode temp = null;
		//need to initialize with an extremely large unobtainable number - or find a better solution
		double distance = 10000000000000000000000000000000000000000000000000000000000000000000000000.0;
		
		for (MapNode mapnode : this.localMap.getMapNodes()){ //for all nodes in localmaps' nodes -- this will be changed to global map nodes when that is finished, and then do distance formula to find the nearest node
			if (distance > (double) Math.sqrt((Math.pow(xPos - mapnode.getXPos(), 2)) + (Math.pow(yPos - mapnode.getYPos(), 2)))){
				distance = (double) Math.sqrt((Math.pow(xPos - mapnode.getXPos(), 2)) + (Math.pow(yPos - mapnode.getYPos(), 2)));
				temp = mapnode;
			}
		}
		
		if (distance == 0)
			return temp;
		else {
			//this will change to check to make sure the neighbor is valid
			start.getNeighbors().clear();
			this.localMap.getMapNodes().add(start);
			start.addNeighbor(temp); //add the new nodes link with the closest node
			start.setXFeet(temp.getXFeet() + ((temp.getXPos() - start.getXPos()) * localMap.getMapScale()));
			start.setYFeet(temp.getYFeet() + ((temp.getYPos() - start.getYPos()) * localMap.getMapScale()));
			// start.runTransform();
			temp.addNeighbor(start); //add the new node as a neighbor to the closest node
			return start;
		}
	}

	public MapNode findNearestAttributedNode(String nodeAttribute, MapNode startNode){
		MapNode temp = null; //initialize a new node
		double distance = 10000000000000000000000000000000000000000000000000000000000000000000000000.0; //need to set distance to a value that is unattainable
		for (MapNode mapnode : GUIFront.getGlobalMap().getMapNodes()){ //for all nodes in localmaps nodes -- this will be changed to global map nodes when that is finished
			if (mapnode.getAttributes().getType().toString().equals(nodeAttribute)){ //if this is true do distance formula to find the closest node that has that attribute
				if (distance > (double) Math.sqrt((Math.pow(startNode.getXFeet() - mapnode.getXFeet(), 2)) + (Math.pow(startNode.getYFeet() - mapnode.getYFeet(), 2)))){
					distance = (double) Math.sqrt((Math.pow(startNode.getXFeet() - mapnode.getXFeet(), 2)) + (Math.pow(startNode.getYFeet() - mapnode.getYFeet(), 2)));
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
	public ArrayList<MapNode> getPath(){
		return this.path;
	}
	public void setPath(ArrayList<MapNode> path){
		this.path = path;
	}
}


package main.gui;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.AStar;
import main.Attributes;
import main.LocalMap;
import main.Types;
import main.MapNode;
import main.GlobalMap;
import main.StepByStep;
import main.gui.GUIFront.TweenPanel;
import main.util.Constants;
import main.util.SaveUtil;


@SuppressWarnings("serial")
public class GUIBack implements Serializable {

	private LocalMap localMap;
	private ArrayList<MapNode> path;

	/**
	 * Constructor: Initializes Backend fields to the default map to be loaded.
	 * TODO: Change to Campus Map when it is complete
	 */
	public GUIBack(){
		String defaultMapImage = "CCM.jpg";
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
		System.out.println("Starting load");
		ArrayList<ArrayList<ArrayList<String>>> allNeighborList = new ArrayList<ArrayList<ArrayList<String>>>();
		ArrayList<LocalMap> localMapList = new ArrayList<LocalMap>();


		for(String fileName: fileNames){
			//find exclusively the file name
			String fileParts[] = fileName.split("/");
			String mapAppend = fileParts[fileParts.length-1];
			mapAppend = SaveUtil.removeExtension(mapAppend) + "_";
			//setup an array list of nodes for the local map and an array list of strings for linking
			ArrayList<MapNode> loadedNodes = new ArrayList<MapNode>();
			ArrayList<ArrayList<String>> neighborNodes = new ArrayList<ArrayList<String>>();
			Document dom;

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				String xmlFileName = SaveUtil.removeExtension(fileName) + ".localmap";
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
					//debug print

					//get the neighbor values and store those node ID's in the neighbor nodes arraylist
					Element neighborCheck = ((Element)currentNode.getElementsByTagName("Neighbors").item(0));
					if(!neighborCheck.getTextContent().equals("none")){
						NodeList neighborList = neighborCheck.getElementsByTagName("Neighbor");
						for(int j = 0; j < neighborList.getLength(); ++j){
							Element neighbor = (Element) neighborList.item(j);
							neighborNodes.get(i).add(neighbor.getTextContent().trim());
						}
					}
					else{
						//no neighbors, nothing to add
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
					attr.setType(Types.parseType(type));



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
		//this will probably feel like black magic (BECAUSE IT IS)
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

	public int getDistance(ArrayList<ArrayList<MapNode>> mapNodes, boolean wayPoint) {
		//StepByStep getDistance = new StepByStep(mapNodes, wayPoint);
		//int distance = getDistance.calculateTotalDistance();
		int distance = 0;
		return distance;
	}
	/**
	 * @return ArrayList<String> - this is necessary to allow GUIFront to convert the strings in the array into rows of the column
	 */
	//honestly think we should role with the commented-out code, and get rid of generateStepByStep from Global -- Need someone's opinion though
	public ArrayList<String> displayStepByStep(ArrayList<ArrayList<MapNode>> mapNodes, boolean wayPoint) {
		//StepByStep directions = new StepByStep(mapNodes, wayPoint);
		//ArrayList<String> print = directions.printDirection();
		ArrayList<String> print = new ArrayList<String>();
		return print;
	}

	public void selectNodesForNavigation() {

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

		return astar.reconstructPath();
	}
	//this needs to be added as a way of reseting the path to have no nodes in it...
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

	public ArrayList<ArrayList<MapNode>> getMeRoutes(MapNode start, MapNode end, GlobalMap globalmap){
		ArrayList<ArrayList<MapNode>> routes = new ArrayList<ArrayList<MapNode>>();
		ArrayList<MapNode> route = new ArrayList<MapNode>();
		ArrayList<MapNode> globalNodes = this.runAStar(start, end);
		System.out.println(globalNodes.size());
		globalmap.addToMapNodes(start);
		globalmap.addToMapNodes(end);
		for (int i = 0; i < globalNodes.size(); i++) {
			//if this is the first time through, no nodes have been added
			//immediately add this to a new route
			if (i == 0) {
				route.add(globalNodes.get(i));
				globalNodes.get(i).getLocalMap().setStart(globalNodes.get(i));
			}
			//this isnt the first node and it isnt the last node
			else if (i != globalNodes.size()-1){
				//if this is on the same local map as the last one...
				if(globalNodes.get(i).getLocalMap().equals(globalNodes.get(i-1).getLocalMap())){
					route.add(globalNodes.get(i));
				}
				//time to move onto a new route, add this one to routes and clear it
				else{
					globalNodes.get(i-1).getLocalMap().setEnd(globalNodes.get(i-1));
					routes.add(route);
					route = new ArrayList<MapNode>();
					route.add(globalNodes.get(i));
				}
			}//end else if
			else {
				globalNodes.get(i-1).getLocalMap().setEnd(globalNodes.get(i-1));
				route.add(globalNodes.get(i));
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
		start.setXFeet(start.getLocalMap().getMapScale()*start.getXPos());
		start.setYFeet(start.getLocalMap().getMapScale()*start.getYPos());
		start.runTransform();
		System.out.println(start.getXFeet());
		System.out.println(start.getYFeet());
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
		if (distance == 0){
			return temp;
		} else {
			//this will change to check to make sure the neighbor is valid
			//MapNode start = temp;
			start.getNeighbors().clear();
			this.localMap.getMapNodes().add(start);
			this.localMap.getMapNodes().add(temp);
			this.localMap.linkNodes(start.getNodeID(), temp.getNodeID());
			start.addNeighbor(temp); //add the new nodes link with the closest node
			temp.addNeighbor(start); //add the new node as a neighbor to the closest node
			return start;
		}
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
	public ArrayList<MapNode> getPath(){
		return this.path;
	}
	public void setPath(ArrayList<MapNode> path){
		this.path = path;
	}
	
	public LocalMap saveMap(LocalMap localMap) {
		//get the filename of the map image, remove the image extension,
		//and slap on a .localmap
		System.out.println("Preparing to save...");
		String fileName = localMap.getMapImageName();
		System.out.println(localMap.getMapScale());
		String mapImageName = fileName;
		fileName = SaveUtil.removeExtension(fileName);
		//String mapNameNoExtension = fileName;
		String mapAppend = fileName + "_";
		fileName = fileName.concat(".localmap");
		fileName = Constants.LOCAL_MAP_PATH + "/" + fileName;
		int i = 1;
		for(MapNode node : this.localMap.getMapNodes()){
			node.setNodeID(mapAppend + Integer.toString(i++));
		}
		Document dom;
	    Element e = null;

	    // instance of a DocumentBuilderFactory
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    try {
	        // use factory to get an instance of document builder
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        // create instance of DOM
	        dom = db.newDocument();

	        // create the root element
	        Element rootEle = dom.createElement("MapNodes");
	        
	        Element imageName = dom.createElement("ImageName");
	        imageName.appendChild(dom.createTextNode(mapImageName));
	        rootEle.appendChild(imageName);
	        
	        for(MapNode node : this.localMap.getMapNodes()){
	        	node.setXFeet(node.getLocalMap().getMapScale()*node.getXPos());
	        	System.out.println(node.getLocalMap().getMapScale());
	        	System.out.println("xFeet: " + node.getXFeet());
	        	node.setYFeet(node.getLocalMap().getMapScale()*node.getYPos());
	        	System.out.println("yFeet: " + node.getYFeet());
	        	e = dom.createElement("Node");
	        	//node id
	        	System.out.println("Saving ID");
	        	Element id = dom.createElement("NodeID");
        		id.appendChild(dom.createTextNode(node.getNodeID()));
	        	
	        	//x y and z coords
	        	System.out.println("Saving xPos");
	        	Element xPos = dom.createElement("XPos");
	        	xPos.appendChild(dom.createTextNode(Double.toString(node.getXPos())));
	        	
	        	System.out.println("Saving yPos");
	        	Element yPos = dom.createElement("YPos");	
	        	yPos.appendChild(dom.createTextNode(Double.toString(node.getYPos())));
	        	
	        	System.out.println("Saving xFeet");
	        	Element xFeet = dom.createElement("XFeet");
	        	xFeet.appendChild(dom.createTextNode(Double.toString(node.getXFeet())));
	        	
	        	System.out.println("Saving Feet");
	        	Element yFeet = dom.createElement("YFeet");	
	        	yFeet.appendChild(dom.createTextNode(Double.toString(node.getYFeet())));
	        	
	        	System.out.println("Saving zPos");
	        	Element zFeet = dom.createElement("ZFeet");
	        	zFeet.appendChild(dom.createTextNode(Double.toString(node.getZFeet())));
	        	
	        	System.out.println("Saving Type");
	        	Element type = dom.createElement("Type");
	        	type.appendChild(dom.createTextNode(node.getAttributes().getType().toString()));
	        	
	        	System.out.println("Saving Neighbors");
	        	Element neighbors = dom.createElement("Neighbors");
	        	//if there are no neighbors, populate the xml field with none
	        	if(node.getNeighbors().size() == 0){
	        		neighbors.appendChild(dom.createTextNode("none"));
	        	}
	        	else{
		        	for(MapNode n: node.getNeighbors()){
		        		Element ngbr = dom.createElement("Neighbor");
		        		ngbr.appendChild(dom.createTextNode(n.getNodeID()));
		        		neighbors.appendChild(ngbr);
		        	}
	        	}
	        	//if there are any cross map neighbors, make sure these are saved
	        	if(!(node.getCrossMapNeighbors().size() == 0)){
	        		for(String s: node.getCrossMapNeighbors()){
	        			Element crossNgbr = dom.createElement("Neighbor");
	        			crossNgbr.appendChild(dom.createTextNode(s));
	        			neighbors.appendChild(crossNgbr);
	        		}
	        	}
	        	
	        	Element attributes = dom.createElement("Attributes");
	        	
	        	
	        	System.out.println("Saving Official Name");
	        	Element officialName = dom.createElement("OfficialName");
	        	officialName.appendChild(dom.createTextNode(node.getAttributes().getOfficialName()));
	        	if(node.getAttributes().isBikeable());
	        	System.out.println("Saving bikeable");
	        	Element isBikeable = dom.createElement("Bikeable");
	        	isBikeable.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isBikeable())));
	        	System.out.println("Saving handicapped");
	        	Element isHandicapped = dom.createElement("Handicapped");
	        	isHandicapped.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isHandicapped())));
	        	System.out.println("Saving stairs");
	        	Element isStairs = dom.createElement("Stairs");
	        	isStairs.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isStairs())));
	        	System.out.println("Saving outside");
	        	Element isOutside = dom.createElement("Outside");
	        	isOutside.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isOutside())));
	        	System.out.println("Saving POI");
	        	Element isPOI = dom.createElement("POI");
	        	isPOI.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isPOI())));

	        	attributes.appendChild(officialName);
	        	attributes.appendChild(isBikeable);
	        	attributes.appendChild(isHandicapped);
	        	attributes.appendChild(isStairs);
	        	attributes.appendChild(isOutside);
	        	attributes.appendChild(isPOI);
	        	attributes.appendChild(type);
	        	
	        	System.out.println("Saving Aliases");
	        	Element aliases = dom.createElement("Aliases");
	        	//if there are no neighbors, populate the xml field with none
	        	if(node.getAttributes().getAliases().size() == 0){
	        		aliases.appendChild(dom.createTextNode("none"));
	        	}
	        	else{
		        	for(String a: node.getAttributes().getAliases()){
		        		Element alias = dom.createElement("Alias");
		        		alias.appendChild(dom.createTextNode(a));
		        		aliases.appendChild(alias);
		        	}
	        	}
	        	
	        	attributes.appendChild(aliases);
	        	
	        	/*
	        	Element fScore = dom.createElement("FScore");
	        	fScore.appendChild(dom.createElement(Double.toString(node.getFScore())));
	        	
	        	Element gScore = dom.createElement("GScore");
	        	gScore.appendChild(dom.createElement(Double.toString(node.getGScore())));
	        	
	        	Element hScore = dom.createElement("HScore");
	        	hScore.appendChild(dom.createElement(Double.toString(node.getHScore())));
	        	*/

	        	
	        	e.appendChild(id);
	        	e.appendChild(xPos);
	        	e.appendChild(yPos);
	        	e.appendChild(xFeet);
	        	e.appendChild(yFeet);
	        	e.appendChild(zFeet);
	        	e.appendChild(neighbors);
	        	e.appendChild(attributes);
	        	
	        	/*
	        	e.appendChild(officialName);
	        	e.appendChild(isBikeable);
	        	e.appendChild(isHandicapped);
	        	e.appendChild(isStairs);
	        	e.appendChild(isOutside);
	        	e.appendChild(isPOI);
	        	*/
	        	
	        	
	        	rootEle.appendChild(e);
	        	
	        	System.out.println(rootEle.getChildNodes().getLength());
	        }
	        
	        dom.appendChild(rootEle);
	        try {
	        	
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            //tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            //tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, fileName);
	            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	            System.out.println("Done transformation setup");
	            // send DOM to file
	            tr.transform(new DOMSource(dom), 
	                                 new StreamResult(new FileOutputStream(fileName)));

	        } catch (TransformerException te) {
	        	System.out.println("Transformer Exception");
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
	        	System.out.println("IOException");
	            System.out.println(ioe.getMessage());
	        }
	        
	       
	    } catch (ParserConfigurationException pce) {
	        System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
	    }
		
	    System.out.println("Done saving...");
		return localMap;
	}
}


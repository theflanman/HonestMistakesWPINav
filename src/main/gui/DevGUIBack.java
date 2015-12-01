package main.gui;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import main.Attributes;
import main.LocalMap;
import main.LocationType;
import main.MapNode;
import main.util.Constants;
import main.util.SaveUtil;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DevGUIBack implements Serializable  {

	private LocalMap localMap;
	
	// constructor
	public DevGUIBack(LocalMap localMap){
		this.localMap = localMap;
	}
	
	public void loadMapImage() {
		
	}
	
	public void addMapNode() {
		// TODO call LocalMap.addNode(...)
	}
	
	public void removeMapNode() {
		
	}
	
	public void linkNode(MapNode n1, MapNode n2) {
		
	}
	
	public void delinkNode(MapNode n1, MapNode n2) {
		
	}
	
	/**
	 * 
	 * @param fileName is name of file that stores the object data (requires extension)
	 */
	/*
	public void saveMap() {
		String fileName = this.localMap.getMapImageName();
				
		fileName = SaveUtil.removeExtension(fileName);
		//this.localMap.setMapImageName(fileName);
		fileName = fileName.concat(".localmap");
		
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(Constants.LOCAL_MAP_PATH + "/" + fileName);
			ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
			objOut.writeObject(this.localMap);
			
			objOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	
	public void saveMap() {
		//get the filename of the map image, remove the image extension,
		//and slap on a .localmap
		System.out.println("Preparing to save...");
		String fileName = this.localMap.getMapImageName();
		String mapImageName = fileName;
		fileName = SaveUtil.removeExtension(fileName);
		fileName = fileName.concat(".localmap");
		fileName = Constants.LOCAL_MAP_PATH + "/" + fileName;
		int i = 1;
		for(MapNode node : this.localMap.getMapNodes()){
			node.setNodeID(i++);
		}
		
		
		/**
		 * XML Saving based on the code from Costis Aivalis
		 * on SE
		 * 
		 * http://stackoverflow.com/questions/7373567/java-how-to-read-and-write-xml-files
		 */
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
	        	
	        	e = dom.createElement("Node");
	        	//node id
	        	System.out.println("Saving ID");
	        	Element id = dom.createElement("NodeID");
        		id.appendChild(dom.createTextNode(Integer.toString(node.getNodeID())));
	        	
	        	//x y and z coords
	        	System.out.println("Saving xPos");
	        	Element xPos = dom.createElement("XPos");
	        	xPos.appendChild(dom.createTextNode(Double.toString(node.getXPos())));
	        	
	        	System.out.println("Saving yPos");
	        	Element yPos = dom.createElement("YPos");	
	        	yPos.appendChild(dom.createTextNode(Double.toString(node.getYPos())));
	        	
	        	System.out.println("Saving zPos");
	        	Element zPos = dom.createElement("ZPos");
	        	zPos.appendChild(dom.createTextNode(Double.toString(node.getZPos())));
	        	
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
		        		ngbr.appendChild(dom.createTextNode(Integer.toString(n.getNodeID())));
		        		neighbors.appendChild(ngbr);
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
	        	e.appendChild(zPos);
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
		
	}
	/**
	 * loads a LocalMap into this class
	 * 
	 * @param fileName is name of file that stores the object data (requires an extension)
	 */
	/*
	public void loadMap(String fileName) {
		
		if(! fileName.contains(".localmap")){
			System.out.println("DevGUIBack.loadMap(fileName) requires a .localmap file as input...exiting");
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
	*/
	
	public void loadMap(String fileName){
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
				loadedNodes.get(i).setNodeID(Integer.parseInt(nodeID));
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
						if(potentialNode.getNodeID() == neighborID){
						
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
	
	
	public void selectNode() {
	
	}
	
	

	public LocalMap getLocalMap() {
		return localMap;
	}

	public void setLocalMap(LocalMap localMap) {
		this.localMap = localMap;
	}

}

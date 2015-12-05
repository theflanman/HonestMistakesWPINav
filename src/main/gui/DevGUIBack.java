package main.gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import main.Attributes;
import main.LocalMap;
import main.Types;
import main.MapNode;
import main.util.Constants;
import main.util.SaveUtil;

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
import org.w3c.dom.Element;

@SuppressWarnings("serial")
public class DevGUIBack implements Serializable  {

	private LocalMap localMap;
	
	// constructor
	public DevGUIBack(LocalMap localMap){
		this.localMap = localMap;
	}
			
	public void saveMap() {
		//get the filename of the map image, remove the image extension, and slap on a .localmap
		String fileName = this.localMap.getMapImageName();
		String mapImageName = fileName;
		fileName = SaveUtil.removeExtension(fileName);
		String mapAppend = fileName + "_";
		fileName = fileName.concat(".localmap");
		fileName = Constants.LOCAL_MAP_PATH + "/" + fileName;
		int i = 1;
		for(MapNode node : this.localMap.getMapNodes()){
			node.setNodeID(mapAppend + Integer.toString(i++));
		}
		
		/**
		 * XML Saving based on the code from Costis Aivalis
		 * on SE
		 * 
		 * http://stackoverflow.com/questions/7373567/java-how-to-read-and-write-xml-files
		 */
		Document dom;
	    Element e = null;
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); // instance of a DocumentBuilderFactory
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
	        	node.setYFeet(node.getLocalMap().getMapScale()*node.getYPos());
	        	e = dom.createElement("Node");
	        	//node id
	        	Element id = dom.createElement("NodeID");
        		id.appendChild(dom.createTextNode(node.getNodeID()));
	        	
	        	//x y and z coords
	        	Element xPos = dom.createElement("XPos");
	        	xPos.appendChild(dom.createTextNode(Double.toString(node.getXPos())));

	        	Element yPos = dom.createElement("YPos");	
	        	yPos.appendChild(dom.createTextNode(Double.toString(node.getYPos())));

	        	Element xFeet = dom.createElement("XFeet");
	        	xFeet.appendChild(dom.createTextNode(Double.toString(node.getXFeet())));

	        	Element yFeet = dom.createElement("YFeet");	
	        	yFeet.appendChild(dom.createTextNode(Double.toString(node.getYFeet())));

	        	Element zFeet = dom.createElement("ZFeet");
	        	zFeet.appendChild(dom.createTextNode(Double.toString(node.getZFeet())));

	        	Element type = dom.createElement("Type");
	        	type.appendChild(dom.createTextNode(node.getAttributes().getType().toString()));

	        	Element neighbors = dom.createElement("Neighbors");
	        	
	        	//if there are no neighbors, populate the xml field with none
	        	if(node.getNeighbors().size() == 0)
	        		neighbors.appendChild(dom.createTextNode("none"));
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

	        	Element officialName = dom.createElement("OfficialName");
	        	officialName.appendChild(dom.createTextNode(node.getAttributes().getOfficialName()));
	        	
	        	Element isBikeable = dom.createElement("Bikeable");
	        	isBikeable.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isBikeable())));
	        	
	        	Element isHandicapped = dom.createElement("Handicapped");
	        	isHandicapped.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isHandicapped())));

	        	Element isStairs = dom.createElement("Stairs");
	        	isStairs.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isStairs())));
	        	
	        	Element isOutside = dom.createElement("Outside");
	        	isOutside.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isOutside())));
	        	
	        	Element isPOI = dom.createElement("POI");
	        	isPOI.appendChild(dom.createTextNode(Boolean.toString(node.getAttributes().isPOI())));

	        	attributes.appendChild(officialName);
	        	attributes.appendChild(isBikeable);
	        	attributes.appendChild(isHandicapped);
	        	attributes.appendChild(isStairs);
	        	attributes.appendChild(isOutside);
	        	attributes.appendChild(isPOI);
	        	attributes.appendChild(type);
	        	
	        	Element aliases = dom.createElement("Aliases");
	        	
	        	// if there are no neighbors, populate the .xml field with none
	        	if(node.getAttributes().getAliases().size() == 0)
	        		aliases.appendChild(dom.createTextNode("none"));
	        	else{
		        	for(String a: node.getAttributes().getAliases()){
		        		Element alias = dom.createElement("Alias");
		        		alias.appendChild(dom.createTextNode(a));
		        		aliases.appendChild(alias);
		        	}
	        	}
	        	
	        	attributes.appendChild(aliases);
	        	
	        	e.appendChild(id);
	        	e.appendChild(xPos);
	        	e.appendChild(yPos);
	        	e.appendChild(xFeet);
	        	e.appendChild(yFeet);
	        	e.appendChild(zFeet);
	        	e.appendChild(neighbors);
	        	e.appendChild(attributes);
	        	
	        	rootEle.appendChild(e);
	        }
	        
	        dom.appendChild(rootEle);
	        try {
	            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	            
	            transformer.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(fileName))); // send DOM to file
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
	
	public void loadMap(String fileName){
		String fileParts[] = fileName.split("/");

		String mapAppend = fileParts[fileParts.length-1];
		String mapNameNoExtension = SaveUtil.removeExtension(mapAppend);
		mapAppend = SaveUtil.removeExtension(mapAppend) + "_";
		ArrayList<MapNode> loadedNodes = new ArrayList<MapNode>();
		ArrayList<ArrayList<String>> neighborNodes = new ArrayList<ArrayList<String>>();
		Document dom;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(fileName);
			Element doc = dom.getDocumentElement();
			String mapImageName = doc.getElementsByTagName("ImageName").item(0).getTextContent();
			
			// first pass, just setup arrays of map nodes and neighbor array
			NodeList xmlNodeList = doc.getElementsByTagName("Node");
			for (int i = 0; i < xmlNodeList.getLength(); ++i){
				ArrayList<String> newList = new ArrayList<String>();
				MapNode newMapNode = new MapNode();
				
				loadedNodes.add(newMapNode);
				neighborNodes.add(newList);
			}
			
			// second pass, store all map node data except neighbor linking
			for (int i = 0; i < xmlNodeList.getLength(); ++i){
				// get the first xml node from the root node
				Element currentNode = (Element) xmlNodeList.item(i);
				
				// extract these node variables (ID, x, y, z);
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
								
				Element neighborCheck = ((Element)currentNode.getElementsByTagName("Neighbors").item(0));
				ArrayList<String> crossMapNeighbors = new ArrayList<String>();
				if(!neighborCheck.getTextContent().equals("none")){
					NodeList neighborList = neighborCheck.getElementsByTagName("Neighbor");
					
					//for each element in this nodes neighbors
					for(int j = 0; j < neighborList.getLength(); ++j){
						crossMapNeighbors = new ArrayList<String>();
						Element neighbor = (Element) neighborList.item(j);
						String neighborID = neighbor.getTextContent().trim();
						if(!(neighborID.split("_")[0]).equals(mapNameNoExtension)){
							crossMapNeighbors.add(neighborID);
						}
						neighborNodes.get(i).add(neighborID);
						System.out.println("    " + neighbor.getTextContent().trim());
					}
					loadedNodes.get(i).setCrossMapNeighbors(crossMapNeighbors);
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
				attr.setOutside(Boolean.parseBoolean(outside));
				attr.setType(Types.parseType(type));
				
				loadedNodes.get(i).setAttributes(attr);
			}
			
			//third pass; link nodes
			for (int i = 0; i < xmlNodeList.getLength(); i++){
				//loop through the list of neighbor associations to do assignment for each one
				for (int j = 0; j < neighborNodes.get(i).size(); j++){
					
					String neighborID = neighborNodes.get(i).get(j);
					
					//need to get the node associated with this ID
					for(MapNode potentialNode : loadedNodes){
						if(potentialNode.getNodeID().equals(neighborID)){
							MapNode currentNode = loadedNodes.get(i);
							currentNode.addNeighbor(potentialNode);
						}
					}//end inner for		
				}//end middle for
			}//end outer for
			
			this.localMap = new LocalMap(mapImageName, loadedNodes);
			for(MapNode anode: loadedNodes){
				anode.setLocalMap(this.localMap);
			}
		} catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
		
	}
	
	public LocalMap getLocalMap() {
		return localMap;
	}

	public void setLocalMap(LocalMap localMap) {
		this.localMap = localMap;
	}

}

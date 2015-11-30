package main.gui;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import main.LocalMap;
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
		fileName = SaveUtil.removeExtension(fileName);
		fileName = fileName.concat(".localmap");
		
		
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
	        Element rootEle = dom.createElement("mapnodes");
	        
	        
	        for(MapNode node : this.localMap.getMapNodes()){
	        	
	        	e = dom.createElement("node");
	        	
	        	Element id = dom.createElement("NodeID");
	        	id.appendChild(dom.createTextNode(Integer.toString(node.getNodeID())));
	        	
	        	Element xPos = dom.createElement("XPos");
	        	xPos.appendChild(dom.createTextNode(Double.toString(node.getXPos())));
	        	
	        	
	        	e.appendChild(id);
	        	e.appendChild(xPos);
	        	
	        	rootEle.appendChild(e);
	        }
	        
	        dom.appendChild(rootEle);
	        try {
	            Transformer tr = TransformerFactory.newInstance().newTransformer();
	            tr.setOutputProperty(OutputKeys.INDENT, "yes");
	            tr.setOutputProperty(OutputKeys.METHOD, "xml");
	            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, fileName);
	            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	            // send DOM to file
	            tr.transform(new DOMSource(dom), 
	                                 new StreamResult(new FileOutputStream(fileName)));

	        } catch (TransformerException te) {
	            System.out.println(te.getMessage());
	        } catch (IOException ioe) {
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
	
	public void selectNode() {
	
	}
	
	

	public LocalMap getLocalMap() {
		return localMap;
	}

	public void setLocalMap(LocalMap localMap) {
		this.localMap = localMap;
	}

}

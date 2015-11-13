package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * Class purposes:
 * 	GUIBackend - Keeps track of information regarding the CURRENTLY LOADED map
 *  GlobalMap - Keeps track of information regarding ALL maps.
 * @author Trevor
 */

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L; // Serial Version ID
	private static GUIBackend backend;
	private static GlobalMap globalMap;
	private ImageIcon nodeImage = new ImageIcon("src\\images\\node.png");
	
	// GUI Components
	private JLabel lblMapImage, lblNodes;
	private JPanel panelImage;
	
	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public MainGUI(int numLocalMaps, File[] localMapFilenames) throws IOException, ClassNotFoundException {
		// Instantiate GUIBackend to its default (which should be the campus map). Currently set to "map1.localmap"
		GUIBackend backend = new GUIBackend();
		
		// Initialize the GlobalMap variable with all of the LocalMaps and all of their nodes
		globalMap = new GlobalMap();
		
		LocalMap[] tmpListLocal = new LocalMap[numLocalMaps]; // temporary list of LocalMaps to be initialized
		for(int i = 0; i < numLocalMaps; i++){		
			System.out.println(localMapFilenames[i].getName());
			backend.loadLocalMap(localMapFilenames[i].getName()); // sets the current LocalMap each filename from the "localmaps" folder
			tmpListLocal[i] = backend.getLocalMap();
		}
		globalMap.setLocalMaps(tmpListLocal);
		backend.setLocalMap(tmpListLocal[0]);
		
		System.out.println(backend.getLocalMap().getMapImage());
		
		// Need to somehow know how many MapNodes there are ahead of time, else use an ArrayList
		ArrayList<MapNode> allNodes = new ArrayList<MapNode>();
		for(LocalMap local : tmpListLocal){
			allNodes.addAll(local.getMapNodes()); // add the collection of nodes to the ArrayList of Global
		}
		globalMap.setMapNodes(allNodes);		 
		
		/**
		 * Setup JFrame
		 */
		setTitle("WPI Nav Tool");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon map = new ImageIcon("src/images/" + backend.getLocalMap().getMapImage()); // load default map image
		setVisible(true);
		
		// load map image
		lblMapImage = new JLabel("");
		lblMapImage.setIcon(map);
		
		lblNodes = new JLabel("");
		
		
		/**
		 * Display MapNodes on the map
		 * TODO: Implement this once there is testable Node Data
		 */		
		/*
		for(MapNode n : backend.getLocalMap().getMapNodes()){
			Graphics g = this.getGraphics();
			nodeImage.paintIcon(lblNodes, g, (int)n.getX(), (int)n.getY());
			//g.fillOval((int)n.getX(), (int)n.getY(), 10, 10);
			
			System.out.println("we found a node");
			System.out.println(n.getX() + "\t" + n.getY());
		}*/
				
		// Initialize GUI components
		panelImage = new JPanel();	
		
		// Set location and properties of the panel that holds the label representing the map image
		panelImage.setBackground(Color.WHITE);
		
		panelImage.add(lblMapImage);
		panelImage.add(lblNodes);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelImage, GroupLayout.PREFERRED_SIZE, 1091, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(265, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelImage, GroupLayout.PREFERRED_SIZE, 681, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(58, Short.MAX_VALUE))
		);
		
		GroupLayout gl_panelImage = new GroupLayout(panelImage);
		gl_panelImage.setHorizontalGroup(
			gl_panelImage.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelImage.createSequentialGroup()
					.addComponent(lblNodes)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblMapImage, GroupLayout.DEFAULT_SIZE, 1091, Short.MAX_VALUE))
		);
		gl_panelImage.setVerticalGroup(
			gl_panelImage.createParallelGroup(Alignment.LEADING)
				.addComponent(lblMapImage, GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
				.addGroup(gl_panelImage.createSequentialGroup()
					.addComponent(lblNodes)
					.addContainerGap())
		);
		panelImage.setLayout(gl_panelImage);
		getContentPane().setLayout(groupLayout);
		
	}
}

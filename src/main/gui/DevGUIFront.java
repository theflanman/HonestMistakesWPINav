package main.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import main.LocalMap;
import main.MapNode;
import main.util.Constants;
import main.util.MapPanel;
import main.util.SaveUtil;


public class DevGUIFront extends JFrame {
	static HashMap<String, ArrayList<MapNode>> localMap = new HashMap<String, ArrayList<MapNode>>(); // path to file, Integer data
	static ArrayList<MapNode> points = new ArrayList<MapNode>(); // currently loaded list of points
	static File inputFile;
	static Image pic;
	String path; // current path
	private int nodeCounter = 0;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField xPosField;
	private JTextField yPosField;
	private JTextField zPosField;
	private MapNode lastClicked;
	private MapNode edgeStart;
	private MapNode edgeRemove;
	private MapNode nodeToRemove;
	private boolean multiSelect;
	private ArrayList<MapNode> selectedNodes = new ArrayList<MapNode>();
	private boolean edgeStarted = false;
	private boolean edgeRemovalStarted = false;
	private JTextField setNameTextbox;
	private JTextField aliasesTextBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					DevGUIFront frame = new DevGUIFront();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public void keyPressed(KeyEvent e){
		System.out.println("Key pressed");
	}
	
	
	/**
	 * Create the frame.
	 */
	public DevGUIFront() {

		System.out.println("Initializing...");
	//	setExtendedState(Frame.MAXIMIZED_BOTH); //This has the application automatically open maximized.
		
		// This sets the size and behavior of the application window itself.
		setPreferredSize(new Dimension(1380, 760));
		setResizable(true);
		setTitle("Map Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1300, 760);

		// This defines the radio buttons that can be selected for the cursor options, because they are used in the following section of code.
		JRadioButton rdbtnSelectNode = new JRadioButton("Select Node");
		JRadioButton rdbtnRemoveEdge = new JRadioButton("Remove Edge");
		JRadioButton rdbtnRemoveNode = new JRadioButton("Remove Node");
		JRadioButton rdbtnPlaceNode = new JRadioButton("Place Node");
		JRadioButton rdbtnMakeEdge = new JRadioButton("Make Edge");
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);



		MapPanel mapPanel = new MapPanel();

		int threshold = 10; //threshold is a radius for selecting nodes on the map - they are very tiny otherwise and hard to click precisely

		
		
		// This is code that determines what needs to happen on each mouse click in the map panel.
		
		mapPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point offset = mapPanel.getLocationOnScreen();
				if(rdbtnPlaceNode.isSelected()){
					edgeStarted = false; //These two calls are a basic attempt to stop edge addition and removal from becoming confusing.
					edgeRemovalStarted = false; //It is not evident whether the user has clicked a first node yet in the edge, so changing to a different operation will reset it.
					Point p = me.getLocationOnScreen();
					MapNode n = new MapNode((double) p.x - offset.x, (double) p.y - offset.y, (double) 0); // make a new mapnode with those points
					nodeCounter++;
					Graphics g = mapPanel.getGraphics();
					points.add(n);
					xPosField.setText(""+n.getXPos());
					yPosField.setText(""+n.getYPos());
					zPosField.setText(""+n.getZPos());
					//nodeNameField.setText(n.getnodeName());
					lastClicked = n;
					mapPanel.renderMapPublic(g, points);
				} else if (rdbtnSelectNode.isSelected()){
					edgeStarted = false;
					edgeRemovalStarted = false;
					
					if(!me.isControlDown()) {
						selectedNodes.clear();
					}
					if(me.isShiftDown()){
						multiSelect = true;
					}
					else{
						multiSelect = false;
					}
					for(MapNode n : points){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
							xPosField.setText(""+n.getXPos());
							yPosField.setText(""+n.getYPos());
							zPosField.setText(""+n.getZPos());
							
							//nodeNameField.setText(n.getnodeName());
							if(multiSelect){
								MapNode lastPoint = lastClicked;
								MapNode currentPoint  = n;
								double minX = Math.min(currentPoint.getXPos(), lastPoint.getXPos());
								double maxX = Math.max(currentPoint.getXPos(), lastPoint.getXPos());
								double minY = Math.min(currentPoint.getYPos(), lastPoint.getYPos());
								double maxY = Math.max(currentPoint.getYPos(), lastPoint.getYPos());
								for(MapNode mn : points){
									if(mn.getXPos() >= minX && mn.getXPos() <= maxX &&
											mn.getYPos() >= minY && mn.getYPos() <= maxY){
										if(!selectedNodes.contains(mn)){
											selectedNodes.add(mn);
										}
									}
								}
								
							}
							lastClicked = n;
							
							//handle shift clicking here by adding multiple nodes
							if(!selectedNodes.contains(n)){
								selectedNodes.add(n);
							}

							System.out.println(selectedNodes.size());
						}
					}
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderSelectedNodes(g, points, selectedNodes);

					
				}
				else if(rdbtnMakeEdge.isSelected()) {
					edgeRemovalStarted = false;
					if(edgeStarted == false) {
						for(MapNode n : points){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeStart = n;
								edgeStarted = true;
							}
						}

					}
					else {
						for(MapNode n : points) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								edgeStarted = false;
								n.addNeighbor(edgeStart);
								edgeStart.addNeighbor(n);
								mapPanel.renderMapPublic(g, points);
							}
						}
					}
				}
				else if(rdbtnRemoveEdge.isSelected()) {
					edgeStarted = false;
					if(edgeRemovalStarted == false) {
						for(MapNode n : points){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeRemove = n;
								edgeRemovalStarted = true;
							}
						}

					}
					else {
						for(MapNode n : points) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								edgeRemovalStarted = false;
								n.removeNeighbor(edgeRemove);
								edgeRemove.removeNeighbor(n);
								mapPanel.renderMapPublic(g, points);
							}
						}
					}

				}
				else if(rdbtnRemoveNode.isSelected()) {
					edgeStarted = false;
					edgeRemovalStarted = false;
					for(MapNode n : points){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
							for(MapNode m : n.getNeighbors()) {
							//	n.removeNeighbor(m);
								m.removeNeighbor(n);
							}
							nodeToRemove = n;
							

						}
					}
					nodeToRemove.getNeighbors().removeIf((MapNode q)->q.getXPos() > -1000000000); //Intent is to remove all neighbors. Foreach loop doesn't like this.
					
					points.remove(nodeToRemove);
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderMapPublic(g, points);
				}
			}
		}); 
		mapPanel.setBackground(Color.WHITE);


		// This is code for the file menu with the save map, load map, load new image, dropdown menu.
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadMap = new JMenuItem("Load Map");
		mnFile.add(mntmLoadMap);
		mntmLoadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int option = chooser.showOpenDialog(DevGUIFront.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					inputFile = chooser.getSelectedFile();
					String inputFileName = inputFile.getName();
					
					// load localMap
					DevGUIBack devGUIBack = new DevGUIBack(null);
					devGUIBack.loadMap(Constants.LOCAL_MAP_PATH + "/" + inputFileName);
					LocalMap loadedLocalMap = devGUIBack.getLocalMap(); 
					
					String imagePath = SaveUtil.removeExtension(inputFileName);
					imagePath = imagePath + ".jpg";
										
					// set the image
					try {
						pic = ImageIO.read(new File(Constants.IMAGES_PATH + "/" + imagePath));
					} catch (IOException e1) {
						e1.printStackTrace();}
					
					//  picLabel.setIcon(new ImageIcon(pic));
					mapPanel.setBgImage(pic);

					// set the points
					Graphics g = mapPanel.getGraphics();
					points = loadedLocalMap.getMapNodes();
					mapPanel.renderMapPublic(g, points);
				}
			}
		});

		JMenuItem mntmSaveMap = new JMenuItem("Save Map");
		mnFile.add(mntmSaveMap);
		mntmSaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fileName = inputFile.getName();
				fileName = SaveUtil.removeExtension(fileName);
				fileName = fileName + ".jpg";
				LocalMap thisMap = new LocalMap(fileName, points);
				DevGUIBack devGUIBack = new DevGUIBack(thisMap);
				
				devGUIBack.saveMap();
			}
		});

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JMenuItem mntmNewMapImage = new JMenuItem("New Map Image");
		mntmNewMapImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int option = chooser.showOpenDialog(DevGUIFront.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					inputFile = chooser.getSelectedFile();

					try{
						pic = ImageIO.read(inputFile);
						//  picLabel.setIcon(new ImageIcon(pic));
						mapPanel.setBgImage(pic);

						Graphics g = mapPanel.getGraphics();
						mapPanel.renderMapPublic(g, points);
					}
					catch(IOException ex){
						ex.printStackTrace();
						System.exit(1);
					}
				}
			}
		});
		mnFile.add(mntmNewMapImage);
		mnFile.add(mntmExit);

		// This is code for the panel showing information on the current selected node.
		
		JPanel nodeInfoPanel = new JPanel();
		nodeInfoPanel.setBounds(926, 214, 294, 108);
		nodeInfoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nodeInfoPanel.setLayout(null);
		
		// Labels
		JLabel lblNodeInformation = new JLabel("Node Information");
		lblNodeInformation.setBounds(2, 2, 99, 16);
		nodeInfoPanel.add(lblNodeInformation);
 
		JLabel lblXposition = new JLabel("x-position");
		lblXposition.setBounds(2, 26, 67, 16);
		nodeInfoPanel.add(lblXposition);

		JLabel lblYposition = new JLabel("y-position");
		lblYposition.setBounds(2, 53, 55, 16);
		nodeInfoPanel.add(lblYposition);

		JLabel lblZposition = new JLabel("z-position");
		lblZposition.setBounds(2, 80, 55, 16);
		nodeInfoPanel.add(lblZposition);
		
		// Text fields
		xPosField = new JTextField();
		xPosField.setBounds(122, 23, 165, 22);
		nodeInfoPanel.add(xPosField);
		xPosField.setColumns(10);

		yPosField = new JTextField();
		yPosField.setBounds(122, 50, 165, 22);
		nodeInfoPanel.add(yPosField);
		yPosField.setColumns(10);

		zPosField = new JTextField();
		zPosField.setBounds(122, 77, 165, 22);
		nodeInfoPanel.add(zPosField);
		zPosField.setColumns(10);

		
		// This is code for the panel with the radio buttons for the cursor options
		
		JPanel cursorPanel = new JPanel();
		cursorPanel.setBounds(926, 13, 130, 183);
		cursorPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		cursorPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel_1 = new JLabel(" Cursor Options");
		cursorPanel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		cursorPanel.add(rdbtnPlaceNode);
		buttonGroup.add(rdbtnPlaceNode);
		rdbtnPlaceNode.setSelected(true);

		cursorPanel.add(rdbtnRemoveNode);
		buttonGroup.add(rdbtnRemoveNode);

		cursorPanel.add(rdbtnSelectNode);
		buttonGroup.add(rdbtnSelectNode);

		cursorPanel.add(rdbtnMakeEdge);
		buttonGroup.add(rdbtnMakeEdge);
		cursorPanel.add(rdbtnRemoveEdge);
		buttonGroup.add(rdbtnRemoveEdge);
		getContentPane().setLayout(null);


		// This is code pertaining to the panel holding the map panel.
		
		JPanel panel = new JPanel();
		panel.setLocation(12, 13);
		panel.setPreferredSize(new Dimension(1000, 800));
		panel.setSize(new Dimension(1192, 653));
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(Color.WHITE);
		
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBounds(13,13,900, 600);
		panel.add(mapPanel);
		getContentPane().add(panel);
		getContentPane().add(cursorPanel);
		getContentPane().add(nodeInfoPanel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(923, 431, 351, 259);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		//set the official name of each of the selected nodes to the
		//given value in the box
		JButton btnSetName = new JButton("Set Name");
		btnSetName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode node : selectedNodes){
					//node.setAttributeName(setNameTextbox.getText())
				}
			}
		});
		btnSetName.setBounds(10, 5, 102, 23);
		panel_1.add(btnSetName);
		
		setNameTextbox = new JTextField();
		setNameTextbox.setBounds(122, 6, 201, 20);
		panel_1.add(setNameTextbox);
		setNameTextbox.setColumns(10);
		
		JComboBox typeComboBox = new JComboBox();
		typeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Food", "Office", "Classroom", "Water Fountain", "Bathroom", "Parking", "Walking", "Door", "Elevator", "Laboratoy", "Other"}));
		typeComboBox.setBounds(139, 81, 184, 20);
		panel_1.add(typeComboBox);
		
		//get the location type from the combo box and store this
		//for every selected node
		JButton btnSetAttributes = new JButton("Set Location Type");
		btnSetAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode node: selectedNodes){
					//node.setAttributeType(typeComboBox.getSelectedIndex());	
				}
			}
		});
		btnSetAttributes.setBounds(10, 80, 119, 23);
		panel_1.add(btnSetAttributes);
		
		
		
		Checkbox outsideCheckBox = new Checkbox("Outside?");
		outsideCheckBox.setBounds(175, 171, 95, 22);
		panel_1.add(outsideCheckBox);
		
		Checkbox bikeCheckBox = new Checkbox("Bike-friendly?");
		bikeCheckBox.setBounds(10, 143, 95, 22);
		panel_1.add(bikeCheckBox);
		
		Checkbox handicappedCheckBox = new Checkbox("Handicapped Accessible?");
		handicappedCheckBox.setBounds(10, 171, 154, 22);
		panel_1.add(handicappedCheckBox);
		
		Checkbox stairsCheckBox = new Checkbox("Stairs?");
		stairsCheckBox.setBounds(10, 199, 95, 22);
		panel_1.add(stairsCheckBox);
		
		Checkbox poiCheckBox = new Checkbox("Point of Interest?");
		poiCheckBox.setBounds(175, 143, 107, 22);
		panel_1.add(poiCheckBox);
		
		//set the boolean values for each of the selected nodes according
		//to the checkboxes
		JButton btnSetBooleans = new JButton("Set Booleans");
		btnSetBooleans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isOutside = outsideCheckBox.getState();
				boolean isBikeable = bikeCheckBox.getState();
				boolean isHandicapped = handicappedCheckBox.getState();
				boolean isStairs = stairsCheckBox.getState();
				boolean isPOI = poiCheckBox.getState();
				for(MapNode node: selectedNodes){
					//node.setAttributeBooleans(isOutside, isBikeable, isHandicapped, isStairs, isPOI);
				}
			}
		});
		btnSetBooleans.setBounds(10, 114, 100, 23);
		panel_1.add(btnSetBooleans);
		
		//set the aliases for the selected nodes.
		//assumes alias values will be set as CSV format
		JButton btnSetAliases = new JButton("Set Aliases");
		btnSetAliases.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] aliases = ((aliasesTextBox.getText()).split("\\s*,\\s*") );
				ArrayList<String> aliasArray = new ArrayList<String>();
				for(String str: aliases){
					aliasArray.add(str);
				}
				for(MapNode node : selectedNodes){
					//node.setAttributeAliases(aliasArray);
				}
			}
		});
		btnSetAliases.setBounds(10, 39, 89, 23);
		panel_1.add(btnSetAliases);
		
		aliasesTextBox = new JTextField();
		aliasesTextBox.setBounds(122, 40, 201, 20);
		panel_1.add(aliasesTextBox);
		aliasesTextBox.setColumns(10);
	}
}
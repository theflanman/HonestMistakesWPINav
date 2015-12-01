package main.gui;
import main.Attributes;
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
import main.Types;

public class DevGUIFront extends JFrame {
	static HashMap<String, ArrayList<MapNode>> localMap = new HashMap<String, ArrayList<MapNode>>(); // path to file, Integer data
	static ArrayList<MapNode> points = new ArrayList<MapNode>(); // currently loaded list of points
	static ArrayList<MapNode> points2 = new ArrayList<MapNode>(); // Points for the second map
	static File inputFile;
	static File inputFile2;
	static Image pic;
	static Image pic2;
	static LocalMap local1;
	static LocalMap local2;
	String path; // current path
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
	private ArrayList<MapNode> selectedNodes2 = new ArrayList<MapNode>();
	private boolean edgeStarted = false;
	private boolean edgeRemovalStarted = false;
	private boolean twoMapView = false;
	private JTextField textFieldOfficialName;
	private JComboBox typeBox;
	private JTextArea txtrAliases;
	private JCheckBox chckbxStairs;
	private JCheckBox chckbxPOI;
	private JCheckBox chckbxHandicapped;
	private JCheckBox chckbxBikeable;
	private JCheckBox chckbxOutside;
	
	private Attributes defaultAttributes;
	
	/*public enum Type {
		FOOD, OFFICE, CLASSROOM, WATERFOUNTAIN, BATHROOM, PARKING, WALKING, DOOR, ELEVATOR, LAB, OTHER
	} */
	
	public String[] typeList = new String[] {"Food", "Office", "Classroom", "Waterfountain", "Bathroom", "Parking", "Walking", "Door", "Elevator", "Lab", "Other"};
	
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

	//	setExtendedState(Frame.MAXIMIZED_BOTH); //This has the application automatically open maximized.
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
		MapPanel mapPanel2 = new MapPanel();

		int threshold = 10; //threshold is a radius for selecting nodes on the map - they are very tiny otherwise and hard to click precisely

		Attributes defaultAttributes = new Attributes();
		defaultAttributes.setBikeable(false);
		defaultAttributes.setHandicapped(true);
		defaultAttributes.setOutside(false);
		defaultAttributes.setPOI(false);
		defaultAttributes.setStairs(false);
		defaultAttributes.setType(Types.WALKING);
		
		



		// This is code for the file menu with the save map, load map, load new image, dropdown menu.
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadMap = new JMenuItem("Load Map");
		mnFile.add(mntmLoadMap);
		mntmLoadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(Constants.LOCAL_MAP_PATH));
				int option = chooser.showOpenDialog(DevGUIFront.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					inputFile = chooser.getSelectedFile();
					String inputFileName = inputFile.getName();
					
					// load localMap
					DevGUIBack devGUIBack = new DevGUIBack(null);
					devGUIBack.loadMap(Constants.LOCAL_MAP_PATH + "/" + inputFileName);
					local1 = devGUIBack.getLocalMap();
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
					points = local1.getMapNodes();
					mapPanel.renderMapPublic(g, points, null);
				}
			}
		});
		
		JMenuItem mntmLoadExtraMap = new JMenuItem("Load Extra Map");
		mntmLoadExtraMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(Constants.LOCAL_MAP_PATH));
				int option = chooser.showOpenDialog(DevGUIFront.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					inputFile2 = chooser.getSelectedFile();
					String inputFileName = inputFile2.getName();
					
					// load localMap
					DevGUIBack devGUIBack = new DevGUIBack(null);
					devGUIBack.loadMap(Constants.LOCAL_MAP_PATH + "/" + inputFileName); 
					local2 = devGUIBack.getLocalMap();
					String imagePath = SaveUtil.removeExtension(inputFileName);
					imagePath = imagePath + ".jpg";
										
					// set the image
					try {
						pic2 = ImageIO.read(new File(Constants.IMAGES_PATH + "/" + imagePath));
					} catch (IOException e1) {
						e1.printStackTrace();}
					
					//  picLabel.setIcon(new ImageIcon(pic));
					mapPanel2.setBgImage(pic2);

					// set the points
					Graphics g = mapPanel2.getGraphics();
					points2 = local2.getMapNodes();
					mapPanel2.renderMapPublic(g, points2, null);
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
				
				if (twoMapView && !local1.getMapImageName().equals(local2.getMapImageName())) {
					String fileName2 = inputFile2.getName();
					fileName2 = SaveUtil.removeExtension(fileName2);
					fileName2 = fileName2 + ".jpg";
					LocalMap thisMap2 = new LocalMap(fileName2, points2);
					DevGUIBack devGUIBack2 = new DevGUIBack(thisMap2);
					devGUIBack2.saveMap();
				}
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
				chooser.setCurrentDirectory(new File(Constants.IMAGES_PATH));
				int option = chooser.showOpenDialog(DevGUIFront.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					inputFile = chooser.getSelectedFile();

					try{
						pic = ImageIO.read(inputFile);
						//  picLabel.setIcon(new ImageIcon(pic));
						String imagePath = SaveUtil.removeExtension(inputFile.getName());
						imagePath = imagePath + ".jpg";
						
						mapPanel.setBgImage(pic);
						points = new ArrayList<MapNode>();
						Graphics g = mapPanel.getGraphics();
						mapPanel.renderMapPublic(g, points, null);
						local1 = new LocalMap(imagePath, points);

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
		
		// This is the View menu, allowing a developer to switch to showing one or two maps at a time.
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmShowTwoMaps = new JMenuItem("Show Two Maps");
		mnView.add(mntmShowTwoMaps);
		
		JMenuItem mntmShowOneMap = new JMenuItem("Show One Map");
		mnView.add(mntmShowOneMap);

		// This is code for the panel showing information on the current selected node.
		
		JPanel nodeInfoPanel = new JPanel();
		nodeInfoPanel.setBounds(920, 203, 294, 136);
		nodeInfoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nodeInfoPanel.setLayout(null);
		
		// Labels
		JLabel lblNodeInformation = new JLabel(" Node Information");
		lblNodeInformation.setBounds(2, 2, 133, 16);
		nodeInfoPanel.add(lblNodeInformation);

		JLabel lblXposition = new JLabel(" x-position");
		lblXposition.setBounds(2, 26, 67, 16);
		nodeInfoPanel.add(lblXposition);

		JLabel lblYposition = new JLabel(" y-position");
		lblYposition.setBounds(2, 53, 73, 16);
		nodeInfoPanel.add(lblYposition);

		JLabel lblZposition = new JLabel(" z-feet");
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

		JButton btnMakeChanges = new JButton("Edit Node");
		btnMakeChanges.setBounds(4, 106, 115, 25);
		nodeInfoPanel.add(btnMakeChanges);
		
		btnMakeChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lastClicked.setXPos(Double.parseDouble(xPosField.getText()));
				lastClicked.setYPos(Double.parseDouble(yPosField.getText()));
				lastClicked.setZFeet(Double.parseDouble(zPosField.getText()));
				Graphics g = mapPanel.getGraphics();
				mapPanel.renderMapPublic(g, points, lastClicked);
				if (twoMapView) {
					Graphics g2 = mapPanel2.getGraphics();
					mapPanel2.renderMapPublic(g2, points2, lastClicked);
				}
			}
		});
		
		// This is code for the panel with the radio buttons for the cursor options
		
		JPanel cursorPanel = new JPanel();
		cursorPanel.setBounds(920, 10, 130, 183);
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
		panel.setLocation(10, 13);
	//	panel.setPreferredSize(new Dimension(1000, 800));
	//	panel.setSize(new Dimension(1192, 653));
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(Color.WHITE);
		
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBounds(10,10,900, 600);
		panel.add(mapPanel);
		getContentPane().add(panel);
		getContentPane().add(cursorPanel);
		getContentPane().add(nodeInfoPanel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(920, 349, 301, 312);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel = new JLabel(" Attributes");
		lblNewLabel.setBounds(2, 2, 66, 16);
		panel_1.add(lblNewLabel);
		
		chckbxBikeable = new JCheckBox("Bikeable");
		chckbxBikeable.setBounds(8, 215, 113, 25);
		panel_1.add(chckbxBikeable);
		
		chckbxHandicapped = new JCheckBox("Handicapped accessable");
		chckbxHandicapped.setBounds(8, 242, 176, 25);
		panel_1.add(chckbxHandicapped);
		
		chckbxOutside = new JCheckBox("Outside");
		chckbxOutside.setBounds(150, 188, 113, 25);
		panel_1.add(chckbxOutside);
		
		chckbxPOI = new JCheckBox("Point of interest");
		chckbxPOI.setBounds(150, 215, 141, 25);
		panel_1.add(chckbxPOI);
		
		chckbxStairs = new JCheckBox("Stairs");
		chckbxStairs.setBounds(8, 188, 113, 25);
		panel_1.add(chckbxStairs);
		
		JLabel lblNewLabel_2 = new JLabel("Name");
		lblNewLabel_2.setBounds(12, 31, 56, 16);
		panel_1.add(lblNewLabel_2);
		
		textFieldOfficialName = new JTextField();
		textFieldOfficialName.setBounds(61, 28, 196, 22);
		panel_1.add(textFieldOfficialName);
		textFieldOfficialName.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Aliases");
		lblNewLabel_3.setBounds(12, 55, 56, 16);
		panel_1.add(lblNewLabel_3);
		
		typeBox = new JComboBox(Types.values());
		//typeBox.setModel(new DefaultComboBoxModel(Type.values()));
		typeBox.setMaximumRowCount(10);
		typeBox.setBounds(12, 154, 204, 25);
		panel_1.add(typeBox);
		
		typeBox.setSelectedIndex(-1);
		
		JButton btnEditName = new JButton("Edit Name");
		btnEditName.setBounds(12, 276, 97, 25);
		panel_1.add(btnEditName);
		
		JButton btnSetDfltAttr = new JButton("Set Default Attributes");
		btnSetDfltAttr.setBounds(121, 276, 168, 25);
		panel_1.add(btnSetDfltAttr);
		
		txtrAliases = new JTextArea();
		JScrollPane scrollpane = new JScrollPane(txtrAliases);
		scrollpane.setBounds(14, 73, 277, 68);
		panel_1.add(scrollpane);
		txtrAliases.setLineWrap(true);
		JPanel panel2 = new JPanel();
		panel2.setLocation(465, 10);
		
	//	
	//	panel2.setLocation(930, 500);
		panel2.setSize(445, 600);
		panel2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel2.setBackground(Color.WHITE);
		panel2.setLayout(new BorderLayout(0, 0));
		panel2.setBounds(465, 10, 445, 600);
		mapPanel2.setBounds(2, 2, 443, 598); //TODO Figure out why this line of code is needed when it isn't needed for mapPanel!
		panel2.add(mapPanel2);
	//	getContentPane().add(panel2);
		
	//	panel.setSize(new Dimension(445, 600));
	//	getContentPane().add(panel2);
		
		mntmShowTwoMaps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				panel.setSize(new Dimension(445, 600));
				getContentPane().add(panel2);
				panel.setBackground(Color.WHITE);
				mnFile.add(mntmLoadExtraMap);
				twoMapView = true;
				mntmSaveMap.setText("Save Maps");
				Graphics g2 = mapPanel2.getGraphics();
				mapPanel2.renderMapPublic(g2, points2, lastClicked);
			}
		});
		
		mntmShowOneMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			getContentPane().remove(panel2);
				panel.setSize(new Dimension(900, 600));
				mnFile.remove(mntmLoadExtraMap);
				twoMapView = false;
				mntmSaveMap.setText("Save Map");
			}
		});
		
		btnSetDfltAttr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultAttributes.setBikeable(chckbxBikeable.isSelected());
				defaultAttributes.setStairs(chckbxStairs.isSelected());
				defaultAttributes.setPOI(chckbxPOI.isSelected());
				defaultAttributes.setHandicapped(chckbxHandicapped.isSelected());
				defaultAttributes.setOutside(chckbxOutside.isSelected());
				defaultAttributes.setType((Types) typeBox.getSelectedItem());
			}
		});
		
		btnEditName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(lastClicked != null) {
					lastClicked.getAttributes().setOfficialName(textFieldOfficialName.getText());
					String aliases[] = txtrAliases.getText().split("\\r?\\n");
				    ArrayList<String>aliasesArray = new ArrayList<>(Arrays.asList(aliases)) ;
				    lastClicked.getAttributes().setAliases(aliasesArray);
				}
			}
		});
		
		chckbxStairs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(lastClicked != null) {
					lastClicked.getAttributes().setStairs(chckbxStairs.isSelected());
				}
			}
		});

		chckbxBikeable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(lastClicked != null) {
					lastClicked.getAttributes().setBikeable(chckbxBikeable.isSelected());
				}
			}
		});
		
		chckbxPOI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(lastClicked != null) {
					lastClicked.getAttributes().setPOI(chckbxPOI.isSelected());
				}
			}
		});
		
		chckbxOutside.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(lastClicked != null) {
					lastClicked.getAttributes().setOutside(chckbxOutside.isSelected());
				}
			}
		});
		
		chckbxHandicapped.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(lastClicked != null) {
					lastClicked.getAttributes().setHandicapped(chckbxHandicapped.isSelected());
				}
			}
		});
		
		// This is code that determines what needs to happen on each mouse click in the map panel.
		
		mapPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point offset = mapPanel.getLocationOnScreen();
				if(rdbtnPlaceNode.isSelected()){
					edgeStarted = false; //These two calls are a basic attempt to stop edge addition and removal from becoming confusing.
					edgeRemovalStarted = false; //It is not evident whether the user has clicked a first node yet in the edge, so changing to a different operation will reset it.
					Point p = me.getLocationOnScreen();
					MapNode n = new MapNode((double) p.x - offset.x, (double) p.y - offset.y, local1); // make a new mapnode with those points
					Graphics g = mapPanel.getGraphics();
					
					points.add(n);
					n.setDefaultAttributes(defaultAttributes);
					setInfoFields(n);
					lastClicked = n;
					mapPanel.renderMapPublic(g, points, lastClicked);
					if(twoMapView) {
						Graphics g2 = mapPanel2.getGraphics();
						mapPanel2.renderMapPublic(g2, points2, lastClicked);
					}
				} else if (rdbtnSelectNode.isSelected()){
					edgeStarted = false;
					edgeRemovalStarted = false;
					if(!me.isControlDown()){
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
							setInfoFields(n);
							
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
							
							if(!selectedNodes.contains(n)){
								selectedNodes.add(n);
							}
							
							System.out.println(selectedNodes.size());
							
							Graphics g = mapPanel.getGraphics();
							
							mapPanel.renderSelectedNodes(g, points, selectedNodes, lastClicked);
							if(twoMapView) {
								Graphics g2 = mapPanel2.getGraphics();
								mapPanel2.renderSelectedNodes(g2, points2, selectedNodes2, lastClicked);
							}
							
						}
					}
				}
				else if(rdbtnMakeEdge.isSelected()) {
					edgeRemovalStarted = false;
					if(edgeStarted == false) {
						for(MapNode n : points){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeStart = n;
								edgeStarted = true;
								setInfoFields(n);							
								lastClicked = n;
								Graphics g = mapPanel.getGraphics();
								mapPanel.renderMapPublic(g, points, lastClicked);
								if(twoMapView) {
									Graphics g2 = mapPanel2.getGraphics();
									mapPanel2.renderMapPublic(g2, points2, lastClicked);
								}
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
								setInfoFields(n);							
								lastClicked = n;
								mapPanel.renderMapPublic(g, points, lastClicked);
								if(twoMapView) {
									Graphics g2 = mapPanel2.getGraphics();
									mapPanel2.renderMapPublic(g2, points2, lastClicked);
								}
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
								setInfoFields(n);							
								lastClicked = n;
								Graphics g = mapPanel.getGraphics();
								mapPanel.renderMapPublic(g, points, lastClicked);
								if(twoMapView) {
									Graphics g2 = mapPanel2.getGraphics();
									mapPanel2.renderMapPublic(g2, points2, lastClicked);
								}
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
								setInfoFields(n);							
								lastClicked = n;
								mapPanel.renderMapPublic(g, points, lastClicked);
								if(twoMapView) {
									Graphics g2 = mapPanel2.getGraphics();
									mapPanel2.renderMapPublic(g2, points2, lastClicked);
								}
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
					if(twoMapView) {
						Graphics g2 = mapPanel2.getGraphics();
						mapPanel2.renderMapPublic(g2, points2, lastClicked);
					}
				}
			}
		}); 
		mapPanel.setBackground(Color.WHITE);
		
		
		// This is code for the second map panel.
		
		mapPanel2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point offset = mapPanel2.getLocationOnScreen();
				if(rdbtnPlaceNode.isSelected()){
					edgeStarted = false; //These two calls are a basic attempt to stop edge addition and removal from becoming confusing.
					edgeRemovalStarted = false; //It is not evident whether the user has clicked a first node yet in the edge, so changing to a different operation will reset it.
					Point p = me.getLocationOnScreen();
					MapNode n = new MapNode((double) p.x - offset.x, (double) p.y - offset.y, local2); // make a new mapnode with those points
					Graphics g = mapPanel.getGraphics();
					Graphics g2 = mapPanel2.getGraphics();
					points2.add(n);
					n.setDefaultAttributes(defaultAttributes);
					setInfoFields(n);
					lastClicked = n;
					mapPanel2.renderMapPublic(g2, points2, lastClicked);
					mapPanel.renderMapPublic(g, points, lastClicked);
				} else if (rdbtnSelectNode.isSelected()){
					/*
					edgeStarted = false;
					edgeRemovalStarted = false;
					for(MapNode n : points2){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
							setInfoFields(n);
							lastClicked = n;
							Graphics g = mapPanel.getGraphics();
							Graphics g2 = mapPanel2.getGraphics();
							mapPanel2.renderMapPublic(g2, points2, lastClicked);
							mapPanel.renderMapPublic(g, points, lastClicked);
						}
					}
					*/
					edgeStarted = false;
					edgeRemovalStarted = false;
					if(!me.isControlDown()){
						selectedNodes2.clear();
					}
					if(me.isShiftDown()){
						multiSelect = true;
					}
					else{
						multiSelect = false;
					}
					
					for(MapNode n : points2){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
							setInfoFields(n);
							
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
										if(!selectedNodes2.contains(mn)){
											selectedNodes2.add(mn);
										}
									}
								}
								
							}
													
							lastClicked = n;
							
							if(!selectedNodes2.contains(n)){
								selectedNodes2.add(n);
							}
							
							System.out.println(selectedNodes2.size());
						}
					}
					Graphics g = mapPanel.getGraphics();
					Graphics g2 = mapPanel2.getGraphics();
					mapPanel2.renderSelectedNodes(g2, points2, selectedNodes2, lastClicked);
					mapPanel.renderSelectedNodes(g, points, selectedNodes, lastClicked);
					
						

				
				} else if(rdbtnMakeEdge.isSelected()) {
					edgeRemovalStarted = false;
					if(edgeStarted == false) {
						for(MapNode n : points2){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeStart = n;
								edgeStarted = true;
								setInfoFields(n);
								lastClicked = n;
								Graphics g = mapPanel.getGraphics();
								Graphics g2 = mapPanel2.getGraphics();
								mapPanel2.renderMapPublic(g2, points2, lastClicked);
								mapPanel.renderMapPublic(g, points, lastClicked);
							}
						}

					}
					else {
						for(MapNode n : points2) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								Graphics g2 = mapPanel2.getGraphics();
								edgeStarted = false;
								n.addNeighbor(edgeStart);
								edgeStart.addNeighbor(n);
								setInfoFields(n);
								lastClicked = n;
								mapPanel2.renderMapPublic(g2, points2, lastClicked);
								mapPanel.renderMapPublic(g, points, lastClicked);
							}
						}
					}
				}
				else if(rdbtnRemoveEdge.isSelected()) {
					edgeStarted = false;
					if(edgeRemovalStarted == false) {
						for(MapNode n : points2){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeRemove = n;
								edgeRemovalStarted = true;
								setInfoFields(n);
								lastClicked = n;
								Graphics g = mapPanel.getGraphics();
								Graphics g2 = mapPanel2.getGraphics();
								mapPanel2.renderMapPublic(g2, points2, lastClicked);
								mapPanel.renderMapPublic(g, points, lastClicked);
							}
						}

					}
					else {
						for(MapNode n : points2) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								Graphics g2 = mapPanel2.getGraphics();
								edgeRemovalStarted = false;
								n.removeNeighbor(edgeRemove);
								edgeRemove.removeNeighbor(n);
								setInfoFields(n);
								lastClicked = n;
								mapPanel2.renderMapPublic(g2, points2, lastClicked);
								mapPanel.renderMapPublic(g, points, lastClicked);
							}
						}
					}

				}
				else if(rdbtnRemoveNode.isSelected()) {
					edgeStarted = false;
					edgeRemovalStarted = false;
					for(MapNode n : points2){
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
					
					points2.remove(nodeToRemove);
					Graphics g2 = mapPanel2.getGraphics();
					mapPanel2.renderMapPublic(g2, points2);
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderMapPublic(g, points);
				}
			}
		}); 
		mapPanel2.setBackground(Color.WHITE);
		
		//Initialize both map panels
		/*
		String inputFileName = "StrattonHallF1.localmap";
		String inputFileName2 = "StrattonHallF2.localmap";
		
		
		// load localMap
		DevGUIBack devGUIBack = new DevGUIBack(null);
		devGUIBack.loadMap(Constants.LOCAL_MAP_PATH + "/" + inputFileName);
		local1 = devGUIBack.getLocalMap();
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
		Graphics f = mapPanel.getGraphics();
		points = local1.getMapNodes();
		mapPanel.renderMapPublic(f, points, null);
		
		
		DevGUIBack devGUIBack2 = new DevGUIBack(null);
		devGUIBack2.loadMap(Constants.LOCAL_MAP_PATH + "/" + inputFileName2); 
		local2 = devGUIBack2.getLocalMap();
		String imagePath2 = SaveUtil.removeExtension(inputFileName2);
		imagePath2 = imagePath2 + ".jpg";
							
		// set the image
		try {
			pic2 = ImageIO.read(new File(Constants.IMAGES_PATH + "/" + imagePath2));
		} catch (IOException e1) {
			e1.printStackTrace();}
		
		//  picLabel.setIcon(new ImageIcon(pic));
		mapPanel2.setBgImage(pic2);

		// set the points
		Graphics g2 = mapPanel2.getGraphics();
		points2 = local2.getMapNodes();
		mapPanel2.renderMapPublic(g2, points2, null);
		
		*/
	}
	
	private void setInfoFields(MapNode n) {
		xPosField.setText(""+n.getXPos());
		yPosField.setText(""+n.getYPos());
		zPosField.setText(""+n.getZFeet());
//		zPosField.setText(""+n.getZPos());
		Attributes a = n.getAttributes();
		typeBox.setSelectedItem(a.getType());
		chckbxStairs.setSelected(a.isStairs());
		chckbxHandicapped.setSelected(a.isHandicapped());
		chckbxOutside.setSelected(a.isOutside());
		chckbxPOI.setSelected(a.isPOI());
		chckbxBikeable.setSelected(a.isBikeable());
		txtrAliases.setText("");
		for(String s : a.getAliases()) {
			txtrAliases.append(s);
			txtrAliases.append("\n");
		}
		textFieldOfficialName.setText(a.getOfficialName());
	}
}
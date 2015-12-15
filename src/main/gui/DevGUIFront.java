package main.gui;
import main.Attributes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;

import java.awt.*;
import java.io.*;

import javax.swing.*;

import main.LocalMap;
import main.MapNode;
import main.gui.frontutil.MapPanel;
import main.gui.frontutil.Popup;
import main.util.Constants;
import main.util.GeneralUtil;
import main.util.proxy.IProxyImage;
import main.util.proxy.ProxyImage;
import main.Types;

@SuppressWarnings("serial")
public class DevGUIFront extends JFrame {
	static HashMap<String, ArrayList<MapNode>> localMap = new HashMap<String, ArrayList<MapNode>>(); // path to file, Integer data
	static ArrayList<MapNode> points = new ArrayList<MapNode>(); // currently loaded list of points
	static ArrayList<MapNode> points2 = new ArrayList<MapNode>(); // Points for the second map
	static File inputFile;
	static File inputFile2;
	static IProxyImage pic;
	static IProxyImage pic2;
	static LocalMap local1;
	static LocalMap local2;
	String path; // current path
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField xPosField, yPosField, zPosField;
	private MapNode lastClicked;
	private MapNode edgeStart, edgeRemove, nodeToRemove;
	private boolean multiSelect;
	private ArrayList<MapNode> selectedNodes = new ArrayList<MapNode>();
	private boolean edgeStarted = false;
	private boolean edgeRemovalStarted = false;
	private boolean twoMapView = false;
	private JTextField textFieldOfficialName;
	@SuppressWarnings("rawtypes")
	private JComboBox typeBox;
	private JTextArea txtrAliases;
	private JCheckBox chckbxStairs, chckbxPOI, chckbxHandicapped, chckbxBikeable, chckbxOutside;
	private JPanel panel, panel2;
	private boolean clickMiss;

	private static DevGUIFront frame;

	public String[] typeList = new String[] {"Food", "Office", "Classroom", "Waterfountain", "Bathroom", "Parking", "Walking", "Door", "Elevator", "Lab", "Other"};
	@SuppressWarnings("unused")
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();

	 Properties prop = new Properties();

	

	public static void main(String[] args) {


		try {
			
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					frame = new DevGUIFront();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DevGUIFront() {
		prop.put("logoString", "EoN"); 
		AluminiumLookAndFeel.setCurrentTheme(prop);

		SwingUtilities.updateComponentTreeUI(this);

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
					String imagePath = GeneralUtil.removeExtension(inputFileName);
					imagePath = imagePath + ".png";

					pic = new ProxyImage(imagePath);

					//  picLabel.setIcon(new ImageIcon(pic));
					mapPanel.setBgImage(pic.getImage(Constants.IMAGES_PATH));

					// set the points
					Graphics g = mapPanel.getGraphics();
					selectedNodes.clear();
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
					String imagePath = GeneralUtil.removeExtension(inputFileName);
					imagePath = imagePath + ".png";

					// set the image
					pic2 = new ProxyImage(imagePath);

					mapPanel2.setBgImage(pic2.getImage(Constants.IMAGES_PATH));

					// set the points
					Graphics g = mapPanel2.getGraphics();
					selectedNodes.clear();
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
				fileName = GeneralUtil.removeExtension(fileName);
				fileName = fileName + ".png";
				LocalMap thisMap = new LocalMap(fileName, points);
				DevGUIBack devGUIBack = new DevGUIBack(thisMap);
				devGUIBack.saveMap();

				if (twoMapView && !local1.getMapImageName().equals(local2.getMapImageName())) {
					String fileName2 = inputFile2.getName();
					fileName2 = GeneralUtil.removeExtension(fileName2);
					fileName2 = fileName2 + ".png";
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

					String imagePath = GeneralUtil.removeExtension(inputFile.getName());
					imagePath = imagePath + ".png";

					pic = new ProxyImage(imagePath);

					mapPanel.setBgImage(pic.getImage(Constants.IMAGES_PATH));

					selectedNodes.clear();
					points = new ArrayList<MapNode>();
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderMapPublic(g, points, null);
					local1 = new LocalMap(imagePath, points);
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
				selectedNodes.clear(); //selected points could be in two maps at once, causing issues later if all of them are edited
			}
		});

		// {{ Help Menu Item
		JMenu mnHelp = new JMenu("Help"); // Help
		menuBar.add(mnHelp);
		
		ArrayList<JMenuItem> mnHelpList = new ArrayList<JMenuItem>();
		mnHelpList.add(new JMenuItem("About"));
		mnHelp.add(mnHelpList.get(0));
		
		mnHelpList.get(0).addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {			
				Popup popup = new Popup(new Color(95, 172, 213));
				popup.setToAbout();
				popup.setVisible(true);
			}
		});
		// }} Help
		
		// This is code for the panel showing information on the current selected node.
		JPanel nodeInfoPanel = new JPanel();
		nodeInfoPanel.setBounds(920, 203, 294, 136);
		nodeInfoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nodeInfoPanel.setLayout(null);

		// Labels
		JLabel lblNodeInformation = new JLabel(" Node Information");
		lblNodeInformation.setFont(new Font("Tahoma", Font.PLAIN, 15));
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

		// Sets x and y position in pixels and z position in feet for the last clicked node from the z position text field.
		btnMakeChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(lastClicked != null) {
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
			}
		});

		// Sets the z position in feet for all currently selected nodes from the z position text field.
		JButton btnNewButton = new JButton("Set Selected Z-Feet");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode n: selectedNodes) {
					n.setZFeet(Double.parseDouble(zPosField.getText()));
				}

			}
		});
		btnNewButton.setBounds(132, 106, 155, 25);
		nodeInfoPanel.add(btnNewButton);

		// This is code for the panel with the radio buttons for the cursor options
		JPanel cursorPanel = new JPanel();
		cursorPanel.setBounds(920, 10, 130, 183);
		cursorPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		cursorPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel_1 = new JLabel(" Cursor Options");
		cursorPanel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));

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

		// This pertains to the panel holding the map panel.
		panel = new JPanel();
		panel.setLocation(10, 13);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(Color.WHITE);

		panel.setLayout(new BorderLayout(0, 0));
		panel.setBounds(10,10,900, 600);
		panel.add(mapPanel);
		getContentPane().add(panel);
		getContentPane().add(cursorPanel);
		getContentPane().add(nodeInfoPanel);

		// This is the panel holding the extra map panel for two map view
		panel2 = new JPanel();
		panel2.setLocation(465, 10);
		panel2.setSize(445, 600);
		panel2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel2.setBackground(Color.WHITE);
		panel2.setLayout(new BorderLayout(0, 0));
		panel2.setBounds(465, 10, 445, 600);
		mapPanel2.setBounds(2, 2, 443, 598);
		panel2.add(mapPanel2);

		//This is a panel that holds all the options relating to node attributes
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(920, 349, 294, 312);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel = new JLabel(" Attributes");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(2, 2, 107, 16);
		panel_1.add(lblNewLabel);

		chckbxBikeable = new JCheckBox("Bikeable");
		chckbxBikeable.setBounds(8, 215, 113, 25);
		panel_1.add(chckbxBikeable);

		chckbxHandicapped = new JCheckBox("Handicapped accessible");
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


		// This sets the attributes of a special node such that new nodes will be initialized with this node's attributes.
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

		//Edits the name of the last clicked node
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

		// These define the check boxes to change the status of all currently selected nodes when checked or unchecked to reflect this current status.
		chckbxStairs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode n : selectedNodes) {
					n.getAttributes().setStairs(chckbxStairs.isSelected());
				}
			}
		});

		chckbxBikeable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode n : selectedNodes) {
					n.getAttributes().setBikeable(chckbxBikeable.isSelected());
				}
			}
		});

		chckbxPOI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode n : selectedNodes) {
					n.getAttributes().setPOI(chckbxPOI.isSelected());
				}
			}
		});

		chckbxOutside.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode n : selectedNodes) {
					n.getAttributes().setOutside(chckbxOutside.isSelected());
				}
			}
		});

		chckbxHandicapped.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode n : selectedNodes) {
					n.getAttributes().setHandicapped(chckbxHandicapped.isSelected());
				}
			}
		});

		// This sets the type of all currently selected nodes, using the enumerated type Types.
		typeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(selectedNodes.size() > 1) { //If size is 1, need to set the type using the code below.
					for(MapNode n : selectedNodes) {
						n.getAttributes().setType((Types) typeBox.getSelectedItem());
					}
				}
				else if(selectedNodes.size() == 1) {
					selectedNodes.get(0).getAttributes().setType((Types) typeBox.getSelectedItem());
				}
			}
		});



		// This is code for a panel with options that select nodes by tope, or act on all selected nodes.
		JPanel highlightPanel = new JPanel();
		highlightPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		highlightPanel.setBounds(1062, 10, 151, 183);
		getContentPane().add(highlightPanel);
		highlightPanel.setLayout(null);

		JLabel lblHighlight = new JLabel(" Select nodes with:");
		lblHighlight.setBounds(2, 31, 137, 16);
		highlightPanel.add(lblHighlight);


		String[] attributeSelectedOptions = new String[] {"foodLocation", "office", "classRoom", 
				"waterFountain", "bathRoom", "parking", "walking", "door",
				"elevator", "laboratory", "other", "is Stairs", "is Bikeable", "is Accessible", 
				"is Outside", "is POI"};
		JComboBox attributeSelected = new JComboBox(attributeSelectedOptions);
		attributeSelected.setSelectedIndex(-1);
		attributeSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.selectAllNodes(attributeSelected.getSelectedIndex());
			}
		});
		attributeSelected.setBounds(5, 54, 137, 22);
		highlightPanel.add(attributeSelected);

		JButton btnMakeEdges = new JButton("Connect Edges");
		btnMakeEdges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(MapNode n: selectedNodes) {
					for(MapNode m : selectedNodes) {
						n.addNeighbor(m);
					}
				}
			}
		});
		btnMakeEdges.setBounds(5, 89, 134, 32);
		highlightPanel.add(btnMakeEdges);

		JButton btnLinear = new JButton("Align Nodes");
		btnLinear.setBounds(5, 134, 134, 32);
		highlightPanel.add(btnLinear);

		JLabel lblSelectionOptions = new JLabel(" Selection Options");
		lblSelectionOptions.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSelectionOptions.setBounds(2, 2, 121, 16);
		highlightPanel.add(lblSelectionOptions);

		btnLinear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.linearizeNodes();
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
					MapNode n = new MapNode((double) p.x - offset.x - mapPanel.getXOffset(), (double) p.y - offset.y - mapPanel.getYOffset(), local1); // make a new mapnode with those points
					Graphics g = mapPanel.getGraphics();

					points.add(n);
					n.setDefaultAttributes(defaultAttributes);
					setInfoFields(n);
					lastClicked = n;
					selectedNodes.clear();
					selectedNodes.add(n);
					mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
					if(twoMapView) {
						Graphics g2 = mapPanel2.getGraphics();
						mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
					}
				} 
				else if (rdbtnSelectNode.isSelected()){
					edgeStarted = false;
					edgeRemovalStarted = false;

					if(!me.isControlDown())
						selectedNodes.clear();
					if(me.isShiftDown())
						multiSelect = true;
					else
						multiSelect = false;

					clickMiss = true; //If the for-loop finishes and this is not changed to false, the info fields will be cleared and no nodes will be shown as clicked.
					for(MapNode n : points){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX() - mapPanel.getXOffset()) <= threshold) && 
								(Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY() - mapPanel.getYOffset()) <= threshold )){

							setInfoFields(n);
							clickMiss = false;
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
										if(!selectedNodes.contains(mn))
											selectedNodes.add(mn);
									}
								}
							}

							lastClicked = n;

							if(!selectedNodes.contains(n))
								selectedNodes.add(n);

						} //end the threshold selection if

					}
					if(clickMiss == true) {
						clearInfoFields();
					}
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
					if(twoMapView) {
						Graphics g2 = mapPanel2.getGraphics();
						mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
					}
				}
				else if(rdbtnMakeEdge.isSelected()) {
					edgeRemovalStarted = false;

					if(edgeStarted == false) {
						for(MapNode n : points){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX() - mapPanel.getXOffset()) <= threshold) 
									&& (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY() - mapPanel.getYOffset()) <= threshold )){

								edgeStart = n;
								edgeStarted = true;
								setInfoFields(n);							
								lastClicked = n;
								selectedNodes.clear();
								selectedNodes.add(n);
								Graphics g = mapPanel.getGraphics();
								mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
								if(twoMapView) {
									Graphics g2 = mapPanel2.getGraphics();
									mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
								}
							}
						}

					}
					else {
						for(MapNode n : points) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

							if((Math.abs(me.getLocationOnScreen().getX() - mapPanel.getXOffset() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY()- mapPanel.getYOffset() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								edgeStarted = false;
								n.addNeighbor(edgeStart);
								edgeStart.addNeighbor(n);
								setInfoFields(n);							
								lastClicked = n;
								selectedNodes.clear();
								selectedNodes.add(n);
								mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
								if(twoMapView) {
									Graphics g2 = mapPanel2.getGraphics();
									mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
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
							if((Math.abs(me.getLocationOnScreen().getX()- mapPanel.getXOffset() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY()- mapPanel.getYOffset() - offset.y - tmp.getY()) <= threshold )){
								edgeRemove = n;
								edgeRemovalStarted = true;
								setInfoFields(n);							
								lastClicked = n;
								selectedNodes.clear();
								selectedNodes.add(n);
								Graphics g = mapPanel.getGraphics();
								mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
								if(twoMapView) {
									Graphics g2 = mapPanel2.getGraphics();
									mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
								}
							}
						}

					}
					else {
						for(MapNode n : points) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX()- mapPanel.getXOffset() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY()- mapPanel.getYOffset() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								edgeRemovalStarted = false;
								n.removeNeighbor(edgeRemove);
								edgeRemove.removeNeighbor(n);
								setInfoFields(n);							
								lastClicked = n;
								selectedNodes.clear();
								selectedNodes.add(n);
								mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
								if(twoMapView) {
									Graphics g2 = mapPanel2.getGraphics();
									mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
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

						if((Math.abs(me.getLocationOnScreen().getX()- mapPanel.getXOffset() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY()- mapPanel.getYOffset() - offset.y - tmp.getY()) <= threshold )){


							nodeToRemove = n;
						}
					}
					for(int i=0; i<nodeToRemove.getNeighbors().size(); i++) { //Remove edges from the neighbors to the node to remove
						nodeToRemove.getNeighbors().get(i).removeNeighbor(nodeToRemove);
					}
					//Remove edges from the node to remove to its neighbors.
					nodeToRemove.getNeighbors().removeIf((MapNode q)->q.getXPos() > -1000000000); 
					points.remove(nodeToRemove);
					lastClicked = null;
					selectedNodes.clear();
					clearInfoFields();
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
					if(twoMapView) {
						Graphics g2 = mapPanel2.getGraphics();
						mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
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
					MapNode n = new MapNode((double) p.x - offset.x - mapPanel2.getXOffset(), (double) p.y - offset.y - mapPanel2.getYOffset(), local2); // make a new mapnode with those points
					Graphics g = mapPanel.getGraphics();
					Graphics g2 = mapPanel2.getGraphics();
					points2.add(n);
					n.setDefaultAttributes(defaultAttributes);
					setInfoFields(n);
					lastClicked = n;
					selectedNodes.clear();
					selectedNodes.add(n);
					mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
					mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
				} 
				else if (rdbtnSelectNode.isSelected()){
					edgeStarted = false;
					edgeRemovalStarted = false;

					if(!me.isControlDown())
						selectedNodes.clear();
					if(me.isShiftDown())
						multiSelect = true;
					else
						multiSelect = false;

					clickMiss = true;

					for(MapNode n : points2){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX() - mapPanel2.getXOffset()) <= threshold) && 
								(Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY() - mapPanel2.getYOffset()) <= threshold )){

							setInfoFields(n);
							clickMiss = false;

							if(multiSelect){
								MapNode lastPoint = lastClicked;
								MapNode currentPoint  = n;
								double minX = Math.min(currentPoint.getXPos(), lastPoint.getXPos());
								double maxX = Math.max(currentPoint.getXPos(), lastPoint.getXPos());
								double minY = Math.min(currentPoint.getYPos(), lastPoint.getYPos());
								double maxY = Math.max(currentPoint.getYPos(), lastPoint.getYPos());
								for(MapNode mn : points2){
									if(mn.getXPos() >= minX && mn.getXPos() <= maxX &&
											mn.getYPos() >= minY && mn.getYPos() <= maxY){
										if(!selectedNodes.contains(mn))
											selectedNodes.add(mn);
									}
								}
							}

							lastClicked = n;

							if(!selectedNodes.contains(n))
								selectedNodes.add(n);
						}
					}

					if(clickMiss == true) {
						clearInfoFields();
					}
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
					if(twoMapView) {
						Graphics g2 = mapPanel2.getGraphics();
						mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
					}
				}
				else if(rdbtnMakeEdge.isSelected()) {
					edgeRemovalStarted = false;

					if(edgeStarted == false) {
						for(MapNode n : points2){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - mapPanel2.getXOffset() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - mapPanel2.getYOffset() - offset.y - tmp.getY()) <= threshold )){
								edgeStart = n;
								edgeStarted = true;
								setInfoFields(n);
								lastClicked = n;
								selectedNodes.clear();
								selectedNodes.add(n);
								Graphics g = mapPanel.getGraphics();
								Graphics g2 = mapPanel2.getGraphics();
								mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
								mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
							}
						}

					}
					else {
						for(MapNode n : points2) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - mapPanel2.getXOffset() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - mapPanel2.getYOffset() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								Graphics g2 = mapPanel2.getGraphics();
								edgeStarted = false;
								n.addNeighbor(edgeStart);
								edgeStart.addNeighbor(n);
								setInfoFields(n);
								lastClicked = n;
								selectedNodes.clear();
								selectedNodes.add(n);
								mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
								mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
							}
						}
					}
				}
				else if(rdbtnRemoveEdge.isSelected()) {
					edgeStarted = false;
					if(edgeRemovalStarted == false) {
						for(MapNode n : points2){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - mapPanel2.getXOffset() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - mapPanel2.getYOffset() - offset.y - tmp.getY()) <= threshold )){
								edgeRemove = n;
								edgeRemovalStarted = true;
								setInfoFields(n);
								lastClicked = n;
								selectedNodes.clear();
								selectedNodes.add(n);
								Graphics g = mapPanel.getGraphics();
								Graphics g2 = mapPanel2.getGraphics();
								mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
								mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
							}
						}

					}
					else {
						for(MapNode n : points2) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - mapPanel2.getXOffset() - offset.x - tmp.getX()) <= threshold) 
									&& (Math.abs(me.getLocationOnScreen().getY() - mapPanel2.getYOffset() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								Graphics g2 = mapPanel2.getGraphics();
								edgeRemovalStarted = false;
								n.removeNeighbor(edgeRemove);
								edgeRemove.removeNeighbor(n);
								setInfoFields(n);
								lastClicked = n;
								selectedNodes.clear();
								selectedNodes.add(n);
								mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
								mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
							}
						}
					}

				}
				else if(rdbtnRemoveNode.isSelected()) {
					edgeStarted = false;
					edgeRemovalStarted = false;
					for(MapNode n : points2){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - mapPanel2.getXOffset() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - mapPanel2.getYOffset() - offset.y - tmp.getY()) <= threshold )){
							nodeToRemove = n;
						}
					}
					
					for(int i=0; i<nodeToRemove.getNeighbors().size(); i++) { //Remove edges from the neighbors to the node to remove
						nodeToRemove.getNeighbors().get(i).removeNeighbor(nodeToRemove);
					}
					
					nodeToRemove.getNeighbors().removeIf((MapNode q)->q.getXPos() > -1000000000); //Remove edges from the node to its neighbors.
					points2.remove(nodeToRemove);
					lastClicked = null;
					selectedNodes.clear();
					clearInfoFields();
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderMapPublic(g, points, selectedNodes, lastClicked);
					if(twoMapView) {
						Graphics g2 = mapPanel2.getGraphics();
						mapPanel2.renderMapPublic(g2, points2, selectedNodes, lastClicked);
					}
				}
			}
		}); 

		mapPanel2.setBackground(Color.WHITE);
	}

	//The info fields on the right  are set to reflect the attributes and location of the point passed as a parameter.
	private void setInfoFields(MapNode n) {
		xPosField.setText(""+n.getXPos());
		yPosField.setText(""+n.getYPos());
		zPosField.setText(""+n.getZFeet());
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

	//All info fields on the right hand side of the GUI are cleared.
	private void clearInfoFields() {
		xPosField.setText("");
		yPosField.setText("");
		zPosField.setText("");
		typeBox.setSelectedItem(-1);
		chckbxStairs.setSelected(false);
		chckbxHandicapped.setSelected(false);
		chckbxOutside.setSelected(false);
		chckbxPOI.setSelected(false);
		chckbxBikeable.setSelected(false);
		txtrAliases.setText("");
		textFieldOfficialName.setText("");
	}

	//This function uses projection to align points on a line determined by two of the points. Called by "Linearize" button to line up a bunch of selected points.
	private void linearizeNodes() {
		double xSlope, ySlope, scaledLength, squaredHypotenuse, dotProduct, point1X, point2X, point1Y, point2Y;
		if(!twoMapView) {
			MapNode[] extremePoints = new MapNode[2];
			extremePoints = findExtremes(); //The two nodes in extremePoints determine the line L.
			if (extremePoints != null) {
				MapNode point1 = extremePoints[0];
				MapNode point2 = extremePoints[1];
				point1X = point1.getXPos(); //Save the two extreme points.
				point2X = point2.getXPos();
				point1Y = point1.getYPos();
				point2Y = point2.getYPos();
				xSlope = point2X - point1X; //Determine the slope of the line L.
				ySlope = point2Y - point1Y;
				squaredHypotenuse = xSlope*xSlope + ySlope*ySlope; //Take modulus of L, or the dot product with itself.
				for(MapNode node : selectedNodes) {
					node.setXPos(node.getXPos() - point1X); //Correction for line not passing through origin
					node.setYPos(node.getYPos() - point1Y);
					dotProduct = node.getXPos()*xSlope + node.getYPos()*ySlope; //Dot the vector u determined by a given node with a vector determining L.
					scaledLength = dotProduct / squaredHypotenuse; //Divide the two previous dot products, this is u*L/L*L.
					node.setXPos(scaledLength*xSlope + point1X); //The projection of u onto L from the origin is (u*L/L*L)L. Use this as an offset from the first point on the line.
					node.setYPos(scaledLength*ySlope + point1Y);
				}
			}
		}
		else {
			System.out.println("Switch to single map view first please."); 
		}
	}

	//Identify the two nodes that should determine the line the rest of the nodes are adjusted to fit. The leftmost and rightmost, or the highest and lowest, are used, whichever difference is greater.
	private MapNode[] findExtremes() {
		if(selectedNodes.size() < 3) { //Nothing to do if there are only one or two nodes.
			return null;
		}
		MapNode[] extremes = new MapNode[2];
		MapNode lowX = selectedNodes.get(0); //Start by setting all extremes to the first node in the list.
		MapNode highX = selectedNodes.get(0);
		MapNode lowY = selectedNodes.get(0);
		MapNode highY = selectedNodes.get(0);
		for(MapNode node : selectedNodes) { //When a node is farther left, right, up, or down than any previous node, set this as the extreme one.
			if(node.getXPos() < lowX.getXPos()) {
				lowX = node;
			}
			if(node.getXPos() > highX.getXPos()) {
				highX = node;
			}
			if(node.getYPos() < lowY.getYPos()) {
				lowY = node;
			}
			if(node.getYPos() > highY.getYPos()) {
				highY = node;
			}
		}
		if(highX.getXPos() - lowX.getXPos() >= highY.getYPos() - lowY.getYPos()) { //Determine whether to use the nodes farthest left and right, or up and down, choose the larger difference.
			extremes[0] = lowX;
			extremes[1] = highX;
		}
		else {
			extremes[0] = lowY;
			extremes[1] = highY;
		}
		return extremes;
	}

	//Selects all nodes with a certain attribute, determined by the index parameter.
	private void selectAllNodes(int index) {
		Types typeSelected;
		selectedNodes.clear();
		clearInfoFields();
		if(index < 11 && index > -1) {
			typeSelected = Types.OTHER;
			switch(index) {
			case 0:
				typeSelected = Types.FOOD;
				break;
			case 1:
				typeSelected = Types.OFFICE;
				break;
			case 2:
				typeSelected = Types.CLASSROOM;
				break;
			case 3:
				typeSelected = Types.WATERFOUNTAIN;
				break;
			case 4:
				typeSelected = Types.BATHROOM;
				break;
			case 5:
				typeSelected = Types.PARKING;
				break;
			case 6:
				typeSelected = Types.WALKING;
				break;
			case 7:
				typeSelected = Types.DOOR;
				break;
			case 8:
				typeSelected = Types.ELEVATOR;
				break;
			case 9:
				typeSelected = Types.LAB;
				break;
			case 10:
				typeSelected = Types.OTHER;
				break;
			}
			for(MapNode node : points) {
				if(node.getAttributes().getType().equals(typeSelected)) {
					selectedNodes.add(node);
				}
			}

		}
		else if(index >= 11) {
			switch(index) {
			case 11:
				for(MapNode node : points) {
					if(node.getAttributes().isStairs()) {
						selectedNodes.add(node);
					}
				}
				break;
			case 12:
				for(MapNode node : points) {
					if(node.getAttributes().isBikeable()) {
						selectedNodes.add(node);
					}
				}
				break;
			case 13:
				for(MapNode node : points) {
					if(node.getAttributes().isHandicapped()) {
						selectedNodes.add(node);
					}
				}
				break;
			case 14:
				for(MapNode node : points) {
					if(node.getAttributes().isOutside()) {
						selectedNodes.add(node);
					}
				}
				break;
			case 15:
				for(MapNode node : points) {
					if(node.getAttributes().isPOI()) {
						selectedNodes.add(node);
					}
				}
				break;
			}
		}
	}
}
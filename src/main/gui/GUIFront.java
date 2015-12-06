package main.gui;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
<<<<<<< HEAD
=======
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;

import main.*;
import main.gui.frontutil.*;
import main.util.Constants;
import main.util.Speaker;
import main.util.WrappableCellRenderer;

import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.JLayeredPane;

/** This class contains code for the main applications GUI interface as well as
 *  implementation for its various functionality such as drawing the route.
 * 
 *  Tween code adapted from Aurelien Ribon's Sliding Layout Demo 
 * 
 *  @author Trevor
 */
@SuppressWarnings("serial")
public class GUIFront extends JFrame {

	private static GUIBack backend;
	private static GlobalMap globalMap;
	private static boolean setStart = false; // keeps track of whether you have set a start or end node yet
	private static boolean setEnd = false;
	private static boolean drawLine = false;
	private static boolean removeLine = false;
	private static MapNode startNode = null, endNode = null;
	private static String allText = "";
	private static ArrayList<MapNode> mapnodes = new ArrayList<MapNode>();
	private static ArrayList<ArrayList<MapNode>> paths = new ArrayList<ArrayList<MapNode>>();
	private static ArrayList<ArrayList<MapNode>> routes = new ArrayList<ArrayList<MapNode>>();
	private static JButton btnClear;
	private static JButton btnPreviousMap;
	private static JButton btnNextMap;
	private static boolean allowSetting = true; 
	private static int index = 0;
	private static ArrayList<MapNode> thisRoute;

	private static AffineTransform transform; // the current state of image transformation
	private static Point2D mainReferencePoint; // the reference point indicating where the click started from during transformation
	private static PanHandler panHandle;
	private static ZoomHandler zoomHandle;

	// MapPanel components
	private static JTextField textFieldEnd, textFieldStart;
	private static JLabel searchLabelStart;
	private static JLabel searchLabelEnd;

	// Directions Components
	private static JLabel labelStepByStep, labelClickHere, labelDistance;
	private static JScrollPane scrollPane;
	private static boolean currentlyOpen = false; // keeps track of whether the panel is slid out or not
<<<<<<< HEAD
	private static DefaultListModel<String> listModel = new DefaultListModel<String>(); // Setup a default list of elements
	private static ListCellRenderer renderer;
=======
	private DefaultListModel<String> listModel = new DefaultListModel<String>(); // Setup a default list of elements
	private ListCellRenderer renderer;
	private int MAX_LIST_WIDTH = 180; // maximum width of the list in pixels, the size of panelDirections is 200px
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa
	private static JList<String> listDirections;

	// Menu Bar
	private JMenuBar menuBar;
	private JMenu mnFile, mnOptions, mnHelp, mnLocations;
	private JMenu mnAtwaterKent, mnBoyntonHall, mnCampusCenter, mnGordonLibrary, mnHigginsHouse, mnHigginsHouseGarage, mnProjectCenter, mnStrattonHall;
	private JMenuItem mntmAK1, mntmAK2, mntmAK3, mntmAKB, mntmBoy1, mntmBoy2, mntmBoy3, mntmBoyB, mntmCC1, mntmCC2, mntmCC3, mntmCCM;
	private JMenuItem mntmGL1, mntmGL2, mntmGL3, mntmGLB, mntmGLSB, mntmHH1, mntmHH2, mntmHH3, mntmHHG1, mntmHHG2, mntmPC1, mntmPC2;
	private JMenuItem mntmSH1, mntmSH2, mntmSH3, mntmSHB, mntmEmail, mntmExit;

	private static SLPanel slidePanel;
	private static ArrayList<TweenPanel> panels = new ArrayList<TweenPanel>();
	private static TweenPanel panelMap, panelDirections;
	private static SLConfig mainConfig;
	private static SLConfig panelDirectionsConfig;
	private static JTabbedPane mainTabPane;
	private static JTextArea txtAreaDirections;

	/** @description Create the frame.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public GUIFront(File[] localMapFilenames) throws IOException, ClassNotFoundException {

		// initialize GUIFront
		ArrayList<MapNode> allNodes = new ArrayList<MapNode>();
		GUIFront.init(localMapFilenames, allNodes);

		// Maximizes JFrame on start
		setTitle("Era of Navigation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 820, 699);
		setResizable(false);
		setPreferredSize(new Dimension(820, 650));

		// Initializes Panning and Zooming
		setPanHandle(new PanHandler());
		setZoomHandle(new ZoomHandler());		

		// Initializes Menu Bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		initializeMenuBar();

		// Initialize Main User Pane
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Image of the default map loaded into backend
		String defaultMapImage = Constants.DEFAULT_MAP_IMAGE;
		Image mapImage = new ImageIcon(Constants.IMAGES_PATH + "/" + defaultMapImage).getImage();
		JLabel labelInvalidEntry = new JLabel("Invalid Entry");
		labelInvalidEntry.setVisible(false);

		// action listener for searching in the start text box
		Action searchStartAction = new AbstractAction(){
			@Override 
			public void actionPerformed(ActionEvent e){
				labelInvalidEntry.setVisible(false);
				if (textFieldStart.getText().equals("")){
					//will need some way to alert the user that they need to enter a start location
					System.out.println("Need to enter a valid start location");
				} 
				else if (!(textFieldStart.getText().equals(""))) {//if there is something entered check if the name is valid and then basically add the start node
					String startString = textFieldStart.getText();
					boolean valid = false;
					for (MapNode mapnode : getBackend().getLocalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
						if(startString.equals(mapnode.getAttributes().getOfficialName()) || mapnode.getAttributes().getAliases().contains(startString)){
							//if the startString is equal to the official name of the startString is one of a few accepted alias' we will allow the start node to be placed
							startNode = mapnode; //set the startNode and place it on the map
							getGlobalMap().setStartNode(startNode);
							if (!setStart) {
								getGlobalMap().getChosenNodes().add(0, startNode);
							} 
							else {
								getGlobalMap().getChosenNodes().set(0, startNode);
							}
							setStart = true;
							valid = true;
						}
					}
					if (valid == false){
						//tell user this entry is invalid
						System.out.println("Invalid entry");
						labelInvalidEntry.setVisible(true);
					}
				}	
			}
		};

		// action listener for searching in the end text box
		Action searchEndAction = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){

				labelInvalidEntry.setVisible(false);

				//if the user presses enter without having entered anything in this box
				if (textFieldEnd.getText().equals(""))
					System.out.println("Need to enter a valid start location"); // TODO: will need some way to alert the user that they need to enter an end location
				//if there is something entered check if the name is valid and then basically add the end node
				else if (! (textFieldEnd.getText().equals(""))) { 
					String endString = textFieldEnd.getText(); //entered text = endString constant
					boolean valid = false;
					Attributes attribute = new Attributes(); //will most likely need some other way of obtaining this information

					//Test if the entered information is a valid node in local map - this will be updated to global map when that is finished
					MapNode n = allNodes.get(0);
					if (startNode == null){
						startNode = n;

						getGlobalMap().setStartNode(startNode);
						if (!setStart)
							getGlobalMap().getChosenNodes().add(0, startNode);
						else 
							getGlobalMap().getChosenNodes().set(0, startNode);
					}

					for (MapNode mapnode : getBackend().getLocalMap().getMapNodes()){

						// if endstring is the official name or one of a few different accepted aliases we will allow the end node to be placed
						if(endString.equals(mapnode.getAttributes().getOfficialName()) || mapnode.getAttributes().getAliases().contains(endString)){

							endNode = mapnode;

							getGlobalMap().setEndNode(endNode);
							if (!setEnd)
								getGlobalMap().getChosenNodes().add(1, endNode);
							else
								getGlobalMap().getChosenNodes().set(1, endNode);

							setEnd = true;
							valid = true;
						} 
					}

					// check if the entry in the text field is an attribute not an official name
					if (attribute.getPossibleEntries().containsKey(endString)){ 
						String findNearestThing = attribute.getPossibleEntries().get(endString);

						//if there is no valid start node, this cannot be done - why? because you need a valid start node to find the closest node with the given attribute
						if(startNode != null){ 
							valid = true;
							MapNode node = getBackend().findNearestAttributedNode(findNearestThing, startNode); //same idea as findNearestNode - just finds the nearest node to the startnode that gives the entered attribute

							if (node != null){ //if no node was found, you should not place a node on the map otherwise do it 
								endNode = node;

								getGlobalMap().setEndNode(endNode);
								if (!setEnd)
									getGlobalMap().getChosenNodes().add(1, endNode);
								else
									getGlobalMap().getChosenNodes().set(1, endNode);

								setEnd = true;
							}
						} 
						//if there is something entered in the start field as well as the end field we can go ahead and place both at the same time...
						else if(!(textFieldStart.getText().equals(""))){ 
							String startString = textFieldStart.getText();
							for (MapNode mapnode : getBackend().getLocalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
								if(startString.equals(mapnode.getAttributes().getOfficialName())){
									startNode = mapnode; //set the startNode and then draw it on the map
									getGlobalMap().setStartNode(startNode);
									if (!setStart) {
										getGlobalMap().getChosenNodes().add(0, startNode);
									} 
									else {
										getGlobalMap().getChosenNodes().set(0, startNode);
									}
									setStart = true;
								}
							}

							//make sure that the startNode value is still not null, otherwise this won't work if it is
							if (startNode != null){ 
								MapNode node = getBackend().findNearestAttributedNode(findNearestThing, startNode); //same idea as findNearestNode - just finds the nearest node to the startnode that gives the entered attribute
								if (node != null){ //if no node was found, you should not do this and return an error, else do the following 
									endNode = node; //set the end node and place that node on the map
									getGlobalMap().setEndNode(endNode);
									if (!setEnd) {
										getGlobalMap().getChosenNodes().add(1, endNode);
									} 
									else {
										getGlobalMap().getChosenNodes().set(1, endNode);
									}
									setEnd = true;
									valid = true;
								}
							}
						}
					}

					if (valid == false){
						// TODO: tell user this entry is invalid
						System.out.println("Invalid entry");
						labelInvalidEntry.setVisible(true);
					}
				}
			}
		};

		// GroupLayout code for tabbedpane and textfields (Temporary)
		mainTabPane = new JTabbedPane();
		mainTabPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		// add search action listeners to the text fields
		textFieldStart = new JTextField();
		textFieldStart.setText("");
		textFieldStart.addActionListener(searchStartAction); //give start text field an action

		textFieldEnd = new JTextField("");
		textFieldEnd.setColumns(10);		
		textFieldEnd.addActionListener(searchEndAction); //give end text field an action

		// add search labels above text boxes
		searchLabelStart = new JLabel("Starting Location");
		searchLabelStart.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchLabelEnd = new JLabel("Ending Location");
		searchLabelEnd.setFont(new Font("Tahoma", Font.PLAIN, 12));

		// Clear button will call all of the reset code
		setBtnClear(new JButton("Clear All"));
		getBtnClear().setEnabled(false);
		getBtnClear().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				reset();
			}
		});

		// Button for Routing a Path
		JButton buttonRoute = new JButton("Route");
		buttonRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (buttonRoute.isEnabled()) {
					setAllowSetting(false); //once calculate button is pressed user should not be allowed to replace nodes until the original line is removed
					setAllText(""); //must set the initial text as empty every time calculate button is pressed

					// play sound
					Speaker speaker = new Speaker(Constants.BUTTON_PATH);
					speaker.play();
<<<<<<< HEAD

=======
					System.out.println("Start node: " + globalMap.getStartNode().getNodeID());
					System.out.println("End node: " + globalMap.getEndNode().getNodeID());
					System.out.println("Number of nodes in globalMap: " + globalMap.getMapNodes().size());
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa
					routes = backend.getMeRoutes(globalMap.getStartNode(), globalMap.getEndNode(), globalMap);
					if (routes.isEmpty()){
						mapnodes = backend.runAStar(backend.getLocalMap().getStartNode(), backend.getLocalMap().getEndNode());
					} 
					else {
						for (int i = 0; i < routes.size(); i++){
							LocalMap localmap = routes.get(i).get(0).getLocalMap();
							if (localmap.getEndNode() == null){
								int size = routes.get(i).size() - 1;
								localmap.setStartNode(routes.get(i).get(size));
							}

							if (localmap.getStartNode() == null){
								localmap.setEndNode(routes.get(i).get(0));
							}
							ArrayList<MapNode> route = routes.get(i);
							getPaths().add(route);
						}
					}

					setThisRoute(routes.get(0));
					panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getPaths().get(0).get(0).getLocalMap().getMapImageName()).getImage());
					panelMap.setMapNodes(getPaths().get(0).get(0).getLocalMap().getMapNodes());
					getBackend().setLocalMap(getPaths().get(0).get(0).getLocalMap());
					index = 0;
<<<<<<< HEAD

					if (paths.size() > 1)
						btnNextMap.setEnabled(true);

					drawLine = true; //draw the line on the map

=======
					if (paths.size() > 1){
						btnNextMap.setEnabled(true);
					}
					//basically waypoint stuff -- find a path between every node in the chosenNodes list of mapnodes
					/*for(int i = 0; i < globalMap.getChosenNodes().size() - 1; i++){
						ArrayList<MapNode> wayPoints = new ArrayList<MapNode>();
						//wayPoints = backend.getMeRoutes(panelMap.chosenNodes.get(i), panelMap.chosenNodes.get(i + 1));
						wayPoints = backend.runAStar(globalMap.getChosenNodes().get(i), globalMap.getChosenNodes().get(i + 1));
						paths.add(wayPoints);
					}*/
					//draw the line on the map
					drawLine = true;
					//set the initial distance as 0 
					int distance = 0;
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa
					//update the step by step directions and distance for each waypoint added
					int distance = 0; //set the initial distance as 0 
					for (ArrayList<MapNode>wayPoints : getPaths()){
						String all = "";

						distance += backend.getDistance(wayPoints);
						for (String string : backend.displayStepByStep(wayPoints)) {
							listModel.addElement(string); // add it to the list model
						}
						setAllText(getAllText() + all + "\n");
					}

<<<<<<< HEAD
					getTxtAreaDirections().setText(getAllText());

					getLabelDistance().setText("Distance in feet:" + distance);
					getBtnClear().setEnabled(true);
				}
			}
		});
=======
					lblDistance.setText("Distance in feet:" + distance);
					//this sets the textarea with the step by step directions
					//textArea1.setText(allText);
					//btnRoute.setEnabled(false);
					btnClear.setEnabled(true);
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

		// initialize panels
		GUIFront.initPanels(mapImage);

		// TODO probably remove
		addMouseListener(getPanHandle());
		addMouseMotionListener(getPanHandle());
		addMouseWheelListener(getZoomHandle());

		getContentPane().add(btnNextMap, BorderLayout.SOUTH);
		getContentPane().add(btnPreviousMap, BorderLayout.SOUTH);
		getContentPane().add(mainTabPane);

		// Group Layout code for all components
		GUIFront.groupLayout(contentPane, labelInvalidEntry, buttonRoute);
		pack();
		setVisible(true);
	}

	/** @description Initializes the menu bar with various options.
	 * 
	 */
	public void initializeMenuBar(){
		// ---- File Menu ----
		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmEmail = new JMenuItem("Email"); // Code to open up the email sender
		mntmEmail.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				EmailGUI newEmail = new EmailGUI();
				newEmail.setVisible(true); //Opens EmailGUI Pop-Up
			}
		});
		mntmExit = new JMenuItem("Exit"); // terminates the session, anything need to be saved first?
		mntmExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0); 
			}
		});
		mnFile.add(mntmEmail);
		mnFile.add(mntmExit);

<<<<<<< HEAD
		// ---- Options -----
		mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		// ---- Locations -----
		mnLocations = new JMenu("Locations");
		menuBar.add(mnLocations);

		// Atwater Kent
		mnAtwaterKent = new JMenu("Atwater Kent");
		mntmAK1 = new JMenuItem("Floor 1");
		mntmAK1.addActionListener(new ActionListener(){
=======
		/**
		 * Tween related code to make the animations work
		 */
		slidePanel = new SLPanel();
		panelMap = new TweenPanel(backend.getLocalMap().getMapNodes(), mapPath, "1");
		panels.add(panelMap);
		panelDirections = new TweenPanel("2");

		/**
		 * Adding new components onto the Step By Step slideout panel
		 */
		JPanel stepByStepUI = new JPanel();

		lblClickHere = new JLabel("<<<");
		lblClickHere.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblClickHere.setVisible(true);
		stepByStepUI.add(lblClickHere);

		lblStepByStep = new JLabel("Step by Step Directions!");
		lblStepByStep.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblStepByStep.setBounds(23, 11, 167, 14);
		lblStepByStep.setVisible(false);
		stepByStepUI.add(lblStepByStep);

		scrollPane = new JScrollPane();
		scrollPane.setSize(new Dimension(180, 400));
		scrollPane.setVisible(false);
		stepByStepUI.add(scrollPane);

		// Create a new list and be able to get the current width ofthe viewport it is contained in (the scrollpane)
		renderer = new WrappableCellRenderer(MAX_LIST_WIDTH / 7); // 7 pixels per 1 character
			
		listDirections = new JList<String>(listModel);
		listDirections.setCellRenderer(renderer);
		listDirections.setFixedCellWidth(MAX_LIST_WIDTH); // give it a set width in pixels
		scrollPane.setViewportView(listDirections);
		listDirections.setVisible(false);
		listDirections.setVisibleRowCount(10); // only shows 10 directions before scrolling
		
		lblDistance = new JLabel();
		lblDistance.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDistance.setVisible(false);

		panelDirections.add(lblDistance, BorderLayout.SOUTH);
		panelDirections.add(stepByStepUI, BorderLayout.NORTH);

		// Set action to allow for sliding
		panelDirections.setAction(panelDirectionsAction);

		/**
		 * The configuration files describe what will take place for each animation. So by default we want the map larger 
		 * and the side panel very small. When we click the directions panel we want that to slide out, zoomRatio the map panel, and
		 * adjust the sizes
		 */
		mainConfig = new SLConfig(slidePanel)
		.gap(10, 10)
		.row(1f).col(700).col(50) // 700xH | 50xH
		.place(0, 0, panelMap)
		.place(0, 1, panelDirections);

		panelDirectionsConfig = new SLConfig(slidePanel)
		.gap(10, 10)
		.row(1f).col(550).col(200) // 550xH | 200xH
		.place(0, 0, panelMap)
		.place(0, 1, panelDirections);

		// Initialize tweening
		slidePanel.setTweenManager(SLAnimator.createTweenManager());
		slidePanel.initialize(mainConfig);

		// add to the tabbed pane
		mainPanel.add(slidePanel, BorderLayout.CENTER);
		getContentPane().add(mainPanel);
		btnNextMap = new JButton("Next Map -->");
		btnNextMap.setEnabled(false);
		btnNextMap.addActionListener(new ActionListener(){
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(0).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(0).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(0));
			}
		});
		mntmAK2 = new JMenuItem("Floor 2");
		mntmAK2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(1).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(1).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(1));
			}
		});
		mntmAK3 = new JMenuItem("Floor 3");
		mntmAK3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(2).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(2).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(2));
			}
		});
		mntmAKB = new JMenuItem("Basement");
		mntmAKB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(3).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(3).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(3));
			}
		});
		mnAtwaterKent.add(mntmAK1);
		mnAtwaterKent.add(mntmAK2);
		mnAtwaterKent.add(mntmAK3);
		mnAtwaterKent.add(mntmAKB);

		// Boynton Hall
		mnBoyntonHall = new JMenu("Boynton Hall");
		mntmBoy1 = new JMenuItem("Floor 1");
		mntmBoy1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(4).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(4).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(4));
			}
		});
		mntmBoy2 = new JMenuItem("Floor 2");
		mntmBoy2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(5).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(5).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(5));
			}
		});
		mntmBoy3 = new JMenuItem("Floor 3");
		mntmBoy3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(6).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(6).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(6));
			}
		});
		mntmBoyB = new JMenuItem("Basement");
		mntmBoyB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(7).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(7).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(7));
			}
		});
		mnBoyntonHall.add(mntmBoy1);
		mnBoyntonHall.add(mntmBoy2);
		mnBoyntonHall.add(mntmBoy3);
		mnBoyntonHall.add(mntmBoyB);

		// Campus Center
		mnCampusCenter = new JMenu("Campus Center");
		mntmCC1 = new JMenuItem("Floor 1");
		mntmCC1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(8).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(8).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(8));
			}
		});
		mntmCC2 = new JMenuItem("Floor 2");
		mntmCC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(9).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(9).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(9));
			}
		});
		mntmCC3 = new JMenuItem("Floor 3");
		mntmCC3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(10).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(10).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(10));
			}
		});
		mnCampusCenter.add(mntmCC1);
		mnCampusCenter.add(mntmCC2);
		mnCampusCenter.add(mntmCC3);

		// Campus Map
		mntmCCM = new JMenuItem("Campus Map");
		mntmCCM.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(11).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(11).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(11));
			}
		});

		// Gordon Library
		mnGordonLibrary = new JMenu("Gordon Library");
		mntmGL1 = new JMenuItem("Floor 1");
		mntmGL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(12).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(12).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(12));
			}
		});
		mntmGL2 = new JMenuItem("Floor 2");
		mntmGL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(13).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(13).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(13));
			}
		});
		mntmGL3 = new JMenuItem("Floor 3");
		mntmGL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(14).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(14).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(14));
			}
		});
		mntmGLB = new JMenuItem("Basement");
		mntmGLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(15).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(15).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(15));
			}
		});
		mntmGLSB = new JMenuItem("Sub Basement");
		mntmGLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(16).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(16).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(16));
			}
		});
		mnGordonLibrary.add(mntmGL1);
		mnGordonLibrary.add(mntmGL2);
		mnGordonLibrary.add(mntmGL3);
		mnGordonLibrary.add(mntmGLB);
		mnGordonLibrary.add(mntmGLSB);

		// Higgins House
		mnHigginsHouse = new JMenu("Higgins House");
		mntmHH1 = new JMenuItem("Floor 1");
		mntmHH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(17).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(17).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(17));
			}
		});
		mntmHH2 = new JMenuItem("Floor 2");
		mntmHH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(18).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(18).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(18));
			}
		});
		mntmHH3 = new JMenuItem("Floor 3");
		mntmHH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(19).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(19).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(19));
			}
		});
		mnHigginsHouse.add(mntmHH1);
		mnHigginsHouse.add(mntmHH2);
		mnHigginsHouse.add(mntmHH3);

		// Higgins House Garage
		mnHigginsHouseGarage = new JMenu("Higgins House Garage");
		mntmHHG1 = new JMenuItem("Floor 1");
		mntmHHG1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(20).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(20).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(20));
			}
		});
		mntmHHG2 = new JMenuItem("Floor 2");
		mntmHHG2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(21).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(21).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(21));
			}
		});
		mnHigginsHouseGarage.add(mntmHHG1);
		mnHigginsHouseGarage.add(mntmHHG2);

		// Project Center
		mnProjectCenter = new JMenu("Project Center");
		mntmPC1 = new JMenuItem("Floor 1");
		mntmPC1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(22).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(22).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(22));
			}
		});
		mntmPC2 = new JMenuItem("Floor 2");
		mntmPC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(23).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(23).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(23));
			}
		});
		mnProjectCenter.add(mntmPC1);
		mnProjectCenter.add(mntmPC2);

		// Stratton Hall
		mnStrattonHall = new JMenu("Stratton Hall");
		mntmSH1 = new JMenuItem("Floor 1");
		mntmSH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(24).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(24).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(24));
			}
		});
		mntmSH2 = new JMenuItem("Floor 2");
		mntmSH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(25).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(25).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(25));
			}
		});
		mntmSH3 = new JMenuItem("Floor 3");
		mntmSH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(26).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(26).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(26));
			}
		});
		mntmSHB = new JMenuItem("Basement");
		mntmSHB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getGlobalMap().getLocalMaps().get(27).getMapImageName()).getImage());
				panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(27).getMapNodes());
				getBackend().setLocalMap(getGlobalMap().getLocalMaps().get(27));
			}
		});
		mnStrattonHall.add(mntmSH1);
		mnStrattonHall.add(mntmSH2);
		mnStrattonHall.add(mntmSH3);
		mnStrattonHall.add(mntmSHB);

		mnLocations.add(mnAtwaterKent); // indices: 0, 1, 2, 3
		mnLocations.add(mnBoyntonHall); // indices: 4, 5, 6, 7
		mnLocations.add(mnCampusCenter);// indices: 8, 9, 10
		mnLocations.add(mntmCCM); // index: 11
		mnLocations.add(mnGordonLibrary); // indices: 11, 12, 13, 14, 15
		mnLocations.add(mnHigginsHouse); // indices: 16, 17, 18
		mnLocations.add(mnHigginsHouseGarage); //indices: 19, 20
		mnLocations.add(mnProjectCenter); // indices: 21, 22
		mnLocations.add(mnStrattonHall); // indices 23, 24, 25, 26

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
	}
<<<<<<< HEAD
=======
		
	// This goes in GUIFront
	public void initializeMenuBar(){
		// ---- File Menu ----
		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmEmail = new JMenuItem("Email"); // Code to open up the email sender
		mntmEmail.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				EmailGUI newEmail = new EmailGUI();
				newEmail.setVisible(true); //Opens EmailGUI Pop-Up
			}
		});
		mntmExit = new JMenuItem("Exit"); // terminates the session, anything need to be saved first?
		mntmExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0); 
			}
		});
		mnFile.add(mntmEmail);
		mnFile.add(mntmExit);

		// ---- Options -----
		mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		// ---- Options -----
		mnLocations = new JMenu("Locations");
		menuBar.add(mnLocations);

		// Atwater Kent
		mnAtwaterKent = new JMenu("Atwater Kent");
		mntmAK1 = new JMenuItem("Floor 1");
		mntmAK1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(0).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(0).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(0));
			}
		});
		mntmAK2 = new JMenuItem("Floor 2");
		mntmAK2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(1).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(1).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(1));
			}
		});
		mntmAK3 = new JMenuItem("Floor 3");
		mntmAK3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(2).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(2).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(2));
			}
		});
		mntmAKB = new JMenuItem("Basement");
		mntmAKB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(3).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(3).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(3));
			}
		});
		mnAtwaterKent.add(mntmAK1);
		mnAtwaterKent.add(mntmAK2);
		mnAtwaterKent.add(mntmAK3);
		mnAtwaterKent.add(mntmAKB);

		// Boynton Hall
		mnBoyntonHall = new JMenu("Boynton Hall");
		mntmBoy1 = new JMenuItem("Floor 1");
		mntmBoy1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(4).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(4).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(4));
			}
		});
		mntmBoy2 = new JMenuItem("Floor 2");
		mntmBoy2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(5).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(5).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(5));
			}
		});
		mntmBoy3 = new JMenuItem("Floor 3");
		mntmBoy3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(6).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(6).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(6));
			}
		});
		mntmBoyB = new JMenuItem("Basement");
		mntmBoyB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(7).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(7).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(7));
			}
		});
		mnBoyntonHall.add(mntmBoy1);
		mnBoyntonHall.add(mntmBoy2);
		mnBoyntonHall.add(mntmBoy3);
		mnBoyntonHall.add(mntmBoyB);

		// Campus Center
		mnCampusCenter = new JMenu("Campus Center");
		mntmCC1 = new JMenuItem("Floor 1");
		mntmCC1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(8).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(8).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(8));
			}
		});
		mntmCC2 = new JMenuItem("Floor 2");
		mntmCC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(9).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(9).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(9));
			}
		});
		mntmCC3 = new JMenuItem("Floor 3");
		mntmCC3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(10).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(10).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(10));
			}
		});
		mnCampusCenter.add(mntmCC1);
		mnCampusCenter.add(mntmCC2);
		mnCampusCenter.add(mntmCC3);

		// Campus Map
		mntmCCM = new JMenuItem("Campus Map");
		mntmCCM.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(11).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(11).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(11));
			}
		});

		// Gordon Library
		mnGordonLibrary = new JMenu("Gordon Library");
		mntmGL1 = new JMenuItem("Floor 1");
		mntmGL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(12).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(12).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(12));
			}
		});
		mntmGL2 = new JMenuItem("Floor 2");
		mntmGL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(13).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(13).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(13));
			}
		});
		mntmGL3 = new JMenuItem("Floor 3");
		mntmGL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(14).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(14).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(14));
			}
		});
		mntmGLB = new JMenuItem("Basement");
		mntmGLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(15).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(15).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(15));
			}
		});
		mntmGLSB = new JMenuItem("Sub Basement");
		mntmGLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(16).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(16).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(16));
			}
		});
		mnGordonLibrary.add(mntmGL1);
		mnGordonLibrary.add(mntmGL2);
		mnGordonLibrary.add(mntmGL3);
		mnGordonLibrary.add(mntmGLB);
		mnGordonLibrary.add(mntmGLSB);

		// Higgins House
		mnHigginsHouse = new JMenu("Higgins House");
		mntmHH1 = new JMenuItem("Floor 1");
		mntmHH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(17).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(17).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(17));
			}
		});
		mntmHH2 = new JMenuItem("Floor 2");
		mntmHH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(18).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(18).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(18));
			}
		});
		mntmHH3 = new JMenuItem("Floor 3");
		mntmHH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(19).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(19).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(19));
			}
		});
		mnHigginsHouse.add(mntmHH1);
		mnHigginsHouse.add(mntmHH2);
		mnHigginsHouse.add(mntmHH3);

		// Higgins House Garage
		mnHigginsHouseGarage = new JMenu("Higgins House Garage");
		mntmHHG1 = new JMenuItem("Floor 1");
		mntmHHG1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(20).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(20).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(20));
			}
		});
		mntmHHG2 = new JMenuItem("Floor 2");
		mntmHHG2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(21).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(21).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(21));
			}
		});
		mnHigginsHouseGarage.add(mntmHHG1);
		mnHigginsHouseGarage.add(mntmHHG2);

		// Project Center
		mnProjectCenter = new JMenu("Project Center");
		mntmPC1 = new JMenuItem("Floor 1");
		mntmPC1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(22).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(22).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(22));
			}
		});
		mntmPC2 = new JMenuItem("Floor 2");
		mntmPC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(23).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(23).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(23));
			}
		});
		mnProjectCenter.add(mntmPC1);
		mnProjectCenter.add(mntmPC2);

		// Stratton Hall
		mnStrattonHall = new JMenu("Stratton Hall");
		mntmSH1 = new JMenuItem("Floor 1");
		mntmSH1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(24).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(24).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(24));
			}
		});
		mntmSH2 = new JMenuItem("Floor 2");
		mntmSH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(25).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(25).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(25));
			}
		});
		mntmSH3 = new JMenuItem("Floor 3");
		mntmSH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(26).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(26).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(26));
			}
		});
		mntmSHB = new JMenuItem("Basement");
		mntmSHB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(27).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(27).getMapNodes());
				backend.setLocalMap(globalMap.getLocalMaps().get(27));
			}
		});
		mnStrattonHall.add(mntmSH1);
		mnStrattonHall.add(mntmSH2);
		mnStrattonHall.add(mntmSH3);
		mnStrattonHall.add(mntmSHB);

		mnLocations.add(mnAtwaterKent); // indices: 0, 1, 2, 3
		mnLocations.add(mnBoyntonHall); // indices: 4, 5, 6, 7
		mnLocations.add(mnCampusCenter);// indices: 8, 9, 10
		mnLocations.add(mntmCCM); // index: 11
		mnLocations.add(mnGordonLibrary); // indices: 11, 12, 13, 14, 15
		mnLocations.add(mnHigginsHouse); // indices: 16, 17, 18
		mnLocations.add(mnHigginsHouseGarage); //indices: 19, 20
		mnLocations.add(mnProjectCenter); // indices: 21, 22
		mnLocations.add(mnStrattonHall); // indices 23, 24, 25, 26

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
	}
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

	/** @description Enable actions on main panel
	 * 
	 */
	public static void enableActions(){
		for (TweenPanel panel : panels){
			panel.enableAction();
		}
		panelDirections.enableAction();
	}

	/** @description Disables actions on main panel
	 * 
	 */
	public static void disableActions(){
		for (TweenPanel panel : panels){
			panel.disableAction();
		}
		panelDirections.disableAction();
	}

	/** @description Tells the Tween panel what to do when opening
	 *  The animation functions come in pairs of action and back-action. 
	 *  This tells the engine where to move it, and if it needs to move other panels 
	 */	
	private final static Runnable panelDirectionsAction = new Runnable() {
		@Override 
		public void run() {
			disableActions();
			setCurrentlyOpen(true);

			slidePanel.createTransition()
			.push(new SLKeyframe(panelDirectionsConfig, 0.6f)
			.setCallback(new SLKeyframe.Callback() {
				@Override 
				public void done() {
					panelDirections.setAction(panelDirectionsBackAction);
					panelDirections.enableAction();
				}}))
				.play();
		}};

<<<<<<< HEAD
		/** @description Tells the Tween panel what to do when closing
		 *  The animation functions come in pairs of action and back-action. 
		 *  This tells the engine where to move it, and if it needs to move other panels 
		 */	
		private final static Runnable panelDirectionsBackAction = new Runnable() {
			@Override 
			public void run() {
				disableActions();
				setCurrentlyOpen(false);

				slidePanel.createTransition()
				.push(new SLKeyframe(mainConfig, 0.6f)
				.setCallback(new SLKeyframe.Callback() {
					@Override 
					public void done() {
						panelDirections.setAction(panelDirectionsAction);
						enableActions();
					}}))
					.play();
			}};
=======
	private final Runnable panelDirectionsBackAction = new Runnable() {
		@Override 
		public void run() {
			disableActions();
			currentlyOpen = false;

			slidePanel.createTransition()
			.push(new SLKeyframe(mainConfig, 0.6f)
					.setCallback(new SLKeyframe.Callback() {
						@Override 
						public void done() {
							panelDirections.setAction(panelDirectionsAction);
							enableActions();
						}}))
			.play();
		}};
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

			/** @description Resets all of the relevant information on the form and the background information
			 * @author Andrew Petit
			 */
			public void reset() {
<<<<<<< HEAD
				setAllowSetting(true); //allow user to re place nodes only once reset is pressed
				getGlobalMap().setStartNode(null);
				getGlobalMap().setEndNode(null);
				getTxtAreaDirections().setText(""); // clear directions
=======
				allowSetting = true; //allow user to re place nodes only once reset is pressed
				globalMap.setStartNode(null);
				globalMap.setEndNode(null);
				reset = true;
				listModel.removeAllElements(); // clear directions
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

				// allows the user to re-input start and end nodes
				setEnd = false;
				setStart = false;
				getPaths().clear();
				getBackend().removePath(getGlobalMap().getMiddleNodes());
				btnNextMap.setEnabled(false);
				btnPreviousMap.setEnabled(false);
				getGlobalMap().getChosenNodes().clear();
				getLabelDistance().setText("");
				getBtnClear().setEnabled(false);
				setRemoveLine(true);
			}

			/** @description initializes GUIFront constructor
			 * 
			 * @param localMapFilenames names of map images
			 */
			public static void init(File[] localMapFilenames, ArrayList<MapNode> allNodes){
				// Instantiate GUIBack to its default
				GUIBack initGUIBack = new GUIBack();
				setBackend(initGUIBack);

				// Initialize the GlobalMap variable with all of the LocalMaps and all of their nodes
				setGlobalMap(new GlobalMap());

				// create a list of all local map filename strings
				String[] localMapFilenameStrings = new String[localMapFilenames.length];
				for(int i = 0; i < localMapFilenames.length; i++){
					String path = localMapFilenames[i].getName();
					localMapFilenameStrings[i] = path;
				}

				ArrayList<LocalMap> localMapList = getBackend().loadLocalMaps(localMapFilenameStrings);

				getGlobalMap().setLocalMaps(localMapList);

				for(LocalMap localMap : localMapList){
					if(localMap.getMapImageName().equals(Constants.DEFAULT_MAP_IMAGE))
						getBackend().setLocalMap(localMap);
				}

				// add the collection of nodes to the ArrayList of GlobalMap
				for (LocalMap local : localMapList) {
					allNodes.addAll(local.getMapNodes());
				}
				getGlobalMap().setMapNodes(allNodes);
			}

<<<<<<< HEAD
			public static void initPanels(Image mapImage){
				panelMap = new TweenPanel(getBackend().getLocalMap().getMapNodes(), mapImage, "1");
				panels.add(panelMap);
				panelDirections = new TweenPanel("2");

				// Adding new components onto the Step By Step slideout panel
				JPanel stepByStepUI = new JPanel();

				setLabelClickHere(new JLabel("<<<"));
				getLabelClickHere().setFont(new Font("Tahoma", Font.BOLD, 13));
				getLabelClickHere().setVisible(true);
				stepByStepUI.add(getLabelClickHere());
=======
					if(direction < 0){ // moving up, so zoom in	(no greater than 100%)
						if(zoomAmount <= (.9 + .001))
							zoomAmount += 0.1;
					} else { // moving down, zoom out (no less than 50%)
						if(zoomAmount >= 0.5)
							zoomAmount -= 0.1;
					}

					panelMap.setScale(zoomAmount);
				}
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

				setLabelStepByStep(new JLabel("Step by Step Directions!"));
				getLabelStepByStep().setFont(new Font("Tahoma", Font.BOLD, 13));
				getLabelStepByStep().setBounds(23, 11, 167, 14);
				getLabelStepByStep().setVisible(false);
				stepByStepUI.add(getLabelStepByStep());

				setScrollPane(new JScrollPane());
				getScrollPane().setBounds(10, 30, 180, 322);
				getScrollPane().setVisible(false);
				stepByStepUI.add(getScrollPane());

				// Create a new list and be able to get the current width ofthe viewport it is contained in (the scrollpane)
				renderer = new WrappableCellRenderer(Constants.MAX_LIST_WIDTH / 7); // 7 pixels per 1 character

				listDirections = new JList<String>(listModel);
				listDirections.setCellRenderer(renderer);
				listDirections.setFixedCellWidth(Constants.MAX_LIST_WIDTH); // give it a set width in pixels
				scrollPane.setViewportView(listDirections);
				listDirections.setVisible(false);
				listDirections.setVisibleRowCount(10); // only shows 10 directions before scrolling

				labelDistance = new JLabel();
				labelDistance.setFont(new Font("Tahoma", Font.BOLD, 13));
				labelDistance.setVisible(false);

				panelDirections.add(labelDistance, BorderLayout.SOUTH);
				panelDirections.add(stepByStepUI, BorderLayout.NORTH);

				setTxtAreaDirections(new JTextArea());
				getTxtAreaDirections().setRows(22);
				getTxtAreaDirections().setEditable(false);

				getScrollPane().setViewportView(getTxtAreaDirections());

				getTxtAreaDirections().setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				getTxtAreaDirections().setWrapStyleWord(true);
				getTxtAreaDirections().setLineWrap(true);
				getTxtAreaDirections().setVisible(false);

				setLabelDistance(new JLabel());
				getLabelDistance().setFont(new Font("Tahoma", Font.BOLD, 13));
				getLabelDistance().setVisible(false);

				panelDirections.add(getLabelDistance(), BorderLayout.SOUTH);

				// Set action to allow for sliding
				panelDirections.setAction(panelDirectionsAction);

				/* The configuration files describe what will take place for each animation. So by default we want the map larger 
				 * and the side panel very small. When we click the directions panel we want that to slide out, zoomRatio the map panel, and
				 * adjust the sizes
				 */
				slidePanel = new SLPanel();
				mainConfig = new SLConfig(slidePanel)
				.gap(10, 10)
				.row(1f).col(700).col(50) // 700xH | 50xH
				.place(0, 0, panelMap)
				.place(0, 1, panelDirections);

				panelDirectionsConfig = new SLConfig(slidePanel)
				.gap(10, 10)
				.row(1f).col(550).col(200) // 550xH | 200xH
				.place(0, 0, panelMap)
				.place(0, 1, panelDirections);

				// Initialize tweening
				slidePanel.setTweenManager(SLAnimator.createTweenManager());
				slidePanel.initialize(mainConfig);

				// add to the tabbed pane
				mainTabPane.add(slidePanel, BorderLayout.CENTER);

				btnNextMap = new JButton("Next Map -->");
				btnNextMap.setEnabled(false);
				btnNextMap.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent ae){
						index++;
						if (index <= 0)
							btnPreviousMap.setEnabled(false);
						if (index < getPaths().size() - 1)
							btnNextMap.setEnabled(true);
						if (index >= getPaths().size() - 1)
							btnNextMap.setEnabled(false);
						if (index > 0)
							btnPreviousMap.setEnabled(true);

						panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getPaths().get(index).get(0).getLocalMap().getMapImageName()).getImage());
						panelMap.setMapNodes(getPaths().get(index).get(0).getLocalMap().getMapNodes());
						getBackend().setLocalMap(getPaths().get(index).get(0).getLocalMap());
						setThisRoute(getPaths().get(index));
						setDrawLine(true);
					}
				});

				// Add buttons to move between two maps
				btnPreviousMap = new JButton("<-- Previous Map");
				btnPreviousMap.setEnabled(false);
				btnPreviousMap.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent ae) {
						index--;
						if (index <= 0)
							btnPreviousMap.setEnabled(false);
						if (index < getPaths().size() - 1)
							btnNextMap.setEnabled(true);
						if (index >= getPaths().size() - 1)
							btnNextMap.setEnabled(false);
						if (index > 0)
							btnPreviousMap.setEnabled(true);

						panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + getPaths().get(index).get(0).getLocalMap().getMapImageName()).getImage());
						panelMap.setMapNodes(getPaths().get(index).get(0).getLocalMap().getMapNodes());
						getBackend().setLocalMap(getPaths().get(index).get(0).getLocalMap());
						setThisRoute(getPaths().get(index));
						setDrawLine(true);
					}
				});
			}

			public static void groupLayout(JPanel contentPane, JLabel labelInvalidEntry, JButton buttonRoute) {
				GroupLayout gl_contentPane = new GroupLayout(contentPane);
				gl_contentPane.setHorizontalGroup(
						gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
												.addGap(10)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(searchLabelStart)
														.addComponent(textFieldStart, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
														.addGap(18)
														.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
																.addComponent(searchLabelEnd, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
																.addComponent(textFieldEnd, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
																.addGap(18)
																.addComponent(labelInvalidEntry)
																.addGap(227)
																.addComponent(buttonRoute, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
																.addGap(18)
																.addComponent(getBtnClear()))
																.addComponent(mainTabPane, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)))
																.addGroup(gl_contentPane.createSequentialGroup()
																		.addGap(79)
																		.addComponent(btnPreviousMap)
																		.addPreferredGap(ComponentPlacement.RELATED, 335, Short.MAX_VALUE)
																		.addComponent(btnNextMap)
																		.addGap(170))
						);

				gl_contentPane.setVerticalGroup(
						gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(searchLabelStart)
												.addGap(6)
												.addComponent(textFieldStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_contentPane.createSequentialGroup()
														.addComponent(searchLabelEnd)
														.addGap(6)
														.addComponent(textFieldEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
														.addGroup(gl_contentPane.createSequentialGroup()
																.addGap(24)
																.addComponent(labelInvalidEntry))
																.addGroup(gl_contentPane.createSequentialGroup()
																		.addGap(11)
																		.addComponent(buttonRoute))
																		.addGroup(gl_contentPane.createSequentialGroup()
																				.addGap(11)
																				.addComponent(getBtnClear())))
																				.addGap(18)
																				.addComponent(mainTabPane, GroupLayout.PREFERRED_SIZE, 445, GroupLayout.PREFERRED_SIZE)
																				.addGap(18)
																				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
																						.addComponent(btnPreviousMap)
																						.addComponent(btnNextMap))
																						.addGap(35))
						);
				contentPane.setLayout(gl_contentPane);
			}

			public static String getAllText() {
				return allText;
			}

			public static void setAllText(String allText) {
				GUIFront.allText = allText;
			}

			public static Point2D getMainReferencePoint() {
				return mainReferencePoint;
			}

<<<<<<< HEAD
			public static void setMainReferencePoint(Point2D mainReferencePoint) {
				GUIFront.mainReferencePoint = mainReferencePoint;
			}
=======

			public static class TweenPanel extends JPanel {
				ArrayList<MapNode> localNodes;
				public ArrayList<MapNode> chosenNodes;
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

			public static TweenPanel getPanelMap() {
				return panelMap;
			}

			public static void setPanelMap(TweenPanel panelMap) {
				GUIFront.panelMap = panelMap;
			}

<<<<<<< HEAD
			public static GUIBack getBackend() {
				return backend;
			}
=======
				/**
				 * Class for a custom panel to do drawing and tweening. This can be seperated into a seperate class file
				 * but it functions better as a private class
				 */
				public TweenPanel(ArrayList<MapNode> mapNodes, Image mapPath, String panelId){

					setLayout(new BorderLayout());

					this.localNodes = mapNodes;

					labelMainPanel.setFont(new Font("Sans", Font.BOLD, 90));
					labelMainPanel.setVerticalAlignment(SwingConstants.CENTER);
					labelMainPanel.setHorizontalAlignment(SwingConstants.CENTER);
					labelMainPanel.setText(panelID);

					this.mapImage = mapPath;
					this.panelID = panelID;
					
					panX = 0;
					panY = 0;
					zoomRatio = 1;

					addMouseListener(panHandle);
					addMouseMotionListener(panHandle);
					addMouseWheelListener(zoomHandle);

					//ArrayList<MapNode> chosenNodes = new ArrayList<MapNode>(); // Index 0: StartNode; Index 1: EndNode
					addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent me) {
							if (allowSetting == true){
								// figure out where the closest map node is, set that node as a startnode the StartingNode
								//clickedAt = me.getPoint();
								MapNode node = backend.findNearestNode(mainReferencePoint.getX() + panX, mainReferencePoint.getY() + panY, backend.getLocalMap());
								GUIBack tempBack = backend;
								System.out.println("Node found is: " + node.getNodeID());

								if(globalMap.getChosenNodes().size() == 0){
									globalMap.setStartNode(node);
									globalMap.getStartNode().setLocalMap(backend.getLocalMap());
									backend.getLocalMap().setStart(node);
									System.out.println("This has happened");
									btnClear.setEnabled(true);
								}
								else{
									MapNode endNode = globalMap.getEndNode();
									//if(endNode != null)
										//globalMap.addToMiddleNodes(endNode);
									//globalMap.getEndNode().getLocalMap().getMiddleNodes().add(endNode);
									//globalMap.getEndNode().getLocalMap().setEnd(null);

									globalMap.setEndNode(node);
									globalMap.getEndNode().setLocalMap(backend.getLocalMap());
									backend.getLocalMap().setEnd(node);
								}
								globalMap.getChosenNodes().add(node);
							}
							//	btnRoute.setEnabled(true);
							repaint();
						}	
					});
				}
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

			public static void setBackend(GUIBack backend) {
				GUIFront.backend = backend;
			}

			public static GlobalMap getGlobalMap() {
				return globalMap;
			}

			public static void setGlobalMap(GlobalMap globalMap) {
				GUIFront.globalMap = globalMap;
			}

			public static AffineTransform getTransform() {
				return transform;
			}

			public static void setTransform(AffineTransform transform) {
				GUIFront.transform = transform;
			}

			public static PanHandler getPanHandle() {
				return panHandle;
			}

			public static void setPanHandle(PanHandler panHandle) {
				GUIFront.panHandle = panHandle;
			}

			public static ZoomHandler getZoomHandle() {
				return zoomHandle;
			}

			public static void setZoomHandle(ZoomHandler zoomHandle) {
				GUIFront.zoomHandle = zoomHandle;
			}

			public static boolean isAllowSetting() {
				return allowSetting;
			}

			public static void setAllowSetting(boolean allowSetting) {
				GUIFront.allowSetting = allowSetting;
			}

			public static JButton getBtnClear() {
				return btnClear;
			}

			public static void setBtnClear(JButton btnClear) {
				GUIFront.btnClear = btnClear;
			}

			public static boolean isCurrentlyOpen() {
				return currentlyOpen;
			}

<<<<<<< HEAD
			public static void setCurrentlyOpen(boolean currentlyOpen) {
				GUIFront.currentlyOpen = currentlyOpen;
			}
=======
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);

					Graphics2D graphics = (Graphics2D) g;

					if(this.mapImage == null) // StepByStep
						if(!currentlyOpen){
							lblStepByStep.setVisible(false);
							lblClickHere.setVisible(true);
							lblDistance.setVisible(false);
							scrollPane.setVisible(false);
							listDirections.setVisible(false);
						} else {
							lblStepByStep.setVisible(true);
							lblDistance.setVisible(true);
							lblClickHere.setVisible(false);
							scrollPane.setVisible(true);
							listDirections.setVisible(true);
						}
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

			public static JLabel getLabelStepByStep() {
				return labelStepByStep;
			}

			public static void setLabelStepByStep(JLabel labelStepByStep) {
				GUIFront.labelStepByStep = labelStepByStep;
			}

			public static JLabel getLabelClickHere() {
				return labelClickHere;
			}

			public static void setLabelClickHere(JLabel labelClickHere) {
				GUIFront.labelClickHere = labelClickHere;
			}

			public static JLabel getLabelDistance() {
				return labelDistance;
			}

<<<<<<< HEAD
			public static void setLabelDistance(JLabel labelDistance) {
				GUIFront.labelDistance = labelDistance;
			}
=======
						// Colors start and end differently
						// Draws the map and places pre-existing node data onto the map as
						// well start and end nodes if they have been set
						graphics.drawImage(this.mapImage, 0, 0, this);
						// Sets the color of the start and end nodes to be different
						graphics.setColor(Color.RED);
						//for (/*int i = 0; i < globalMap.getChosenNodes().size(); i++*/ArrayList<MapNode> mapnode : paths) {
							//System.out.println(globalMap.getChosenNodes().size());
							if(!(paths.isEmpty())){
								if (paths.get(index).get(0) != null){
									graphics.setColor(Color.RED);
									graphics.fillOval((int) paths.get(index).get(0).getXPos() - (int)panX - 5, (int) paths.get(index).get(0).getYPos() - (int)panY - 5, 10, 10);
								}
							//} 
							//else if(i == globalMap.getChosenNodes().size() - 1){
								if (paths.get(index).get(paths.get(index).size() - 1) != null){
									graphics.setColor(Color.GREEN);
									graphics.fillOval((int) paths.get(index).get(paths.get(index).size() - 1).getXPos() - (int)panX - 5, (int) paths.get(index).get(paths.get(index).size() - 1).getYPos() - (int)panY - 5, 10, 10);
								}
							//}
							//else {
								//graphics.setColor(Color.GREEN);
								//graphics.fillOval((int) globalMap.getEndNode().getXPos() - (int)panX - 5, (int) globalMap.getEndNode().getYPos() - (int)panY - 5, 10, 10);
							}
						//}
							
						if (globalMap.getStartNode() != null){
							if (globalMap.getStartNode().getLocalMap() == backend.getLocalMap()){
								graphics.setColor(Color.RED);
								graphics.fillOval((int) globalMap.getStartNode().getXPos() - (int)panX - 5, (int) globalMap.getStartNode().getYPos() - (int)panY - 5, 10, 10);
							}
						}
						
						if(globalMap.getEndNode() != null){
							if (globalMap.getEndNode().getLocalMap() == backend.getLocalMap()){
								graphics.setColor(Color.GREEN);
								graphics.fillOval((int) globalMap.getEndNode().getXPos() - (int)panX - 5, (int) globalMap.getEndNode().getYPos() - (int)panY - 5, 10, 10);
							}
						}
>>>>>>> 118a7c92e0d1948167578055c6fdf7946aa591fa

			public static JScrollPane getScrollPane() {
				return scrollPane;
			}

			public static void setScrollPane(JScrollPane scrollPane) {
				GUIFront.scrollPane = scrollPane;
			}

			public static JTextArea getTxtAreaDirections() {
				return txtAreaDirections;
			}

			public static void setTxtAreaDirections(JTextArea txtAreaDirections) {
				GUIFront.txtAreaDirections = txtAreaDirections;
			}

			public static ArrayList<MapNode> getMapnodes() {
				return mapnodes;
			}

			public static void setMapnodes(ArrayList<MapNode> mapnodes) {
				GUIFront.mapnodes = mapnodes;
			}

			public static ArrayList<ArrayList<MapNode>> getPaths() {
				return paths;
			}

			public static void setPaths(ArrayList<ArrayList<MapNode>> paths) {
				GUIFront.paths = paths;
			}

			public static boolean isDrawLine() {
				return drawLine;
			}

			public static boolean setDrawLine(boolean drawLine) {
				GUIFront.drawLine = drawLine;
				return drawLine;
			}

			public static boolean isRemoveLine() {
				return removeLine;
			}

			public static void setRemoveLine(boolean removeLine) {
				GUIFront.removeLine = removeLine;
			}

			public static ArrayList<MapNode> getThisRoute() {
				return thisRoute;
			}

			public static void setThisRoute(ArrayList<MapNode> thisRoute) {
				GUIFront.thisRoute = thisRoute;
			}
}

package main.gui;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import main.util.Constants;
import main.util.GeneralUtil;
import main.util.ProxyImage;
import main.util.IProxyImage;
import main.util.Speaker;
import main.util.WrappableCellRenderer;

import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.JLayeredPane;

/**
 * This class contains code for the main applications GUI interface as well as
 * implementation for its various functionality such as drawing the route.
 * 
 * Tween code adapted from Aurelien Ribon's Sliding Layout Demo 
 * 
 * @author Trevor
 */
@SuppressWarnings("serial")
public class GUIFront extends JFrame {

	public static GUIBack backend;
	private static GlobalMap globalMap;
	private static boolean setStart = false; // keeps track of whether you have set a start or end node yet
	private static boolean setEnd = false;
	public static boolean drawLine = false;
	public static boolean removeLine = false;
	public static boolean reset = false;
	public static MapNode startNode = null, endNode = null;
	public static String allText = "";
	public static ArrayList<GUIBack> backends = new ArrayList<GUIBack>();
	public static ArrayList<MapNode> mapnodes = new ArrayList<MapNode>();
	public static ArrayList<ArrayList<MapNode>> paths = new ArrayList<ArrayList<MapNode>>();
	public static ArrayList<ArrayList<MapNode>> routes = new ArrayList<ArrayList<MapNode>>();
	public static JButton btnClear, btnRoute;
	private JButton btnPreviousMap, btnNextMap;
	public static boolean allowSetting = true;
	public static JTabbedPane mainPanel; 
	public static ArrayList<MapNode> allNodes;
	public static int index = 0;
	public static ArrayList<MapNode> thisRoute;
	public static ArrayList<ArrayList<ArrayList<MapNode>>> listOfRoutes;
	public static ArrayList<ArrayList<ArrayList<MapNode>>> listOfPaths = new ArrayList<ArrayList<ArrayList<MapNode>>>();
	public static ArrayList<LocalMap> tempMaps = new ArrayList<LocalMap>();
	public static HashMap<String, double[]> panValues = new HashMap<String, double[]>();
	public static double[] panNums = {0.0, 0.0};
	static AffineTransform transform; // the current state of image transformation
	static Point2D mainReferencePoint; // the reference point indicating where the click started from during transformation
	static PanHandler panHandle;
	static ZoomHandler zoomHandle;


	// MapPanel components
	private static JPanel contentPane;
	private static JTextField textFieldEnd, textFieldStart;
	private JLabel lblStart, lblEnd;
	private static GroupLayout gl_contentPane;

	// Directions Components
	private static JLabel lblStepByStep, lblClickHere, lblDistance;
	private static JScrollPane scrollPane;
	private static boolean currentlyOpen = false; // keeps track of whether the panel is slid out or not
	private DefaultListModel<String> listModel = new DefaultListModel<String>(); // Setup a default list of elements
	private ListCellRenderer renderer;
	private int MAX_LIST_WIDTH = 180; // maximum width of the list in pixels, the size of panelDirections is 200px
	private static JList<String> listDirections;

	// Menu Bar
	private JMenuBar menuBar;
	private JMenu mnFile, mnOptions, mnHelp, mnLocations;
	private JMenu mnColorScheme;
	private JMenuItem mntmDefaultCampus, mntmGrayscale, mntmWPI, mntmSkyBlue;
	private JMenu mnAtwaterKent, mnBoyntonHall, mnCampusCenter, mnGordonLibrary, mnHigginsHouse, mnHigginsHouseGarage, mnProjectCenter, mnStrattonHall;
	private JMenuItem mntmAK1, mntmAK2, mntmAK3, mntmAKB, mntmBoy1, mntmBoy2, mntmBoy3, mntmBoyB, mntmCC1, mntmCC2, mntmCC3, mntmCCM;
	private JMenuItem mntmGL1, mntmGL2, mntmGL3, mntmGLB, mntmGLSB, mntmHH1, mntmHH2, mntmHH3, mntmHHG1, mntmHHG2, mntmPC1, mntmPC2;
	private JMenuItem mntmSH1, mntmSH2, mntmSH3, mntmSHB, mntmEmail, mntmExit;

	private SLPanel slidePanel;
	private JPanel stepByStepUI;
	public static ArrayList<TweenPanel> panels = new ArrayList<TweenPanel>();
	public static TweenPanel panelMap, panelDirections;
	private SLConfig mainConfig, panelDirectionsConfig;

	private Color routeButtonColor, otherButtonsColor, backgroundColor, sideBarColor;
	static ColorSchemes allSchemes;
	static ColorSetting colors;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public GUIFront(int numLocalMaps, File[] localMapFilenames) throws IOException, ClassNotFoundException {

		setVisible(false);

		allSchemes = new ColorSchemes();  
		colors = allSchemes.setColorScheme("Default Campus");

		routeButtonColor = colors.getRouteButtonColor();
		otherButtonsColor = colors.getOtherButtonsColor();
		backgroundColor = colors.getMainBackColor();
		sideBarColor = colors.getSideBarColor();


		// Instantiate GUIBack to its default
		GUIBack initial = new GUIBack(/*defaultMapImage, null*/);
		backends.add(0, initial);

		// Initialize the GlobalMap variable with all of the LocalMaps and all
		// of their nodes
		globalMap = new GlobalMap();

		String[] localMapFilenameStrings = new String[localMapFilenames.length];
		for(int i = 0; i < localMapFilenames.length; i++){
			String path = localMapFilenames[i].getName();
			localMapFilenameStrings[i] = path;
			//String xmlFileName = SaveUtil.removeExtension(path) + ".localmap";	
			//screen.setProgress("Loading" + xmlFileName, 69);  // progress bar with a message
		}
		/*
		for (int i = 0; i < 1000; i++){
			for(long j = 0; j < 50000; j++){
				double x = 5*5*8888*8888*8*8*8*(Math.pow(28, 33));
			}
			splashScreen.setProgress("Loading", i);
		}*/

		backend = initial;

		ArrayList<LocalMap> localMapList = backend.loadLocalMaps(localMapFilenameStrings);

		globalMap.setLocalMaps(localMapList);

		for(LocalMap localMap : localMapList){
			if(localMap.getMapImageName().equals(Constants.DEFAULT_MAP_IMAGE))
				backend.setLocalMap(localMap);
		}

		// add the collection of nodes to the ArrayList of GlobalMap
		allNodes = new ArrayList<MapNode>();
		for (LocalMap local : localMapList) {
			panValues.put(local.getMapImageName(), new double[]{0.0, 0.0});
			allNodes.addAll(local.getMapNodes());
		}
		globalMap.setMapNodes(allNodes);
		//backend = initial;

		/**
		 * GUI related code
		 */
		// This will setup the main JFrame to be maximized on start
		setTitle("Era of Navigation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1412, 743);
		setResizable(false);
		setPreferredSize(new Dimension(820, 650));
		panHandle = new PanHandler();
		zoomHandle = new ZoomHandler();

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		initializeMenuBar();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(backgroundColor);
		setContentPane(contentPane);

		// Image of the default map loaded into backend
		String defaultMapImage = Constants.DEFAULT_MAP_IMAGE;
		IProxyImage mapPath = new ProxyImage(defaultMapImage);
		JLabel lblInvalidEntry = new JLabel("Invalid Entry");
		lblInvalidEntry.setVisible(false);
		Action actionEnd = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				lblInvalidEntry.setVisible(false);
				System.out.println("Enter was pressed");
				//if the user presses enter without having entered anything in this box
				if (textFieldEnd.getText().equals("")){
					System.out.println("Need to enter a valid start location"); // TODO: will need some way to alert the user that they need to enter an end location
				} 
				else if (!(textFieldEnd.getText().equals(""))) { //if there is something entered check if the name is valid and then basically add the end node
					String endString = textFieldEnd.getText(); //entered text = endString constant
					boolean valid = false;
					Attributes attribute = new Attributes(); //will most likely need some other way of obtaining this information

					//Test if the entered information is a valid node in local map - this will be updated to global map when that is finished
					MapNode n = allNodes.get(0);
					if (startNode == null){
						startNode = n;

						globalMap.setStartNode(startNode);
						if (!setStart){
							globalMap.getChosenNodes().add(0, startNode);
						} else {
							globalMap.getChosenNodes().set(0, startNode);
						}

					}

					for (MapNode mapnode : backend.getLocalMap().getMapNodes()){
						// this follows a similar pattern to how the original nodes are set with the radio buttons
						if(endString.equals(mapnode.getAttributes().getOfficialName()) || mapnode.getAttributes().getAliases().contains(endString)){
							//if endstring is the official name or one of a few different accepted aliases we will allow the end node to be placed
							endNode = mapnode;
							System.out.println("This is the ending node");
							globalMap.setEndNode(endNode);
							if (!setEnd) {
								globalMap.getChosenNodes().add(1, endNode);
								System.out.println(globalMap.getChosenNodes().size());					
							} else {
								globalMap.getChosenNodes().set(1, endNode);
							}

							setEnd = true;
							valid = true;
						} 
					}

					if (attribute.getPossibleEntries().containsKey(endString)){ //check if the entry in the text field is an attribute not an official name
						String findNearestThing = attribute.getPossibleEntries().get(endString);
						if(startNode != null){ //if there is no valid start node, this cannot be done - why? because you need a valid start node to find the closest node with the given attribute
							valid = true;
							MapNode node = backend.findNearestAttributedNode(findNearestThing, startNode); //same idea as findNearestNode - just finds the nearest node to the startnode that gives the entered attribute
							if (node != null){ //if no node was found, you should not place a node on the map otherwise do it 
								endNode = node;
								System.out.println("This is the ending node!");
								globalMap.setEndNode(endNode);
								if (!setEnd) {
									globalMap.getChosenNodes().add(1, endNode);
									System.out.println(globalMap.getChosenNodes().size());					
								} else {
									globalMap.getChosenNodes().set(1, endNode);
								}
								setEnd = true;
								//btnCalculateRoute.setEnabled(true);
							}
						} 
						else if(!(textFieldStart.getText().equals(""))){ //if there is something entered in the start field as well as the end field we can go ahead and place both at the same time...
							String startString = textFieldStart.getText();
							for (MapNode mapnode : backend.getLocalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
								if(startString.equals(mapnode.getAttributes().getOfficialName())){
									startNode = mapnode; //set the startNode and then draw it on the map
									System.out.println("This is the starting node");
									globalMap.setStartNode(startNode);
									if (!setStart) {
										globalMap.getChosenNodes().add(0, startNode);
										System.out.println(globalMap.getChosenNodes().size());					
									} else {
										globalMap.getChosenNodes().set(0, startNode);
									}
									setStart = true;
								}
							}

							if (startNode != null){ //make sure that the startNode value is still not null, otherwise this won't work if it is
								MapNode node = backend.findNearestAttributedNode(findNearestThing, startNode); //same idea as findNearestNode - just finds the nearest node to the startnode that gives the entered attribute
								if (node != null){ //if no node was found, you should not do this and return an error, else do the following 
									endNode = node; //set the end node and place that node on the map
									System.out.println("This is the ending node!");
									globalMap.setEndNode(endNode);
									if (!setEnd) {
										globalMap.getChosenNodes().add(1, endNode);
										System.out.println(globalMap.getChosenNodes().size());					
									} else {
										globalMap.getChosenNodes().set(1, endNode);
									}
									setEnd = true;
									//btnCalculateRoute.setEnabled(true);
									valid = true;
								}
							}
						}
					}
					if (valid == false){
						// TODO: tell user this entry is invalid
						System.out.println("Invalid entry");
						lblInvalidEntry.setVisible(true);
					}
				}
			}
		};
		//when you press enter after entering stuff in textfieldStart
		Action actionStart = new AbstractAction()
		{
			@Override 
			public void actionPerformed(ActionEvent e)
			{
				lblInvalidEntry.setVisible(false);
				System.out.println("Enter was pressed");
				if (textFieldStart.getText().equals("")){
					//will need some way to alert the user that they need to enter a start location
					System.out.println("Need to enter a valid start location");
				} else if (!(textFieldStart.getText().equals(""))) {//if there is something entered check if the name is valid and then basically add the start node
					String startString = textFieldStart.getText();
					boolean valid = false;
					for (MapNode mapnode : backend.getLocalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
						if(startString.equals(mapnode.getAttributes().getOfficialName()) || mapnode.getAttributes().getAliases().contains(startString)){
							//if the startString is equal to the official name of the startString is one of a few accepted alias' we will allow the start node to be placed
							startNode = mapnode; //set the startNode and place it on the map
							System.out.println("This is the starting node");
							globalMap.setStartNode(startNode);
							if (!setStart) {
								globalMap.getChosenNodes().add(0, startNode);
								System.out.println(globalMap.getChosenNodes().size());					
							} else {
								globalMap.getChosenNodes().set(0, startNode);
							}
							setStart = true;
							valid = true;
						}
					}
					if (valid == false){
						//tell user this entry is invalid
						System.out.println("Invalid entry");
						lblInvalidEntry.setVisible(true);
					}
				}	
			}
		};

		/**
		 * GroupLayout code for tabbedpane and textfields (Temporary)
		 */
		mainPanel = new JTabbedPane();
		mainPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		mainPanel.setBackground(backgroundColor);
		textFieldStart = new JTextField();
		textFieldStart.setText("");
		//give start text field an action
		textFieldStart.addActionListener(actionStart);

		textFieldEnd = new JTextField("");
		textFieldEnd.setColumns(10);
		//give end text field an action		
		textFieldEnd.addActionListener(actionEnd);

		lblStart = new JLabel("Starting Location");
		lblStart.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblEnd = new JLabel("Ending Location");
		lblEnd.setFont(new Font("Tahoma", Font.PLAIN, 12));

		// Clear button will call all of the reset code
		btnClear = new JButton("Clear All");
		btnClear.setBackground(otherButtonsColor);
		btnClear.setEnabled(false);
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				reset();
			}
		});

		btnRoute = new JButton("Route");
		btnRoute.setBackground(routeButtonColor);
		btnRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnRoute.isEnabled()) {

					allowSetting = false; //once calculate button is pressed user should not be allowed to replace nodes until the original line is removed
					allText = ""; //must set the initial text as empty every time calculate button is pressed
					Speaker speaker = new Speaker(Constants.BUTTON_PATH);
					speaker.play();
					System.out.println("Start node: " + globalMap.getStartNode().getNodeID());
					System.out.println("End node: " + globalMap.getEndNode().getNodeID());
					System.out.println("Number of nodes in globalMap: " + globalMap.getMapNodes().size());
					routes = backend.getMeRoutes(globalMap.getStartNode(), globalMap.getEndNode(), globalMap);
					if (routes.isEmpty()){
						mapnodes = backend.runAStar(backend.getLocalMap().getStart(), backend.getLocalMap().getEnd());
					} else {
						for (int i = 0; i < routes.size(); i++){
							LocalMap localmap = routes.get(i).get(0).getLocalMap();
							if (localmap.getEnd() == null){
								int size = routes.get(i).size() - 1;
								localmap.setStart(routes.get(i).get(size));
							}

							if (localmap.getStart() == null){
								localmap.setEnd(routes.get(i).get(0));
							}
							ArrayList<MapNode> route = routes.get(i);
							paths.add(route);
						}
					}


					thisRoute = routes.get(0);
					panelMap.setMapImage(new ProxyImage(paths.get(0).get(0).getLocalMap().getMapImageName()));
					panelMap.setMapNodes(paths.get(0).get(0).getLocalMap().getMapNodes());
					String previousMap = backend.getLocalMap().getMapImageName();
					panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

					backend.setLocalMap(paths.get(0).get(0).getLocalMap());

					double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
					panelMap.panX = tempPan[0];
					panelMap.panY = tempPan[1];

					for(MapNode n : backend.getLocalMap().getMapNodes()){
						n.setXPos(n.getXPos() - panelMap.panX);
						n.setYPos(n.getYPos() - panelMap.panY);
					}

					panelMap.panX = 0.0;
					panelMap.panY = 0.0;
					panelMap.setScale(1.0);

					index = 0;
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
					//update the step by step directions and distance for each waypoint added
					for (ArrayList<MapNode>wayPoints : paths){
						distance += backend.getDistance(wayPoints);
						for (String string : backend.displayStepByStep(wayPoints)) {
							listModel.addElement(string); // add it to the list model
							allText += string + "\n";
						}
					}

					lblDistance.setText("Distance in feet:" + distance);
					//this sets the textarea with the step by step directions
					//textArea1.setText(allText);
					//btnRoute.setEnabled(false);
					btnClear.setEnabled(true);


					//btnEmail_1.setEnabled(true); //this is where email button should be enabled
				}
			}
		});

		/**
		 * Tween related code to make the animations work
		 */
		sideBarColor = colors.getSideBarColor();

		slidePanel = new SLPanel();
		slidePanel.setBackground(sideBarColor);
		panelMap = new TweenPanel(backend.getLocalMap().getMapNodes(), mapPath, "1", Constants.IMAGES_PATH);
		panelMap.setBackground(backgroundColor);
		panels.add(panelMap);
		panelDirections = new TweenPanel("2");

		/**
		 * Adding new components onto the Step By Step slideout panel
		 */
		stepByStepUI = new JPanel();
		stepByStepUI.setBackground(sideBarColor);

		lblClickHere = new JLabel("<<<");
		lblClickHere.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblClickHere.setVisible(true);
		stepByStepUI.add(lblClickHere);

		lblStepByStep = new JLabel("Step by Step Directions!");
		lblStepByStep.setBackground(sideBarColor);
		lblStepByStep.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblStepByStep.setBounds(23, 11, 167, 14);
		lblStepByStep.setVisible(false);
		stepByStepUI.add(lblStepByStep);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 30, 180, 322);
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
		lblDistance.setBackground(sideBarColor);
		lblDistance.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDistance.setVisible(false);

		panelDirections.add(lblDistance, BorderLayout.SOUTH);
		panelDirections.add(stepByStepUI, BorderLayout.NORTH);
		panelDirections.setBackground(sideBarColor);

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
		btnNextMap = new JButton("Next Map-->");
		btnNextMap.setEnabled(false);
		btnNextMap.setBackground(otherButtonsColor);
		btnNextMap.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){				
				index++;
				if (index <= 0){
					btnPreviousMap.setEnabled(false);
				}
				if (index < paths.size() - 1){
					btnNextMap.setEnabled(true);
				}

				if (index >= paths.size() - 1){
					btnNextMap.setEnabled(false);
				}
				if (index > 0){
					btnPreviousMap.setEnabled(true);
				}
				LocalMap localMap = paths.get(index).get(0).getLocalMap();
				panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
				panelMap.setMapNodes(paths.get(index).get(0).getLocalMap().getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});
				backend.setLocalMap(localMap);

				double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);

				thisRoute = paths.get(index);
				drawLine = true;
			}
		});
		getContentPane().add(btnNextMap, BorderLayout.SOUTH);

		// Add buttons to move between two maps
		btnPreviousMap = new JButton("<-- Previous Map");
		/*if (index <= 0){
			btnPreviousMap.setEnabled(false);
		}*/
		btnPreviousMap.setEnabled(false);
		btnPreviousMap.setBackground(otherButtonsColor);
		btnPreviousMap.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae) {
				index--;
				if (index <= 0){
					btnPreviousMap.setEnabled(false);
				}
				if (index < paths.size() - 1){
					btnNextMap.setEnabled(true);
				}

				if (index >= paths.size() - 1){
					btnNextMap.setEnabled(false);
				}
				if (index > 0){
					btnPreviousMap.setEnabled(true);
				}
				panelMap.setMapImage(new ProxyImage(paths.get(index).get(0).getLocalMap().getMapImageName()));
				panelMap.setMapNodes(paths.get(index).get(0).getLocalMap().getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});
				backend.setLocalMap(paths.get(index).get(0).getLocalMap());

				double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
				thisRoute = paths.get(index);
				drawLine = true;
				// TODO: Fill in this method once we know how to draw/load maps
			}
		});
		getContentPane().add(btnPreviousMap, BorderLayout.SOUTH);

		// Group Layout code for all components
		gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(10)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblStart)
												.addComponent(textFieldStart, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(lblEnd, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
												.addComponent(textFieldEnd, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addComponent(lblInvalidEntry)
										.addGap(227)
										.addComponent(btnRoute, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(btnClear))
								.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)))
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
										.addComponent(lblStart)
										.addGap(6)
										.addComponent(textFieldStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(lblEnd)
										.addGap(6)
										.addComponent(textFieldEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(24)
										.addComponent(lblInvalidEntry))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(11)
										.addComponent(btnRoute))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(11)
										.addComponent(btnClear)))
						.addGap(18)
						.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 445, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnPreviousMap)
								.addComponent(btnNextMap))
						.addGap(35))
				);

		System.out.println("cSV a");
		GUIFront.changeStreetView(gl_contentPane, Constants.DEFAULT_STREET_IMAGE);

		//check if it is done loading then make the gui visible
		if(backend.splashFlag){	 
			setVisible(true);
		}
		pack();
	}


	public void  setColoring(String scheme){
		colors = allSchemes.setColorScheme(scheme);

		routeButtonColor = colors.getRouteButtonColor(); // update components
		otherButtonsColor = colors.getOtherButtonsColor();
		backgroundColor = colors.getMainBackColor();
		sideBarColor = colors.getSideBarColor();

		btnRoute.setBackground(routeButtonColor);
		slidePanel.setBackground(sideBarColor);
		panelMap.setBackground(backgroundColor);
		stepByStepUI.setBackground(sideBarColor);
		lblStepByStep.setBackground(sideBarColor);
		lblDistance.setBackground(sideBarColor);
		panelDirections.setBackground(sideBarColor);
		btnNextMap.setBackground(otherButtonsColor);
		btnPreviousMap.setBackground(otherButtonsColor);
		contentPane.setBackground(backgroundColor);
		btnClear.setBackground(otherButtonsColor);


	}
	public static void changeStreetView(GroupLayout gl_contentPane, String imagePath){

		try{
			mainPanel.remove(1); // remove 2nd tab
		} catch(IndexOutOfBoundsException e){
			// do nothing; there just isn't a 2nd tab 
		}

		// connect Street View Panel to mainPanel
		SLPanel streetViewSLPanel = new SLPanel();
		mainPanel.addTab("Street View", null, streetViewSLPanel, null);
		contentPane.setLayout(gl_contentPane);

		IProxyImage streetViewPath = new ProxyImage(imagePath);
		TweenPanel streetViewTweenPanel = new TweenPanel(new ArrayList<MapNode>(), streetViewPath , "3", Constants.STREET_PATH);

		SLConfig streetViewConfig = new SLConfig(streetViewSLPanel)
				.gap(10, 10)
				.row(1f).col(700).col(50) // 700xH | 50xH
				.place(0, 0, streetViewTweenPanel);

		streetViewSLPanel.initialize(streetViewConfig);
		System.out.println("end changing street view");
	}

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

		//Color Scheme
		mnColorScheme = new JMenu("Color Scheme");
		mnOptions.add(mnColorScheme);

		mntmDefaultCampus = new JMenuItem("Default Campus");
		mntmDefaultCampus.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("Default Campus");
			}
		});

		mntmGrayscale = new JMenuItem("Grayscale");
		mntmGrayscale.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("Greyscale"); // set the color scheme to grayscale
			}
		});


		mntmWPI = new JMenuItem("WPI Theme");
		mntmWPI.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("WPI Default"); // set the color scheme to grayscale
			}
		});


		mntmSkyBlue = new JMenuItem("Sky Blue");


		mnColorScheme.add(mntmDefaultCampus);
		mnColorScheme.add(mntmGrayscale);
		mnColorScheme.add(mntmWPI);

		// ---- Options -----
		mnLocations = new JMenu("Locations");
		menuBar.add(mnLocations);

		// Atwater Kent
		mnAtwaterKent = new JMenu("Atwater Kent");
		mntmAK1 = new JMenuItem("Floor 1");
		mntmAK1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(0).getMapImageName());
				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(0).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(0).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(0));

				double[] tempPan = panValues.get("AK1.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);

			}
		});
		mntmAK2 = new JMenuItem("Floor 2");
		mntmAK2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(1).getMapImageName());
				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(1).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(1).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(1));

				double[] tempPan = panValues.get("AK2.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmAK3 = new JMenuItem("Floor 3");
		mntmAK3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(2).getMapImageName());
				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(2).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(2).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(2));

				double[] tempPan = panValues.get("AK3.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmAKB = new JMenuItem("Basement");
		mntmAKB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(3).getMapImageName());
				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(3).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(3).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(3));

				double[] tempPan = panValues.get("AKB.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
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
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(4).getMapImageName());
				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(4).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(4).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(4));

				double[] tempPan = panValues.get("Boy1.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmBoy2 = new JMenuItem("Floor 2");
		mntmBoy2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(5).getMapImageName());
				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(5).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(5).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(5));

				double[] tempPan = panValues.get("Boy2.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmBoy3 = new JMenuItem("Floor 3");
		mntmBoy3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(6).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(6).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(6).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(6));

				double[] tempPan = panValues.get("Boy3.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmBoyB = new JMenuItem("Basement");
		mntmBoyB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(7).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(7).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(7).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(7));

				double[] tempPan = panValues.get("BoyB.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
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
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(8).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(8).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(8).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(8));

				double[] tempPan = panValues.get("CC1.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmCC2 = new JMenuItem("Floor 2");
		mntmCC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(9).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(9).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(9).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(9));

				double[] tempPan = panValues.get("CC2.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmCC3 = new JMenuItem("Floor 3");
		mntmCC3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(10).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(10).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(10).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(10));

				double[] tempPan = panValues.get("CC3.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
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
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(11).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(11).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(11).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(11));

				double[] tempPan = panValues.get("CCM.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});

		// Gordon Library
		mnGordonLibrary = new JMenu("Gordon Library");
		mntmGL1 = new JMenuItem("Floor 1");
		mntmGL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(12).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(12).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(12).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(12));

				double[] tempPan = panValues.get("GL1.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmGL2 = new JMenuItem("Floor 2");
		mntmGL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(13).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(13).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(13).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(13));

				double[] tempPan = panValues.get("GL2.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmGL3 = new JMenuItem("Floor 3");
		mntmGL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(14).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(14).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(14).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(14));

				double[] tempPan = panValues.get("GL3.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmGLB = new JMenuItem("Basement");
		mntmGLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(15).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(15).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(15).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(15));

				double[] tempPan = panValues.get("GLB.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmGLSB = new JMenuItem("Sub Basement");
		mntmGLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(16).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(16).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(16).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(16));

				double[] tempPan = panValues.get("GLSB.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
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
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(17).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(17).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(17).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(17));

				double[] tempPan = panValues.get("HH1.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmHH2 = new JMenuItem("Floor 2");
		mntmHH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(18).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(18).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(18).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(18));

				double[] tempPan = panValues.get("HH2.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmHH3 = new JMenuItem("Floor 3");
		mntmHH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(19).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(19).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(19).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(19));

				double[] tempPan = panValues.get("HH3.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
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
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(20).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(20).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(20).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(20));

				double[] tempPan = panValues.get("HHG1.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmHHG2 = new JMenuItem("Floor 2");
		mntmHHG2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(21).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(21).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(21).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(21));

				double[] tempPan = panValues.get("HHG2.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
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
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(22).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(22).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(22).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(22));

				double[] tempPan = panValues.get("PC1.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmPC2 = new JMenuItem("Floor 2");
		mntmPC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(23).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(23).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(23).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(23));

				double[] tempPan = panValues.get("PC2.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
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
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(24).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(24).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(24).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(24));

				double[] tempPan = panValues.get("SH1.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmSH2 = new JMenuItem("Floor 2");
		mntmSH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(25).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(25).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(25).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(25));

				double[] tempPan = panValues.get("SH2.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmSH3 = new JMenuItem("Floor 3");
		mntmSH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(26).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(26).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(26).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(26));

				double[] tempPan = panValues.get("SH3.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
			}
		});
		mntmSHB = new JMenuItem("Basement");
		mntmSHB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(27).getMapImageName());

				panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(27).getMapImageName()));
				panelMap.setMapNodes(globalMap.getLocalMaps().get(27).getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

				backend.setLocalMap(globalMap.getLocalMaps().get(27));

				double[] tempPan = panValues.get("SHB.png");
				panelMap.panX = tempPan[0];
				panelMap.panY = tempPan[1];

				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() - panelMap.panX);
					n.setYPos(n.getYPos() - panelMap.panY);
				}

				panelMap.panX = 0.0;
				panelMap.panY = 0.0;
				panelMap.setScale(1.0);
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

	/**
	 * Enable/Disable actions
	 */
	public void enableActions(){
		for (TweenPanel panel : panels){
			panel.enableAction();
		}
		panelDirections.enableAction();
	}
	public void disableActions(){
		for (TweenPanel panel : panels){
			panel.disableAction();
		}
		panelDirections.disableAction();
	}

	/**
	 * The animation functions come in pairs of action and back-action. This tells the engine where to move it, and 
	 * if it needs to move other panels 
	 */	
	// Step by Step Panel
	private final Runnable panelDirectionsAction = new Runnable() {
		@Override 
		public void run() {
			disableActions();
			currentlyOpen = true;

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

			/**
			 * @author Andrew Petit
			 * @description Resets all of the relevant information on the form and the background information
			 */
			public void reset() {
				allowSetting = true; //allow user to re place nodes only once reset is pressed
				globalMap.getStartNode().getLocalMap().setStart(null);
				if (globalMap.getEndNode() != null){
					globalMap.getEndNode().getLocalMap().setEnd(null);
					globalMap.setEndNode(null);
				}
				globalMap.setStartNode(null);
				globalMap.getAllNodes().clear();
				reset = true;
				listModel.removeAllElements(); // clear directions

				// allows the user to re-input start and end nodes
				setEnd = false;
				setStart = false;
				paths.clear();
				listOfPaths.clear();
				if (listOfRoutes != null){
					listOfRoutes.clear();
				}
				backend.removePath(globalMap.getChosenNodes());
				btnNextMap.setEnabled(false);
				btnPreviousMap.setEnabled(false);

				globalMap.getChosenNodes().clear();
				lblDistance.setText("");
				btnClear.setEnabled(false);
				btnRoute.setEnabled(false);
				//btnEmail.setEnabled(false); -- for some reason this does not work -- will be looking into...
				removeLine = true;
			}

			/**
			 * A class to handle zooming based on scrolling of the mouse wheel. 
			 * Current implementation allows for between 50% and 200% zoom. Anything less than 50%
			 * with the current map sizes makes the images disappear.
			 * TODO: Potentially add double click functionality ? 
			 * TODO: Potentially add button functionality ?
			 * 
			 * @author Gatrie
			 */
			class ZoomHandler implements MouseWheelListener {

				double zoomAmount;

				public ZoomHandler(){
					this.zoomAmount = 1; // the amount to zoom 
				}

				@Override
				public void mouseWheelMoved(MouseWheelEvent mwe) {
					int direction = mwe.getWheelRotation();

					if(direction < 0){ // moving up, so zoom in	(no greater than 100%)
						if(zoomAmount <= (.9 + .001))
							zoomAmount += 0.1;
					} else { // moving down, zoom out (no less than 50%)
						if(zoomAmount >= 0.5)
							zoomAmount -= 0.1;
					}

					panelMap.setScale(zoomAmount);
				}

			}

			/**
			 * Handles events related to panning the map image efficiently. 
			 * Created with reference to code at: http://web.eecs.utk.edu/
			 * @author Trevor
			 */
			class PanHandler implements MouseListener, MouseMotionListener {
				double startX, startY; // reference points of original transformation
				AffineTransform startTransform; // original state of transformation

				@Override
				public void mouseDragged(MouseEvent me) {
					// now we want to start in reference to the initial transformation of THIS object, ie startTransform
					try {
						mainReferencePoint = startTransform.inverseTransform(me.getPoint(), null);
					} catch (NoninvertibleTransformException e){
						e.printStackTrace();
					}

					// Now figure out the difference
					double distanceMovedX = mainReferencePoint.getX() - startX;
					double distanceMovedY = mainReferencePoint.getY() - startY;

					// reset the start points to the clicked point (remember, this is stored in mainReferencePoint)
					startX = mainReferencePoint.getX();
					startY = mainReferencePoint.getY();

					panelMap.panX += distanceMovedX;
					panelMap.panY += distanceMovedY;

					// Update the map node locations relative to the map image
					for(MapNode n : backend.getLocalMap().getMapNodes()){
						n.setXPos(n.getXPos() + distanceMovedX);
						n.setYPos(n.getYPos() + distanceMovedY);

					}

					/**
					 * Handles events related to panning the map image efficiently. 
					 * Created with reference to code at: http://web.eecs.utk.edu/
					 * @author Trevor
					 */
					MapNode tmpStart, tmpEnd; // temporary variables for clarity

				}

				/**
				 * Will save the point clicked at and the state of the initial transformation as
				 * panning is likely to occur.
				 */
				@Override
				public void mousePressed(MouseEvent me) {
					/**
					 * Suppose that T:U->V is a linear transformation. If there is a function S:V->U such that
					 *	S*T=I   T*S=I, then T is invertible.
					 *	
					 *	Check to make sure the current transformation is invertible and get that point
					 */
					try {
						mainReferencePoint = transform.inverseTransform(me.getPoint(), null);
					} catch (NoninvertibleTransformException e) {
						e.printStackTrace();
					}

					// save the starting points and initial transformation
					startX = mainReferencePoint.getX();
					startY = mainReferencePoint.getY();
					startTransform = transform;
				}

				@Override
				public void mouseReleased(MouseEvent me) {
				}

				@Override
				public void mouseMoved(MouseEvent arg0) {
					// TODO Auto-generated method stub
				}
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
				}

			}

			public static class TweenPanel extends JPanel {
				//ColorSchemes allSchemes = new ColorSchemes();  
				//ColorSetting colors = allSchemes.setColorScheme("Default Campus");


				ArrayList<MapNode> localNodes;
				public ArrayList<MapNode> chosenNodes;

				private final TweenManager tweenManager = SLAnimator.createTweenManager();
				private JLabel labelMainPanel = new JLabel();
				private JLabel labelStep = new JLabel();
				private IProxyImage mapImage;
				private Runnable action;
				private boolean actionEnabled = true;
				private boolean hover = false;
				private int borderThickness = 2;
				private String panelID;

				double panX, panY;
				double zoomRatio;

				String packageName;
				boolean shouldPaint;

				/**
				 * Class for a custom panel to do drawing and tweening. This can be seperated into a seperate class file
				 * but it functions better as a private class
				 */
				public TweenPanel(ArrayList<MapNode> mapNodes, IProxyImage mapPath, String panelId, String packageName){
					// determine whether anything should be painted onto this tab
					if(packageName.equals(Constants.STREET_PATH))
						this.shouldPaint = false;
					else
						this.shouldPaint = true;

					this.packageName = packageName;

					setLayout(new BorderLayout());

					globalMap.setAllNodes(globalMap.getMapNodes());

					this.localNodes = mapNodes;

					labelMainPanel.setFont(new Font("Sans", Font.BOLD, 90));
					labelMainPanel.setVerticalAlignment(SwingConstants.CENTER);
					labelMainPanel.setHorizontalAlignment(SwingConstants.CENTER);
					labelMainPanel.setText(panelID);

					this.mapImage = mapPath;
					this.panelID = panelID;

					zoomRatio = 1;

					addMouseListener(panHandle);
					addMouseMotionListener(panHandle);
					addMouseWheelListener(zoomHandle);

					addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent me) {
							if (allowSetting == true){
								// figure out where the closest map node is, set that node as a startnode the StartingNode
								MapNode node = backend.findNearestNode(mainReferencePoint.getX() + panX, mainReferencePoint.getY() + panY, backend.getLocalMap());
								System.out.println("Node found is: " + node.getNodeID());

								if(globalMap.getChosenNodes().size() == 0){
									globalMap.setStartNode(node);
									globalMap.getChosenNodes().add(node);
									globalMap.getAllNodes().add(node);
									backend.getLocalMap().setStart(node);
									System.out.println("This has happened");
									btnClear.setEnabled(true);
								}
								else{
									if(globalMap.getChosenNodes().size() == 1){
										globalMap.getChosenNodes().add(node);
										globalMap.setEndNode(node);
										backend.getLocalMap().setEnd(node);
									} else {
										MapNode endNode = globalMap.getEndNode();
										LocalMap localMap = endNode.getLocalMap();
										for (LocalMap localmap : globalMap.getLocalMaps()){
											if (localMap == localmap){
												localmap.setEnd(null);
											}
										}
										globalMap.getChosenNodes().add(node);
										globalMap.setEndNode(node);
										backend.getLocalMap().setEnd(node);
									}
								}
								// Enable the route button if both start and end have been set
								if(globalMap.getStartNode() != null && globalMap.getEndNode() != null)
									btnRoute.setEnabled(true);
							}
							repaint();
						}	
					});
				}

				/**
				 * Constructor for Step by Step Directions panel. There needs to be two seperate ones as they both don't need map images
				 */
				public TweenPanel(String panelID) {
					setLayout(new BorderLayout());

					labelStep.setFont(new Font("Sans", Font.BOLD, 90));
					labelMainPanel.setVerticalAlignment(SwingConstants.CENTER);
					labelMainPanel.setHorizontalAlignment(SwingConstants.CENTER);
					labelMainPanel.setText("Step by Step Directions");

					this.panelID = panelID;

					addMouseListener(new MouseAdapter() {
						@Override
						public void mouseEntered(MouseEvent e) {
							hover = true;
							if (actionEnabled) 
								showBorder();
						}

						@Override
						public void mouseExited(MouseEvent e) {
							hover = false;
							hideBorder();
						}

						@Override
						public void mouseReleased(MouseEvent e) {
							if (action != null && actionEnabled)
								action.run();
						}
					});

				}

				public ArrayList<MapNode> getMapNodes() {
					return localNodes;
				}

				public void setMapNodes(ArrayList<MapNode> localNodes) {
					this.localNodes = localNodes;
				}

				public Image getMapImage() {
					return mapImage.getImage(packageName);
				}

				public void setMapImage(IProxyImage mapImage) {
					this.mapImage = mapImage;
				}
				public void setColors(String scheme){
					colors = allSchemes.setColorScheme(scheme);
				}

				/**
				 * Sets the action of the panel
				 * @param action The action (or animation) to perform
				 */
				public void setAction(Runnable action) {
					this.action = action;
				}

				/**
				 * Enables the component to do an action and if mouse is hovering highlight the border
				 */
				public void enableAction() {
					actionEnabled = true; 
					if (hover)
						showBorder();
				}

				/**
				 * Disable the component from doing any actions
				 */
				public void disableAction() {
					actionEnabled = false;
				}

				/**
				 * Actual tween animation to show the border. Will highlight with specified border thickness
				 */
				private void showBorder() {
					tweenManager.killTarget(borderThickness);
					Tween.to(TweenPanel.this, Accessor.BORDER_THICKNESS, 0.4f)
					.target(10)
					.start(tweenManager);
				}

				/**
				 * Hides the highlighted border once the mouse leaves the component
				 */
				private void hideBorder() {
					tweenManager.killTarget(borderThickness);
					Tween.to(TweenPanel.this, Accessor.BORDER_THICKNESS, 0.4f)
					.target(2)
					.start(tweenManager);
				}

				@Override
				protected void paintComponent(Graphics g) {
					Color startNodeColor = colors.getStartNodeColor();
					Color endNodeColor = colors.getEndNodeColor();
					Color lineColor = colors.getLineColor();
					Color outlineColor = colors.getOutlineColor();

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

					else {
						// Save the current transformed state incase something goes wrong
						AffineTransform saveTransform = graphics.getTransform();
						transform = new AffineTransform(saveTransform);

						// account for changes in zoom
						transform.translate(getWidth() / 2, getHeight() /2);
						transform.scale(zoomRatio, zoomRatio);
						transform.translate(-getWidth() / 2, -getHeight() / 2);

						transform.translate(panX, panY); // move to designated location
						graphics.setTransform(transform);

						// Scale the map relative to the panels current size and your current viewing window
						graphics.drawImage(this.mapImage.getImage(packageName), 0, 0, this);	

						// Colors start and end differently
						// Draws the map and places pre-existing node data onto the map as
						// well start and end nodes if they have been set
						graphics.drawImage(this.mapImage.getImage(packageName), 0, 0, this);

						// Sets the color of the start and end nodes to be different for each new waypoint
						if(this.shouldPaint){
							// Sets the color of the start and end nodes to be different
							graphics.setColor(startNodeColor);

							if(!(paths.isEmpty())){
								if (paths.get(index).get(0) != null){
									graphics.setColor(startNodeColor);
									graphics.fillOval((int) paths.get(index).get(0).getXPos() - (int)panX - 5, (int) paths.get(index).get(0).getYPos() - (int)panY - 5, 10, 10);
									graphics.setColor(outlineColor);
									graphics.drawOval((int) paths.get(index).get(0).getXPos() - (int)panX - 5, (int) paths.get(index).get(0).getYPos() - (int)panY - 5, 10, 10);

								}
								if (paths.get(index).get(paths.get(index).size() - 1) != null){
									graphics.setColor(endNodeColor);
									graphics.fillOval((int) paths.get(index).get(paths.get(index).size() - 1).getXPos() - (int)panX - 5, (int) paths.get(index).get(paths.get(index).size() - 1).getYPos() - (int)panY - 5, 10, 10);
									graphics.setColor(outlineColor);
									graphics.drawOval((int) paths.get(index).get(paths.get(index).size() - 1).getXPos() - (int)panX - 5, (int) paths.get(index).get(paths.get(index).size() - 1).getYPos() - (int)panY - 5, 10, 10);

								}
							}
							if (globalMap.getStartNode() != null){
								if (globalMap.getStartNode().getLocalMap() == backend.getLocalMap()){
									graphics.setColor(startNodeColor);
									graphics.fillOval((int) backend.getLocalMap().getStart().getXPos() - (int)panX - 5, (int) backend.getLocalMap().getStart().getYPos() - (int)panY - 5, 10, 10);
									graphics.setColor(outlineColor);
									graphics.drawOval((int) globalMap.getStartNode().getXPos() - (int)panX - 5, (int) globalMap.getStartNode().getYPos() - (int)panY - 5, 10, 10);

								}

								if(globalMap.getEndNode() != null){
									if (globalMap.getEndNode().getLocalMap() == backend.getLocalMap()){
										graphics.setColor(endNodeColor);
										graphics.fillOval((int) globalMap.getEndNode().getXPos() - (int)panX - 5, (int) globalMap.getEndNode().getYPos() - (int)panY - 5, 10, 10);
									}
									graphics.setColor(outlineColor);
									graphics.drawOval((int) globalMap.getEndNode().getXPos() - (int)panX - 5, (int) globalMap.getEndNode().getYPos() - (int)panY - 5, 10, 10);

								}
								if (globalMap.getChosenNodes().size() > 2){
									for (int i = 1; i < globalMap.getChosenNodes().size() - 1; i++){
										if (globalMap.getChosenNodes().get(i).getLocalMap() == backend.getLocalMap()){
											graphics.setColor(Color.ORANGE);
											graphics.fillOval((int) globalMap.getChosenNodes().get(i).getXPos() - (int)panX - 5, (int) globalMap.getChosenNodes().get(i).getYPos() - (int)panY - 5, 10, 10);
										}
									}
								}


								if (GUIFront.drawLine = true) { // this should have a "=" not "=="
									if (paths.isEmpty()) {
										for (int i = 0; i < mapnodes.size() - 1; i++) {
											double x1 = backend.getCoordinates(mapnodes).get(i)[0];
											double y1 = backend.getCoordinates(mapnodes).get(i)[1];
											double x2 = backend.getCoordinates(mapnodes).get(i + 1)[0];
											double y2 = backend.getCoordinates(mapnodes).get(i + 1)[1];
											double alpha = 0.5;
											Color color = new Color(0, 1, 1, (float) alpha);
											Graphics2D g2 = (Graphics2D) g;
											g2.setStroke(new BasicStroke(5));
											g2.setColor(lineColor);
											g2.drawLine((int) x1 - (int)panX, (int) y1 - (int)panY, (int) x2 - (int)panX, (int) y2 - (int)panY);
										}
										drawLine = false;
										removeLine = true;
									} 
									else {
										for (int i = 0; i < thisRoute.size() - 1; i++) {
											double x1 = backend.getCoordinates(thisRoute).get(i)[0];
											double y1 = backend.getCoordinates(thisRoute).get(i)[1];
											double x2 = backend.getCoordinates(thisRoute).get(i + 1)[0];
											double y2 = backend.getCoordinates(thisRoute).get(i + 1)[1];
											double alpha = 0.5;
											Color color = new Color(0, 1, 1, (float) alpha);
											Graphics2D g2 = (Graphics2D) g;
											g2.setStroke(new BasicStroke(5));
											g2.setColor(lineColor);
											g2.drawLine((int) x1 - (int)panX, (int) y1 - (int)panY, (int) x2 - (int)panX, (int) y2 - (int)panY);
										}
										drawLine = false;
										removeLine = true;
									}
								} else if (GUIFront.removeLine == true) {
									if (paths.isEmpty()){
										for (int i = 0; i < mapnodes.size() - 1; i++) {
											double x1 = backend.getCoordinates(mapnodes).get(i)[0];
											double y1 = backend.getCoordinates(mapnodes).get(i)[1];
											double x2 = backend.getCoordinates(mapnodes).get(i + 1)[0];
											double y2 = backend.getCoordinates(mapnodes).get(i + 1)[1];
											double alpha = 0.5;
											Color color = new Color(0, 1, 1, (float) alpha);
											Graphics2D g2 = (Graphics2D) g;
											g2.setStroke(new BasicStroke(5));
											g2.setColor(lineColor);
											g2.drawLine((int) x1 - (int)panX, (int) y1 - (int)panY, (int) x2 - (int)panX, (int) y2 - (int)panY);
										}
										drawLine = true;
										removeLine = false;
									} else {
										for (int i = 0; i < thisRoute.size() - 1; i++) {
											double x1 = backend.getCoordinates(thisRoute).get(i)[0];
											double y1 = backend.getCoordinates(thisRoute).get(i)[1];
											double x2 = backend.getCoordinates(thisRoute).get(i + 1)[0];
											double y2 = backend.getCoordinates(thisRoute).get(i + 1)[1];
											Graphics2D g2 = (Graphics2D) g;
											g2.setStroke(new BasicStroke(5));
											g2.setColor(lineColor);
											g2.drawLine((int) x1 - (int)panX, (int) y1 - (int)panY, (int) x2 - (int)panX, (int) y2 - (int)panY);
										}
										drawLine = true;
										removeLine = false;
									}
								}
							}
							repaint();
							graphics.setTransform(saveTransform); // reset to original transform to prevent weird border mishaps
						}
					}
				}

				public String getID(){
					return this.panelID;
				}
				public void setScale(double scaleAmt){
					this.zoomRatio = scaleAmt;
				}

				/**
				 * Tween accessor class.
				 * This class handles all of the relevant information regarding the target components tweening information
				 */
				public static class Accessor extends SLAnimator.ComponentAccessor {
					public static final int BORDER_THICKNESS = 100;

					/**
					 * Gets the thickness values to be used in animation
					 * @param target The component we are creating an animation on
					 * @param tweenType A variable used to decide which kind of animation we want to do, in this case there's only one option
					 * @param returnValues A list of values containing the desired borderThickness to draw
					 * @return returnVal Inidicates success or failure
					 */
					@Override
					public int getValues(Component target, int tweenType, float[] returnValues) {
						TweenPanel tp = (TweenPanel) target;

						int ret = super.getValues(target, tweenType, returnValues);
						if (ret >= 0) return ret;

						switch (tweenType) {
						case BORDER_THICKNESS: returnValues[0] = tp.borderThickness; return 1;
						default: return -1;
						}
					}

					/**
					 * Sets the animation values to the specified 
					 * @param target The component we are creating an animation on
					 * @param tweenType A variable used to decide which kind of animation we want to do, in this case there's only one option
					 * @param newValues A list of values containing the desired borderThickness to draw, with a value at index 0
					 */
					@Override
					public void setValues(Component target, int tweenType, float[] newValues) {
						TweenPanel tp = (TweenPanel) target;

						super.setValues(target, tweenType, newValues);

						switch (tweenType) {
						case BORDER_THICKNESS:
							tp.borderThickness = Math.round(newValues[0]);
							tp.repaint();
							break;
						}
					}
				} // end Accessor Class

			} // end TweenPanel Class


}

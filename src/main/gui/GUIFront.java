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
import java.io.File;

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
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.*;
import main.util.Constants;
import main.util.GeneralUtil;
import main.util.ProxyImage;
import main.util.IProxyImage;
import main.util.PanelSave;
import main.util.Speaker;
import main.util.WrappableCellRenderer;

import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

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
	public static boolean drawLine2 = false;
	public static boolean drawLine3 = false;
	public static boolean removeLine = false;
	public static boolean reset = false;
	public static MapNode startNode = null, endNode = null;
	public static String allText = "";
	public static ArrayList<GUIBack> backends = new ArrayList<GUIBack>();
	public static ArrayList<MapNode> mapnodes = new ArrayList<MapNode>();
	public static ArrayList<ArrayList<MapNode>> paths = new ArrayList<ArrayList<MapNode>>();
	public static ArrayList<ArrayList<MapNode>> routes = new ArrayList<ArrayList<MapNode>>();
	public static JButton btnClear, btnRoute;
	private JButton btnPreviousMap, btnNextMap, btnNextStep, btnPreviousStep;
	public static boolean allowSetting = true;
	public static JTabbedPane mainPanel; 
	public static ArrayList<MapNode> allNodes;
	public static int index = 0;
	public static int index2 = 0;
	public static ArrayList<MapNode> thisRoute = new ArrayList<MapNode>();
	public static HashMap<String, double[]> panValues = new HashMap<String, double[]>();
	public static HashMap<String, double[]> defaults = new HashMap<String, double[]>();
	public static double offsetX = 0;
	public static double offsetY = 0;

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
	private static boolean[] mapViewButtons;

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
	private JMenuItem mntmDefaultCampus, mntmGrayscale, mntmWPI, mntmFlowerPower, mntmAllBlue;
	private JMenu mnAtwaterKent, mnBoyntonHall, mnCampusCenter, mnFullerLabs, mnGordonLibrary, mnHigginsHouse, mnHigginsHouseGarage, mnProjectCenter, mnStrattonHall;
	private JMenuItem mntmAK1, mntmAK2, mntmAK3, mntmAKB, mntmBoy1, mntmBoy2, mntmBoy3, mntmBoyB, mntmCC1, mntmCC2, mntmCC3, mntmCCM;
	private JMenuItem mntmFL1, mntmFL2, mntmFL3, mntmFLB, mntmFLSB;
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
		}

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

		// Adding default values for pan and zoom to the hashmap

		defaults.put("AK1.png", new double[]{-80.0, -125.0, 0.78}); // 0
		defaults.put("AK2.png", new double[]{-80.0, -130.0, 0.80});
		defaults.put("AK3.png", new double[]{-80.0, -120.0, 0.78});
		defaults.put("AKB.png", new double[]{-80.0, -180.0, 0.80}); // 3

		defaults.put("Boy1.png", new double[]{-80.0, -140.0, 0.90}); // 4
		defaults.put("Boy2.png", new double[]{-80.0, -140.0, 0.90});
		defaults.put("Boy3.png", new double[]{-80.0, -140.0, 0.90});
		defaults.put("BoyB.png", new double[]{-80.0, -140.0, 0.90}); // 7

		defaults.put("CC1.png", new double[]{-80.0, -110.0, 0.68}); // 8
		defaults.put("CC2.png", new double[]{-80.0, -110.0, 0.66});
		defaults.put("CC3.png", new double[]{-80.0, -120.0, 0.80}); // 10

		defaults.put("CCM.png", new double[]{-500.0, -250.0, 0.40}); // 11


		defaults.put("FL1.png", new double[]{-80.0, -110.0, 0.7}); // 12
		defaults.put("FL2.png", new double[]{-80.0, -110.0, 0.7}); // 13
		defaults.put("FL3.png", new double[]{-80.0, -110.0, 0.7}); // 14
		defaults.put("FLB.png", new double[]{-80.0, -110.0, 0.7}); // 15
		defaults.put("FLSB.png", new double[]{-80.0, -110.0, 0.7}); // 16

		defaults.put("GL1.png", new double[]{-80.0, -110.0, 0.74}); // 17
		defaults.put("GL2.png", new double[]{-80.0, -110.0, 0.74});
		defaults.put("GL3.png", new double[]{-80.0, -110.0, 0.74});
		defaults.put("GLB.png", new double[]{-80.0, -110.0, 0.74});
		defaults.put("GLSB.png", new double[]{-80.0, -110.0, 0.74}); //21

		defaults.put("HH1.png", new double[]{-80.0, -110.0, 0.65}); // 22
		defaults.put("HH2.png", new double[]{-80.0, -110.0, 0.65});
		defaults.put("HH3.png", new double[]{-80.0, -110.0, 0.65}); // 24

		defaults.put("HHG1.png", new double[]{-80.0, -95.0, 0.86}); // 25
		defaults.put("HHG2.png", new double[]{-80.0, -95.0, 0.86}); // 26

		defaults.put("PC1.png", new double[]{-80.0, -140.0, 0.80}); // 27
		defaults.put("PC2.png", new double[]{-80.0, -110.0, 0.82}); // 28

		defaults.put("SH1.png", new double[]{-80.0, -110.0, 0.82}); // 29
		defaults.put("SH2.png", new double[]{-80.0, -140.0, 0.90});
		defaults.put("SH3.png", new double[]{-80.0, -110.0, 0.90});

		defaults.put("SHB.png", new double[]{-80.0, -110.0, 0.90}); // 32

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
							globalMap.setEndNode(endNode);
							if (!setEnd) {
								globalMap.getChosenNodes().add(1, endNode);					
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
								globalMap.setEndNode(endNode);
								if (!setEnd) {
									globalMap.getChosenNodes().add(1, endNode);					
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
									startNode = mapnode; //set the startNode and then draw it on the ma									globalMap.setStartNode(startNode);
									if (!setStart) {
										globalMap.getChosenNodes().add(0, startNode);					
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
									globalMap.setEndNode(endNode);
									if (!setEnd) {
										globalMap.getChosenNodes().add(1, endNode);					
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
				if (textFieldStart.getText().equals("")){
					//will need some way to alert the user that they need to enter a start location
				} else if (!(textFieldStart.getText().equals(""))) {//if there is something entered check if the name is valid and then basically add the start node
					String startString = textFieldStart.getText();
					boolean valid = false;
					for (MapNode mapnode : backend.getLocalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
						if(startString.equals(mapnode.getAttributes().getOfficialName()) || mapnode.getAttributes().getAliases().contains(startString)){
							//if the startString is equal to the official name of the startString is one of a few accepted alias' we will allow the start node to be placed
							startNode = mapnode; //set the startNode and place it on the map
							globalMap.setStartNode(startNode);
							if (!setStart) {
								globalMap.getChosenNodes().add(0, startNode);
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
		mapViewButtons = new boolean[2];
		mainPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				if(mainPanel.getSelectedIndex() == 0){					
					btnRoute.setEnabled(mapViewButtons[0]);
					btnClear.setEnabled(mapViewButtons[1]);
				}
				else if(mainPanel.getSelectedIndex() == 1){
					storeButtonStates();

					btnClear.setEnabled(false);
					btnRoute.setEnabled(false);	
				}
			}

			private void storeButtonStates(){
				if(btnRoute.isEnabled()) mapViewButtons[0] = true;
				else mapViewButtons[0] = false;

				if(btnClear.isEnabled()) mapViewButtons[1] = true;
				else mapViewButtons[1] = false;
			}
		});

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
		btnRoute.setEnabled(false);
		btnRoute.setBackground(routeButtonColor);
		btnRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnRoute.isEnabled()) {

					allowSetting = false; //once calculate button is pressed user should not be allowed to replace nodes until the original line is removed
					allText = ""; //must set the initial text as empty every time calculate button is pressed
					Speaker speaker = new Speaker(Constants.BUTTON_PATH); //sets the speaker to play the designated sound for calculate route
					speaker.play();


					//refer to Andrew Petit if you get confused with the next 250 or so lines
					//get me routes for different maps -- waypoint functionallity is added here 
					ArrayList<MapNode> getNodesOnSameMap = new ArrayList<MapNode>(); //will add all nodes from the same map to this 
					ArrayList<ArrayList<String>> stepByStep = new ArrayList<ArrayList<String>>();
					ArrayList<String> directions = new ArrayList<String>();
					boolean hasWayPoint = true;
					for (int i = 0; i < globalMap.getChosenNodes().size() - 1; i++){ //for all nodes in chosen nodes (start, end, and waypoints included) 
						//if the nodes are on the same map add all those nodes into an arraylist together
						if (globalMap.getChosenNodes().get(i).getLocalMap() == globalMap.getChosenNodes().get(i + 1).getLocalMap()){
							ArrayList<MapNode> nodesOnSameMap = backend.runAStar(globalMap.getChosenNodes().get(i), globalMap.getChosenNodes().get(i + 1));
							if (globalMap.getChosenNodes().size() == i + 2){
								directions = backend.displayStepByStep(nodesOnSameMap, false); //no more waypoints 
							} else {
								directions = backend.displayStepByStep(nodesOnSameMap, true); //more waypoints
							}
							stepByStep.add(directions); //essentially makes a list of all step by step directions to be added to the jlist
							directions = new ArrayList<String>(); //reset directions
							for (MapNode node : nodesOnSameMap){
								getNodesOnSameMap.add(node);
							}
						} else { //if the next node in chosen nodes is not on the same local map as the previous node in chosen nodes
							if (!(getNodesOnSameMap.isEmpty())){ //check if getNodesOnSameMap is not empty and if it is not add the nodes from the first array gathered from running getmeroutes as the nodes in this array should be on the same map
								ArrayList<MapNode> wayPoints = new ArrayList<MapNode>();
								ArrayList<ArrayList<MapNode>> getNodesOnDifferentMap = backend.getMeRoutes(globalMap.getChosenNodes().get(i), globalMap.getChosenNodes().get(i + 1));
								//this is is needed to make waypoints compatible with step by step - basically just add all nodes gathered from getmeroutes and send that to step by step
								for (ArrayList<MapNode> mapnodes : getNodesOnDifferentMap){
									for (int k = 0; k < mapnodes.size() - 1; k++){
										if (globalMap.getChosenNodes().size() == i + 2){
											wayPoints.add(mapnodes.get(k));
											hasWayPoint = false; // no more waypoints
										} else {
											wayPoints.add(mapnodes.get(k));
											hasWayPoint = true; // more waypoints
										}
									}
								}
								directions = backend.displayStepByStep(wayPoints, hasWayPoint);
								stepByStep.add(directions);

								for (MapNode mapnode : getNodesOnDifferentMap.get(0)){ 
									getNodesOnSameMap.add(mapnode);
								}
								//need to make temporary getNodesOnSameMap add all those nodes to all the nodes that are in getNodesOnDifferentMap
								paths.add(getNodesOnSameMap);
								for (int k = 1; k < getNodesOnDifferentMap.size(); k++){ //now for the other lists that have been added into OnDifferentMap -- we need to add those ArrayLists into a new spot in paths as they should NOT be on the same local map
									paths.add(getNodesOnDifferentMap.get(k));
								}
								directions = new ArrayList<String>();
								wayPoints = new ArrayList<MapNode>();
								getNodesOnSameMap = new ArrayList<MapNode>(); //reinitialize getNodesOnSameMap to allow the user to place those nodes in the same index of path for the next time a node is placed on the same map 
							} else { //if getNodesOnSameMap was not empty we should just go ahead an the ArrayList<ArrayList<MapNode>> that getMeRoutes returns to the next index in paths as these nodes should not have the same local map 
								ArrayList<ArrayList<MapNode>> getNodesOnDifferentMap = backend.getMeRoutes(globalMap.getChosenNodes().get(i), globalMap.getChosenNodes().get(i + 1));
								ArrayList<MapNode> wayPoints = new ArrayList<MapNode>();
								for (ArrayList<MapNode> mapnodes : getNodesOnDifferentMap){ //same idea as before go through the list of all nodes from the arraylist of arraylist of mapnodes returned by getmerroutes send that arraylist to step by step
									for (int k = 0; k < mapnodes.size() - 1; k++){
										if (globalMap.getChosenNodes().size() == i + 2){
											wayPoints.add(mapnodes.get(k));
											hasWayPoint = false; //no more waypoints
										} else {
											wayPoints.add(mapnodes.get(k));
											hasWayPoint = true; //more waypoints
										}
									}
								}
								directions = backend.displayStepByStep(wayPoints, hasWayPoint);
								stepByStep.add(directions);
								for (ArrayList<MapNode> mapnodes : getNodesOnDifferentMap){
									paths.add(mapnodes);
								}
								directions = new ArrayList<String>();
								wayPoints = new ArrayList<MapNode>();
							}
						}
					}
					if (paths.isEmpty()){
						if (!(getNodesOnSameMap.isEmpty())){
							paths.add(getNodesOnSameMap);
						}
					} else {
						if (!(getNodesOnSameMap.isEmpty()) && (paths.get(paths.size() - 1).get(0).getLocalMap() == getNodesOnSameMap.get(0).getLocalMap())){
							for(MapNode mapnode : getNodesOnSameMap){
								paths.get(paths.size() - 1).add(mapnode);
							}
						} else { //if not, but getNodesOnSameMap is not empty we should just go ahead and add those nodes to another index in paths
							paths.add(getNodesOnSameMap);
						}
					}

					//reinitialize getNodesOnSameMap for the next time this fun method is run...
					getNodesOnSameMap = new ArrayList<MapNode>();

					//for each route set start and end values for that routes local map
					for (int i = 0; i < paths.size() - 1; i++){
						LocalMap localmap = paths.get(i).get(0).getLocalMap();
						if (localmap.getEnd() == null){
							int size = paths.get(i).size() - 1;
							localmap.setStart(paths.get(i).get(size));
						}
						if (localmap.getStart() == null){
							localmap.setEnd(paths.get(i).get(0));
						}
					}

					GUIFront.changeStreetView(gl_contentPane, paths.get(0).get(0).getLocalMap().getMapImageName());					

					//get the first route to allow calculate route to go back to the initial map when starting to show the route
					thisRoute = paths.get(0);
					//the following code is needed for panning, we must update the panX and panY every time the map changes 
					panelMap.setMapImage(new ProxyImage(paths.get(0).get(0).getLocalMap().getMapImageName()));
					panelMap.setMapNodes(paths.get(0).get(0).getLocalMap().getMapNodes());
					String previousMap = backend.getLocalMap().getMapImageName();
					panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});
					backend.setLocalMap(paths.get(0).get(0).getLocalMap());
					double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
					double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
					panelMap.panX = defPan[0];
					panelMap.panY = defPan[1];
					panelMap.setScale(defPan[2]);
					offsetX = defPan[0] - tempPan[0];
					offsetY = defPan[1] - tempPan[1];
					for(MapNode n : backend.getLocalMap().getMapNodes()){
						n.setXPos(n.getXPos() + offsetX);
						n.setYPos(n.getYPos() + offsetY);
					}					

					//set the initial index at 0 so that when pressing nextMap button you can scroll to the next map or previous map
					index = 0;

					//set the initial index 2 at 0 so that when pressing nextstep button you can go to the next step or previous step
					index2 = 0;
					//if we have more than one arraylist in paths this means that more than one map will be shown to the user
					if (paths.size() > 1){
						btnNextMap.setEnabled(true);
					}

					btnNextStep.setEnabled(true);

					//draw the line on the map
					drawLine = true;
					//set the initial distance as 0 
					int distance = 0;
					//update the step by step directions and distance for each waypoint added
					listModel.addElement("Welcome to the Era of Navigation!");
					for (ArrayList<String> strings: stepByStep){
						for (String string : strings) {
							listModel.addElement(string); // add it to the list model
						}
					}

					for (ArrayList<MapNode> wayPoints : paths){
						distance += backend.getDistance(wayPoints, true); //the boolean value should not matter here 
					}

					lblDistance.setText("Distance in feet:" + distance);

					btnClear.setEnabled(true);
					btnRoute.setEnabled(false);
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
		mainPanel.setTitleAt(0, "Map View");
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
				index2 = 0;
				drawLine2 = false;
				drawLine3 = false;
				btnPreviousStep.setEnabled(false);
				btnNextStep.setEnabled(true);
				LocalMap localMap = paths.get(index).get(0).getLocalMap();

				GUIFront.changeStreetView(gl_contentPane, localMap.getMapImageName());
				panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
				panelMap.setMapNodes(localMap.getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});
				backend.setLocalMap(localMap);

				double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
				double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
				panelMap.panX = defPan[0];
				panelMap.panY = defPan[1];
				panelMap.setScale(defPan[2]);
				offsetX = defPan[0] - tempPan[0];
				offsetY = defPan[1] - tempPan[1];
				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() + offsetX);
					n.setYPos(n.getYPos() + offsetY);
				}

				thisRoute = paths.get(index);
				drawLine = true;
			}
		});
		getContentPane().add(btnNextMap, BorderLayout.SOUTH);

		// Add buttons to move between two maps
		btnPreviousMap = new JButton("<-- Previous Map");
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
				index2 = 0;
				drawLine2 = false;
				drawLine3 = false;
				btnPreviousStep.setEnabled(false);
				btnNextStep.setEnabled(true);

				LocalMap localMap = paths.get(index).get(0).getLocalMap();

				GUIFront.changeStreetView(gl_contentPane, localMap.getMapImageName());

				panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
				panelMap.setMapNodes(localMap.getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});
				backend.setLocalMap(localMap);

				double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
				double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
				panelMap.panX = defPan[0];
				panelMap.panY = defPan[1];
				panelMap.setScale(defPan[2]);
				offsetX = defPan[0] - tempPan[0];
				offsetY = defPan[1] - tempPan[1];
				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() + offsetX);
					n.setYPos(n.getYPos() + offsetY);
				}

				thisRoute = paths.get(index);
				drawLine = true;
			}
		});
		getContentPane().add(btnPreviousMap, BorderLayout.SOUTH);

		btnNextStep = new JButton("Next Step->");
		btnNextStep.setEnabled(false);
		btnNextStep.setBackground(otherButtonsColor);
		btnNextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index2++;
				if (index2 <= 0){
					btnPreviousStep.setEnabled(false);
				}
				if (index2 < paths.get(index).size() - 1){
					btnNextStep.setEnabled(true);
				}

				if (index2 >= paths.get(index).size() - 1){
					btnNextStep.setEnabled(false);
				}
				if (index2 > 0){
					btnPreviousStep.setEnabled(true);
				}
				drawLine3 = false;
				drawLine2 = true;
			}
		});

		btnPreviousStep = new JButton("<-Previous Step");
		btnPreviousStep.setEnabled(false);
		btnPreviousStep.setBackground(otherButtonsColor);
		btnPreviousStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index2--;
				if (index2 <= 0){
					btnPreviousStep.setEnabled(false);
				}
				if (index2 < paths.get(index).size() - 1){
					btnNextStep.setEnabled(true);
				}

				if (index2 >= paths.get(index).size() - 1){
					btnNextStep.setEnabled(false);
				}
				if (index2 > 0){
					btnPreviousStep.setEnabled(true);
				}
				drawLine2 = false;
				drawLine3 = true;
			}
		});

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
																.addGap(37)
																.addComponent(btnPreviousStep)
																.addGap(75)
																.addComponent(btnNextStep)
																.addPreferredGap(ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
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
																				.addComponent(btnNextMap)
																				.addComponent(btnNextStep)
																				.addComponent(btnPreviousStep))
																				.addGap(35))
				);

		GUIFront.changeStreetView(gl_contentPane, Constants.DEFAULT_STREET_IMAGE);

		//check if it is done loading then make the gui visible
		if(backend.splashFlag){	 
			setVisible(true);
		}
		pack();
		setLocationRelativeTo(null);
		
		changeMapTo(11 ,0 ,0 ,1);
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

	}

	/*
	 * @author Nick Gigliotti
	 * 
	 * @param a list of MapNodes which represents all the nodes in a path across multiple local maps
	 * 
	 * @return a list of LocalMaps that are in the path of nodes give
	 */
	public ArrayList<LocalMap> createListOfMaps(ArrayList<ArrayList<MapNode>> path) {
		// Initializes list
		ArrayList<LocalMap> pathLocalMaps = new ArrayList<LocalMap>();

		for (ArrayList<MapNode> pathNodes: path) {
			// Iterates through the list of nodes in the inputed ArrayList
			for (MapNode node: pathNodes) {

				// If the return list doesn't contain the current nodes LocalMap, it adds it to the return list
				if (! pathLocalMaps.contains(node.getLocalMap())){
					pathLocalMaps.add(node.getLocalMap());
				}
			}
		}
		return pathLocalMaps;
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

				int holdIndex = index;
				PanelSave savePanel = new PanelSave();

				//TODO Find a variable that includes the list of nodes in a path
				ArrayList<LocalMap> pathLocalMaps = createListOfMaps(paths);
				index = 0;

				int countFiles = 1;

				// Goes through each of the maps in the path and captures and saves an image
				for (LocalMap local: pathLocalMaps) {
					savePanel.saveImage(panelMap, local.getMapImageName());
					if (index < paths.size() - 1){
						index ++;
					}
					if (index >= paths.size() - 1){
						index = 0;
					}


					LocalMap localMap = paths.get(index).get(0).getLocalMap();
					panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
					panelMap.setMapNodes(paths.get(index).get(0).getLocalMap().getMapNodes());
					String previousMap = backend.getLocalMap().getMapImageName();
					panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});
					backend.setLocalMap(localMap);

					double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
					double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
					panelMap.panX = defPan[0];
					panelMap.panY = defPan[1];
					panelMap.setScale(defPan[2]);
					offsetX = defPan[0] - tempPan[0];
					offsetY = defPan[1] - tempPan[1];
					for(MapNode n : backend.getLocalMap().getMapNodes()){
						n.setXPos(n.getXPos() + offsetX);
						n.setYPos(n.getYPos() + offsetY);
					}

					thisRoute = paths.get(index);
					drawLine = true;

					savePanel.saveImage(panelMap, "Map #" + countFiles + "_" + local.getMapImageName());
					countFiles ++;
					if (index < paths.size() - 1){
						index ++;
					}
					if (index >= paths.size() - 1){
						index = 0;
					}
				}
				System.out.println("Finished Saving Path Images");

				index = holdIndex;
				// Email Pop-Up
				EmailGUI newEmail = new EmailGUI();
				newEmail.setVisible(true); //Opens EmailGUI Pop-Up

				LocalMap localMap = paths.get(index).get(0).getLocalMap();
				panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
				panelMap.setMapNodes(paths.get(index).get(0).getLocalMap().getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});
				backend.setLocalMap(localMap);

				double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
				double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
				panelMap.panX = defPan[0];
				panelMap.panY = defPan[1];
				panelMap.setScale(defPan[2]);
				offsetX = defPan[0] - tempPan[0];
				offsetY = defPan[1] - tempPan[1];
				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() + offsetX);
					n.setYPos(n.getYPos() + offsetY);
				}

				thisRoute = paths.get(index);
				drawLine = true;
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

		mntmFlowerPower = new JMenuItem("Flower Power");
		mntmFlowerPower.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("Flower Power");
			}
		});


		mntmAllBlue = new JMenuItem("All Blue");
		mntmAllBlue.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("All Blue");
			}
		});

		mnColorScheme.add(mntmDefaultCampus);
		mnColorScheme.add(mntmGrayscale);
		mnColorScheme.add(mntmWPI);
		mnColorScheme.add(mntmFlowerPower);
		mnColorScheme.add(mntmAllBlue);

		// Atwater Kent
		mnAtwaterKent = new JMenu("Atwater Kent");
		mntmAK1 = new JMenuItem("Floor 1");
		mntmAK1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(0, 0, 0, 1);
			}
		});
		mntmAK2 = new JMenuItem("Floor 2");
		mntmAK2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				changeMapTo(1, 0, 0, 1);
			}
		});
		mntmAK3 = new JMenuItem("Floor 3");
		mntmAK3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(2, 0, 0, 1);
			}
		});
		mntmAKB = new JMenuItem("Basement");
		mntmAKB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				changeMapTo(3, 0, 0, 1);
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
				changeMapTo(4, 0, 0, 1);
			}
		});
		mntmBoy2 = new JMenuItem("Floor 2");
		mntmBoy2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				changeMapTo(5, 0, 0, 1);
			}
		});
		mntmBoy3 = new JMenuItem("Floor 3");
		mntmBoy3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				changeMapTo(6, 0, 0, 1);
			}
		});
		mntmBoyB = new JMenuItem("Basement");
		mntmBoyB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(7, 0, 0, 1);
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
				changeMapTo(8, 0, 0, 1);
			}
		});
		mntmCC2 = new JMenuItem("Floor 2");
		mntmCC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				changeMapTo(9, 0, 0, 1);
			}
		});
		mntmCC3 = new JMenuItem("Floor 3");
		mntmCC3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				changeMapTo(10, 0, 0, 1);
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
				changeMapTo(11, 0, 0, 1);
			}
		});

		// Fuller Labs
		mnFullerLabs = new JMenu("Fuller Labs");
		mntmFL1 = new JMenuItem("Floor 1");
		mntmFL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(12, 0, 0, 1);
			}
		});
		mntmFL2 = new JMenuItem("Floor 2");
		mntmFL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(13, 0, 0, 1);
			}
		});
		mntmFL3 = new JMenuItem("Floor 3");
		mntmFL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(14, 0, 0, 1);
			}
		});
		mntmFLB = new JMenuItem("Basement");
		mntmFLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(15, 0, 0, 1);
			}
		});
		mntmFLSB = new JMenuItem("Sub Basement");
		mntmFLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(16, 0, 0, 1);
			}
		});
		mnFullerLabs.add(mntmFL1);
		mnFullerLabs.add(mntmFL2);
		mnFullerLabs.add(mntmFL3);
		mnFullerLabs.add(mntmFLB);
		mnFullerLabs.add(mntmFLSB);

		// Gordon Library
		mnGordonLibrary = new JMenu("Gordon Library");
		mntmGL1 = new JMenuItem("Floor 1");
		mntmGL1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(17, 0, 0, 1);
			}
		});
		mntmGL2 = new JMenuItem("Floor 2");
		mntmGL2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				changeMapTo(18, 0, 0, 1);
			}
		});
		mntmGL3 = new JMenuItem("Floor 3");
		mntmGL3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				changeMapTo(19, 0, 0, 1);
			}
		});
		mntmGLB = new JMenuItem("Basement");
		mntmGLB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(20, 0, 0, 1);
			}
		});
		mntmGLSB = new JMenuItem("Sub Basement");
		mntmGLSB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(21, 0, 0, 1);
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
				changeMapTo(22, 0, 0, 1);
			}
		});
		mntmHH2 = new JMenuItem("Floor 2");
		mntmHH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				changeMapTo(23, 0, 0, 1);
			}
		});
		mntmHH3 = new JMenuItem("Floor 3");
		mntmHH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){				
				changeMapTo(24, 0, 0, 1);
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
				changeMapTo(25, 0, 0, 1);
			}
		});
		mntmHHG2 = new JMenuItem("Floor 2");
		mntmHHG2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(26, 0, 0, 1);
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
				changeMapTo(27, 0, 0, 1);
			}
		});
		mntmPC2 = new JMenuItem("Floor 2");
		mntmPC2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				changeMapTo(28, 0, 0, 1);
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
				changeMapTo(29, 0, 0, 1);
			}
		});
		mntmSH2 = new JMenuItem("Floor 2");
		mntmSH2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				changeMapTo(30, 0, 0, 1);
			}
		});
		mntmSH3 = new JMenuItem("Floor 3");
		mntmSH3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){	
				changeMapTo(31, 0, 0, 1);
			}
		});
		mntmSHB = new JMenuItem("Basement");
		mntmSHB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){			
				changeMapTo(32, 0, 0, 1);
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
		mnLocations.add(mnFullerLabs); // indices 12, 13, 14, 15, 16
		mnLocations.add(mnGordonLibrary); // indices: 17, 18, 19, 20, 21
		mnLocations.add(mnHigginsHouse); // indices: 22, 23, 24
		mnLocations.add(mnHigginsHouseGarage); //indices: 25, 26
		mnLocations.add(mnProjectCenter); // indices: 27, 28
		mnLocations.add(mnStrattonHall); // indices 29, 30, 31, 32

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
	}

	/**
	 * Changes the map to the .localmap at the given index in the file system
	 * @param index The index of the map to be loaded (AK = 0, SHB = 27)
	 * @param panX The default panning horizontal value for the map to be loaded
	 * @param panY The default panning vertical value for the map to be loaded
	 * @param scale The default zoom scale for the map to be loaded
	 */
	public static void changeMapTo(int index, int panX, int panY, double scale){
		GUIFront.changeStreetView(gl_contentPane, globalMap.getLocalMaps().get(index).getMapImageName());

		panelMap.setMapImage(new ProxyImage(globalMap.getLocalMaps().get(index).getMapImageName()));
		panelMap.setMapNodes(globalMap.getLocalMaps().get(index).getMapNodes());
		String previousMap = backend.getLocalMap().getMapImageName();
		panValues.put(previousMap, new double[]{panelMap.panX, panelMap.panY});

		backend.setLocalMap(globalMap.getLocalMaps().get(index));

		double[] tempPan = panValues.get(globalMap.getLocalMaps().get(index).getMapImageName());
		double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
		panelMap.panX = defPan[0];
		panelMap.panY = defPan[1];
		panelMap.setScale(defPan[2]);
		offsetX = defPan[0] - tempPan[0];
		offsetY = defPan[1] - tempPan[1];
		for(MapNode n : backend.getLocalMap().getMapNodes()){
			n.setXPos(n.getXPos() + offsetX);
			n.setYPos(n.getYPos() + offsetY);
		}
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
		}
	};

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
		}
	};

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
		thisRoute.clear();
		backend.removePath(globalMap.getChosenNodes());
		btnNextMap.setEnabled(false);
		btnPreviousMap.setEnabled(false);
		btnNextStep.setEnabled(false);
		btnPreviousStep.setEnabled(false);

		globalMap.getChosenNodes().clear();
		lblDistance.setText("");
		btnClear.setEnabled(false);
		btnRoute.setEnabled(false);
		drawLine2 = false;
		drawLine3 = false;
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
			this.zoomAmount = 0.5; // default zoom amount
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
		boolean isMapView;

		Polygon pCPolygon, aKPolygon, bPolygon, cCPolygon, fLPolygon, gLPolygon, hHPolygon, hHGPolygon, sHPolygon, fPolygon;

		/**
		 * Class for a custom panel to do drawing and tweening. This can be seperated into a seperate class file
		 * but it functions better as a private class
		 */
		public TweenPanel(ArrayList<MapNode> mapNodes, IProxyImage mapPath, 
				String panelId, String packageName){

			// determine whether anything should be painted onto this tab
			if(packageName.equals(Constants.STREET_PATH))
				this.isMapView = false;
			else
				this.isMapView = true;

			this.packageName = packageName;

			setLayout(new BorderLayout());

			globalMap.setAllNodes(globalMap.getMapNodes());

			this.localNodes = mapNodes;

			labelMainPanel.setFont(new Font("Sans", Font.BOLD, 90));
			labelMainPanel.setVerticalAlignment(SwingConstants.CENTER);
			labelMainPanel.setHorizontalAlignment(SwingConstants.CENTER);
			labelMainPanel.setText(panelID);

			this.mapImage = mapPath;

			if(this.isMapView){
				zoomRatio = zoomHandle.zoomAmount; // get the initialized zoom amount
			}
			else{
				zoomRatio = 1;
				panX = 0;
				panY = 0;
			}

			initializePolygons();

			addMouseListener(panHandle);
			addMouseMotionListener(panHandle);
			addMouseWheelListener(zoomHandle);

			if(this.isMapView){		
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent me) {
						if (allowSetting == true){

							// Reset the main reference point incase we are clicking away from a popup menu
							try {
								mainReferencePoint = transform.inverseTransform(me.getPoint(), null);
							} catch (NoninvertibleTransformException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							// figure out where the closest map node is, set that node as a startnode the StartingNode
							MapNode node = backend.findNearestNode(mainReferencePoint.getX() + panX, mainReferencePoint.getY() + panY, backend.getLocalMap());

							// {{

							//AK
							if(aKPolygon.contains(mainReferencePoint)){
								JPopupMenu popupMenu = new JPopupMenu();

								popupMenu.add(new JMenuItem("Atwater Kent"))
								.setFont(new Font("Helvetica", Font.BOLD, 12));
								popupMenu.addSeparator();

								popupMenu.add(new JMenuItem("Floor 1"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(0, 0, 0, 1); // atwater kent 1
									}
								});
								popupMenu.add(new JMenuItem("Floor 2"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(1, 0, 0, 1); // atwater kent 2
									}
								});
								popupMenu.add(new JMenuItem("Floor 3"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(2, 0, 0, 1); // atwater kent 3
									}
								});
								popupMenu.add(new JMenuItem("Basement"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(3, 0, 0, 1); // atwater kent basement
									}
								});

								popupMenu.show(panelMap, me.getX(), me.getY());							
								return; 
							}

							//Boynton
							if(bPolygon.contains(mainReferencePoint)){
								JPopupMenu popupMenu = new JPopupMenu();

								popupMenu.add(new JMenuItem("Boynton Hall"))
								.setFont(new Font("Helvetica", Font.BOLD, 12));
								popupMenu.addSeparator();

								popupMenu.add(new JMenuItem("Floor 1"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(4, 0, 0, 1); // boynton hall 1
									}
								});
								popupMenu.add(new JMenuItem("Floor 2"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(5, 0, 0, 1); // boynton hall 2
									}
								});
								popupMenu.add(new JMenuItem("Floor 3"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(6, 0, 0, 1); // boynton hall 3
									}
								});
								popupMenu.add(new JMenuItem("Basement"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(7, 0, 0, 1); // boynton hall basement
									}
								});

								popupMenu.show(panelMap, me.getX(), me.getY());
								return; 
							}

							//Campus Center
							if(cCPolygon.contains(mainReferencePoint)){
								JPopupMenu popupMenu = new JPopupMenu();

								popupMenu.add(new JMenuItem("Campus Center"))
								.setFont(new Font("Helvetica", Font.BOLD, 12));
								popupMenu.addSeparator();

								popupMenu.add(new JMenuItem("Floor 1"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(8, 0, 0, 1); // campus center 1
									}
								});
								popupMenu.add(new JMenuItem("Floor 2"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(9, 0, 0, 1); // campus center 2
									}
								});
								popupMenu.add(new JMenuItem("Floor 3"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(10, 0, 0, 1); // campus center 3
									}
								});

								popupMenu.show(panelMap, me.getX(), me.getY());
								return; 
							}

							// Fuller Labs
							if(fLPolygon.contains(mainReferencePoint)){
								JPopupMenu popupMenu = new JPopupMenu();

								popupMenu.add(new JMenuItem("Fuller Labs"))
								.setFont(new Font("Helvetica", Font.BOLD, 12));
								popupMenu.addSeparator();

								popupMenu.add(new JMenuItem("Floor 1"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(12, 0, 0, 1); // fuller labs 1
									}
								});	
								popupMenu.add(new JMenuItem("Floor 2"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(13, 0, 0, 1); // fuller labs 2
									}
								});	
								popupMenu.add(new JMenuItem("Floor 3"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(14, 0, 0, 1); // fuller labs 3
									}
								});	
								popupMenu.add(new JMenuItem("Basement"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(15, 0, 0, 1); // fuller labs basement
									}
								});	
								popupMenu.add(new JMenuItem("Sub Basement"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(16, 0, 0, 1); // fuller labs sub basement
									}
								});	

								popupMenu.show(panelMap, me.getX(), me.getY());
								return; 
							}								

							//Library
							if(gLPolygon.contains(mainReferencePoint)){
								JPopupMenu popupMenu = new JPopupMenu();

								popupMenu.add(new JMenuItem("Gordon Library"))
								.setFont(new Font("Helvetica", Font.BOLD, 12));
								popupMenu.addSeparator();

								popupMenu.add(new JMenuItem("Floor 1"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(17, 0, 0, 1); // library 1
									}
								});
								popupMenu.add(new JMenuItem("Floor 2"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(18, 0, 0, 1); // library 2
									}
								});
								popupMenu.add(new JMenuItem("Floor 3"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(19, 0, 0, 1); // library 3
									}
								});
								popupMenu.add(new JMenuItem("Basement"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(20, 0, 0, 1); // library basement
									}
								});

								popupMenu.add(new JMenuItem("Sub Basement"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(21, 0, 0, 1); // library sub basement
									}
								});

								popupMenu.show(panelMap, me.getX(), me.getY());
								return; 
							}

							//Higgins
							if(hHPolygon.contains(mainReferencePoint)){
								JPopupMenu popupMenu = new JPopupMenu();

								popupMenu.add(new JMenuItem("Higgins House"))
								.setFont(new Font("Helvetica", Font.BOLD, 12));
								popupMenu.addSeparator();

								popupMenu.add(new JMenuItem("Floor 1"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(22, 0, 0, 1); // higgins house 1
									}
								});
								popupMenu.add(new JMenuItem("Floor 2"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(23, 0, 0, 1); // higgins house 2
									}
								});
								popupMenu.add(new JMenuItem("Floor 3"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(24, 0, 0, 1); // higgins house 3
									}
								});


								popupMenu.show(panelMap, me.getX(), me.getY());
								return; 
							}

							//Project Center
							if(pCPolygon.contains(mainReferencePoint)){
								JPopupMenu popupMenu = new JPopupMenu();

								popupMenu.add(new JMenuItem("Project Center"))
								.setFont(new Font("Helvetica", Font.BOLD, 12));
								popupMenu.addSeparator();

								popupMenu.add(new JMenuItem("Floor 1"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(27, 0, 0, 1); // project center 1
									}
								});
								popupMenu.add(new JMenuItem("Floor 2"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(28, 0, 0, 1); // project center 2
									}
								});

								popupMenu.show(panelMap, me.getX(), me.getY());
								return; 
							}


							//Stratton
							if(sHPolygon.contains(mainReferencePoint)){
								JPopupMenu popupMenu = new JPopupMenu();

								popupMenu.add(new JMenuItem("Stratton Hall"))
								.setFont(new Font("Helvetica", Font.BOLD, 12));
								popupMenu.addSeparator();

								popupMenu.add(new JMenuItem("Floor 1"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(29, 0, 0, 1); // stratton hall 1
									}
								});
								popupMenu.add(new JMenuItem("Floor 2"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(30, 0, 0, 1); // stratton hall 2
									}
								});
								popupMenu.add(new JMenuItem("Floor 3"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(31, 0, 0, 1); // stratton hall 3
									}
								});

								popupMenu.add(new JMenuItem("Basement"))
								.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										changeMapTo(32, 0, 0, 1); // stratton hall basement 3
									}
								});

								popupMenu.show(panelMap, me.getX(), me.getY());
								return; 
							}
							// }}

						
							//refer to Andrew Petit if this doesn't make sense
							if(globalMap.getChosenNodes().size() == 0){//set the start node of the globalnodes list of chosenNodes if that list is empty
								globalMap.setStartNode(node);
								globalMap.getChosenNodes().add(node);
								globalMap.getAllNodes().add(node);
								backend.getLocalMap().setStart(node);//remember to set the start node of that localMap the user is currently on
								btnClear.setEnabled(true); //enable clear button if some node has been added
							}
							else{
								if(globalMap.getChosenNodes().size() == 1){//if only the start node has been placed, place the end node
									globalMap.getChosenNodes().add(node);
									globalMap.setEndNode(node);
									globalMap.getAllNodes().add(node);
									backend.getLocalMap().setEnd(node);//remember to set the start node of that localMap the user is currently on
									btnClear.setEnabled(true); //enable clear button if some node has been added
								}
								else{
									//this means we need to account for waypoints
									MapNode endNode = globalMap.getEndNode();
									LocalMap localMap = endNode.getLocalMap();
									for (LocalMap localmap : globalMap.getLocalMaps()){ //go back to the localMap we set to be the end, and re make it null as that node is no longer the globalMap's end node
										if (localMap == localmap){
											localmap.setEnd(null);
										}
									}
									globalMap.getChosenNodes().add(node);
									globalMap.setEndNode(node);
									backend.getLocalMap().setEnd(node); //re set the end node here to the new local map the user is on

								}
								// Enable the route button if both start and end have been set
								if(globalMap.getStartNode() != null && globalMap.getEndNode() != null)
									btnRoute.setEnabled(true); //enable the button only if the user has selected a start and a end location
							}

							repaint();

						}	
					}
				});
			}

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

		/**
		 * Block of code to initialize all of the polygons representing clickable regions on buildings. Seperated
		 * for readability.
		 */
		private void initializePolygons(){				
			//Atwater Kent
			aKPolygon = new Polygon();
			aKPolygon.addPoint(1109, 241);
			aKPolygon.addPoint(1067, 311);
			aKPolygon.addPoint(1089, 325);
			aKPolygon.addPoint(1082, 339);
			aKPolygon.addPoint(1159, 381);
			aKPolygon.addPoint(1168, 369);
			aKPolygon.addPoint(1189, 381);
			aKPolygon.addPoint(1229, 310);
			aKPolygon.addPoint(1195, 289);
			aKPolygon.addPoint(1176, 318);
			aKPolygon.addPoint(1128, 290);
			aKPolygon.addPoint(1144, 260);

			//Boynton
			bPolygon = new Polygon();
			bPolygon.addPoint(1044, 734);
			bPolygon.addPoint(1037, 775);
			bPolygon.addPoint(1065, 780);
			bPolygon.addPoint(1066, 773);
			bPolygon.addPoint(1117, 782);
			bPolygon.addPoint(1116, 787);
			bPolygon.addPoint(1127, 789);
			bPolygon.addPoint(1127, 783);
			bPolygon.addPoint(1134, 783);
			bPolygon.addPoint(1138, 754);
			bPolygon.addPoint(1072, 743);
			bPolygon.addPoint(1073, 739);

			//Campus Center
			cCPolygon = new Polygon();
			cCPolygon.addPoint(938, 346);
			cCPolygon.addPoint(920, 450);
			cCPolygon.addPoint(899, 448);
			cCPolygon.addPoint(910, 457);
			cCPolygon.addPoint(911, 467);
			cCPolygon.addPoint(904, 477);
			cCPolygon.addPoint(893, 479);
			cCPolygon.addPoint(882, 473);
			cCPolygon.addPoint(881, 458);
			cCPolygon.addPoint(875, 476);
			cCPolygon.addPoint(813, 466);
			cCPolygon.addPoint(821, 418);
			cCPolygon.addPoint(834, 420);
			cCPolygon.addPoint(850, 431);
			cCPolygon.addPoint(857, 422);
			cCPolygon.addPoint(850, 417);
			cCPolygon.addPoint(860, 405);
			cCPolygon.addPoint(854, 391);
			cCPolygon.addPoint(860, 383);
			cCPolygon.addPoint(875, 383);
			cCPolygon.addPoint(884, 372);
			cCPolygon.addPoint(873, 364);
			cCPolygon.addPoint(890, 340);

			// Fuller Labs
			fLPolygon = new Polygon();
			fLPolygon.addPoint(1225, 445);
			fLPolygon.addPoint(1301, 408);
			fLPolygon.addPoint(1284, 371);
			fLPolygon.addPoint(1305, 359);
			fLPolygon.addPoint(1274, 300);
			fLPolygon.addPoint(1242, 314);
			fLPolygon.addPoint(1255, 341);
			fLPolygon.addPoint(1211, 363);
			fLPolygon.addPoint(1220, 382);
			fLPolygon.addPoint(1199, 393);

			//Library
			gLPolygon = new Polygon();
			gLPolygon.addPoint(1245, 512);
			gLPolygon.addPoint(1304, 525);
			gLPolygon.addPoint(1279, 640);
			gLPolygon.addPoint(1220, 628);
			gLPolygon.addPoint(1226, 568);

			//Higgins House
			hHPolygon = new Polygon();
			hHPolygon.addPoint(800, 305);
			hHPolygon.addPoint(775, 288);
			hHPolygon.addPoint(787, 271);
			hHPolygon.addPoint(757, 250);
			hHPolygon.addPoint(766, 235);
			hHPolygon.addPoint(791, 250);
			hHPolygon.addPoint(808, 231);
			hHPolygon.addPoint(834, 246);
			hHPolygon.addPoint(847, 231);
			hHPolygon.addPoint(862, 241);
			hHPolygon.addPoint(849, 258);
			hHPolygon.addPoint(839, 253);

			// Higgins House Garage
			hHGPolygon = new Polygon();
			hHGPolygon.addPoint(875, 167);
			hHGPolygon.addPoint(890, 178);
			hHGPolygon.addPoint(870, 206);
			hHGPolygon.addPoint(855, 196);

			//project center
			pCPolygon = new Polygon();
			pCPolygon.addPoint(1019, 598);
			pCPolygon.addPoint(1030, 535);
			pCPolygon.addPoint(1068, 543);
			pCPolygon.addPoint(1056, 604);

			//Stratton
			sHPolygon = new Polygon();
			sHPolygon.addPoint(1014, 613);
			sHPolygon.addPoint(1052, 618);
			sHPolygon.addPoint(1038, 701);
			sHPolygon.addPoint(1000, 695);

			//Fuller
			fPolygon = new Polygon();
			fPolygon.addPoint(1225, 445);
			fPolygon.addPoint(1301, 408);
			fPolygon.addPoint(1284, 371);
			fPolygon.addPoint(1305, 359);
			fPolygon.addPoint(1274, 300);
			fPolygon.addPoint(1242, 314);
			fPolygon.addPoint(1255, 341);
			fPolygon.addPoint(1211, 363);
			fPolygon.addPoint(1220, 382);
			fPolygon.addPoint(1199, 393);

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
			double alpha = 0.25;
			Color color = new Color((float) 0, (float) .5, (float) 1, (float) alpha);
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
				} 
				else {
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
				//graphics.drawImage(this.mapImage.getImage(packageName), 0, 0, this);	

				// Colors start and end differently
				// Draws the map and places pre-existing node data onto the map as
				// well start and end nodes if they have been set
				graphics.drawImage(this.mapImage.getImage(packageName), 0, 0, this);

				// Sets the color of the start and end nodes to be different for each new waypoint
				if(this.isMapView){

					// if this is the campus map, draw the building polygons
					if(backend.getLocalMap().getMapImageName().equals(Constants.DEFAULT_MAP_IMAGE)){

						// Draw the panels over the building
						graphics.setColor(lineColor);
						graphics.setStroke(new BasicStroke (7));
						graphics.draw(aKPolygon);
						graphics.draw(bPolygon);
						graphics.draw(cCPolygon);
						graphics.draw(fLPolygon);
						graphics.draw(gLPolygon);
						graphics.draw(pCPolygon);
						graphics.draw(hHPolygon);
						graphics.draw(hHGPolygon);
						graphics.draw(sHPolygon);
					}

					// Sets the color of the start and end nodes to be different
					graphics.setStroke(new BasicStroke(1));
					graphics.setColor(startNodeColor);
					
					if(!(paths.isEmpty())){ //only try this if paths is not empty - otherwise this will result in errors
						if (paths.get(index).get(0) != null){ // make sure that the start node (which it should never be) is not null
							graphics.setColor(startNodeColor);
							graphics.fillOval((int) paths.get(index).get(0).getXPos() - (int)panX - 5, (int) paths.get(index).get(0).getYPos() - (int)panY - 5, 10, 10);
							graphics.setColor(outlineColor);
							graphics.drawOval((int) paths.get(index).get(0).getXPos() - (int)panX - 5, (int) paths.get(index).get(0).getYPos() - (int)panY - 5, 10, 10);
						}
						if (paths.get(index).get(paths.get(index).size() - 1) != null){ //make sure the end node (which it should never be) is not null
							graphics.setColor(endNodeColor);
							graphics.fillOval((int) paths.get(index).get(paths.get(index).size() - 1).getXPos() - (int)panX - 5, (int) paths.get(index).get(paths.get(index).size() - 1).getYPos() - (int)panY - 5, 10, 10);
							graphics.setColor(outlineColor);
							graphics.drawOval((int) paths.get(index).get(0).getXPos() - (int)panX - 5, (int) paths.get(index).get(0).getYPos() - (int)panY - 5, 10, 10);
						}
					}
					//drawing for originally placed nodes
					if (globalMap.getStartNode() != null){ //when globalMap start is updated place its position on the map if the localmap the user is on is where that node should be placed
						if (globalMap.getStartNode().getLocalMap() == backend.getLocalMap()){
							graphics.setColor(startNodeColor);
							graphics.fillOval((int) backend.getLocalMap().getStart().getXPos() - (int)panX - 5, (int) backend.getLocalMap().getStart().getYPos() - (int)panY - 5, 10, 10);
							graphics.setColor(outlineColor);
							graphics.drawOval((int) backend.getLocalMap().getStart().getXPos() - (int)panX - 5, (int) backend.getLocalMap().getStart().getYPos() - (int)panY - 5, 10, 10);
						}
					}

					if(globalMap.getEndNode() != null){ //when globalMap end is updated place its position on the map if the localMap the user is on is where that node should be placed
						if (globalMap.getEndNode().getLocalMap() == backend.getLocalMap()){
							graphics.setColor(endNodeColor);
							graphics.fillOval((int) globalMap.getEndNode().getXPos() - (int)panX - 5, (int) globalMap.getEndNode().getYPos() - (int)panY - 5, 10, 10);
							graphics.setColor(outlineColor);
							graphics.drawOval((int) globalMap.getEndNode().getXPos() - (int)panX - 5, (int) globalMap.getEndNode().getYPos() - (int)panY - 5, 10, 10);
						}
					}
					if (globalMap.getChosenNodes().size() > 2){ //check if there are waypoints -- if the user is on a map where one or more of these nodes should be placed than place them
						for (int i = 1; i < globalMap.getChosenNodes().size() - 1; i++){
							if (globalMap.getChosenNodes().get(i).getLocalMap() == backend.getLocalMap()){
								graphics.setColor(Color.ORANGE);
								graphics.fillOval((int) globalMap.getChosenNodes().get(i).getXPos() - (int)panX - 5, (int) globalMap.getChosenNodes().get(i).getYPos() - (int)panY - 5, 10, 10);
								graphics.setColor(outlineColor);
								graphics.drawOval((int) globalMap.getChosenNodes().get(i).getXPos() - (int)panX - 5, (int) globalMap.getChosenNodes().get(i).getYPos() - (int)panY - 5, 10, 10);
							}
						}
					}

					//this is where you draw the lines
					if (GUIFront.drawLine == true) {
						for (int i = 0; i < thisRoute.size() - 1; i++){//basically go through the current map and draw the lines for all links between nodes in a route on that map
							double x1 = backend.getCoordinates(thisRoute).get(i)[0];
							double y1 = backend.getCoordinates(thisRoute).get(i)[1];
							double x2 = backend.getCoordinates(thisRoute).get(i + 1)[0];
							double y2 = backend.getCoordinates(thisRoute).get(i + 1)[1];
							Graphics2D g2 = (Graphics2D) g;
							g2.setStroke(new BasicStroke(5));
							g2.setColor(lineColor);
							g2.drawLine((int) x1 - (int)panX, (int) y1 - (int)panY, (int) x2 - (int)panX, (int) y2 - (int)panY);
						}
						drawLine = false;
						removeLine = true;
						//this is where you remove the lines
					} else if (GUIFront.removeLine == true) { 
						for (int i = 0; i < thisRoute.size() - 1; i++) {//basically go through the current map and remove all lines for all links between nodes in a route on that map
							double x1 = backend.getCoordinates(thisRoute).get(i)[0];
							double y1 = backend.getCoordinates(thisRoute).get(i)[1];
							double x2 = backend.getCoordinates(thisRoute).get(i + 1)[0];
							double y2 = backend.getCoordinates(thisRoute).get(i + 1)[1];
							Graphics2D g2 = (Graphics2D) g;
							g2.setStroke(new BasicStroke(5));
							g2.setColor(lineColor); // you are going to want to make this transparent ... before next step button is added otherwise this will cause some issues
							g2.drawLine((int) x1 - (int)panX, (int) y1 - (int)panY, (int) x2 - (int)panX, (int) y2 - (int)panY);
						}
						drawLine = true;
						removeLine = false;
					}

					if (drawLine2 == true){
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(2));
						g2.setColor(Color.YELLOW);
						g2.drawLine((int) paths.get(index).get(index2 - 1).getXPos() - (int)panX, (int) paths.get(index).get(index2 - 1).getYPos() - (int)panY, (int) paths.get(index).get(index2).getXPos() - (int)panX, (int) paths.get(index).get(index2).getYPos() - (int)panY);
					}

					if (drawLine3 == true){
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(2));
						g2.setColor(Color.YELLOW);
						g2.drawLine((int) paths.get(index).get(index2 + 1).getXPos() - (int)panX, (int) paths.get(index).get(index2 + 1).getYPos() - (int)panY, (int) paths.get(index).get(index2).getXPos() - (int)panX, (int) paths.get(index).get(index2).getYPos() - (int)panY);
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

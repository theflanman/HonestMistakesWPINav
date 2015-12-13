package main.gui;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;

import javax.swing.JList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.*;
import main.gui.frontutil.ColorSchemes;
import main.gui.frontutil.ColorSetting;
import main.gui.frontutil.EmailGUI;
import main.gui.frontutil.PanHandler;
import main.gui.frontutil.TweenPanel;
import main.gui.frontutil.WrappableCellRenderer;
import main.gui.frontutil.ZoomHandler;
import main.util.Constants;
import main.util.GUIFrontUtil;
import main.util.PanelSave;
import main.util.Speaker;
import main.util.proxy.IProxyImage;
import main.util.proxy.ProxyImage;

import javax.swing.SwingConstants;

import javax.swing.ListCellRenderer;
import javax.swing.JComboBox;

import ca.odell.glazedlists.EventList;
//import ca.odell.glazedlists.BasicEventList;
//import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
//import ca.odell.glazedlists.TextFilterator;
//import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
//import ca.odell.glazedlists.swing.GlazedListsSwing;
import ca.odell.glazedlists.swing.EventComboBoxModel;

/**
 * This class contains code for the main applications GUI interface as well as
 * implementation for its various functionality such as drawing the route.
 * 
 * Tween code adapted from Aurelien Ribon's Sliding Layout Demo 
 * 
 * Note:
 * 	The "// {{" is CoffeeByte syntax for specifying code collapsing
 * 
 * @author Trevor
 */
@SuppressWarnings("serial")
public class GUIFront extends JFrame {

	// {{ Class Members
	public static GUIBack backend;
	private static GlobalMap globalMap;
	public static boolean drawLine = false;
	public static boolean drawLine2 = false;
	public static boolean drawLine3 = false;
	public static boolean drawNodes = true;
	public static boolean reset = false;
	public static MapNode startNode = null, endNode = null;
	public static String allText = "";
	public static ArrayList<GUIBack> backends = new ArrayList<GUIBack>();
	public static ArrayList<MapNode> mapnodes = new ArrayList<MapNode>();
	public static ArrayList<ArrayList<MapNode>> paths = new ArrayList<ArrayList<MapNode>>();
	public static ArrayList<ArrayList<MapNode>> routes = new ArrayList<ArrayList<MapNode>>();
	public static JButton btnClear, btnRoute;
	public static JComboBox start, end;
	private JButton btnPreviousMap, btnNextMap, btnNextStep, btnPreviousStep;
	public static boolean allowSetting = true;
	public static JTabbedPane mainPanel; 
	public static ArrayList<MapNode> allNodes;
	public static int index = 0;
	public static int index2 = 0;
	public static int index3 = 0;
	public static boolean btnNextStepWasPressedLast;
	public static int[] index3Help = {};
	public static ArrayList<MapNode> thisRoute = new ArrayList<MapNode>();
	public static HashMap<String, double[]> panValues = new HashMap<String, double[]>();
	public static HashMap<String, double[]> defaults = new HashMap<String, double[]>();
	public static double offsetX = 0;
	public static double offsetY = 0;

	public static double[] panNums = {0.0, 0.0};
	private static AffineTransform transform; // the current state of image transformation
	private static Point2D mainReferencePoint; // the reference point indicating where the click started from during transformation
	private static PanHandler panHandle;
	private static ZoomHandler zoomHandle;

	// MapPanel components
	private static JPanel contentPane;
	private JLabel lblStart, lblEnd;
	private static GroupLayout gl_contentPane;
	private static boolean[] mapViewButtons;

	// Directions Components
	private static JLabel lblStepByStep, lblClickHere, lblDistance, lblInvalidEntry;
	private static JScrollPane scrollPane;
	private static boolean currentlyOpen = false; // keeps track of whether the panel is slid out or not
	private DefaultListModel<String> listModel = new DefaultListModel<String>(); // Setup a default list of elements
	@SuppressWarnings("rawtypes")
	private ListCellRenderer renderer;
	private int MAX_LIST_WIDTH = 180; // maximum width of the list in pixels, the size of panelDirections is 200px
	private static JList<String> listDirections;

	// Menu Bar
	private JMenuBar menuBar;
	private JMenu mnFile, mnOptions, mnHelp, mnLocations;
	private JMenu mnColorScheme;
	private ArrayList<JMenuItem> mntmColorSchemes = new ArrayList<JMenuItem>();
	private ArrayList<JMenu> mnBuildings = new ArrayList<JMenu>();
	private JMenuItem mntmEmail, mntmExit;

	private SLPanel slidePanel;
	private JPanel stepByStepUI;
	public static ArrayList<TweenPanel> panels = new ArrayList<TweenPanel>();
	public static TweenPanel panelMap, panelDirections;
	private SLConfig mainConfig, panelDirectionsConfig;

	private static Color routeButtonColor;
	private static Color otherButtonsColor;
	private static Color backgroundColor;
	private static Color sideBarColor;
	private static ColorSchemes allSchemes;
	private static ColorSetting colors;
	// }}
	
	/** GUIFront Constructor
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public GUIFront(int numLocalMaps, File[] localMapFilenames) throws IOException, ClassNotFoundException {
		// main application is invisible during loading screen
		setVisible(false); 

		// initializes color schemes, GUIBack, local maps, and global map
		GUIFront.init(localMapFilenames);
		
		// This will setup the main JFrame to be maximized on start
		setTitle("Era of Navigation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1412, 743);
		setPreferredSize(new Dimension(820, 650));

		// Setup Pan and Zoom
		setPanHandle(new PanHandler());
		setZoomHandle(new ZoomHandler());

		
		//Initialize Start and End Combo Boxes 
		ArrayList<String>officialName = new ArrayList<String>();
		for(MapNode mapnode : globalMap.getMapNodes()){
			officialName.add(mapnode.getAttributes().getOfficialName());
		}
		start = new JComboBox(officialName.toArray());
		start.setEditable(true);
		AutoCompleteSupport.install(start, GlazedLists.eventListOf(officialName.toArray()));
		
		end = new JComboBox();
		end.setEditable(true);
		AutoCompleteSupport.install(end, GlazedLists.eventListOf(officialName.toArray()));
		
		
		// Initialize Top Menu Bar
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		initializeMenuBar();

		// Initialize Main Content Pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(backgroundColor);
		setContentPane(contentPane);
		
		// Adding default values for pan and zoom to the hashmap
		defaults = GUIFrontUtil.initPanZoom();

		// Image of the default map loaded into backend
		String defaultMapImage = Constants.DEFAULT_MAP_IMAGE;
		IProxyImage mapPath = new ProxyImage(defaultMapImage);
		lblInvalidEntry = new JLabel("Invalid Entry");
		lblInvalidEntry.setVisible(false);
		
		//when you press enter after entering stuff in textfieldStart
		Action actionStart = new AbstractAction()
		{
			@Override 
			public void actionPerformed(ActionEvent e)
			{
				if (globalMap.getStartNode() != null){
					LocalMap localmap = globalMap.getStartNode().getLocalMap();
					if (globalMap.getEndNode() != null){
						LocalMap localmapEnd = globalMap.getEndNode().getLocalMap();
						localmapEnd.setEndNode(null);
					}
					globalMap.getChosenNodes().clear();
					globalMap.setEndNode(null);
					localmap.setStartNode(null);
					globalMap.setStartNode(null);
				}
				String startString = (String) start.getSelectedItem();
				System.out.println(startString);
				lblInvalidEntry.setVisible(false);
				System.out.println("Enter was pressed");
				if (startString == null){
					//will need some way to alert the user that they need to enter a start location
					System.out.println("Need to enter a valid start location");
				} 
				else if (startString != null) {//if there is something entered check if the name is valid and then basically add the start node
					boolean valid = false;
					for (MapNode mapnode : getGlobalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
						if(startString.equals(mapnode.getAttributes().getOfficialName()) /*|| mapnode.getAttributes().getAliases().contains(startString)*/){
							//if the startString is equal to the official name of the startString is one of a few accepted alias' we will allow the start node to be placed
							btnClear.setEnabled(true); //enable clear button if some node has been added
							System.out.println("This is the starting node");
							mapnode.setXFeet(mapnode.getLocalMap().getMapScale()*mapnode.getXPos());
							mapnode.setYFeet(mapnode.getLocalMap().getMapScale()*mapnode.getYPos());
							mapnode.runTransform();
							if (getGlobalMap().getChosenNodes().isEmpty() && getGlobalMap().getChosenNodes() != null){
								getGlobalMap().setStartNode(mapnode);
								getGlobalMap().getChosenNodes().add(getGlobalMap().getStartNode());
							}
							LocalMap localmap = getGlobalMap().getStartNode().getLocalMap();
							localmap.setStartNode(getGlobalMap().getStartNode());
							panelMap.setMapImage(new ProxyImage(getGlobalMap().getStartNode().getLocalMap().getMapImageName()));
							panelMap.setMapNodes(getGlobalMap().getStartNode().getLocalMap().getMapNodes());
							String previousMap = backend.getLocalMap().getMapImageName();
							panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
							backend.setLocalMap(localmap);
							backend.getLocalMap().setStartNode(getGlobalMap().getStartNode());
							double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
							double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
							panelMap.setPanX(defPan[0]);
							panelMap.setPanY(defPan[1]);
							panelMap.setScale(defPan[2]);
							offsetX = defPan[0] - tempPan[0];
							offsetY = defPan[1] - tempPan[1];
							for(MapNode node : backend.getLocalMap().getMapNodes()){
								node.setXPos(node.getXPos() + offsetX);
								node.setYPos(node.getYPos() + offsetY);
							}	
							btnClear.setEnabled(true);
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
		
		Action actionEnd = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (globalMap.getEndNode() != null){
					LocalMap localmap = globalMap.getEndNode().getLocalMap();
					globalMap.getChosenNodes().remove(globalMap.getEndNode());
					localmap.setEndNode(null);
					globalMap.setEndNode(null);
				}
				JComboBox source = (JComboBox)e.getSource();
				String endString = (String) source.getSelectedItem();
				lblInvalidEntry.setVisible(false);
				//if the user presses enter without having entered anything in this box
				if (endString == null){
					System.out.println("Need to enter a valid start location"); 
				}else if (!(endString.equals(""))) { //if there is something entered check if the name is valid and then basically add the end node
					//String endString = (String) end.getSelectedItem(); //entered text = endString constant
					boolean valid = false;
					Attributes attribute = new Attributes();

					//Test if the entered information is a valid node in local map - this will be updated to global map when that is finished
					String startString = (String) start.getSelectedItem();
					if (startString != null){
						for (MapNode mapnode : getGlobalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
							if(startString.equals(mapnode.getAttributes().getOfficialName())){
								startNode = mapnode; //set the startNode and then draw it on the map
								getGlobalMap().setStartNode(startNode);
								getGlobalMap().getChosenNodes().add(getGlobalMap().getStartNode());
								LocalMap localmap = getGlobalMap().getStartNode().getLocalMap();
								localmap.setStartNode(startNode);
								btnClear.setEnabled(true);
							}
						}
					} else if (globalMap.getStartNode() == null){
							MapNode n = backend.getLocalMap().getMapNodes().get(0);
							getGlobalMap().setStartNode(n);	
							getGlobalMap().getChosenNodes().add(getGlobalMap().getStartNode());
							LocalMap localmap = getGlobalMap().getStartNode().getLocalMap();
							localmap.setStartNode(getGlobalMap().getStartNode());
							btnClear.setEnabled(true);
					}

					for (MapNode mapnode : getGlobalMap().getMapNodes()){
						// this follows a similar pattern to how the original nodes are set with the radio buttons
						if(endString.equals(mapnode.getAttributes().getOfficialName()) /*|| mapnode.getAttributes().getAliases().contains(endString)*/){
							//if endstring is the official name or one of a few different accepted aliases we will allow the end node to be placed
							valid = true;
							getGlobalMap().setEndNode(mapnode);
							getGlobalMap().getChosenNodes().add(mapnode);
							LocalMap localmap = getGlobalMap().getEndNode().getLocalMap();
							localmap.setEndNode(mapnode);
							panelMap.setMapImage(new ProxyImage(getGlobalMap().getEndNode().getLocalMap().getMapImageName()));
							panelMap.setMapNodes(getGlobalMap().getEndNode().getLocalMap().getMapNodes());
							String previousMap = backend.getLocalMap().getMapImageName();
							panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
							backend.setLocalMap(localmap);
							double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
							double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
							panelMap.setPanX(defPan[0]);
							panelMap.setPanY(defPan[1]);
							panelMap.setScale(defPan[2]);
							offsetX = defPan[0] - tempPan[0];
							offsetY = defPan[1] - tempPan[1];
							for(MapNode node : backend.getLocalMap().getMapNodes()){
								node.setXPos(node.getXPos() + offsetX);
								node.setYPos(node.getYPos() + offsetY);
							}	
							btnRoute.setEnabled(true);
						} 
					}
					
					if(globalMap.getEndNode() == null){
						if (attribute.getPossibleEntries().containsKey(endString)){
							String nearestAttribute = attribute.getPossibleEntries().get(endString);
							valid = true;
							MapNode node = backend.findNearestAttributedNode(nearestAttribute, globalMap.getStartNode()); //same idea as findNearestNode - just finds the nearest node to the startnode that gives the entered attribute
							if (node != null){ //if no node was found, you should not place a node on the map otherwise do it 
								globalMap.setEndNode(node);
								globalMap.getChosenNodes().add(globalMap.getEndNode());
								LocalMap localmap = globalMap.getEndNode().getLocalMap();
								localmap.setEndNode(node);
								panelMap.setMapImage(new ProxyImage(globalMap.getEndNode().getLocalMap().getMapImageName()));
								panelMap.setMapNodes(globalMap.getEndNode().getLocalMap().getMapNodes());
								String previousMap = backend.getLocalMap().getMapImageName();
								panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
								backend.setLocalMap(localmap);
								double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
								double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
								panelMap.setPanX(defPan[0]);
								panelMap.setPanY(defPan[1]);
								panelMap.setScale(defPan[2]);
								offsetX = defPan[0] - tempPan[0];
								offsetY = defPan[1] - tempPan[1];
								for(MapNode node2 : backend.getLocalMap().getMapNodes()){
									node2.setXPos(node2.getXPos() + offsetX);
									node2.setYPos(node2.getYPos() + offsetY);
								}					
								btnRoute.setEnabled(true);
							}
						} 
					}

					if (valid == false){
						System.out.println("Invalid entry");
						lblInvalidEntry.setVisible(true);
					}
				}
			}
		};		

		// {{ GroupLayout code for tabs and text fields
		
		// Main Panel
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
		
		end.addActionListener(actionEnd);
		start.addActionListener(actionStart);

		// Start/End Labels
		lblStart = new JLabel("Starting Location");
		lblStart.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblEnd = new JLabel("Ending Location");
		lblEnd.setFont(new Font("Tahoma", Font.PLAIN, 12));

		// Clear All Button
		btnClear = new JButton("Clear All");
		btnClear.setBackground(otherButtonsColor);
		btnClear.setEnabled(false);
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				reset();
			}
		});

		// Route Button
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

					/** @author Andrew Petit
					 * 
					 * get me routes for different maps -- waypoint functionallity is added here 
					 */				
					ArrayList<MapNode> getNodesOnSameMap = new ArrayList<MapNode>(); //will add all nodes from the same map to this 
					ArrayList<ArrayList<String>> stepByStep = new ArrayList<ArrayList<String>>();
					ArrayList<String> directions = new ArrayList<String>();
					boolean hasWayPoint = true;
					for (int i = 0; i < getGlobalMap().getChosenNodes().size() - 1; i++){ //for all nodes in chosen nodes (start, end, and waypoints included) 
						//if the nodes are on the same map add all those nodes into an arraylist together
						if (getGlobalMap().getChosenNodes().get(i).getLocalMap() == getGlobalMap().getChosenNodes().get(i + 1).getLocalMap()){
							ArrayList<MapNode> nodesOnSameMap = backend.runAStar(getGlobalMap().getChosenNodes().get(i), getGlobalMap().getChosenNodes().get(i + 1));
							if (getGlobalMap().getChosenNodes().size() == i + 2){
								directions = backend.displayStepByStep(nodesOnSameMap, false); //no more waypoints 
							} else {
								directions = backend.displayStepByStep(nodesOnSameMap, true); //more waypoints
							}
							stepByStep.add(directions); //essentially makes a list of all step by step directions to be added to the jlist
							directions = new ArrayList<String>(); //reset directions
							for (MapNode node : nodesOnSameMap){
								getNodesOnSameMap.add(node);
							}
						} 
						else { //if the next node in chosen nodes is not on the same local map as the previous node in chosen nodes
							if (!(getNodesOnSameMap.isEmpty())){ //check if getNodesOnSameMap is not empty and if it is not add the nodes from the first array gathered from running getmeroutes as the nodes in this array should be on the same map
								ArrayList<MapNode> wayPoints = new ArrayList<MapNode>();
								ArrayList<ArrayList<MapNode>> getNodesOnDifferentMap = backend.getMeRoutes(getGlobalMap().getChosenNodes().get(i), getGlobalMap().getChosenNodes().get(i + 1));
								//this is is needed to make waypoints compatible with step by step - basically just add all nodes gathered from getmeroutes and send that to step by step
								for (ArrayList<MapNode> mapnodes : getNodesOnDifferentMap){
									for (int k = 0; k < mapnodes.size() - 1; k++){
										if (getGlobalMap().getChosenNodes().size() == i + 2){
											wayPoints.add(mapnodes.get(k));
											hasWayPoint = false; // no more waypoints
										} 
										else {
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
							} 
							else { //if getNodesOnSameMap was not empty we should just go ahead an the ArrayList<ArrayList<MapNode>> that getMeRoutes returns to the next index in paths as these nodes should not have the same local map 
								ArrayList<ArrayList<MapNode>> getNodesOnDifferentMap = backend.getMeRoutes(getGlobalMap().getChosenNodes().get(i), getGlobalMap().getChosenNodes().get(i + 1));
								ArrayList<MapNode> wayPoints = new ArrayList<MapNode>();
								for (ArrayList<MapNode> mapnodes : getNodesOnDifferentMap){ //same idea as before go through the list of all nodes from the arraylist of arraylist of mapnodes returned by getmerroutes send that arraylist to step by step
									for (int k = 0; k < mapnodes.size() - 1; k++){
										if (getGlobalMap().getChosenNodes().size() == i + 2){
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
					} 
					else {
						if (!(getNodesOnSameMap.isEmpty()) && (paths.get(paths.size() - 1).get(0).getLocalMap() == getNodesOnSameMap.get(0).getLocalMap())){
							for(MapNode mapnode : getNodesOnSameMap){
								paths.get(paths.size() - 1).add(mapnode);
							}
							
						} 
						else //if not, but getNodesOnSameMap is not empty we should just go ahead and add those nodes to another index in paths
							paths.add(getNodesOnSameMap);
					}

					//reinitialize getNodesOnSameMap for the next time this fun method is run...
					getNodesOnSameMap = new ArrayList<MapNode>();

					//for each route set start and end values for that routes local map
					for (int i = 0; i < paths.size() - 1; i++){
						LocalMap localmap = paths.get(i).get(0).getLocalMap();
						if (localmap.getEndNode() == null){
							int size = paths.get(i).size() - 1;
							localmap.setStartNode(paths.get(i).get(size));
						}
						if (localmap.getStartNode() == null)
							localmap.setEndNode(paths.get(i).get(0));
					}
				
					//change the street view to the new map
					GUIFront.changeStreetView(gl_contentPane, paths.get(0).get(0).getLocalMap().getMapImageName());					

					//get the first route to allow calculate route to go back to the initial map when starting to show the route
					thisRoute = paths.get(0);
					//the following code is needed for panning, we must update the panX and panY every time the map changes 
					panelMap.setMapImage(new ProxyImage(paths.get(0).get(0).getLocalMap().getMapImageName()));
					panelMap.setMapNodes(paths.get(0).get(0).getLocalMap().getMapNodes());
					String previousMap = backend.getLocalMap().getMapImageName();
					panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
					backend.setLocalMap(paths.get(0).get(0).getLocalMap());
					double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
					double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
					panelMap.setPanX(defPan[0]);
					panelMap.setPanY(defPan[1]);
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
					index3 = 0; // this is for the jlist to highlight the step the user is on
					//if we have more than one arraylist in paths this means that more than one map will be shown to the user
					if (paths.size() > 1){
						btnNextMap.setEnabled(true);
					}

					btnNextStep.setEnabled(true);

					//draw the line on the map
					drawLine = true;
					drawNodes = true;
					//set the initial distance as 0 
					int distance = 0;
					//update the step by step directions and distance for each waypoint added
					listModel.addElement("Welcome to the Era of Navigation!");
					for (ArrayList<String> strings: stepByStep){
						for (String string : strings) {
							listModel.addElement(string); // add it to the list model
							allText += string + "\n";
						}
					}
					allText = allText.replace("ENDHERE", " ");

					for (ArrayList<MapNode> wayPoints : paths){
						distance += backend.getDistance(wayPoints, true); //the boolean value should not matter here 
					}

					getLblDistance().setText("Distance in feet:" + distance);

					btnClear.setEnabled(true);
					btnRoute.setEnabled(false);
				}

			}
			
		});
		
		// Side Bar Color
		sideBarColor = getColors().getSideBarColor();

		// Slide Panel
		slidePanel = new SLPanel();
		slidePanel.setBackground(sideBarColor);
		panelMap = new TweenPanel(backend.getLocalMap().getMapNodes(), mapPath, "1", Constants.IMAGES_PATH);
		panelMap.setBackground(backgroundColor);
		panels.add(panelMap);
		panelDirections = new TweenPanel("2");

		// Step-by-step UI
		stepByStepUI = new JPanel();
		stepByStepUI.setBackground(sideBarColor);

		// "<<<" Button on Slide Panel
		setLblClickHere(new JLabel("<<<"));
		getLblClickHere().setFont(new Font("Tahoma", Font.BOLD, 13));
		getLblClickHere().setVisible(true);
		stepByStepUI.add(getLblClickHere());

		// Step-by-step Label
		setLblStepByStep(new JLabel("Step by Step Directions!"));
		getLblStepByStep().setBackground(sideBarColor);
		getLblStepByStep().setFont(new Font("Tahoma", Font.BOLD, 13));
		getLblStepByStep().setBounds(23, 11, 167, 14);
		getLblStepByStep().setVisible(false);
		stepByStepUI.add(getLblStepByStep());

		// Scroll Pane
		setScrollPane(new JScrollPane());
		getScrollPane().setBounds(10, 30, 180, 322);
		getScrollPane().setVisible(false);
		stepByStepUI.add(getScrollPane());

		// Create a new list and be able to get the current width of the viewport it is contained in (the scrollpane)
		renderer = new WrappableCellRenderer(MAX_LIST_WIDTH / 7); // 7 pixels per 1 character

		// List Directions
		setListDirections(new JList<String>(listModel));
		getListDirections().setCellRenderer(renderer);
		getListDirections().setFixedCellWidth(MAX_LIST_WIDTH); // give it a set width in pixels
		getScrollPane().setViewportView(getListDirections());
		getListDirections().setVisible(false);
		getListDirections().setVisibleRowCount(10); // only shows 10 directions before scrolling

		getListDirections().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if (evt.getValueIsAdjusting() == false){
					
					if (getListDirections().getSelectedIndex() ==  0){
						//don't do anything because you should not do anything for the welcome to step in the jlist
					}
					else {
						index3 = getListDirections().getSelectedIndex();
						int indexHelp = 0;
						if (index3 >= paths.get(index).size()){
							index2 = 0;
							if (index != 0){
								for (int i = 0; i < index; i++){
									indexHelp += paths.get(i).size() - 1;
								}
							}
							if (index <= 0){
								btnPreviousMap.setEnabled(false);
								btnPreviousStep.setEnabled(false);
							}
							if (index < paths.size() - 1){
								btnNextMap.setEnabled(true);
							}

							if (index >= paths.size() - 1){
								btnNextMap.setEnabled(false);
								btnNextStep.setEnabled(false);
							}
				
							if (index > 0){
								btnPreviousMap.setEnabled(true);
								btnPreviousStep.setEnabled(true);
							}
							if (index < paths.size() - 1){
								btnNextStep.setEnabled(true);
							}
							drawLine2 = false;
							drawLine3 = false;
							LocalMap localMap = paths.get(index).get(0).getLocalMap();
							
							GUIFront.changeStreetView(gl_contentPane, localMap.getMapImageName());
							panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
							panelMap.setMapNodes(localMap.getMapNodes());
							String previousMap = backend.getLocalMap().getMapImageName();
							panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
							backend.setLocalMap(localMap);

							double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
							panelMap.setPanX(tempPan[0]);
							panelMap.setPanY(tempPan[1]);

							for(MapNode n : backend.getLocalMap().getMapNodes()){
								n.setXPos(n.getXPos() - panelMap.getPanX());
								n.setYPos(n.getYPos() - panelMap.getPanY());
							}

							panelMap.setPanX(0.0);
							panelMap.setPanY(0.0);
							panelMap.setScale(1.0);

							thisRoute = paths.get(index);
							drawLine = true;
							drawNodes = true;
						}
						index3 -= indexHelp;
						if (index3 >= paths.get(index).size() - 1)
							index++;
						
						index2 = index3;
						drawLine2 = true;
					}
				}
			}
		});
		
		// Distance Label
		setLblDistance(new JLabel());
		getLblDistance().setBackground(sideBarColor);
		getLblDistance().setFont(new Font("Tahoma", Font.BOLD, 13));
		getLblDistance().setVisible(false);

		// Panel Directions
		panelDirections.add(getLblDistance(), BorderLayout.SOUTH);
		panelDirections.add(stepByStepUI, BorderLayout.NORTH);
		panelDirections.setBackground(sideBarColor);

		// Set action to allow for sliding
		panelDirections.setAction(panelDirectionsAction);

		/* Main Config
		 * The configuration files describe what will take place for each animation. So by default we want the map larger 
		 * and the side panel very small. When we click the directions panel we want that to slide out, zoomRatio the map panel, and
		 * adjust the sizes
		 */
		mainConfig = new SLConfig(slidePanel)
		.gap(10, 10)
		.row(1f).col(14f).col(1f) // Ratio of 14 : 1 when closed
		.place(0, 0, panelMap)
		.place(0, 1, panelDirections);

		// Panel Directions Config
		panelDirectionsConfig = new SLConfig(slidePanel)
		.gap(10, 10)
		.row(1f).col(5f).col(1f) // Ratio of 5 : 1 when open 
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
				index2 = 0;
				index3 = 0;
				index++;
				
				System.out.println("INDEX:" + index);
				System.out.println("PATH SIZE: " + paths.size());

				int pathSize = paths.size() - 2;
				if (index <= 0)
					btnPreviousMap.setEnabled(false);
				if (index >= pathSize)
					btnNextMap.setEnabled(false);
				if (index > 0)
					btnPreviousMap.setEnabled(true);
				if (index < pathSize)
					btnNextMap.setEnabled(true);

				drawLine2 = false;
				drawLine3 = false;
				LocalMap localMap = paths.get(index).get(0).getLocalMap();

				GUIFront.changeStreetView(gl_contentPane, localMap.getMapImageName());
				panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
				panelMap.setMapNodes(localMap.getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
				backend.setLocalMap(localMap);

				double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
				double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
				panelMap.setPanX(defPan[0]);
				panelMap.setPanY(defPan[1]);
				panelMap.setScale(defPan[2]);
				offsetX = defPan[0] - tempPan[0];
				offsetY = defPan[1] - tempPan[1];
				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() + offsetX);
					n.setYPos(n.getYPos() + offsetY);
				}

				thisRoute = paths.get(index);
				drawLine = true;
				drawNodes = true;
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
				index2 = paths.get(index).size() - 1;
				index3 = paths.get(index).size() - 1;
				
				index--;
				
				if (index <= 0){
					btnPreviousMap.setEnabled(false);
				}
				if (index <= paths.size() - 1){
					btnNextMap.setEnabled(true);
					btnNextStep.setEnabled(true);
				}

				if (index > paths.size() - 1){
					btnNextMap.setEnabled(false);
					btnNextStep.setEnabled(false);
				}
				if (index > 0){
					btnPreviousMap.setEnabled(true);
					btnPreviousStep.setEnabled(true);
				}
				if (index < paths.size() - 1){
					index2 = paths.get(index).size() - 1;
					drawLine2 = false;
					drawLine3 = false;

					LocalMap localMap = paths.get(index).get(0).getLocalMap();

					GUIFront.changeStreetView(gl_contentPane, localMap.getMapImageName());

					panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
					panelMap.setMapNodes(localMap.getMapNodes());
					String previousMap = backend.getLocalMap().getMapImageName();
					panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
					backend.setLocalMap(localMap);

	
					double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
					double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
					panelMap.setPanX(defPan[0]);
					panelMap.setPanY(defPan[1]);
					panelMap.setScale(defPan[2]);
					offsetX = defPan[0] - tempPan[0];
					offsetY = defPan[1] - tempPan[1];
					for(MapNode n : backend.getLocalMap().getMapNodes()){
						n.setXPos(n.getXPos() + offsetX);
						n.setYPos(n.getYPos() + offsetY);
					}
				
					thisRoute = paths.get(index);
					drawLine = true;
					drawNodes = true;
				}

				thisRoute = paths.get(index);
				drawLine = true;
				drawNodes = true;
			}
		});
		getContentPane().add(btnPreviousMap, BorderLayout.SOUTH);

		btnNextStep = new JButton("Next Step->");
		btnNextStep.setEnabled(false);
		btnNextStep.setBackground(otherButtonsColor);
		btnNextStepWasPressedLast = true;
		btnNextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawLine3 = false;
				
				System.out.println("PATH SIZE: " + paths.get(index).size());
				
				if(index2 < (paths.get(index).size() - 1))
					index2++;
				if(index3 < (paths.get(index).size() - 1))
					index3++;
				
				// fixes bug that doesn't go to the next step if prev step was pressed
				if(! btnNextStepWasPressedLast){
					index2++;
					index3++;
				}
					
				System.out.println("INDEX: " + index);
				System.out.println("INDEX2: " + index2);
				System.out.println("INDEX3: " + index3);
				System.out.println("----------");
				
				// switch to next map if possible
				if(index2 == paths.get(index).size() - 1 &&
						index3 == paths.get(index).size() - 1 &&
						btnNextMap.isEnabled())
					
					btnNextMap.doClick();
				else if(index2 == (paths.get(index).size() - 1) && 
						index3 == (paths.get(index).size() - 1) &&
						(! btnNextMap.isEnabled()))
					
					btnNextStep.setEnabled(false);
				else {
					drawLine2 = true;
					btnPreviousStep.setEnabled(true);
				}
								
				btnNextStepWasPressedLast = true;
			}
		});

		btnPreviousStep = new JButton("<-Previous Step");
		btnPreviousStep.setEnabled(false);
		btnPreviousStep.setBackground(otherButtonsColor);
		btnPreviousStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawLine2 = false;
				
				if(index2 > 0)
					index2--;
				if(index3 > 0)
					index3--;
				
				// fixes bug that doesn't go to the prev step if next step was pressed last
				if(btnNextStepWasPressedLast){
					index2--;
					index3--;
				}
				
				System.out.println("INDEX: " + index);
				System.out.println("INDEX2: " + index2);
				System.out.println("INDEX3: " + index3);
				System.out.println("----------");
				
				if (index2 < 0){
					btnPreviousMap.doClick();
					drawLine3 = false;
				} 
				else 
					drawLine3 = true;
				
				// switch to prev map if possible
				if(index2 == 0 && index3 == 0 && btnPreviousMap.isEnabled())
					btnPreviousMap.doClick();
				
				if(index2 == 0 && index3 == 0
						&& (! btnPreviousMap.isEnabled()))
					btnPreviousStep.setEnabled(false);
				
				btnNextStepWasPressedLast = false;
			}
		});
		// }} GroupLayout code for tabs and text fields

		// Group Layout code for all components
		GUIFront.initGroupLayout(lblStart, lblEnd, btnPreviousMap, btnPreviousStep, btnNextStep, btnNextMap);
		
		//set street view image to the default one
		GUIFront.changeStreetView(gl_contentPane, Constants.DEFAULT_STREET_IMAGE);

		// check if it is done loading then make the gui visible
		if(backend.splashFlag) 
			setVisible(true);

		pack();
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH); // start the application maximized
		changeMapTo(11, 0, 0, 1);
	}

	// Sets Coloring Schemes
	public void  setColoring(String scheme){
		setColors(getAllSchemes().setColorScheme(scheme));

		routeButtonColor = getColors().getRouteButtonColor(); // update components
		otherButtonsColor = getColors().getOtherButtonsColor();
		backgroundColor = getColors().getMainBackColor();
		sideBarColor = getColors().getSideBarColor();

		btnRoute.setBackground(routeButtonColor);
		slidePanel.setBackground(sideBarColor);
		panelMap.setBackground(backgroundColor);
		stepByStepUI.setBackground(sideBarColor);
		getLblStepByStep().setBackground(sideBarColor);
		getLblDistance().setBackground(sideBarColor);
		panelDirections.setBackground(sideBarColor);
		btnNextMap.setBackground(otherButtonsColor);
		btnPreviousMap.setBackground(otherButtonsColor);
		contentPane.setBackground(backgroundColor);
		btnClear.setBackground(otherButtonsColor);
	}

	/** Changes the picture on the street view tab
	 * 
	 * TODO make useful for more than 2 tabs
	 * 
	 * @author NathanGeorge
	 * @param gl_contentPane
	 * @param imagePath
	 */
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

	/** Populates LocalMaps with Nodes
	 * @author Nick Gigliotti
	 * 
	 * @param a list of MapNodes which represents all the nodes in a path across multiple local maps
	 * 
	 * @return a list of LocalMaps that are in the path of nodes give
	 */
	public static ArrayList<LocalMap> createListOfMaps(ArrayList<ArrayList<MapNode>> path) {
		// Initializes list
		ArrayList<LocalMap> pathLocalMaps = new ArrayList<LocalMap>();

		for (ArrayList<MapNode> pathNodes: path) {
			// Iterates through the list of nodes in the inputed ArrayList
			for (MapNode node: pathNodes) {

				// If the return list doesn't contain the current nodes LocalMap, it adds it to the return list
				if (! pathLocalMaps.contains(node.getLocalMap()))
					pathLocalMaps.add(node.getLocalMap());
			}
		}
		return pathLocalMaps;
	}

	// Initializes all menu bars
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

					LocalMap localMap = paths.get(index).get(0).getLocalMap();
					panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
					panelMap.setMapNodes(paths.get(index).get(0).getLocalMap().getMapNodes());
					String previousMap = backend.getLocalMap().getMapImageName();
					panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
					backend.setLocalMap(localMap);

					double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
					double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
					panelMap.setPanX(defPan[0]);
					panelMap.setPanY(defPan[1]);
					panelMap.setScale(defPan[2]);
					offsetX = defPan[0] - tempPan[0];
					offsetY = defPan[1] - tempPan[1];
					for(MapNode n : backend.getLocalMap().getMapNodes()){
						n.setXPos(n.getXPos() + offsetX);
						n.setYPos(n.getYPos() + offsetY);
					}

					thisRoute = paths.get(index);
					drawLine = true;
					drawNodes = true;

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
				EmailGUI newEmail = new EmailGUI(backgroundColor, sideBarColor, routeButtonColor, otherButtonsColor);
				newEmail.setVisible(true); //Opens EmailGUI Pop-Up

				LocalMap localMap = paths.get(index).get(0).getLocalMap();
				panelMap.setMapImage(new ProxyImage(localMap.getMapImageName()));
				panelMap.setMapNodes(paths.get(index).get(0).getLocalMap().getMapNodes());
				String previousMap = backend.getLocalMap().getMapImageName();
				panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});
				backend.setLocalMap(localMap);

				double[] tempPan = panValues.get(backend.getLocalMap().getMapImageName());
				double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
				panelMap.setPanX(defPan[0]);
				panelMap.setPanY(defPan[1]);
				panelMap.setScale(defPan[2]);
				offsetX = defPan[0] - tempPan[0];
				offsetY = defPan[1] - tempPan[1];
				for(MapNode n : backend.getLocalMap().getMapNodes()){
					n.setXPos(n.getXPos() + offsetX);
					n.setYPos(n.getYPos() + offsetY);
				}

				thisRoute = paths.get(index);
				drawLine = true;
				drawNodes = true;
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

		// ---- Locations -----
		mnLocations = new JMenu("Locations");
		menuBar.add(mnLocations);

		// ---- Color Schemes ----
		mnColorScheme = new JMenu("Color Scheme");
		mnOptions.add(mnColorScheme);

		//schemes are mapped to an index in mntmColorSchemes
		//0 - Default Campus
		//1 - Grayscale
		//2 - WPI Default
		//3 - Flower Power
		//4 - All Blue
		
		for (int i = 0; i < 5; i++)
			mntmColorSchemes.add(new JMenuItem());
		
		mntmColorSchemes.get(0).setText("Default Campus");
		mntmColorSchemes.get(1).setText("Greyscale");
		mntmColorSchemes.get(2).setText("WPI Theme");
		mntmColorSchemes.get(3).setText("Flower Power");
		mntmColorSchemes.get(4).setText("All Blue");
		
		ArrayList<String> colorText = new ArrayList<String>();
		colorText.add(0, "Default Campus");
		colorText.add(1, "Greyscale");
		colorText.add(2, "WPI Default");
		colorText.add(3, "Flower Power");
		colorText.add(4, "All Blue");

		mntmColorSchemes.get(0).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("Default Campus");
			}
		});
		mntmColorSchemes.get(1).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("Greyscale");
			}
		});
		mntmColorSchemes.get(2).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("WPI Default");
			}
		});
		mntmColorSchemes.get(3).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("Flower Power");
			}
		});
		mntmColorSchemes.get(4).addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setColoring("All Blue");
			}
		});
		
		mnColorScheme.add(mntmColorSchemes.get(0));
		mnColorScheme.add(mntmColorSchemes.get(1));
		mnColorScheme.add(mntmColorSchemes.get(2));
		mnColorScheme.add(mntmColorSchemes.get(3));
		mnColorScheme.add(mntmColorSchemes.get(4));

		// here lies the clickable building dropdown menus
		mnBuildings = GUIFrontUtil.initBuildingMenuBar();
		
		// Campus Map
		JMenuItem mntmCCM = new JMenuItem("Campus Map");
		mntmCCM.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){		
				GUIFront.changeMapTo(11, 0, 0, 1);
			}
		});

		mnLocations.add(mnBuildings.get(0)); // indices: 0, 1, 2, 3
		mnLocations.add(mnBuildings.get(1)); // indices: 4, 5, 6, 7
		mnLocations.add(mnBuildings.get(2));// indices: 8, 9, 10
		mnLocations.add(mntmCCM); // index: 11
		mnLocations.add(mnBuildings.get(3)); // indices 12, 13, 14, 15, 16
		mnLocations.add(mnBuildings.get(4)); // indices: 17, 18, 19, 20, 21
		mnLocations.add(mnBuildings.get(5)); // indices: 22, 23, 24
		mnLocations.add(mnBuildings.get(6)); //indices: 25, 26
		mnLocations.add(mnBuildings.get(7)); // indices: 27, 28
		mnLocations.add(mnBuildings.get(8)); // indices 29, 30, 31, 32

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
	}

	/** Changes the map to the .localmap at the given index in the file system
	 * @param index The index of the map to be loaded (AK = 0, SHB = 27)
	 * @param panX The default panning horizontal value for the map to be loaded
	 * @param panY The default panning vertical value for the map to be loaded
	 * @param scale The default zoom scale for the map to be loaded
	 */
	public static void changeMapTo(int index, int panX, int panY, double scale){
		GUIFront.changeStreetView(gl_contentPane, getGlobalMap().getLocalMaps().get(index).getMapImageName());

		panelMap.setMapImage(new ProxyImage(getGlobalMap().getLocalMaps().get(index).getMapImageName()));
		panelMap.setMapNodes(getGlobalMap().getLocalMaps().get(index).getMapNodes());
		String previousMap = backend.getLocalMap().getMapImageName();
		panValues.put(previousMap, new double[]{panelMap.getPanX(), panelMap.getPanY()});

		backend.setLocalMap(getGlobalMap().getLocalMaps().get(index));

		double[] tempPan = panValues.get(getGlobalMap().getLocalMaps().get(index).getMapImageName());
		double[] defPan = defaults.get(backend.getLocalMap().getMapImageName());
		panelMap.setPanX(defPan[0]);
		panelMap.setPanY(defPan[1]);
		panelMap.setScale(defPan[2]);
		offsetX = defPan[0] - tempPan[0];
		offsetY = defPan[1] - tempPan[1];
		for(MapNode n : backend.getLocalMap().getMapNodes()){
			n.setXPos(n.getXPos() + offsetX);
			n.setYPos(n.getYPos() + offsetY);
		}
		
		ArrayList<LocalMap> localMaps = createListOfMaps(paths);
		if(localMaps.contains(backend.getLocalMap())){ // if drawing, and if this map is in the path list, DRAW
			drawLine = true;
			drawNodes = true;
		}
		else{
			drawLine = false;
			
			if(globalMap.getStartNode() != null && globalMap.getEndNode() != null)
				drawNodes = false;
			}
	}

	// Enable Actions
	public void enableActions(){
		for (TweenPanel panel : panels){
			panel.enableAction();
		}
		panelDirections.enableAction();
	}
	
	// Disable Actions
	public void disableActions(){
		for (TweenPanel panel : panels){
			panel.disableAction();
		}
		panelDirections.disableAction();
	}

	/** Step by Step Opening Action
	 * The animation functions come in pairs of action and back-action. This tells the engine where to move it, and 
	 * if it needs to move other panels 
	 */	
	private final Runnable panelDirectionsAction = new Runnable() {
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
		}
	};

	// Step by Step Closing Action
	private final Runnable panelDirectionsBackAction = new Runnable() {
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
		}
	};

	/** Reset GUI
	 * @author Andrew Petit
	 * @description Resets all of the relevant information on the form and the background information
	 */
	public void reset() {
		allowSetting = true; //allow user to re place nodes only once reset is pressed
		getGlobalMap().getStartNode().getLocalMap().setStartNode(null);
		if (getGlobalMap().getEndNode() != null){
			getGlobalMap().getEndNode().getLocalMap().setEndNode(null);
			getGlobalMap().setEndNode(null);
		}
		getGlobalMap().setStartNode(null);
		reset = true;
		listModel.removeAllElements(); // clear directions

		// allows the user to re-input start and end nodes

		paths.clear();
		thisRoute.clear();
		backend.removePath(getGlobalMap().getChosenNodes());
		btnNextMap.setEnabled(false);
		btnPreviousMap.setEnabled(false);
		btnNextStep.setEnabled(false);
		btnPreviousStep.setEnabled(false);
		lblInvalidEntry.setVisible(false);
		//textFieldEnd.setText("");
		//textFieldStart.setText("");

		getGlobalMap().getChosenNodes().clear();
		getLblDistance().setText("");
		btnClear.setEnabled(false);
		btnRoute.setEnabled(false);
		drawLine2 = false;
		drawLine3 = false;
		drawNodes = true;
	}

	// Initialization before initializing the GUI
	public static void init(File[] localMapFilenames){
		// Initialize Color Schemes
		setAllSchemes(new ColorSchemes());  
		setColors(getAllSchemes().setColorScheme("Default Campus"));

		routeButtonColor = getColors().getRouteButtonColor();
		otherButtonsColor = getColors().getOtherButtonsColor();
		backgroundColor = getColors().getMainBackColor();
		sideBarColor = getColors().getSideBarColor();

		// Instantiate GUIBack to its default
		GUIBack initial = new GUIBack();
		backends.add(0, initial);

		// Initialize the GlobalMap variable with all of the LocalMaps and all of their nodes
		setGlobalMap(new GlobalMap());

		String[] localMapFilenameStrings = new String[localMapFilenames.length];
		for(int i = 0; i < localMapFilenames.length; i++){
			String path = localMapFilenames[i].getName();
			localMapFilenameStrings[i] = path;
		}

		backend = initial;

		ArrayList<LocalMap> localMapList = backend.loadLocalMaps(localMapFilenameStrings);
		getGlobalMap().setLocalMaps(localMapList);

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
		getGlobalMap().setMapNodes(allNodes);
	}
	
	// Initialize Group Layout
	public static void initGroupLayout(JLabel lblStart, JLabel lblEnd, 
			JButton btnPreviousMap, JButton btnPreviousStep, JButton btnNextStep, JButton btnNextMap){
		gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(start, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblStart, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(75)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(end, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblEnd, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
							.addGap(76)
							.addComponent(lblInvalidEntry)
							.addGap(226)
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
					.addPreferredGap(ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
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
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(start, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblEnd)
								.addComponent(lblInvalidEntry))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(end, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(11)
							.addComponent(btnRoute))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(11)
							.addComponent(btnClear)))
					.addGap(14)
					.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 445, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPreviousMap)
						.addComponent(btnNextMap)
						.addComponent(btnNextStep)
						.addComponent(btnPreviousStep))
					.addGap(35))
		);

		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()  // top line
					.addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING) // lbl start | txt Start
							.addComponent(lblStart)
							.addComponent(start, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
					.addGap(20)
					.addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING) // lbl end | txt end
							.addComponent(lblEnd)
							.addComponent(end, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
					.addComponent(lblInvalidEntry)
					.addComponent(btnRoute)
					.addComponent(btnClear)
				)
				.addComponent(mainPanel)
				.addGroup(gl_contentPane.createSequentialGroup() // bottom line
					.addComponent(btnPreviousMap)
					.addComponent(btnPreviousStep)
					.addGap(50)
					.addComponent(btnNextStep)
					.addComponent(btnNextMap))
			);
			gl_contentPane.linkSize(SwingConstants.HORIZONTAL, btnRoute, btnClear); // ensure the buttons don't resize

			gl_contentPane.setVerticalGroup(gl_contentPane.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblStart)
						.addComponent(lblEnd))
				.addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(start)
						.addComponent(end)
						.addComponent(lblInvalidEntry)
						.addComponent(btnRoute)
						.addComponent(btnClear))
				.addGroup(gl_contentPane.createSequentialGroup())
						.addComponent(mainPanel)
				.addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnPreviousMap)
						.addComponent(btnPreviousStep)
						.addComponent(btnNextStep)
						.addComponent(btnNextMap)) // Next Map/Step buttons	
			);
	}
	
	// {{ Getters and Setters
	public static Point2D getMainReferencePoint() {
		return mainReferencePoint;
	}
	public static void setMainReferencePoint(Point2D mainReferencePoint) {
		GUIFront.mainReferencePoint = mainReferencePoint;
	}
	public static AffineTransform getTransform() {
		return transform;
	}
	public static void setTransform(AffineTransform transform) {
		GUIFront.transform = transform;
	}
	public static GlobalMap getGlobalMap() {
		return globalMap;
	}
	public static void setGlobalMap(GlobalMap globalMap) {
		GUIFront.globalMap = globalMap;
	}
	public static ZoomHandler getZoomHandle() {
		return zoomHandle;
	}
	public static void setZoomHandle(ZoomHandler zoomHandle) {
		GUIFront.zoomHandle = zoomHandle;
	}
	public static PanHandler getPanHandle() {
		return panHandle;
	}
	public static void setPanHandle(PanHandler panHandle) {
		GUIFront.panHandle = panHandle;
	}
	public static ColorSetting getColors() {
		return colors;
	}
	public static void setColors(ColorSetting colors) {
		GUIFront.colors = colors;
	}
	public static ColorSchemes getAllSchemes() {
		return allSchemes;
	}
	public static void setAllSchemes(ColorSchemes allSchemes) {
		GUIFront.allSchemes = allSchemes;
	}
	public static boolean isCurrentlyOpen() {
		return currentlyOpen;
	}
	public static void setCurrentlyOpen(boolean currentlyOpen) {
		GUIFront.currentlyOpen = currentlyOpen;
	}
	public static JLabel getLblStepByStep() {
		return lblStepByStep;
	}
	public static void setLblStepByStep(JLabel lblStepByStep) {
		GUIFront.lblStepByStep = lblStepByStep;
	}
	public static JLabel getLblClickHere() {
		return lblClickHere;
	}
	public static void setLblClickHere(JLabel lblClickHere) {
		GUIFront.lblClickHere = lblClickHere;
	}
	public static JLabel getLblDistance() {
		return lblDistance;
	}
	public static void setLblDistance(JLabel lblDistance) {
		GUIFront.lblDistance = lblDistance;
	}
	public static JScrollPane getScrollPane() {
		return scrollPane;
	}
	public static void setScrollPane(JScrollPane scrollPane) {
		GUIFront.scrollPane = scrollPane;
	}
	public static JList<String> getListDirections() {
		return listDirections;
	}
	public static void setListDirections(JList<String> listDirections) {
		GUIFront.listDirections = listDirections;
	}
}

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
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
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
import main.util.Speaker;

import javax.swing.JTextField;
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

	private static GUIBack backend;
	private static GlobalMap globalMap;
	private static boolean setStart = false; // keeps track of whether you have set a start or end node yet
	private static boolean setEnd = false;
	public static boolean drawLine = false;
	public static boolean removeLine = false;
	public static boolean reset = false;
	public static MapNode startNode = null, endNode = null;
	public static String allText = "";
	public static ArrayList<GUIBack> backends = new ArrayList<GUIBack>();
	public static ArrayList<ArrayList<MapNode>> paths = new ArrayList<ArrayList<MapNode>>();
	public static JButton btnClear, btnRoute;
	private JButton btnPreviousMap, btnNextMap;
	public static boolean allowSetting = true;
	public static JTabbedPane mainPanel; 
	public static ArrayList<MapNode> allNodes;
	public static int index = 0;
	
	static AffineTransform transform; // the current state of image transformation
	Point2D mainReferencePoint; // the reference point indicating where the click started from during transformation
	static PanHandler panHandle;
	static ZoomHandler zoomHandle;

	// MapPanel components
	private JPanel contentPane;
	private static JTextField textFieldEnd, textFieldStart;
	private JLabel lblStart, lblEnd;
	
	// Directions Components
	private static JLabel lblStepByStep, lblClickHere, lblDistance;
	private static JScrollPane scrollPane;
	private static JTextArea txtAreaDirections;
	private static boolean currentlyOpen = false; // keeps track of whether the panel is slid out or not

	private SLPanel slidePanel;
	public static ArrayList<TweenPanel> panels = new ArrayList<TweenPanel>();
	public static TweenPanel panelMap, panelDirections;
	private SLConfig mainConfig, panelDirectionsConfig;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public GUIFront(int numLocalMaps, File[] localMapFilenames) throws IOException, ClassNotFoundException {
		// Instantiate GUIBack to its default
		String defaultMapImage = Constants.DEFAULT_MAP_IMAGE;
		GUIBack initial = new GUIBack(defaultMapImage, null);
		backends.add(0, initial);

		// Initialize the GlobalMap variable with all of the LocalMaps and all
		// of their nodes
		globalMap = new GlobalMap();

		ArrayList<LocalMap> tmpListLocal = new ArrayList<LocalMap>(); // temporary list of LocalMaps to be initialized
		for (int i = 0; i < numLocalMaps; i++) {
			initial.loadLocalMap(localMapFilenames[i].getName()); // sets the current LocalMap each filename from the "data.localmaps" folder
			tmpListLocal.add(initial.getLocalMap());
		}
		globalMap.setLocalMaps(tmpListLocal);
		initial.setLocalMap(tmpListLocal.get(0));

		// add the collection of nodes to the ArrayList of GlobalMap
		allNodes = new ArrayList<MapNode>();
		for (LocalMap local : tmpListLocal) {

			if (!local.getMapNodes().equals(null)) // as long as the LocalMap isn't null, add its nodes to the GlobalMap
				allNodes.addAll(local.getMapNodes());
		}
		globalMap.setMapNodes(allNodes);
		backend = initial;

		/**
		 * GUI related code
		 */
		// This will setup the main JFrame to be maximized on start
		setTitle("Era of Navigation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 820, 699);
		setResizable(false);
		setPreferredSize(new Dimension(820, 650));
		panHandle = new PanHandler();
		zoomHandle = new ZoomHandler();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// File Menu
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmEmail = new JMenuItem("Email"); // Code to open up the email sender
		mntmEmail.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				EmailGUI newEmail = new EmailGUI();
				newEmail.setVisible(true); //Opens EmailGUI Pop-Up
			}
		});
		JMenuItem mntmExit = new JMenuItem("Exit"); // terminates the session, anything need to be saved first?
		mntmExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.exit(0); 
			}
		});
		mnFile.add(mntmEmail);
		mnFile.add(mntmExit);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenu mnLocations = new JMenu("Locations");
		menuBar.add(mnLocations);
		
		JMenuItem mntmCampusMap = new JMenuItem("Campus Map");
		mnLocations.add(mntmCampusMap);
		mntmCampusMap.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(0).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(0).getMapNodes());
				backend.setLocalMap(tmpListLocal.get(0));
			}
		});
		
		JMenu mnStratton = new JMenu("Stratton");
		mnLocations.add(mnStratton);
		
		JMenuItem mntmFFStratton = new JMenuItem("Floor 1");
		mntmFFStratton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				panelMap.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + globalMap.getLocalMaps().get(1).getMapImageName()).getImage());
				panelMap.setMapNodes(globalMap.getLocalMaps().get(1).getMapNodes());
				backend.setLocalMap(tmpListLocal.get(1));
			}
		});
		mnStratton.add(mntmFFStratton);
		
		JMenuItem mntmSFStratton = new JMenuItem("Floor 2");
		mntmSFStratton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e){
				ArrayList<MapNode> enteredNodes = GUIFront.globalMap.getLocalMaps().get(2).getMapNodes();
				Image mapPath = new ImageIcon(Constants.IMAGES_PATH + "/" + GUIFront.globalMap.getLocalMaps().get(2).getMapImageName()).getImage();
				TweenPanel strattonMap = new TweenPanel(enteredNodes, mapPath, "1");
				//tabbedPane.addTab("Stratton Floor 2", strattonMap);
			}
		});
		mnStratton.add(mntmSFStratton);
		mntmSFStratton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e){
				ArrayList<MapNode> enteredNodes = GUIFront.globalMap.getLocalMaps().get(3).getMapNodes();
				Image mapPath = new ImageIcon(Constants.IMAGES_PATH + "/" + GUIFront.globalMap.getLocalMaps().get(2).getMapImageName()).getImage();
				TweenPanel strattonMap = new TweenPanel(enteredNodes, mapPath, "1");
				//tabbedPane.addTab("Stratton Floor 3", strattonMap);
			}
		});
		JMenuItem mntmThirdStratton = new JMenuItem("Floor 3");
		mntmThirdStratton.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e){
				ArrayList<MapNode> enteredNodes = GUIFront.globalMap.getLocalMaps().get(3).getMapNodes();
				Image mapPath = new ImageIcon(Constants.IMAGES_PATH + "/" + GUIFront.globalMap.getLocalMaps().get(2).getMapImageName()).getImage();
				TweenPanel strattonMap = new TweenPanel(enteredNodes, mapPath, "1");
				//tabbedPane.addTab("Stratton Floor 3", strattonMap);
			}
		});
		mnStratton.add(mntmThirdStratton);
		
		JMenu mnCampusCenter = new JMenu("Campus Center");
		mnLocations.add(mnCampusCenter);
		
		JMenuItem mntmMainCampus = new JMenuItem("Main floor");
		mntmMainCampus.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e){
				ArrayList<MapNode> enteredNodes = GUIFront.globalMap.getLocalMaps().get(4).getMapNodes();
				Image mapPath = new ImageIcon(Constants.IMAGES_PATH + "/" + GUIFront.globalMap.getLocalMaps().get(2).getMapImageName()).getImage();
				TweenPanel campusMap = new TweenPanel(enteredNodes, mapPath, "1");
				//tabbedPane.addTab("Campus Main Floor", campusMap);
			}
		});
		mnCampusCenter.add(mntmMainCampus);
		JMenuItem mntmLowerCampus = new JMenuItem("Lower level");
		mntmLowerCampus.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e){
				ArrayList<MapNode> enteredNodes = GUIFront.globalMap.getLocalMaps().get(5).getMapNodes();
				Image mapPath = new ImageIcon(Constants.IMAGES_PATH + "/" + GUIFront.globalMap.getLocalMaps().get(2).getMapImageName()).getImage();
				TweenPanel campusMap = new TweenPanel(enteredNodes, mapPath, "1");
				//tabbedPane.addTab("Campus Main Floor", campusMap);
			}
		});
		mnCampusCenter.add(mntmLowerCampus);
		
		JMenuItem mntmUpperCampus = new JMenuItem("Upper level");
		mntmUpperCampus.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e){
				ArrayList<MapNode> enteredNodes = GUIFront.globalMap.getLocalMaps().get(6).getMapNodes();
				Image mapPath = new ImageIcon(Constants.IMAGES_PATH + "/" + GUIFront.globalMap.getLocalMaps().get(2).getMapImageName()).getImage();
				TweenPanel campusMap = new TweenPanel(enteredNodes, mapPath, "1");
				//tabbedPane.addTab("Campus Main Floor", campusMap);
			}
		});
		mnCampusCenter.add(mntmUpperCampus);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		

		// Image of the default map loaded into backend
		Image mapPath = new ImageIcon(Constants.IMAGES_PATH + "/" + backend.getLocalMap().getMapImageName()).getImage();
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
					//will need some way to alert the user that they need to enter an end location
					System.out.println("Need to enter a valid start location");
				} else if (!(textFieldEnd.getText().equals(""))) { //if there is something entered check if the name is valid and then basically add the end node
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
						//this follows a similar pattern to how the original nodes are set with the radio buttons
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
						} else if(!(textFieldStart.getText().equals(""))){ //if there is something entered in the start field as well as the end field we can go ahead and place both at the same time...
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
						//tell user this entry is invalid
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
		btnClear.setEnabled(false);
		btnClear.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				reset();
			}
		});
		
		JButton btnRoute = new JButton("Route");
		btnRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnRoute.isEnabled()) {
					allowSetting = false; //once calculate button is pressed user should not be allowed to replace nodes until the original line is removed
					allText = ""; //must set the initial text as empty every time calculate button is pressed
					Speaker speaker = new Speaker(Constants.BUTTON_PATH);
					speaker.play();
					//basically waypoint stuff -- find a path between every node in the chosenNodes list of mapnodes
					for(int i = 0; i < globalMap.getChosenNodes().size() - 1; i++){
						ArrayList<MapNode> wayPoints = new ArrayList<MapNode>();
						//wayPoints = backend.getMeRoutes(panelMap.chosenNodes.get(i), panelMap.chosenNodes.get(i + 1));
						wayPoints = backend.runAStar(globalMap.getChosenNodes().get(i), globalMap.getChosenNodes().get(i + 1));
						paths.add(wayPoints);
					}
					//draw the line on the map
					drawLine = true;
					//set the initial distance as 0 
					int distance = 0;
					//update the step by step directions and distance for each waypoint added
					for (ArrayList<MapNode>wayPoints : paths){
						String all = "";
						distance += backend.getDistance(wayPoints);
						for (String string : backend.displayStepByStep(wayPoints)) {
							all += string + "\n";
						}
						allText += all + "\n";
					}

					// this should only display when the user calculates the
					// astar algorithm
					txtAreaDirections.setText(allText);
					
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
		scrollPane.setBounds(10, 30, 180, 322);
		scrollPane.setVisible(false);
		stepByStepUI.add(scrollPane);
		
		txtAreaDirections = new JTextArea();
		txtAreaDirections.setRows(22);
		txtAreaDirections.setEditable(false);
		scrollPane.setViewportView(txtAreaDirections);
		txtAreaDirections.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		txtAreaDirections.setWrapStyleWord(true);
		txtAreaDirections.setLineWrap(true);
		txtAreaDirections.setVisible(false);
		
		lblDistance = new JLabel();
		lblDistance.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDistance.setVisible(false);
		
		panelDirections.add(lblDistance, BorderLayout.SOUTH);
		panelDirections.add(stepByStepUI, BorderLayout.NORTH);
		
		// Set action to allow for sliding
		panelDirections.setAction(panelDirectionsAction);
		
		/**
		 * The configuration files describe what will take place for each animation. So by default we want the map larger 
		 * and the side panel very small. When we click the directions panel we want that to slide out, scale the map panel, and
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
		btnNextMap.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae){
				// TODO: Fill in this mehtod once we know how to draw/load maps
			}
		});
		getContentPane().add(btnNextMap, BorderLayout.SOUTH);
		
		// Add buttons to move between two maps 
		btnPreviousMap = new JButton("<-- Previous Map");
		btnPreviousMap.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae) {
				// TODO: Fill in this method once we know how to draw/load maps
			}
		});
		getContentPane().add(btnPreviousMap, BorderLayout.SOUTH);
		
		// Group Layout code for all components
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
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
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(79)
					.addComponent(btnPreviousMap)
					.addGap(123)
					.addComponent(btnNextMap, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(379, Short.MAX_VALUE))
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
		contentPane.setLayout(gl_contentPane);
	
		pack();
		setVisible(true);
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
		globalMap.setStartNode(null);
		globalMap.setEndNode(null);
		reset = true;
		txtAreaDirections.setText(""); // clear directions

		// allows the user to re-input start and end nodes
		setEnd = false;
		setStart = false;
		paths.clear();
		backend.removePath(globalMap.getMiddleNodes());

		globalMap.getChosenNodes().clear();
		//backend.removePath(backend.getPath()); // this is obsolete now

		// if the line needs to be removed
		// going to need to add a method here - to remove nodes from path
		lblDistance.setText("");
		//textArea1.setText("");
		btnClear.setEnabled(false);
		//btnRoute.setEnabled(true);
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
				if(zoomAmount != 2)
					zoomAmount += 0.1;
			} else { // moving down, zoom out (no less than 0%)
				if(zoomAmount >= 0.5)
					zoomAmount -= 0.1;
			}
			
			// Set it to slightly above 0, weird errors occur if you do exactly 0
			//if(zoomAmount == 0)
			//	zoomAmount = 0.00001;
			for (TweenPanel panel: panels){
				panel.setScale(zoomAmount);
			}
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
			 * Reset the location of the user selected start end nodes. This solution won't actually add the node to the 
			 * list of nodes on the local map
			 */
			MapNode tmpStart, tmpEnd; // temporary variables for clarity
			if(globalMap.getStartNode() != null){
				tmpStart = globalMap.getStartNode();
				tmpStart.setXPos(tmpStart.getXPos() + distanceMovedX);
				tmpStart.setYPos(tmpStart.getYPos() + distanceMovedY);
				globalMap.setStartNode(tmpStart);
			} 
			if (globalMap.getEndNode() != null){
				tmpEnd = globalMap.getEndNode();
				tmpEnd.setXPos(tmpEnd.getXPos() + distanceMovedX);
				tmpEnd.setYPos(tmpEnd.getYPos() + distanceMovedY);
				globalMap.setEndNode(tmpEnd);
			}
			
			for (MapNode mapnode : globalMap.getMiddleNodes()){
				MapNode tmpMiddle;
				if (mapnode != null){
					tmpMiddle = mapnode;
					tmpMiddle.setXPos(tmpMiddle.getXPos() + distanceMovedX);
					tmpMiddle.setYPos(tmpMiddle.getYPos() + distanceMovedY);
				}
			}
				
			
			panelMap.repaint();	
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
	
	/*public class CloseIcon implements Icon {
		int size = 10;
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			// TODO Auto-generated method stub
			g.drawLine(x, y, x + size, y + size);
			g.drawLine(x + size, y, x, y + size);
			
		}

		@Override
		public int getIconWidth() {
			// TODO Auto-generated method stub
			return size;
		}

		@Override
		public int getIconHeight() {
			// TODO Auto-generated method stub
			return size;
		}
		
	}*/
	
	/*class CloseTabButton extends JPanel implements ActionListener {
		  //private JTabbedPane pane;
		  public CloseTabButton(JTabbedPane tabbedPane, int index) {
		    //pane = JTabbedPane;
		    setOpaque(false);
		    add(new JLabel(
		        tabbedPane.getTitleAt(index),
		        tabbedPane.getIconAt(index),
		        JLabel.LEFT));
		    Icon closeIcon = new CloseIcon();
		    JButton btClose = new JButton(closeIcon);
		    btClose.setPreferredSize(new Dimension(
		        closeIcon.getIconWidth(), closeIcon.getIconHeight()));
		    add(btClose);
		    btClose.addActionListener(this);
		    tabbedPane.setTabComponentAt(index, this);
		  }
		  public void actionPerformed(ActionEvent e) {
		    int i = tabbedPane.indexOfTabComponent(this);
		    if (i != -1) {
		      tabbedPane.remove(i);
		    }
		  }
		}*/

	
	/**
	 * Class for a custom panel to do drawing and tweening. This can be seperated into a seperate class file
	 * but it functions better as a private class
	 */
	public static class TweenPanel extends JPanel {
		ArrayList<MapNode> localNodes;
		//public ArrayList<MapNode> chosenNodes;
		
		private final TweenManager tweenManager = SLAnimator.createTweenManager();
		private JLabel labelMainPanel = new JLabel();
		private JLabel labelStep = new JLabel();
		private Image mapImage;
		private Runnable action;
		private boolean actionEnabled = true;
		private boolean hover = false;
		private int borderThickness = 2;
		private String panelID;
		
		double panX, panY;
		double scale;

		/**
		 * Constructor for any tab that would hold a map
		 * @param mapNodes A list of map nodes of the currently loaded map
		 * @param mapPath The image of the map 
		 * @param panelID Represents the ID of a panel to keep track of it 
		 */	
		public TweenPanel(ArrayList<MapNode> mapNodes, Image mapPath, String panelID) {
			setLayout(new BorderLayout());
			
			this.localNodes = mapNodes;
			//chosenNodes = new ArrayList<MapNode>();

			labelMainPanel.setFont(new Font("Sans", Font.BOLD, 90));
			labelMainPanel.setVerticalAlignment(SwingConstants.CENTER);
			labelMainPanel.setHorizontalAlignment(SwingConstants.CENTER);
			labelMainPanel.setText(panelID);
			
			this.mapImage = mapPath;
			this.panelID = panelID;		
			
			panX = 0;
			panY = 0;
			scale = 1;
			
			addMouseListener(panHandle);
			addMouseMotionListener(panHandle);
			addMouseWheelListener(zoomHandle);
			
			/* Map Node Selection Stuff */
			/**
			 * On mouse click, display the points which represent the start and
			 * end nodes. These will also set the backend to these points on the
			 * panel.
			 */
			//ArrayList<MapNode> chosenNodes = new ArrayList<MapNode>(); // Index 0: StartNode; Index 1: EndNode
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (allowSetting == true){
						// figure out where the closest map node is, set that node as a startnode the StartingNode
						Point clickedAt = me.getPoint();
						MapNode node = backend.findNearestNode(clickedAt.getX(), clickedAt.getY());
					
						if(globalMap.getChosenNodes().size() == 0){
							globalMap.setStartNode(node);
							btnClear.setEnabled(true);
						}
						else{
							MapNode endNode = globalMap.getEndNode();
							if(endNode != null)
							globalMap.addToMiddleNodes(endNode);
						
							globalMap.setEndNode(node);
						//	btnRoute.setEnabled(true);
						}

						globalMap.getChosenNodes().add(node);
						
					}
					repaint();
				}	
			});
		}

		public void setMapNodes(ArrayList<MapNode> mapNodes) {
			this.localNodes = mapNodes;
		}

		public void setMapImage(Image map) {
			this.mapImage = map;
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
			super.paintComponent(g);

			Graphics2D graphics = (Graphics2D) g;

			if(this.mapImage == null) // StepByStep
				if(!currentlyOpen){
					lblStepByStep.setVisible(false);
					lblClickHere.setVisible(true);
					lblDistance.setVisible(false);
					scrollPane.setVisible(false);
					txtAreaDirections.setVisible(false);
				} else {
					lblStepByStep.setVisible(true);
					lblDistance.setVisible(true);
					lblClickHere.setVisible(false);
					scrollPane.setVisible(true);
					txtAreaDirections.setVisible(true);
				}
			else {
				// Save the current transformed state incase something goes wrong
				AffineTransform saveTransform = graphics.getTransform();
				transform = new AffineTransform(saveTransform);
				
				// account for changes in zoom
				transform.translate(getWidth() / 2, getHeight() /2);
				transform.scale(scale, scale);
				transform.translate(-getWidth() / 2, -getHeight() / 2);
				
				transform.translate(panX, panY); // move to designated location
				graphics.setTransform(transform);
				
				// Scale the map relative to the panels current size and your current viewing window
				graphics.drawImage(mapImage, 0, 0, this);	
						
				// Test drawing of map nodes
				for(MapNode n : localNodes){
					graphics.fillOval((int)n.getXPos() - (int)panX - 5, (int)n.getYPos() - (int)panY - 5, 10, 10);
				}
				
				// Colors start and end differently
				// Draws the map and places pre-existing node data onto the map as
				// well start and end nodes if they have been set
				graphics.drawImage(this.mapImage, 0, 0, this);

				// Sets the color of the start and end nodes to be different
				graphics.setColor(Color.RED);
				for (int i = 0; i < globalMap.getChosenNodes().size(); i++) {
					if(i == 0){
						graphics.setColor(Color.RED);
						graphics.fillOval((int) globalMap.getChosenNodes().get(i).getXPos() - (int)panX - 5, (int) globalMap.getChosenNodes().get(i).getYPos() - (int)panY - 5, 10, 10);
					} 
					else if(i == globalMap.getChosenNodes().size()-1){
						graphics.setColor(Color.GREEN);
						graphics.fillOval((int) globalMap.getChosenNodes().get(i).getXPos() - (int)panX - 5, (int) globalMap.getChosenNodes().get(i).getYPos() - (int)panY - 5, 10, 10);
					}
					else {
						graphics.setColor(Color.ORANGE);
						graphics.fillOval((int) globalMap.getChosenNodes().get(i).getXPos() - (int)panX - 5, (int) globalMap.getChosenNodes().get(i).getYPos() - (int)panY - 5, 10, 10);
					}
				}
				
				// essentially draws the line on the screen 
				if (GUIFront.drawLine = true) {
					//for (ArrayList<MapNode> mapNodes: backend.getMeRoutes(startNode, endNode)){
					for (ArrayList<MapNode> mapNodes : paths){
						for (int i = 0; i < mapNodes.size() - 1; i++) {
							double x1 = backend.getCoordinates(mapNodes).get(i)[0];
							double y1 = backend.getCoordinates(mapNodes).get(i)[1];
							double x2 = backend.getCoordinates(mapNodes).get(i + 1)[0];
							double y2 = backend.getCoordinates(mapNodes).get(i + 1)[1];
							double alpha = 0.5;
							Color color = new Color(0, 1, 1, (float) alpha);
							Graphics2D g2 = (Graphics2D) g;
							g2.setStroke(new BasicStroke(5));
							g2.setColor(color);
							g2.drawLine((int) x1 - (int)panX, (int) y1 - (int)panY, (int) x2 - (int)panX, (int) y2 - (int)panY);
						}
						drawLine = false;
						removeLine = true;
					}
				} else if (GUIFront.removeLine == true) {
					//for (ArrayList<MapNode> mapNodes : backend.getMeRoutes(startNode, endNode)){
					for (ArrayList<MapNode> mapNodes : paths){
						for (int i = 0; i < mapNodes.size() - 1; i++) {
							double x1 = backend.getCoordinates(mapNodes).get(i)[0];
							double y1 = backend.getCoordinates(mapNodes).get(i)[1];
							double x2 = backend.getCoordinates(mapNodes).get(i + 1)[0];
							double y2 = backend.getCoordinates(mapNodes).get(i + 1)[1];
							Graphics2D g2 = (Graphics2D) g;
							g2.setStroke(new BasicStroke(5));
							g2.setColor(Color.white);
							g2.drawLine((int) x1 - (int)panX, (int) y1 - (int)panY, (int) x2 - (int)panX, (int) y2 - (int)panY);
						}
						//load next map function in another tab pane
						//
						drawLine = true;
						removeLine = false;
					}
				}
				repaint();
				graphics.setTransform(saveTransform); // reset to original transform to prevent weird border mishaps
			}
		}
		
		public String getID(){
			return this.panelID;
		}
		public void setScale(double scaleAmt){
			this.scale = scaleAmt;
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

		
		/*JLabel lblInvalidEntry = new JLabel("Invalid Entry");
		lblInvalidEntry.setVisible(false);
		panel_10.add(lblInvalidEntry);
		/**
		 * @author Andrew Petit 
		 * @description following textfields and actions are needed in order to have a working search bar
		 */
		/*textFieldEnd = new JTextField();
		panel_9.add(textFieldEnd);
		textFieldEnd.setColumns(10);
		textFieldStart = new JTextField();
		panel_8.add(textFieldStart);
		textFieldStart.setColumns(10);
		//when you press enter after entering stuff in textfieldend
		Action actionEnd = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				lblInvalidEntry.setVisible(false);
				System.out.println("Enter was pressed");
				//if the user presses enter without having entered anything in this box
				if (textFieldEnd.getText().equals("")){
					//will need some way to alert the user that they need to enter an end location
					System.out.println("Need to enter a valid start location");
				} else if (!(textFieldEnd.getText().equals(""))) { //if there is something entered check if the name is valid and then basically add the end node
					String endString = textFieldEnd.getText(); //entered text = endString constant
					boolean valid = false;
					Attributes attribute = new Attributes(); //will most likely need some other way of obtaining this information
					//Test if the entered information is a valid node in local map - this will be updated to global map when that is finished
					MapNode n = allNodes.get(0);
					if (startNode == null){
						startNode = n;
						globalMap.setStartNode(startNode);
						if (!setStart){
							panel.chosenNodes.add(0, startNode);
						} else {
							panel.chosenNodes.set(0, startNode);
						}
					}
					for (MapNode mapnode : backend.getLocalMap().getMapNodes()){
						//this follows a similar pattern to how the original nodes are set with the radio buttons
						if(endString.equals(mapnode.getAttributes().getOfficialName()) || mapnode.getAttributes().getAliases().contains(endString)){
							//if endstring is the official name or one of a few different accepted aliases we will allow the end node to be placed
							endNode = mapnode;
							System.out.println("This is the ending node");
							globalMap.setEndNode(endNode);
							if (!setEnd) {
								panel.chosenNodes.add(1, endNode);
								System.out.println(panel.chosenNodes.size());					
							} else {
								panel.chosenNodes.set(1, endNode);
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
									panel.chosenNodes.add(1, endNode);
									System.out.println(panel.chosenNodes.size());					
								} else {
									panel.chosenNodes.set(1, endNode);
								}
								setEnd = true;
								btnCalculateRoute.setEnabled(true);
							}
						} else if(!(textFieldStart.getText().equals(""))){ //if there is something entered in the start field as well as the end field we can go ahead and place both at the same time...
							String startString = textFieldStart.getText();
							for (MapNode mapnode : backend.getLocalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
								if(startString.equals(mapnode.getAttributes().getOfficialName())){
									startNode = mapnode; //set the startNode and then draw it on the map
									System.out.println("This is the starting node");
									globalMap.setStartNode(startNode);
									if (!setStart) {
										panel.chosenNodes.add(0, startNode);
										System.out.println(panel.chosenNodes.size());					
									} else {
										panel.chosenNodes.set(0, startNode);
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
										panel.chosenNodes.add(1, endNode);
										System.out.println(panel.chosenNodes.size());					
									} else {
										panel.chosenNodes.set(1, endNode);
									}
									setEnd = true;
									btnCalculateRoute.setEnabled(true);
									valid = true;
								}
							}
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
		//give end text field an action		
		textFieldEnd.addActionListener(actionEnd);
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
								panel.chosenNodes.add(0, startNode);
								System.out.println(panel.chosenNodes.size());					
							} else {
								panel.chosenNodes.set(0, startNode);
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

		//give start text field an action
		textFieldStart.addActionListener(actionStart);*/

		/**
		 * Information about the step by step directions
		 */
		/*JLabel lblStepbystepDirections = new JLabel("Step-By-Step Directions");
		lblStepbystepDirections.setFont(new Font("Tahoma", Font.PLAIN, 12));*/
		/*textArea1 = new JTextArea();
		textArea1.setRows(15);
		textArea1.setEditable(false);
		scrollPane_1.setViewportView(textArea1);*/

		/*// adds the distance label to the map interface
		lblDistance = new JLabel("");
		panel_1.add(lblDistance);*/

		/**
		 * @author Andrew Petit
		 * @description Button that initates the drawing of a route on a map
		 */
		/*btnCalculateRoute = new JButton("Calculate Route");
		btnCalculateRoute.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(btnCalculateRoute);
		btnCalculateRoute.setEnabled(false);*/
		
		/*btnCalculateRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnCalculateRoute.isEnabled()) {
					allowSetting = false; //once calculate button is pressed user should not be allowed to replace nodes until the original line is removed
					allText = ""; //must set the initial text as empty every time calculate button is pressed
					Speaker speaker = new Speaker(Constants.BUTTON_PATH);
					speaker.play();
					//basically waypoint stuff -- find a path between every node in the chosenNodes list of mapnodes
					for(int i = 0; i < panel.chosenNodes.size() - 1; i++){
						ArrayList<MapNode> wayPoints = new ArrayList<MapNode>();
						wayPoints = backend.runAStar(panel.chosenNodes.get(i), panel.chosenNodes.get(i + 1));
						paths.add(wayPoints);
					}
					//draw the line on the map
					drawLine = true;
					//set the initial distance as 0 
					int distance = 0;
					//update the step by step directions and distance for each waypoint added
					for (ArrayList<MapNode>wayPoints : paths){
						String all = "";
						distance += backend.getDistance(wayPoints);
						for (String string : backend.displayStepByStep(wayPoints)) {
							all += string + "\n";
						}
						allText += all + "\n";
					}

					// this should only display when the user calculates the
					// astar algorithm
					
					lblDistance.setText("Distance in feet:" + distance);
					//this sets the textarea with the step by step directions
					textArea1.setText(allText);
					btnCalculateRoute.setEnabled(false);
					btnReset.setEnabled(true);


					btnEmail_1.setEnabled(true); //this is where email button should be enabled
				}
			}
		});*/

	/**
	 * @author Andrew Petit
	 * @description Resets all of the relevant information on the form and the background information
	 */
	/*public void reset() {
		allowSetting = true; //allow user to reset nodes only once reset is pressed
		globalMap.setStartNode(null);
		globalMap.setEndNode(null);
		reset = true;

		// allows the user to re-input start and end nodes
		setEnd = false;
		setStart = false;
		paths.clear();
		backend.removePath(globalMap.getMiddleNodes());

		panel.chosenNodes.clear();
		//backend.removePath(backend.getPath()); // this is obsolete now

		// if the line needs to be removed
		// going to need to add a method here - to remove nodes from path
		lblDistance.setText("");
		textArea1.setText("");
		btnReset.setEnabled(false);
		//btnEmail.setEnabled(false); -- for some reason this does not work -- will be looking into...
		removeLine = true;
	}*/

	/**
	 * A local class to allow drawing on a given panel. This allows us to
	 * override the paintComponent() method and ensure any drawn graphics are
	 * displayed without interfering with others. Think of this like a custom
	 * canvas.
	 * 
	 * @author Trevor
	 */
	/*class DrawingPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		ArrayList<MapNode> localNodes;
		Image mapImage;
		ArrayList<MapNode> chosenNodes;*/

		/**
		 * Constructor
		 * 
		 * @param nodes
		 *            The list of nodes already existing on the map. TODO: Make
		 *            these invisible !
		 * @param map
		 *            The map image for the current LocalMap
		 *//*
		public DrawingPanel(ArrayList<MapNode> nodes, Image map, Dimension size){
			setBorder(BorderFactory.createLineBorder(Color.black));
			this.localNodes = nodes;
			this.mapImage = map;
			setOpaque(false);
			chosenNodes = new ArrayList<MapNode>(); // Index 0: StartNode; Index 1: EndNode

			/**
			 * On mouse click, display the points which represent the start and
			 * end nodes. These will also set the backend to these points on the
			 * panel.
			 *//*
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (allowSetting == true){
						// figure out where the closest map node is, set that node as a startnode the StartingNode
						Point clickedAt = me.getPoint();
						MapNode node = backend.findNearestNode(clickedAt.getX(), clickedAt.getY());
					
						if(chosenNodes.size() == 0){
							//globalMap.setStartNode(node);
							btnReset.setEnabled(true);
						}
						else{
							MapNode endNode = globalMap.getEndNode();
							if(endNode != null)
							backend.addToMiddleNodes(endNode);
						
							//globalMap.setEndNode(node);
							btnCalculateRoute.setEnabled(true);
						}

						chosenNodes.add(node);

						repaint();
					}
				}
			});	
		}*/

		/**
		 * Paints the map image to the Panel and temporarily prints a visual
		 * indication of Node locations
		 * 
		 * @param g
		 *            The current graphics object for the main frame
		 */
		/*public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D graphics = (Graphics2D) g;

			// Draws the map and places pre-existing node data onto the map as
			// well start and end nodes if they have been set
			graphics.drawImage(this.mapImage, 0, 0, this);

			// Sets the color of the start and end nodes to be different
			graphics.setColor(Color.RED);
			for (int i = 0; i < this.chosenNodes.size(); i++) {
				if(i == 0){
					graphics.setColor(Color.RED);
					graphics.fillOval((int) this.chosenNodes.get(i).getXPos() - 5, (int) this.chosenNodes.get(i).getYPos() - 5, 10, 10);
				} 
				else if(i == this.chosenNodes.size()-1){
					graphics.setColor(Color.GREEN);
					graphics.fillOval((int) this.chosenNodes.get(i).getXPos() - 5, (int) this.chosenNodes.get(i).getYPos() - 5, 10, 10);
				}
				else {
					graphics.setColor(Color.ORANGE);
					graphics.fillOval((int) this.chosenNodes.get(i).getXPos() - 5, (int) this.chosenNodes.get(i).getYPos() - 5, 10, 10);
				}
			}

			/**
			 * @author Andrew Petit 
			 * @description initiates drawing of the path on the screen from the coordinates of the path 
			 * --if removeline is true redraws line to be white to remove the line from the screen
			 */
			// essentially draws the line on the screen 
			/*if (GUIFront.drawLine = true) {
				for (ArrayList<MapNode> mapNodes : paths){
					for (int i = 0; i < mapNodes.size() - 1; i++) {
						double x1 = backend.getCoordinates(mapNodes).get(i)[0];
						double y1 = backend.getCoordinates(mapNodes).get(i)[1];
						double x2 = backend.getCoordinates(mapNodes).get(i + 1)[0];
						double y2 = backend.getCoordinates(mapNodes).get(i + 1)[1];
						double alpha = 0.5;
						Color color = new Color(0, 1, 1, (float) alpha);
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(5));
						g2.setColor(color);
						g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
					}
				}
				drawLine = false;
				removeLine = true;
			} else if (GUIFront.removeLine == true) {
				for (ArrayList<MapNode> mapNodes : paths){
					for (int i = 0; i < mapNodes.size() - 1; i++) {
						double x1 = backend.getCoordinates(mapNodes).get(i)[0];
						double y1 = backend.getCoordinates(mapNodes).get(i)[1];
						double x2 = backend.getCoordinates(mapNodes).get(i + 1)[0];
						double y2 = backend.getCoordinates(mapNodes).get(i + 1)[1];
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(5));
						g2.setColor(Color.white);
						g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
					}
				}
				drawLine = true;
				removeLine = false;
			}

			repaint();
		}

		public void setMapImage(Image map) {
			this.mapImage = map;
		}

		public void setMapNodes(ArrayList<MapNode> currentLoadedNodes) {
			this.localNodes = currentLoadedNodes;
		}*/
}

package main.gui;

import java.awt.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import main.*;
import main.util.Constants;
import main.util.Speaker;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.JRadioButton;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;

/**
 * This class contains code for the main applications GUI interface as well as
 * implementation for its various functionality such as drawing the route.
 * 
 * @author Trevor
 */
@SuppressWarnings("serial")
public class GUIFront extends JFrame {

	private static GUIBack backend;
	private static GlobalMap globalMap;
	private boolean setStart = false, setEnd = false; // keeps track of whether you have set a start or end node yet
	public static boolean drawLine = false;
	public static boolean removeLine = false;
	public static boolean reset = false;
	public static boolean allowSetting = true;
	public static MapNode endNode = null;
	public static MapNode startNode = null;
	public static String allText = "";
	public static ArrayList<ArrayList<MapNode>> paths = new ArrayList<ArrayList<MapNode>>();

	private JPanel contentPane;
	private DrawingPanel panel;
	private JButton btnCalculateRoute, btnReset, btnEmail;
	private JRadioButton rdbtnSHFloor1, rdbtnSHFloor2, rdbtnSHFloor3;
	private JLabel lblDistance;
	private JTextArea textArea1;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JTextField textFieldStart;
	private JTextField textFieldEnd;


	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public GUIFront(int numLocalMaps, File[] localMapFilenames) throws IOException, ClassNotFoundException {
		// Instantiate GUIBack to its default
		String defaultMapImage = Constants.DEFAULT_MAP_IMAGE;
		backend = new GUIBack(defaultMapImage, null);

		// Initialize the GlobalMap variable with all of the LocalMaps and all
		// of their nodes
		globalMap = new GlobalMap();

		ArrayList<LocalMap> tmpListLocal = new ArrayList<LocalMap>(); // temporary list of LocalMaps to be initialized
		for (int i = 0; i < numLocalMaps; i++) {
			backend.loadLocalMap(localMapFilenames[i].getName()); // sets the current LocalMap each filename from the "data.localmaps" folder
			tmpListLocal.add(backend.getLocalMap());
		}
		globalMap.setLocalMaps(tmpListLocal);
		backend.setLocalMap(tmpListLocal.get(0));

		// add the collection of nodes to the ArrayList of GlobalMap
		ArrayList<MapNode> allNodes = new ArrayList<MapNode>();
		for (LocalMap local : tmpListLocal) {

			if (!local.getMapNodes().equals(null)) // as long as the LocalMap isn't null, add its nodes to the GlobalMap
				System.out.println(local.getMapNodes());
				allNodes.addAll(local.getMapNodes());
		}
		globalMap.setMapNodes(allNodes);

		/**
		 * GUI related code
		 */
		// This will setup the main JFrame to be maximized on start and have a
		// defined content pane
		setTitle("WPI Navigation Tool");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Image of the default map loaded into backend
		Image map = new ImageIcon(Constants.IMAGES_PATH + "/" + backend.getLocalMap().getMapImageName()).getImage();

		/**
		 * Window Builder generated code. GroupLayout auto-generated for custom
		 * formatting.
		 */
		JLayeredPane layeredPane = new JLayeredPane();

		layeredPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		JPanel panel_1 = new JPanel();
		JPanel panel_2 = new JPanel();

		JScrollPane scrollPane_1 = new JScrollPane();

		JPanel panel_3 = new JPanel();

		JPanel panel_5 = new JPanel();

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));

		JLabel lblMapSelection = new JLabel("Map Selection");
		lblMapSelection.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_7 = new JPanel();

		JPanel panel_8 = new JPanel();

		JPanel panel_9 = new JPanel();

		JPanel panel_10 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 1109, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(10)
										.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
									.addComponent(lblMapSelection)))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGap(18)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))))
							.addGroup(gl_contentPane.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(71)
							.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(1114)
					.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(12, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_9, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 22, Short.MAX_VALUE))
					.addGap(7)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addGap(96)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMapSelection)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
							.addGap(26)
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 677, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		JLabel lblInvalidEntry = new JLabel("Invalid Entry");
		lblInvalidEntry.setVisible(false);
		panel_10.add(lblInvalidEntry);
		/**
		 * @author Andrew Petit 
		 * @description following textfields and actions are needed in order to have a working search bar
		 */
		textFieldEnd = new JTextField();
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
						backend.setStartNode(startNode);
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
							backend.setEndNode(endNode);
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
								backend.setEndNode(endNode);
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
									startNode = mapnode; //set the startNode in a similar way that is done when using radio buttons refer to drawing pannel mouse click
									System.out.println("This is the starting node");
									backend.setStartNode(startNode);
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
									endNode = node;
									System.out.println("This is the ending node!");
									backend.setEndNode(endNode);
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
							startNode = mapnode; //set the startNode in a similar way that is done when using radio buttons refer to drawing pannel mouse click
							System.out.println("This is the starting node");
							backend.setStartNode(startNode);
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
		textFieldStart.addActionListener(actionStart);
		// Code for button - if it is pressed allow the program to draw the line
		// on the map
		JButton btnEmail_1 = new JButton("Email ");
		panel_7.add(btnEmail_1);
		btnEmail_1.setEnabled(false); 
		//This needs to be outside of the other action listener or it will cause problems
		btnEmail_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmailGUI newEmail = new EmailGUI();
				newEmail.setVisible(true); //Opens EmailGUI Pop-Up
			}
		});

		/*
		 * Radio Buttons to change the currently loaded map data,
		 * actionlisteners implemented after MapPanel
		 */
		rdbtnSHFloor1 = new JRadioButton("Stratton Hall First Floor");
		rdbtnSHFloor1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnSHFloor1.setSelected(true);
		buttonGroup_1.add(rdbtnSHFloor1);

		rdbtnSHFloor2 = new JRadioButton("Stratton Hall Second Floor");
		rdbtnSHFloor2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		buttonGroup_1.add(rdbtnSHFloor2);

		rdbtnSHFloor3 = new JRadioButton("Stratton Hall Third Floor");
		rdbtnSHFloor3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		buttonGroup_1.add(rdbtnSHFloor3);

		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addComponent(rdbtnSHFloor3)
								.addComponent(rdbtnSHFloor2).addComponent(rdbtnSHFloor1))
								.addContainerGap(19, Short.MAX_VALUE)));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
						.addComponent(rdbtnSHFloor1).addGap(18).addComponent(rdbtnSHFloor2, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGap(13).addComponent(rdbtnSHFloor3).addContainerGap()));
		gl_panel_6.linkSize(SwingConstants.HORIZONTAL, new Component[] { rdbtnSHFloor1, rdbtnSHFloor2, rdbtnSHFloor3 });
		panel_6.setLayout(gl_panel_6);

		/**
		 * Information about the step by step directions
		 */
		JLabel lblStepbystepDirections = new JLabel("Step-By-Step Directions");
		lblStepbystepDirections.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
				gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3.createSequentialGroup().addGap(43)
						.addComponent(lblStepbystepDirections).addContainerGap(49, Short.MAX_VALUE)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_3.createSequentialGroup().addContainerGap(28, Short.MAX_VALUE)
				.addComponent(lblStepbystepDirections)));
		panel_3.setLayout(gl_panel_3);

		textArea1 = new JTextArea();
		textArea1.setRows(15);
		textArea1.setEditable(false);
		scrollPane_1.setViewportView(textArea1);

		// adds the distance label to the map interface
		lblDistance = new JLabel("");
		panel_1.add(lblDistance);

		// disable reset button
		btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnReset.setEnabled(false);
		panel_5.add(btnReset);

		/**
		 * @author Andrew Petit
		 * @description Button that initates the drawing of a route on a map
		 */
		btnCalculateRoute = new JButton("Calculate Route");
		btnCalculateRoute.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(btnCalculateRoute);
		btnCalculateRoute.setEnabled(false);
		
		btnCalculateRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnCalculateRoute.isEnabled()) {
					allowSetting = false;
					allText = "";
					Speaker speaker = new Speaker(Constants.BUTTON_PATH);
					speaker.play();
					for(int i = 0; i < panel.chosenNodes.size() - 1; i++){
						ArrayList<MapNode> wayPoints = new ArrayList<MapNode>();
						wayPoints = backend.runAStar(panel.chosenNodes.get(i), panel.chosenNodes.get(i + 1));
						paths.add(wayPoints);
					}
					drawLine = true;
					int distance = 0;
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

					// STEP BY STEP DIRECTIONS TEXT
					// basically just places each string into the array one row
					// at a time - if, and this is a big IF, /n works in this
					// context

					textArea1.setText(allText);
					btnCalculateRoute.setEnabled(false);
					btnReset.setEnabled(true);


					btnEmail_1.setEnabled(true); //this is where email button should be enabled
				}
			}
		});

		// Creates a new DrawingPanel object which will display the map image
		// and load up MapNode data
		panel = new DrawingPanel(backend.getLocalMap().getMapNodes(), map, layeredPane.getSize());
		GroupLayout gl_layeredPane = new GroupLayout(layeredPane);
		gl_layeredPane.setHorizontalGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 1105, Short.MAX_VALUE));
		gl_layeredPane.setVerticalGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE));
		layeredPane.setLayout(gl_layeredPane);
		contentPane.setLayout(gl_contentPane);

		// code for reset button
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});

		/**
		 * Each radiobutton when selected will load the specified map image and
		 * node data. This will refresh the display as well as call reset() to
		 * ensure proper information is reset.
		 * 
		 * TODO: Make it more intuitive, right now it just gets the LocalMap at
		 * a certain index because they're ordered. Maybe use some sort of
		 * name-getting system in the future.
		 */
		rdbtnSHFloor1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (rdbtnSHFloor1.isSelected()) {
					backend.setLocalMap(globalMap.getLocalMaps().get(0)); // sets the localMap to upstairs and reloads(?)

					panel.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + backend.getLocalMap().getMapImageName()).getImage()); // load map image
					panel.setMapNodes(backend.getLocalMap().getMapNodes());

					reset();
				}
			}
		});
		rdbtnSHFloor2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (rdbtnSHFloor2.isSelected()) {
					backend.setLocalMap(globalMap.getLocalMaps().get(1));
					Image temp = new ImageIcon(Constants.IMAGES_PATH + "/" + backend.getLocalMap().getMapImageName()).getImage();

					panel.setMapImage(new ImageIcon(Constants.IMAGES_PATH + "/" + backend.getLocalMap().getMapImageName()).getImage()); // load map image
					panel.setMapNodes(backend.getLocalMap().getMapNodes());

					reset();
				}
			}
		});
		rdbtnSHFloor3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (rdbtnSHFloor3.isSelected()) {
					backend.setLocalMap(globalMap.getLocalMaps().get(2));
					Image temp = new ImageIcon(Constants.IMAGES_PATH + "/" + backend.getLocalMap().getMapImageName()).getImage();
					panel.setMapImage(
							new ImageIcon(Constants.IMAGES_PATH + "/" + backend.getLocalMap().getMapImageName()).getImage()); // load map image
					panel.setMapNodes(backend.getLocalMap().getMapNodes());

					reset();
				}
			}
		});

		pack();
		setVisible(true);
	}

	/**
	 * @author Andrew Petit
	 * @description Resets all of the relevant information on the form and the background information
	 */
	public void reset() {
		allowSetting = true;
		backend.setStartNode(null);
		backend.setEndNode(null);
		reset = true;

		// allows the user to re-input start and end nodes
		setEnd = false;
		setStart = false;
		/*for(ArrayList<MapNode> mapNodes : paths){
			mapNodes.clear();
			paths.remove(mapNodes);
		}*/
		paths.clear();

		panel.chosenNodes.clear();
		//backend.removePath(backend.getPath()); // this is obsolete now

		// if the line needs to be removed
		// going to need to add a method here - to remove nodes from path
		lblDistance.setText("");
		textArea1.setText("");
		btnReset.setEnabled(false);
		//btnEmail.setEnabled(false); -- for some reason this does not work -- will be looking into...
		removeLine = true;
	}

	/**
	 * A local class to allow drawing on a given panel. This allows us to
	 * override the paintComponent() method and ensure any drawn graphics are
	 * displayed without interfering with others. Think of this like a custom
	 * canvas.
	 * 
	 * @author Trevor
	 */
	class DrawingPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		ArrayList<MapNode> localNodes;
		Image mapImage;
		ArrayList<MapNode> chosenNodes;

		/**
		 * Constructor
		 * 
		 * @param nodes
		 *            The list of nodes already existing on the map. TODO: Make
		 *            these invisible !
		 * @param map
		 *            The map image for the current LocalMap
		 */
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
			 */
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (allowSetting == true){
						// figure out where the closest map node is, set that node as a startnode the StartingNode
						Point clickedAt = me.getPoint();
						MapNode node = backend.findNearestNode(clickedAt.getX(), clickedAt.getY());
					
						if(chosenNodes.size() == 0){
							//backend.setStartNode(node);
							btnReset.setEnabled(true);
						}
						else{
							MapNode endNode = backend.getEndNode();
							if(endNode != null)
							backend.addToMiddleNodes(endNode);
						
							//backend.setEndNode(node);
							btnCalculateRoute.setEnabled(true);
						}

						chosenNodes.add(node);

						repaint();
					}
				}
			});	
		}

		/**
		 * Paints the map image to the Panel and temporarily prints a visual
		 * indication of Node locations
		 * 
		 * @param g
		 *            The current graphics object for the main frame
		 */
		public void paintComponent(Graphics g) {
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
			if (GUIFront.drawLine = true) {
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
		}
	}
}
package main.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.slidinglayout.SLConfig;
import aurelienribon.slidinglayout.SLKeyframe;
import aurelienribon.slidinglayout.SLPanel;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import main.*;
import main.util.Constants;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;

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
	private static boolean setStart = false; // keeps track of whether you have set a start or end node yet
	private static boolean setEnd = false;
	public static boolean drawLine = false;
	public static boolean removeLine = false;
	public static boolean reset = false;
	public static MapNode startNode = null, endNode = null;
	public static String allText = "";

	private JPanel contentPane;
	private static JTextField textFieldEnd, textFieldStart;
	
	private SLPanel slidePanel;
	private TweenPanel panelMap, panelDirections;
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
				allNodes.addAll(local.getMapNodes());
		}
		globalMap.setMapNodes(allNodes);

		/**
		 * GUI related code
		 */
		// This will setup the main JFrame to be maximized on start
		setTitle("Era of Navigation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 499, 396);
		setResizable(false);
		setPreferredSize(new Dimension(820, 650));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Image of the default map loaded into backend
		Image mapPath = new ImageIcon(Constants.IMAGES_PATH + "/" + backend.getLocalMap().getMapImageName()).getImage();

		/**
		 * @author Andrew Petit 
		 * @description following textfields and actions are needed in order to have a working search bar
		 */		
		//when you press enter after entering stuff in textfieldend
		Action actionEnd = new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("Enter was pressed");
				//if the user presses enter without having entered anything in this box
				if (textFieldEnd.getText().equals("")){
					//will need some way to alert the user that they need to enter an end location
					System.out.println("Need to enter a valid start location");
					setEnd = false; // reset setEnd
				} else if (!(textFieldEnd.getText().equals(""))) { //if there is something entered check if the name is valid and then basically add the end node
					String endString = textFieldEnd.getText(); //entered text = endString constant
					boolean valid = false;
					boolean typeUnfound = true;
					Attributes attribute = new Attributes(); //will most likely need some other way of obtaining this information
					//Test if the entered information is a valid node in local map - this will be updated to global map when that is finished
					for (MapNode mapnode : backend.getLocalMap().getMapNodes()){
						//this follows a similar pattern to how the original nodes are set with the radio buttons
						if(endString.equals(mapnode.getAttributes().getOfficialName()) || mapnode.getAttributes().getAliases().contains(endString)){
							//if endstring is the official name or one of a few different accepted aliases we will allow the end node to be placed
							endNode = mapnode;
							System.out.println("This is the ending node");
							backend.setEndNode(endNode);
							if (!setEnd) {
								panelMap.startEndNodes.add(1, endNode);
								System.out.println(panelMap.startEndNodes.size());					
							} else {
								panelMap.startEndNodes.set(1, endNode);
							}
							setEnd = true;
							valid = true;
							typeUnfound = false;
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
									panelMap.startEndNodes.add(1, endNode);
									System.out.println(panelMap.startEndNodes.size());					
								} else {
									panelMap.startEndNodes.set(1, endNode);
								}
								setEnd = true;
								typeUnfound = false;
								//btnCalculateRoute.setEnabled(true);
								}
							} else if(!(textFieldStart.getText().equals(""))){ //if there is something entered in the start field as well as the end field we can go ahead and place both at the same time...
								String startString = textFieldStart.getText();
								for (MapNode mapnode : backend.getLocalMap().getMapNodes()){ //for the time being this will remain local map nodes, once global nodes are done this will be updated
									if(startString.equals(mapnode.getAttributes().getOfficialName())){
										startNode = mapnode; //set the startNode in a similar way that is done when using radio buttons refer to drawing pannel mouse click
										System.out.println("This is the starting node");
										backend.setStartNode(startNode);
										if (!setStart) {
											panelMap.startEndNodes.add(0, startNode);
											System.out.println(panelMap.startEndNodes.size());					
										} else {
											panelMap.startEndNodes.set(0, startNode);
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
											panelMap.startEndNodes.add(1, endNode);
											System.out.println(panelMap.startEndNodes.size());					
										} else {
											panelMap.startEndNodes.set(1, endNode);
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
						}
				}
			}
		};
		
		//when you press enter after entering stuff in textfieldStart
		Action actionStart = new AbstractAction(){
			@Override 
			public void actionPerformed(ActionEvent e){
				System.out.println("Enter was pressed");
				if (textFieldStart.getText().equals("")){
					//will need some way to alert the user that they need to enter a start location
					System.out.println("Need to enter a valid start location");
					setStart = false; // reset setStart
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
								panelMap.startEndNodes.add(0, startNode);
								System.out.println(panelMap.startEndNodes.size());					
							} else {
								panelMap.startEndNodes.set(0, startNode);
							}
							setStart = true;
							valid = true;
						}
					}
					if (valid == false){
						//tell user this entry is invalid
						System.out.println("Invalid entry");
					}
				}	
			}
		};
		
		/**
		 * GroupLayout code for tabbedpane and textfields (Temporary)
		 */
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		textFieldStart = new JTextField();
		textFieldStart.setText("");
		//give start text field an action
		textFieldStart.addActionListener(actionStart);
		
		textFieldEnd = new JTextField("");
		textFieldEnd.setColumns(10);
		//give end text field an action		
		textFieldEnd.addActionListener(actionEnd);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(textFieldStart, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textFieldEnd, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(13)
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		
		
		/**
		 * Tween related code to make the animations work
		 */
		slidePanel = new SLPanel();
		panelMap = new TweenPanel(backend.getLocalMap().getMapNodes(), mapPath, "1");
		panelDirections = new TweenPanel("2");
		panelDirections.setBackground(Color.RED);
		
		// add to the tabbed pane
		tabbedPane.add(slidePanel, BorderLayout.CENTER);
		
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
				.row(1f).col(600).col(150) // 600xH | 150xH
				.place(0, 0, panelMap)
				.place(0, 1, panelDirections);
		
		// Initialize tweening
		slidePanel.setTweenManager(SLAnimator.createTweenManager());
		slidePanel.initialize(mainConfig);
		
		
		pack();
		setVisible(true);
	}
		
	/**
	 * Enable/Disable actions
	 */
	public void enableActions(){
		panelMap.enableAction();
		panelDirections.enableAction();
	}
	public void disableActions(){
		panelMap.disableAction();
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
	 * Resets all of the relevant information on the form and the background
	 * information
	 */
	public void reset() {
		//rdbtnStartNode.setSelected(true); // part of a button group, only one
											// can be selected at a time
		backend.setStartNode(null);
		backend.setEndNode(null);
		reset = true;

		// allows the user to re-input start and end nodes
		setEnd = false;
		setStart = false;

		panelMap.startEndNodes.clear();

		// if the line needs to be removed
		// going to need to add a method here - to remove nodes from path
		//lblDistance.setText("");
		//textArea1.setText("");
		backend.removePath();
		//btnReset.setEnabled(false);
		//btnCalculateRoute.setEnabled(true);
		removeLine = true;
	}
	
	
	
	/**
	 * Class for a custom panel to do drawing and tweening. This can be seperated into a seperate class file
	 * but it functions better as a private class
	 */
	public static class TweenPanel extends JPanel {
		ArrayList<MapNode> localNodes;
		public ArrayList<MapNode> startEndNodes;
		
		private final TweenManager tweenManager = SLAnimator.createTweenManager();
		private JLabel labelMainPanel = new JLabel();
		private JLabel labelStep = new JLabel();
		private Image mapImage;
		private Runnable action;
		private boolean actionEnabled = true;
		private boolean hover = false;
		private int borderThickness = 2;
		private String panelID;

		/**
		 * Constructor for any tab that would hold a map
		 * @param mapNodes A list of map nodes of the currently loaded map
		 * @param mapPath The image of the map 
		 * @param panelID Represents the ID of a panel to keep track of it 
		 */
		public TweenPanel(ArrayList<MapNode> mapNodes, Image mapPath, String panelID) {
			setLayout(new BorderLayout());
			
			this.localNodes = mapNodes;
			startEndNodes = new ArrayList<MapNode>();

			labelMainPanel.setFont(new Font("Sans", Font.BOLD, 90));
			labelMainPanel.setVerticalAlignment(SwingConstants.CENTER);
			labelMainPanel.setHorizontalAlignment(SwingConstants.CENTER);
			labelMainPanel.setText(panelID);
			
			this.mapImage = mapPath;
			this.panelID = panelID;			
			
			/* Map Node Selection Stuff */
			/**
			 * On mouse click, display the points which represent the start and
			 * end nodes. These will also set the backend to these points on the
			 * panel.
			 */
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					Point clickedAt = me.getPoint();
					
					if(textFieldStart.getText().length() <= 1){
						startNode = backend.findNearestNode(clickedAt.getX(), clickedAt.getY());
						
						System.out.println("This is the starting node!");
						backend.setStartNode(startNode);
						
						if (!setStart) {
							startEndNodes.add(0, startNode);
							setStart = true;
						} else {
							startEndNodes.set(0, startNode);
						}
						textFieldStart.setText("Start Node Selected!");
					} else if (textFieldEnd.getText().length() <= 1) {
						endNode = backend.findNearestNode(clickedAt.getX(), clickedAt.getY());
						
						System.out.println("This is the ending node!");
						backend.setEndNode(endNode);
						
						if(!setEnd && setStart) {
							startEndNodes.add(1, endNode);
							setEnd = true;
						} else {
							startEndNodes.set(1, endNode);
						}
						textFieldEnd.setText("End Node Selected!");
					}
					
					// If both have been set, we should calculate a route
					if (setEnd == true && setStart == true){
						//Speaker speaker = new Speaker(Constants.BUTTON_PATH);
						//speaker.play();
						
						System.out.println("About to run AStar");
						backend.setPath(backend.runAStar());

						System.out.println("Just ran AStar");
						drawLine = true;

						// this should only display when the user calculates the
						// astar algorithm
						//lblDistance.setText(backend.getDistance());
						
						// STEP BY STEP DIRECTIONS TEXT
						// basically just places each string into the array one row
						// at a time - if, and this is a big IF, /n works in this
						// context
						for (String string : backend.displayStepByStep()) {
							allText += string + "\n";
						}
						
						//textArea1.setText(allText);
						//btnCalculateRoute.setEnabled(false);
						//btnReset.setEnabled(true);
						
						//btnEmail_1.setEnabled(true); //this is where email button should be enabled
						
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

			if(this.mapImage == null)
				graphics.drawString(getID(), this.getWidth()/2, this.getHeight()/2);
			else {
				// Scale the map relative to the panels current size and your current viewing window
				graphics.drawImage(mapImage, 0, 0, mapImage.getWidth(null), mapImage.getHeight(null), this);	
						
				// Test drawing of map nodes
				for(MapNode n : localNodes){
					graphics.fillOval((int)n.getXPos() - 5, (int)n.getYPos() - 5, 10, 10);
				}
				
				// Colors start and end differently
				for (int i = 0; i < this.startEndNodes.size(); i++) {
					if(i == 0){
						graphics.setColor(Color.RED);
						graphics.fillOval((int) this.startEndNodes.get(i).getXPos() - 5, (int) this.startEndNodes.get(i).getYPos() - 5, 10, 10);
					} else { //if i == 1
						graphics.setColor(Color.GREEN);
						graphics.fillOval((int) this.startEndNodes.get(i).getXPos() - 5, (int) this.startEndNodes.get(i).getYPos() - 5, 10, 10);
					}
				}
				
				// essentially draws the line on the screen 
				if (GUIFront.drawLine = true) {
					for (int i = 0; i < backend.getCoordinates().size() - 1; i++) {
						double x1 = backend.getCoordinates().get(i)[0];
						double y1 = backend.getCoordinates().get(i)[1];
						double x2 = backend.getCoordinates().get(i + 1)[0];
						double y2 = backend.getCoordinates().get(i + 1)[1];
						double alpha = 0.5;
						Color color = new Color(0, 1, 1, (float) alpha);
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(5));
						g2.setColor(color);
						g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
					}
					drawLine = false;
					removeLine = true;
				} else if (GUIFront.removeLine == true) {
					for (int i = 0; i < backend.getCoordinates().size() - 1; i++) {
						double x1 = backend.getCoordinates().get(i)[0];
						double y1 = backend.getCoordinates().get(i)[1];
						double x2 = backend.getCoordinates().get(i + 1)[0];
						double y2 = backend.getCoordinates().get(i + 1)[1];
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(5));
						g2.setColor(Color.white);
						g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
					}
					drawLine = true;
					removeLine = false;
				}
						
			}
		}
		
		public String getID(){
			return this.panelID;
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
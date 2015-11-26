package main.gui;

import java.awt.*;
import java.awt.geom.Line2D;
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
import javax.swing.text.BadLocationException;

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

	private JPanel contentPane;
	private DrawingPanel panel;
	private JButton btnCalculateRoute, btnReset;
	private JRadioButton rdbtnStartNode, rdbtnEndNode;
	private JRadioButton rdbtnSHFloor1, rdbtnSHFloor2, rdbtnSHFloor3;
	private JLabel lblDistance;
	private JTextArea textArea1;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();

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
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));

		JLabel lblSelectANode = new JLabel("Node Selection");
		lblSelectANode.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_5 = new JPanel();

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));

		JLabel lblMapSelection = new JLabel("Map Selection");
		lblMapSelection.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 1109, GroupLayout.PREFERRED_SIZE)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(18)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 207,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(57).addGroup(gl_contentPane
								.createParallelGroup(Alignment.LEADING).addComponent(lblSelectANode)
								.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
								.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup().addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup().addGap(10)
												.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
										.addComponent(lblMapSelection))))
				.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addGap(11).addComponent(lblSelectANode)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 60,
												GroupLayout.PREFERRED_SIZE)
								.addGap(11)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 48,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 48,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
								.addComponent(lblMapSelection).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
								.addGap(38)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
						.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 677, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

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

		rdbtnStartNode = new JRadioButton("Start Location");
		buttonGroup.add(rdbtnStartNode);
		rdbtnStartNode.setSelected(true);
		rdbtnStartNode.setFont(new Font("Tahoma", Font.PLAIN, 12));

		rdbtnEndNode = new JRadioButton("End Location");
		buttonGroup.add(rdbtnEndNode);
		rdbtnEndNode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
						.addContainerGap().addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(rdbtnStartNode).addComponent(rdbtnEndNode))
				.addContainerGap(10, Short.MAX_VALUE)));
		gl_panel_4
				.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						gl_panel_4.createSequentialGroup().addContainerGap(20, Short.MAX_VALUE)
								.addComponent(rdbtnStartNode).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(rdbtnEndNode).addContainerGap()));
		panel_4.setLayout(gl_panel_4);

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

		// Code for button - if it is pressed allow the program to draw the line
		// on the map
		btnCalculateRoute = new JButton("Calculate Route");
		btnCalculateRoute.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_2.add(btnCalculateRoute);
		btnCalculateRoute.setEnabled(false);
		btnCalculateRoute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Speaker speaker = new Speaker(Constants.BUTTON_PATH);
				speaker.play();
				
				if (btnCalculateRoute.isEnabled()) {
					System.out.println("About to run AStar");
					backend.setPath(backend.runAStar());

					System.out.println("Just ran AStar");
					drawLine = true;

					// this should only display when the user calculates the
					// astar algorithm
					lblDistance.setText(backend.getDistance());
					
					// STEP BY STEP DIRECTIONS TEXT
					// basically just places each string into the array one row
					// at a time - if, and this is a big IF, /n works in this
					// context
					String allText = "";
					for (String string : backend.displayStepByStep()) {
						allText += string + "\n";
					}
					
					textArea1.setText(allText);
					btnCalculateRoute.setEnabled(false);
					btnReset.setEnabled(true);
					
					
					/*
					 * @Author Nick Gigliotti
					 * Email Directions Button
					 * 
					 */
					JButton btnEmail = new JButton("Email Directions"); //Initial Email Button
					panel_1.add(btnEmail);
					EmailGUI newEmail = new EmailGUI();
					
			        btnEmail.addActionListener(new ActionListener() {
			        	public void actionPerformed(ActionEvent e) {
			        		
			        		newEmail.setVisible(true); //Opens EmailGUI Pop-Up
			        	}
			        }
			        );
			        

					/*
					 * } else { btnCalculateRoute.setEnabled(true);
					 * 
					 * //if the line needs to be removed //going to need to add
					 * a method here - to remove nodes from path
					 * backend.removePath(); removeLine = true;
					 * lblDistance.setText(backend.getDistance());
					 * textArea1.setText("");
					 * 
					 * //change the name of button back to what it originally
					 * was btnCalculateRoute.setText("Calculate Route"); }
					 */
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
	 * Resets all of the relevant information on the form and the background
	 * information
	 */
	public void reset() {
		rdbtnStartNode.setSelected(true); // part of a button group, only one
											// can be selected at a time
		backend.setStartNode(null);
		backend.setEndNode(null);
		reset = true;

		// allows the user to re-input start and end nodes
		setEnd = false;
		setStart = false;

		panel.startEndNodes.clear();

		// if the line needs to be removed
		// going to need to add a method here - to remove nodes from path
		lblDistance.setText("");
		textArea1.setText("");
		backend.removePath();
		btnReset.setEnabled(false);
		btnCalculateRoute.setEnabled(true);
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
		ArrayList<MapNode> startEndNodes;

		/**
		 * Constructor
		 * 
		 * @param nodes
		 *            The list of nodes already existing on the map. TODO: Make
		 *            these invisible !
		 * @param map
		 *            The map image for the current LocalMap
		 */
		public DrawingPanel(ArrayList<MapNode> nodes, Image map, Dimension size) {
			setBorder(BorderFactory.createLineBorder(Color.black));
			this.localNodes = nodes;
			this.mapImage = map;
			setOpaque(false);
			startEndNodes = new ArrayList<MapNode>(); // Index 0: StartNode
														// Index 1: EndNode

			/**
			 * On mouse click, display the points which represent the start and
			 * end nodes. These will also set the backend to these points on the
			 * panel.
			 */
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					/*
					 * Will work on later when user is able to select ANY
					 * location // Only add new points if you haven't set an End
					 * Node yet if(!setEnd){ Point clickedAt = me.getPoint();
					 * 
					 * // If you've set a start node already, set the end node
					 * if(setStart){ setEnd = true; MapNode newNode = new
					 * MapNode((double)clickedAt.x, (double)clickedAt.y, 0);
					 * startEndNodes.add(newNode); backend.setEndNode(newNode);
					 * btnCalculateRoute.setEnabled(true); // allow user to
					 * calculate route } else { setStart = true; MapNode newNode
					 * = new MapNode((double)clickedAt.x, (double)clickedAt.y,
					 * 0); startEndNodes.add(newNode);
					 * backend.setStartNode(newNode); }
					 * 
					 * // Prohibit any new points for now } else {
					 * System.out.println("You've already selected two points");
					 * } repaint(); }
					 */

					// figure out if there is a map node there, if so, set it as
					// the StartingNode
					Point clickedAt = me.getPoint();
					int clickRadius = 10; // clicks anywhere within a circle of radius 10
					if (rdbtnStartNode.isSelected()) {

						for (MapNode n : localNodes) {
							if ((Math.abs(n.getXPos() - clickedAt.getX()) <= clickRadius)
									&& (Math.abs(n.getYPos() - clickedAt.getY()) <= clickRadius)) {
								System.out.println("This is the starting node!");
								backend.setStartNode(n);
								// If this is the first value, add it at index 0
								// else, set the first index
								if (!setStart) {
									startEndNodes.add(0, n);
									System.out.println(startEndNodes.size());
								} else
									startEndNodes.set(0, n);

								setStart = true; // start node has been set at least once
							}
						}
					} else { // rdbtnEndNode is selected

						for (MapNode n : localNodes) {
							if ((Math.abs(n.getXPos() - clickedAt.getX()) <= clickRadius)
									&& (Math.abs(n.getYPos() - clickedAt.getY()) <= clickRadius)) {
								System.out.println("This is the ending node!");
								backend.setEndNode(n);
								btnCalculateRoute.setEnabled(true);

								// If this is the first EndNode selection, add it at index 1 else, set the second index
								if (!setEnd)
									startEndNodes.add(1, n);
								else
									startEndNodes.set(1, n);

								setEnd = true; // end node has been set at least once
							}
						}
					}
					repaint();
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
			
			graphics.setColor(Color.BLUE);
			for (MapNode n : this.localNodes) {
				graphics.fillOval((int) n.getXPos() - 5, (int) n.getYPos() - 5, 10, 10);
			}
			
			// Sets the color of the start and end nodes to be different
			graphics.setColor(Color.RED);
			for (int i = 0; i < this.startEndNodes.size(); i++) {
				if(i == 0){
					graphics.setColor(Color.RED);
					graphics.fillOval((int) this.startEndNodes.get(i).getXPos() - 5, (int) this.startEndNodes.get(i).getYPos() - 5, 10, 10);
				} else { //if i == 1
					graphics.setColor(Color.GREEN);
					graphics.fillOval((int) this.startEndNodes.get(i).getXPos() - 5, (int) this.startEndNodes.get(i).getYPos() - 5, 10, 10);
				}
			}
						
			// working on adding the links between two nodes before hand - works, but will keep out for now
			//ArrayList<MapNode> mapnodes = new ArrayList<MapNode>();
			//for(MapNode neighbors : this.localNodes){
			//	mapnodes.add(this.neighbors);
			//}
		    /*for(MapNode mapnode : mapnodes){
	        	for(MapNode mapnode2 : mapnode.getNeighbors()){
	        		Graphics2D g3 = (Graphics2D) g;
	        		g3.setStroke(new BasicStroke(2));
	        		g3.setColor(Color.gray);
	        		g3.drawLine((int)mapnode.getXPos(), (int) mapnode.getYPos(), (int) mapnode2.getXPos(), (int) mapnode2.getYPos());
	        		mapnode2.deleteNeighborLink(mapnode);
	        		}
	        	mapnodes.remove(mapnode);
	        	repaint();
	        }*/

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
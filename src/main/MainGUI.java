package main;

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
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


/**
 * This class contains code for the main applications GUI interface as well as implementation for its various
 * functionality such as drawing the route.
 * @author Trevor
 */


@SuppressWarnings("serial")
public class MainGUI extends JFrame {

	private static GUIBackend backend;
	private static GlobalMap globalMap;
	boolean setStart = false, setEnd = false; // keeps track of whether you have set a start or end node yet
	public static boolean drawLine = false;
	public static boolean removeLine = false;
	
	private JPanel contentPane;
	JButton btnCalculateRoute;
	
	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public MainGUI(int numLocalMaps, File[] localMapFilenames) throws IOException, ClassNotFoundException {
		// Instantiate GUIBackend to its default
		String defaultMapImage = "downstairsCC.jpg";
		backend = new GUIBackend(defaultMapImage);
		
		// Initialize the GlobalMap variable with all of the LocalMaps and all of their nodes
		globalMap = new GlobalMap();
		
		ArrayList<LocalMap> tmpListLocal = new ArrayList<LocalMap>(); // temporary list of LocalMaps to be initialized
		for(int i = 0; i < numLocalMaps; i++){		
			backend.loadLocalMap(localMapFilenames[i].getName()); // sets the current LocalMap each filename from the "localmaps" folder
			tmpListLocal.add(backend.getLocalMap());
		}
		globalMap.setLocalMaps(tmpListLocal);
		backend.setLocalMap(tmpListLocal.get(0));
		
		// add the collection of nodes to the ArrayList of GlobalMap
		ArrayList<MapNode> allNodes = new ArrayList<MapNode>();
		for(LocalMap local : tmpListLocal){
			allNodes.addAll(local.getMapNodes()); 
		}
		globalMap.setMapNodes(allNodes);		 
		
		
		
		/**
		 * GUI related code
		 */
		// This will setup the main JFrame to be maximized on start and have a defined content pane
		setTitle("WPI Nav Tool");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		setBounds(0, 0, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		// Image of the default map loaded into backend
		Image map = new ImageIcon("src/images/" + backend.getLocalMap().getMapImage()).getImage();
		
		// SwingBuilder Code related to the JLayeredPane() 
		JLayeredPane layeredPane = new JLayeredPane();
		
		layeredPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JPanel panel_3 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 1109, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(16)
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
						.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 677, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		JLabel lblStepbystepDirections = new JLabel("Step-By-Step Directions");
		panel_3.add(lblStepbystepDirections);
		
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setRows(15);
		textArea_1.setEditable(false);
		scrollPane_1.setViewportView(textArea_1);
		
		//adds the distance label to the map interface
		JLabel lblDistance = new JLabel("");
		panel_1.add(lblDistance);
		
		//Code for button - if it is pressed allow the program to draw the line on the map
		/**TODO
		 * going to need to add functionality to change button title to remove line when user has drawn the line on screen in a better way
		 */
		btnCalculateRoute = new JButton("Calculate Route");
		panel_2.add(btnCalculateRoute);
		btnCalculateRoute.setEnabled(false);
		btnCalculateRoute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(btnCalculateRoute.isEnabled()){
					System.out.println("About to run AStar");
					backend.runAStar();
					System.out.println("Just ran AStar");
					drawLine = true;
					
					//this should only display when the user calculates the astar algorithm
					lblDistance.setText(backend.getDistance());
					
					//basically justs places each string into the array one row at a time - if, and this is a big IF, /n works in this context
					for(String string : backend.displayStepByStep()) {
						textArea_1.append("/n");
						textArea_1.append(string);
					}
					btnCalculateRoute.setText("Remove Route Line");
				} else {
					btnCalculateRoute.setEnabled(true);
					
					//if the line needs to be removed
					//going to need to add a method here - to remove nodes from path
					backend.removePath();
					removeLine = true;
					lblDistance.setText(backend.getDistance());
					textArea_1.setText("");
					
					//change the name of button back to what it originally was
					btnCalculateRoute.setText("Calculate Route");
				}
			}
		});
		
		// Creates a new DrawingPanel object which will display the map image and load up MapNode data
		DrawingPanel panel = new DrawingPanel(backend.getLocalMap().getMapNodes(), map, layeredPane.getSize());
		GroupLayout gl_layeredPane = new GroupLayout(layeredPane);
		gl_layeredPane.setHorizontalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 1105, Short.MAX_VALUE)
		);
		gl_layeredPane.setVerticalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE)
		);
		layeredPane.setLayout(gl_layeredPane);
		contentPane.setLayout(gl_contentPane);
	
		
		pack();
		setVisible(true);
	}
	
	
	/**
	 * A local class to allow drawing on a given panel. This allows us to override the paintComponent() method and
	 * ensure any drawn graphics are displayed without interfering with others. Think of this like a custom canvas.
	 * @author Trevor
	 */
	class DrawingPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		ArrayList<MapNode> localNodes;
		Image mapImage;
		ArrayList<MapNode> startEndNodes;
		
		/**
		 * Constructor
		 * @param nodes The list of nodes already existing on the map. TODO: Make these invisible !
		 * @param map The map image for the current LocalMap
		 */
		public DrawingPanel(ArrayList<MapNode> nodes, Image map, Dimension size) {
	        setBorder(BorderFactory.createLineBorder(Color.black));
	        this.localNodes = nodes;
	        this.mapImage = map;
	        setOpaque(false);
	        startEndNodes = new ArrayList<MapNode>();
	        
	        /**
	         * On mouse click, display the points which represent the start and end nodes. These will also set the backend to
	         * these points on the panel.
	         */
	        addMouseListener(new MouseAdapter() {
	    		@Override
	    		public void mouseClicked(MouseEvent me) {	    				
	    				// Only add new points if you haven't set an End Node yet
	    				if(!setEnd){
		    					Point clickedAt = me.getPoint();
		    					
		    					// If you've set a start node already, set the end node
		    					if(setStart){
		    						setEnd = true;
		    						MapNode newNode = new MapNode((double)clickedAt.x, (double)clickedAt.y, 0);
		    						startEndNodes.add(newNode);
		    						backend.setEndNode(newNode);
		    						btnCalculateRoute.setEnabled(true); // allow user to calculate route
		    					} else {
		    						setStart = true;
		    						MapNode newNode = new MapNode((double)clickedAt.x, (double)clickedAt.y, 0);
		    						startEndNodes.add(newNode);
		    						backend.setStartNode(newNode);
		    					}
	    					
	    				// Prohibit any new points for now
	    				} else {
	    					System.out.println("You've already selected two points");
	    				}
	    				repaint();
	    			}
	    		});
	    }
		
		/**
		 * Paints the map image to the Panel and temporarily prints a visual indication of Node locations
		 * @param g The current graphics object for the main frame
		 */	
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);       
	        Graphics2D graphics = (Graphics2D)g;
	        
	        // Draws the map and places pre-existing node data onto the map as well start and end nodes if they have been set
	        graphics.drawImage(this.mapImage, 0, 0, this);
	        graphics.setColor(Color.BLUE);
	        for(MapNode n : this.localNodes){
	        	graphics.fillOval((int)n.getXPos() - 5, (int)n.getYPos() - 5, 10, 10);
	        }
	        graphics.setColor(Color.RED);
	        for(MapNode n : this.startEndNodes){
	        	graphics.fillOval((int)n.getXPos() - 5, (int)n.getYPos() - 5, 10, 10);
	        }
	        
	        //essentially draws the line on the screen - will need to add a way to remove this line later on
	        //probably need to make a coordinates class - but this currently works
	        if(MainGUI.drawLine = true){
	        	for(int i = 0; i < backend.getCoordinates().size(); i++){
					double x1 = backend.getCoordinates().get(i)[0];
					double y1 = backend.getCoordinates().get(i)[1];
					double x2 = backend.getCoordinates().get(i+1)[0];
					double y2 = backend.getCoordinates().get(i+1)[1];
					g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
					i++;
	        	} 
	        }
	        else if (MainGUI.removeLine == true){
	        	//TODO this really should have a better implementation - but this is a quick fix to an on-going problem
	        	//Would make sense to eventually transform the line into an object, so that it could be easily removed - but that might require adding a .awt canvas, and I'm not entirely sure we want to restructure our entire project
	        	//essentially repaint the line white so that it can't be seen when you remove it
	        	for(int i = 0; i < backend.getCoordinates().size(); i++){
					double x1 = backend.getCoordinates().get(i)[0];
					double y1 = backend.getCoordinates().get(i)[1];
					double x2 = backend.getCoordinates().get(i+1)[0];
					double y2 = backend.getCoordinates().get(i+1)[1];
					g.setColor(Color.white);
					g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
					i++;
	        	}
	        	
	        }
	    }
	}
}

package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JButton;


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
	
	// private ImageIcon nodeImage = new ImageIcon("src\\images\\node.png");
	private JPanel contentPane;
	
	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public MainGUI(int numLocalMaps, File[] localMapFilenames) throws IOException, ClassNotFoundException {
		// Instantiate GUIBackend to its default
		backend = new GUIBackend();
		
		// Initialize the GlobalMap variable with all of the LocalMaps and all of their nodes
		globalMap = new GlobalMap();
		
		LocalMap[] tmpListLocal = new LocalMap[numLocalMaps]; // temporary list of LocalMaps to be initialized
		for(int i = 0; i < numLocalMaps; i++){		
			backend.loadLocalMap(localMapFilenames[i].getName()); // sets the current LocalMap each filename from the "localmaps" folder
			tmpListLocal[i] = backend.getLocalMap();
		}
		globalMap.setLocalMaps(tmpListLocal);
		backend.setLocalMap(tmpListLocal[0]);
		
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
		layeredPane.addMouseListener(new MouseAdapter() {
		@Override
			public void mouseClicked(MouseEvent me) {
				Graphics g = layeredPane.getGraphics();
				
				// Only add new points if you haven't set an End Node yet
				if(!setEnd){
					
					Point p = me.getLocationOnScreen();
					System.out.println("Clicked at: " + p.getX() + "\t" + p.getY());
					
					// If you've set a start node already, set the end node
					if(setStart){
						g.setColor(Color.BLUE);
						g.fillOval((int)p.getX(), (int)p.getY() - 10, 10, 10);
						setEnd = true;
						MapNode newNode = new MapNode((float)p.x, (float)p.y);
						backend.setEndNode(newNode);
					} else {
						g.setColor(Color.RED);
						g.fillOval(p.x, p.y - 10, 10, 10);
						setStart = true;
						MapNode newNode = new MapNode((float)p.x, (float)p.y);
						backend.setStartNode(newNode);
					}
					
				
				// Prohibit any new points for now
				} else {
					System.out.println("You've already selected two points");
				}
				
			}
		});
		layeredPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 1109, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(40)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(24, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(15)
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
							.addGap(562)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
						.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 677, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		//adds the distance label to the map interface
		JLabel lblDistance = new JLabel("");
		panel_1.add(lblDistance);
		
		//Code for button - if it is pressed allow the program to draw the line on the map
		/**TODO
		 * going to need to add functionality to change button title to remove line when user has drawn the line on screen
		 */
		JButton btnCalculateRoute = new JButton("Calculate Route");
		panel_2.add(btnCalculateRoute);
		btnCalculateRoute.setEnabled(false);
		
		//need to remember to make sure the user can actually press the button
		if (!(backend.getStartNode().equals(null)) && !(backend.getEndNode().equals(null))){
			btnCalculateRoute.setEnabled(true);
			if (btnCalculateRoute.getText() == "Calculate Route"){
				btnCalculateRoute.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						backend.runAStar();
						drawLine = true;
						//this should only display when the user calculates the astar algorithm
						lblDistance.setText(backend.getDistance());
						btnCalculateRoute.setText("Remove Route Line");
					}
				});
			/**TODO
			 * add Remove Link functionality
			 */
			} else { //if the button text is "Remove Route Line"
				btnCalculateRoute.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						//code for this...
						
						//change the name back
						btnCalculateRoute.setText("Calculate Route");
					}
				});
			}
		}
		
		// Creates a new DrawingPanel object which will display the map image 
		DrawingPanel panel = new DrawingPanel(backend.getLocalMap().getMapNodes(), map);
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
		
		/**
		 * Constructor
		 * @param nodes The list of nodes already existing on the map. TODO: Make these invisible !
		 * @param map The map image for the current LocalMap
		 */
		public DrawingPanel(ArrayList<MapNode> nodes, Image map) {
	        setBorder(BorderFactory.createLineBorder(Color.black));
	        this.localNodes = nodes;
	        this.mapImage = map;
	        setOpaque(false);
	    }
		
		/**
		 * Paints the map image to the Panel and temporarily prints a visual indication of Node locations
		 * @param g The current graphics object for the main frame
		 */	
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);       
	        
	        g.drawImage(this.mapImage, 0, 0, getWidth(), getHeight(), this);
	        for(MapNode n : this.localNodes){
	        	g.fillOval((int)n.getX(), (int)n.getY(), 10, 10);
	        }
	        
	        //essentially draws the line on the screen - will need to add a way to remove this line later on
	        if(MainGUI.drawLine = true){
	        	for(int i = 0; i < backend.getCoordinates().size(); i++){
					float x1 = backend.getCoordinates().get(i)[0];
					float y1 = backend.getCoordinates().get(i)[1];
					float x2 = backend.getCoordinates().get(i+1)[0];
					float y2 = backend.getCoordinates().get(i+1)[1];
					g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
					i++;
	        	}
	        }
	    }
	}
}

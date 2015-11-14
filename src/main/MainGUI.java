package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;


/**
 * This class contains code for the main applications GUI interface as well as implementation for its various
 * functionality such as drawing the route.
 * @author Trevor
 */
public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L; // Serial Version ID
	private static GUIBackend backend;
	private static GlobalMap globalMap;
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
			System.out.println(localMapFilenames[i].getName());
			backend.loadLocalMap(localMapFilenames[i].getName()); // sets the current LocalMap each filename from the "localmaps" folder
			tmpListLocal[i] = backend.getLocalMap();
		}
		globalMap.setLocalMaps(tmpListLocal);
		backend.setLocalMap(tmpListLocal[0]);
		
		System.out.println(backend.getLocalMap().getMapImage());
		
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
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 1109, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 677, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
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
	    }  
	    
	}
}

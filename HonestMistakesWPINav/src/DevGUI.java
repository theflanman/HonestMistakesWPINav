import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
public class DevGUI extends JFrame {
	static HashMap<String, ArrayList<MapNode>> localMap = new HashMap<String, ArrayList<MapNode>>(); // path to file, Integer data
	static ArrayList<MapNode> points = new ArrayList<MapNode>(); // currently loaded list of points
	String path; // current path
	private ImageIcon nodeImage = new ImageIcon("bluedot.png");
	private int nodeCounter = 0;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField xPosField;
	private JTextField yPosField;
	private JTextField zPosField;
	private JTextField nodeNameField;
	private MapNode lastClicked;
	private MapNode edgeStart;
	private boolean edgeStarted = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// initialize hashmap with map data
		// <<< pretend reading from text file >>
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					DevGUI frame = new DevGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the frame.
	 */
	public DevGUI() {
		int threshold = 10; //threshold is a radius for selecting nodes on the map - they are very tiny otherwise and hard to click precisely
		int circleSize = 12; //Circle size determines size of nodes on map. Should probably be an even number as it's divided by 2 and needs to be an int and it'd be nice if it were exactly half.
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setPreferredSize(new Dimension(1380, 760));
		setResizable(true);
		setTitle("Map Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1380, 760);

		
		JRadioButton rdbtnSelectNode = new JRadioButton("Select Node");
		JRadioButton rdbtnPlaceNode = new JRadioButton("Place Node");
		JRadioButton rdbtnMakeEdge = new JRadioButton("Make Edge");
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon("testmap.png"));
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if(rdbtnPlaceNode.isSelected()){
					Point p = me.getLocationOnScreen();
					MapNode n = new MapNode(p.x, p.y, nodeCounter); // make a new mapnode with those points
					nodeCounter++;
					Graphics g = getGraphics();
					points.add(n);
					//nodeImage.paintIcon(lblNewLabel, g, (int)n.getxPos()+ 5, (int)n.getyPos() + 5);
					g.setColor(Color.blue);
					g.fillOval((int) n.getxPos() + 4, (int) n.getyPos() + 4, circleSize, circleSize);
					xPosField.setText(""+n.getxPos());
					yPosField.setText(""+n.getyPos());
					zPosField.setText(""+n.getzPos());
					nodeNameField.setText(n.getnodeName());
					lastClicked = n;
				} else if (rdbtnSelectNode.isSelected()){
					for(MapNode n : points){
						Point tmp = new Point((int)n.getxPos(), (int)n.getyPos());

						if((Math.abs(me.getLocationOnScreen().getX() - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - tmp.getY()) <= threshold )){
							//txtrXpos.setText("x_pos: " + n.getxPos() + "\ny_pos: " + n.getyPos() + "\nNode ID: " + n.getID());
							// currentNode is now n with nodeID
							xPosField.setText(""+n.getxPos());
							yPosField.setText(""+n.getyPos());
							zPosField.setText(""+n.getzPos());
							nodeNameField.setText(n.getnodeName());
							lastClicked = n;
						}
					}
				}
				else if(rdbtnMakeEdge.isSelected()) {
					if(edgeStarted == false) {
						for(MapNode n : points){
							Point tmp = new Point((int)n.getxPos(), (int)n.getyPos());

							if((Math.abs(me.getLocationOnScreen().getX() - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - tmp.getY()) <= threshold )){
								//txtrXpos.setText("x_pos: " + n.getxPos() + "\ny_pos: " + n.getyPos() + "\nNode ID: " + n.getID());
								// currentNode is now n with nodeID
								edgeStart = n;
								edgeStarted = true;
							}
						}
						
					}
					else {
						for(MapNode n : points) {
							Point tmp = new Point((int)n.getxPos(), (int)n.getyPos());

							if((Math.abs(me.getLocationOnScreen().getX() - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - tmp.getY()) <= threshold )){
								//txtrXpos.setText("x_pos: " + n.getxPos() + "\ny_pos: " + n.getyPos() + "\nNode ID: " + n.getID());
								// currentNode is now n with nodeID
							//	Line2D.Double lin = new Line2D.Double(edgeStart.getX(), edgeStart.getY(), tmp.getX(), tmp.getY());
								Graphics g = getGraphics();
								g.setColor(Color.blue);
								g.drawLine((int) edgeStart.getxPos() + 10, (int) edgeStart.getyPos() + 10, (int) tmp.getX()+ 10, (int) tmp.getY() + 10);
								edgeStarted = false;
								n.addNeighbor(edgeStart);
								edgeStart.addNeighbor(n);
							}
						}
					}
				}
			}
		});
		lblNewLabel.setBackground(Color.WHITE);
		
		JMenuItem mntmLoadMap = new JMenuItem("Load Map");
		mntmLoadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				points.add(new MapNode(100, 100, 0));
				nodeCounter++;
				points.add(new MapNode(200, 200, 1));
				nodeCounter++;
				points.add(new MapNode(150, 100, 2));
				nodeCounter++;
				
				localMap.put("firstValue", points);
				
				Graphics g = getGraphics();
				for(MapNode n : points){
					nodeImage.paintIcon(lblNewLabel, g, (int)n.getxPos(), (int)n.getyPos());
					
				}
			}
		});
		
		JMenuItem mntmSaveMap = new JMenuItem("Save Map");
		mnFile.add(mntmSaveMap);
		mnFile.add(mntmLoadMap);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JMenuItem mntmNewMapImage = new JMenuItem("New Map Image");
		mnFile.add(mntmNewMapImage);
		mnFile.add(mntmExit);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(1000, 800));
		panel.setSize(new Dimension(1000, 700));
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(Color.WHITE);
		
		JPanel cursorPanel = new JPanel();
		cursorPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JPanel nodeInfoPanel = new JPanel();
		nodeInfoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 1192, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(nodeInfoPanel, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE)
						.addComponent(cursorPanel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
					.addGap(93))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 653, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(cursorPanel, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
							.addGap(35)
							.addComponent(nodeInfoPanel, GroupLayout.PREFERRED_SIZE, 407, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(311, Short.MAX_VALUE))
		);
		GridBagLayout gbl_nodeInfoPanel = new GridBagLayout();
		gbl_nodeInfoPanel.columnWidths = new int[]{95, 99, 0};
		gbl_nodeInfoPanel.rowHeights = new int[]{16, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_nodeInfoPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_nodeInfoPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		nodeInfoPanel.setLayout(gbl_nodeInfoPanel);
		
		JLabel lblNodeInformation = new JLabel("Node Information");
		GridBagConstraints gbc_lblNodeInformation = new GridBagConstraints();
		gbc_lblNodeInformation.insets = new Insets(0, 0, 5, 5);
		gbc_lblNodeInformation.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNodeInformation.gridx = 0;
		gbc_lblNodeInformation.gridy = 0;
		nodeInfoPanel.add(lblNodeInformation, gbc_lblNodeInformation);
		
		JLabel lblXposition = new JLabel("x-position");
		GridBagConstraints gbc_lblXposition = new GridBagConstraints();
		gbc_lblXposition.anchor = GridBagConstraints.EAST;
		gbc_lblXposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblXposition.gridx = 0;
		gbc_lblXposition.gridy = 1;
		nodeInfoPanel.add(lblXposition, gbc_lblXposition);
		
		xPosField = new JTextField();
		GridBagConstraints gbc_xPosField = new GridBagConstraints();
		gbc_xPosField.insets = new Insets(0, 0, 5, 0);
		gbc_xPosField.fill = GridBagConstraints.HORIZONTAL;
		gbc_xPosField.gridx = 1;
		gbc_xPosField.gridy = 1;
		nodeInfoPanel.add(xPosField, gbc_xPosField);
		xPosField.setColumns(10);
		
		JLabel lblYposition = new JLabel("y-position");
		GridBagConstraints gbc_lblYposition = new GridBagConstraints();
		gbc_lblYposition.anchor = GridBagConstraints.EAST;
		gbc_lblYposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblYposition.gridx = 0;
		gbc_lblYposition.gridy = 2;
		nodeInfoPanel.add(lblYposition, gbc_lblYposition);
		
		yPosField = new JTextField();
		GridBagConstraints gbc_yPosField = new GridBagConstraints();
		gbc_yPosField.insets = new Insets(0, 0, 5, 0);
		gbc_yPosField.fill = GridBagConstraints.HORIZONTAL;
		gbc_yPosField.gridx = 1;
		gbc_yPosField.gridy = 2;
		nodeInfoPanel.add(yPosField, gbc_yPosField);
		yPosField.setColumns(10);
		
		JLabel lblZposition = new JLabel("z-position");
		GridBagConstraints gbc_lblZposition = new GridBagConstraints();
		gbc_lblZposition.anchor = GridBagConstraints.EAST;
		gbc_lblZposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblZposition.gridx = 0;
		gbc_lblZposition.gridy = 3;
		nodeInfoPanel.add(lblZposition, gbc_lblZposition);
		
		zPosField = new JTextField();
		GridBagConstraints gbc_zPosField = new GridBagConstraints();
		gbc_zPosField.insets = new Insets(0, 0, 5, 0);
		gbc_zPosField.fill = GridBagConstraints.HORIZONTAL;
		gbc_zPosField.gridx = 1;
		gbc_zPosField.gridy = 3;
		nodeInfoPanel.add(zPosField, gbc_zPosField);
		zPosField.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 4;
		nodeInfoPanel.add(lblName, gbc_lblName);
		
		nodeNameField = new JTextField();
		GridBagConstraints gbc_nodeNameField = new GridBagConstraints();
		gbc_nodeNameField.insets = new Insets(0, 0, 5, 0);
		gbc_nodeNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nodeNameField.gridx = 1;
		gbc_nodeNameField.gridy = 4;
		nodeInfoPanel.add(nodeNameField, gbc_nodeNameField);
		nodeNameField.setColumns(10);
		
		JButton btnMakeChanges = new JButton("Make Changes");
		btnMakeChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GridBagConstraints gbc_btnMakeChanges = new GridBagConstraints();
		gbc_btnMakeChanges.insets = new Insets(0, 0, 5, 5);
		gbc_btnMakeChanges.gridx = 0;
		gbc_btnMakeChanges.gridy = 5;
		nodeInfoPanel.add(btnMakeChanges, gbc_btnMakeChanges);
		
        btnMakeChanges.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	if(lastClicked != null) {
            		lastClicked.setnodeName(nodeNameField.getText());
            		lastClicked.setxPos((float) Double.parseDouble(xPosField.getText())); //TODO : Setting x pos doesn't work yet.
            	}
            }
            
        });
            
		
		JLabel lblNoneditableInformation = new JLabel("Non-editable information");
		GridBagConstraints gbc_lblNoneditableInformation = new GridBagConstraints();
		gbc_lblNoneditableInformation.insets = new Insets(0, 0, 5, 5);
		gbc_lblNoneditableInformation.gridx = 0;
		gbc_lblNoneditableInformation.gridy = 7;
		nodeInfoPanel.add(lblNoneditableInformation, gbc_lblNoneditableInformation);
		
		JLabel lblNeighbors = new JLabel("Neighbors");
		GridBagConstraints gbc_lblNeighbors = new GridBagConstraints();
		gbc_lblNeighbors.insets = new Insets(0, 0, 0, 5);
		gbc_lblNeighbors.gridx = 0;
		gbc_lblNeighbors.gridy = 8;
		nodeInfoPanel.add(lblNeighbors, gbc_lblNeighbors);
		
		JLabel lblNewLabel_1 = new JLabel("Cursor Options");
		cursorPanel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		

		cursorPanel.add(rdbtnSelectNode);
		buttonGroup.add(rdbtnSelectNode);
		
		

		cursorPanel.add(rdbtnPlaceNode);
		buttonGroup.add(rdbtnPlaceNode);
		rdbtnPlaceNode.setSelected(true);
		

		cursorPanel.add(rdbtnMakeEdge);
		buttonGroup.add(rdbtnMakeEdge);
		
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(lblNewLabel);
		getContentPane().setLayout(groupLayout);
	}
}
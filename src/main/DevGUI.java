package main;
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
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class DevGUI extends JFrame {
	static HashMap<String, ArrayList<MapNode>> localMap = new HashMap<String, ArrayList<MapNode>>(); // path to file, Integer data
	static ArrayList<MapNode> points = new ArrayList<MapNode>(); // currently loaded list of points
	String path; // current path
	private int nodeCounter = 0;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField xPosField;
	private JTextField yPosField;
	private JTextField zPosField;
	private JTextField nodeNameField;
	private MapNode lastClicked;
	private MapNode edgeStart;
	private MapNode edgeRemove;
	private MapNode nodeToRemove;
	private boolean edgeStarted = false;
	private boolean edgeRemovalStarted = false;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

	
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

		setExtendedState(Frame.MAXIMIZED_BOTH);
		setPreferredSize(new Dimension(1380, 760));
		setResizable(true);
		setTitle("Map Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1380, 760);


		JRadioButton rdbtnSelectNode = new JRadioButton("Select Node");
		JRadioButton rdbtnRemoveEdge = new JRadioButton("Remove Edge");
		JRadioButton rdbtnRemoveNode = new JRadioButton("Remove Node");
		JRadioButton rdbtnPlaceNode = new JRadioButton("Place Node");
		JRadioButton rdbtnMakeEdge = new JRadioButton("Make Edge");
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		MapPanel mapPanel = new MapPanel();
		int threshold = 30; //threshold is a radius for selecting nodes on the map - they are very tiny otherwise and hard to click precisely

		mapPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point offset = mapPanel.getLocationOnScreen();
				if(rdbtnPlaceNode.isSelected()){
					edgeStarted = false; //These two calls are a basic attempt to stop edge addition and removal from becoming confusing.
					edgeRemovalStarted = false; //It is not evident whether the user has clicked a first node yet in the edge, so changing to a different operation will reset it.
					Point p = me.getLocationOnScreen();
					MapNode n = new MapNode((double) p.x - offset.x, (double) p.y - offset.y, (double) 0, nodeCounter); // make a new mapnode with those points
					nodeCounter++;
					Graphics g = mapPanel.getGraphics();
					points.add(n);
					xPosField.setText(""+n.getXPos());
					yPosField.setText(""+n.getYPos());
					zPosField.setText(""+n.getZPos());
					nodeNameField.setText(n.getnodeName());
					lastClicked = n;
					mapPanel.renderMapPublic(g, points);
				} else if (rdbtnSelectNode.isSelected()){
					edgeStarted = false;
					edgeRemovalStarted = false;
					for(MapNode n : points){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
							xPosField.setText(""+n.getXPos());
							yPosField.setText(""+n.getYPos());
							zPosField.setText(""+n.getZPos());
							nodeNameField.setText(n.getnodeName());
							lastClicked = n;
						}
					}
				}
				else if(rdbtnMakeEdge.isSelected()) {
					edgeRemovalStarted = false;
					if(edgeStarted == false) {
						for(MapNode n : points){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeStart = n;
								edgeStarted = true;
							}
						}

					}
					else {
						for(MapNode n : points) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								edgeStarted = false;
								n.addNeighbor(edgeStart);
								edgeStart.addNeighbor(n);
								mapPanel.renderMapPublic(g, points);
							}
						}
					}
				}
				else if(rdbtnRemoveEdge.isSelected()) {
					edgeStarted = false;
					if(edgeRemovalStarted == false) {
						for(MapNode n : points){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeRemove = n;
								edgeRemovalStarted = true;
							}
						}

					}
					else {
						for(MapNode n : points) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel.getGraphics();
								edgeRemovalStarted = false;
								n.removeNeighbor(edgeRemove);
								edgeRemove.removeNeighbor(n);
								mapPanel.renderMapPublic(g, points);
							}
						}
					}

				}
				else if(rdbtnRemoveNode.isSelected()) {
					edgeStarted = false;
					edgeRemovalStarted = false;
					for(MapNode n : points){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
							for(MapNode m : n.getNeighbors()) {
							//	n.removeNeighbor(m);
								m.removeNeighbor(n);
							}
							nodeToRemove = n;
							

						}
					}
					nodeToRemove.getNeighbors().removeIf((MapNode q)->q.getXPos() > -1000000000); //Intent is to remove all neighbors. Foreach loop doesn't like this.
					
					points.remove(nodeToRemove);
					Graphics g = mapPanel.getGraphics();
					mapPanel.renderMapPublic(g, points);
				}
			}
		}); 
		mapPanel.setBackground(Color.WHITE);




		JMenuItem mntmLoadMap = new JMenuItem("Load Map");



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
		mntmNewMapImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int option = chooser.showOpenDialog(DevGUI.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();

					try{
						Image pic = ImageIO.read(file);
						//  picLabel.setIcon(new ImageIcon(pic));
						mapPanel.setBgImage(pic);

						Graphics g = mapPanel.getGraphics();
						mapPanel.renderMapPublic(g, points);
					}
					catch(IOException ex){
						ex.printStackTrace();
						System.exit(1);
					}
				}
			}
		});
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
								.addComponent(cursorPanel, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
						.addGap(93))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 653, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(cursorPanel, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(nodeInfoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(311))
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
					lastClicked.setxPos( Double.parseDouble(xPosField.getText())); //TODO : Setting x pos doesn't work yet.
				}
			}

		});
		cursorPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel_1 = new JLabel(" Cursor Options");
		cursorPanel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		cursorPanel.add(rdbtnPlaceNode);
		buttonGroup.add(rdbtnPlaceNode);
		rdbtnPlaceNode.setSelected(true);

		cursorPanel.add(rdbtnRemoveNode);
		buttonGroup.add(rdbtnRemoveNode);


		// Set up radio button group to determine cursor's function
		cursorPanel.add(rdbtnSelectNode);
		buttonGroup.add(rdbtnSelectNode);

		cursorPanel.add(rdbtnMakeEdge);
		buttonGroup.add(rdbtnMakeEdge);
		cursorPanel.add(rdbtnRemoveEdge);
		buttonGroup.add(rdbtnRemoveEdge);

		panel.setLayout(new BorderLayout(0, 0));
		panel.add(mapPanel);
		getContentPane().setLayout(groupLayout);
	}


}
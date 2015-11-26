package main.gui;
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

import main.LocalMap;
import main.MapNode;
import main.util.Constants;
import main.util.MapPanel;
import main.util.SaveUtil;

// TODO address creation of an edge from one frame to the other...
// TODO handle clicks on "Save Maps" where one or both panels don't have a map loaded.
// TODO start app with campusmap loaded

public class DevGUIFront extends JFrame {
	static HashMap<String, ArrayList<MapNode>> localMap = new HashMap<String, ArrayList<MapNode>>(); // path to file, Integer data
	static ArrayList<MapNode> points = new ArrayList<MapNode>(); // currently loaded list of points
	static ArrayList<MapNode> points2 = new ArrayList<MapNode>(); // Points for the second map
	static File inputFile;
	static File inputFile2;
	static Image pic;
	static Image pic2;
	static LocalMap local1;
	static LocalMap local2;
	String path; // current path
	private int nodeCounter = 0;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField xPosField;
	private JTextField yPosField;
	private JTextField zPosField;
	private MapNode lastClicked;
	private MapNode edgeStart;
	private MapNode edgeRemove;
	private MapNode nodeToRemove;
	private boolean edgeStarted = false;
	private boolean edgeRemovalStarted = false;
	private boolean twoMapView = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					DevGUIFront frame = new DevGUIFront();
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
	public DevGUIFront() {

	//	setExtendedState(Frame.MAXIMIZED_BOTH); //This has the application automatically open maximized.
		
		// This sets the size and behavior of the application window itself.
		setPreferredSize(new Dimension(1380, 760));
		setResizable(true);
		setTitle("Map Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1300, 760);

		// This defines the radio buttons that can be selected for the cursor options, because they are used in the following section of code.
		JRadioButton rdbtnSelectNode = new JRadioButton("Select Node");
		JRadioButton rdbtnRemoveEdge = new JRadioButton("Remove Edge");
		JRadioButton rdbtnRemoveNode = new JRadioButton("Remove Node");
		JRadioButton rdbtnPlaceNode = new JRadioButton("Place Node");
		JRadioButton rdbtnMakeEdge = new JRadioButton("Make Edge");
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);



		MapPanel mapPanel = new MapPanel();
		MapPanel mapPanel2 = new MapPanel();

		int threshold = 10; //threshold is a radius for selecting nodes on the map - they are very tiny otherwise and hard to click precisely

		
		// This is code that determines what needs to happen on each mouse click in the map panel.
		
		mapPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point offset = mapPanel.getLocationOnScreen();
				if(rdbtnPlaceNode.isSelected()){
					edgeStarted = false; //These two calls are a basic attempt to stop edge addition and removal from becoming confusing.
					edgeRemovalStarted = false; //It is not evident whether the user has clicked a first node yet in the edge, so changing to a different operation will reset it.
					Point p = me.getLocationOnScreen();
					MapNode n = new MapNode((double) p.x - offset.x, (double) p.y - offset.y, local1); // make a new mapnode with those points
					nodeCounter++;
					Graphics g = mapPanel.getGraphics();
					points.add(n);
					xPosField.setText(""+n.getXPos());
					yPosField.setText(""+n.getYPos());
			//			zPosField.setText(""+n.getZPos());
					//nodeNameField.setText(n.getnodeName());
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
							//zPosField.setText(""+n.getZPos());
							//nodeNameField.setText(n.getnodeName());
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
		
		
		// This is code for the second map panel.
		
		mapPanel2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
//				xPosField.setText("I'm HERE");
				Point offset = mapPanel2.getLocationOnScreen();
				if(rdbtnPlaceNode.isSelected()){
					edgeStarted = false; //These two calls are a basic attempt to stop edge addition and removal from becoming confusing.
					edgeRemovalStarted = false; //It is not evident whether the user has clicked a first node yet in the edge, so changing to a different operation will reset it.
					Point p = me.getLocationOnScreen();
					MapNode n = new MapNode((double) p.x - offset.x, (double) p.y - offset.y, local2); // make a new mapnode with those points
					nodeCounter++;
					Graphics g = mapPanel2.getGraphics();
					points2.add(n);
					xPosField.setText(""+n.getXPos());
					yPosField.setText(""+n.getYPos());
			//		zPosField.setText(""+n.getZPos());
					//nodeNameField.setText(n.getnodeName());
					lastClicked = n;
					mapPanel2.renderMapPublic(g, points2);
				} else if (rdbtnSelectNode.isSelected()){
					edgeStarted = false;
					edgeRemovalStarted = false;
					for(MapNode n : points2){
						Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());

						if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
							xPosField.setText(""+n.getXPos());
							yPosField.setText(""+n.getYPos());
				//			zPosField.setText(""+n.getZPos());
							//nodeNameField.setText(n.getnodeName());
							lastClicked = n;
						}
					}
				}
				else if(rdbtnMakeEdge.isSelected()) {
					edgeRemovalStarted = false;
					if(edgeStarted == false) {
						for(MapNode n : points2){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeStart = n;
								edgeStarted = true;
							}
						}

					}
					else {
						for(MapNode n : points2) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel2.getGraphics();
								edgeStarted = false;
								n.addNeighbor(edgeStart);
								edgeStart.addNeighbor(n);
								mapPanel2.renderMapPublic(g, points2);
							}
						}
					}
				}
				else if(rdbtnRemoveEdge.isSelected()) {
					edgeStarted = false;
					if(edgeRemovalStarted == false) {
						for(MapNode n : points2){
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() -offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								edgeRemove = n;
								edgeRemovalStarted = true;
							}
						}

					}
					else {
						for(MapNode n : points2) {
							Point tmp = new Point((int)n.getXPos(), (int)n.getYPos());
							if((Math.abs(me.getLocationOnScreen().getX() - offset.x - tmp.getX()) <= threshold) && (Math.abs(me.getLocationOnScreen().getY() - offset.y - tmp.getY()) <= threshold )){
								Graphics g = mapPanel2.getGraphics();
								edgeRemovalStarted = false;
								n.removeNeighbor(edgeRemove);
								edgeRemove.removeNeighbor(n);
								mapPanel2.renderMapPublic(g, points2);
							}
						}
					}

				}
				else if(rdbtnRemoveNode.isSelected()) {
					edgeStarted = false;
					edgeRemovalStarted = false;
					for(MapNode n : points2){
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
					
					points2.remove(nodeToRemove);
					Graphics g = mapPanel2.getGraphics();
					mapPanel2.renderMapPublic(g, points2);
				}
			}
		}); 
		mapPanel2.setBackground(Color.WHITE);
		



		// This is code for the file menu with the save map, load map, load new image, dropdown menu.
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadMap = new JMenuItem("Load Map");
		mnFile.add(mntmLoadMap);
		mntmLoadMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(Constants.LOCAL_MAP_PATH));
				int option = chooser.showOpenDialog(DevGUIFront.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					inputFile = chooser.getSelectedFile();
					String inputFileName = inputFile.getName();
					
					// load localMap
					DevGUIBack devGUIBack = new DevGUIBack(null);
					devGUIBack.loadMap(Constants.LOCAL_MAP_PATH + "/" + inputFileName);
					local1 = devGUIBack.getLocalMap();
					String imagePath = SaveUtil.removeExtension(inputFileName);
					imagePath = imagePath + ".jpg";
										
					// set the image
					try {
						pic = ImageIO.read(new File(Constants.IMAGES_PATH + "/" + imagePath));
					} catch (IOException e1) {
						e1.printStackTrace();}
					
					//  picLabel.setIcon(new ImageIcon(pic));
					mapPanel.setBgImage(pic);

					// set the points
					Graphics g = mapPanel.getGraphics();
					points = local1.getMapNodes();
					mapPanel.renderMapPublic(g, points);
				}
			}
		});
		
		JMenuItem mntmLoadExtraMap = new JMenuItem("Load Extra Map");
		mntmLoadExtraMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(Constants.LOCAL_MAP_PATH));
				int option = chooser.showOpenDialog(DevGUIFront.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					inputFile2 = chooser.getSelectedFile();
					String inputFileName = inputFile2.getName();
					
					// load localMap
					DevGUIBack devGUIBack = new DevGUIBack(null);
					devGUIBack.loadMap(Constants.LOCAL_MAP_PATH + "/" + inputFileName); 
					local2 = devGUIBack.getLocalMap();
					String imagePath = SaveUtil.removeExtension(inputFileName);
					imagePath = imagePath + ".jpg";
										
					// set the image
					try {
						pic2 = ImageIO.read(new File(Constants.IMAGES_PATH + "/" + imagePath));
					} catch (IOException e1) {
						e1.printStackTrace();}
					
					//  picLabel.setIcon(new ImageIcon(pic));
					mapPanel2.setBgImage(pic2);

					// set the points
					Graphics g = mapPanel2.getGraphics();
					points2 = local2.getMapNodes();
					mapPanel2.renderMapPublic(g, points2);
				}
			}
		});

		JMenuItem mntmSaveMap = new JMenuItem("Save Map");
		mnFile.add(mntmSaveMap);
		mntmSaveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fileName = inputFile.getName();
				fileName = SaveUtil.removeExtension(fileName);
				fileName = fileName + ".jpg";
				LocalMap thisMap = new LocalMap(fileName, points);
				DevGUIBack devGUIBack = new DevGUIBack(thisMap);
				devGUIBack.saveMap();
				
				if (twoMapView) {
					String fileName2 = inputFile2.getName();
					fileName2 = SaveUtil.removeExtension(fileName2);
					fileName2 = fileName2 + ".jpg";
					LocalMap thisMap2 = new LocalMap(fileName2, points2);
					DevGUIBack devGUIBack2 = new DevGUIBack(thisMap2);
					devGUIBack2.saveMap();
				}
			}
		});

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
				chooser.setCurrentDirectory(new File(Constants.IMAGES_PATH));
				int option = chooser.showOpenDialog(DevGUIFront.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					inputFile = chooser.getSelectedFile();

					try{
						pic = ImageIO.read(inputFile);
						//  picLabel.setIcon(new ImageIcon(pic));
						String imagePath = SaveUtil.removeExtension(inputFile.getName());
						imagePath = imagePath + ".jpg";
						
						mapPanel.setBgImage(pic);
						points = new ArrayList<MapNode>();
						Graphics g = mapPanel.getGraphics();
						mapPanel.renderMapPublic(g, points);
						local1 = new LocalMap(imagePath, points);

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
		
		// This is the View menu, allowing a developer to switch to showing one or two maps at a time.
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenuItem mntmShowTwoMaps = new JMenuItem("Show Two Maps");
		mnView.add(mntmShowTwoMaps);
		
		JMenuItem mntmShowOneMap = new JMenuItem("Show One Map");
		mnView.add(mntmShowOneMap);

		// This is code for the panel showing information on the current selected node.
		
		JPanel nodeInfoPanel = new JPanel();
		nodeInfoPanel.setBounds(920, 214, 294, 144);
		nodeInfoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nodeInfoPanel.setLayout(null);
		
		// Labels
		JLabel lblNodeInformation = new JLabel("Node Information");
		lblNodeInformation.setBounds(2, 2, 99, 16);
		nodeInfoPanel.add(lblNodeInformation);

		JLabel lblXposition = new JLabel("x-position");
		lblXposition.setBounds(2, 26, 67, 16);
		nodeInfoPanel.add(lblXposition);

		JLabel lblYposition = new JLabel("y-position");
		lblYposition.setBounds(2, 53, 55, 16);
		nodeInfoPanel.add(lblYposition);

		JLabel lblZposition = new JLabel("z-position");
		lblZposition.setBounds(2, 80, 55, 16);
		nodeInfoPanel.add(lblZposition);
		
		// Text fields
		xPosField = new JTextField();
		xPosField.setBounds(122, 23, 165, 22);
		nodeInfoPanel.add(xPosField);
		xPosField.setColumns(10);

		yPosField = new JTextField();
		yPosField.setBounds(122, 50, 165, 22);
		nodeInfoPanel.add(yPosField);
		yPosField.setColumns(10);

		zPosField = new JTextField();
		zPosField.setBounds(122, 77, 165, 22);
		nodeInfoPanel.add(zPosField);
		zPosField.setColumns(10);

		JButton btnMakeChanges = new JButton("Make Changes");
		btnMakeChanges.setBounds(4, 106, 115, 25);
		nodeInfoPanel.add(btnMakeChanges);
		
		btnMakeChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lastClicked.setXPos(Double.parseDouble(xPosField.getText()));
				lastClicked.setYPos(Double.parseDouble(yPosField.getText()));
		//		lastClicked.setZPos(Double.parseDouble(zPosField.getText()));
				Graphics g = mapPanel.getGraphics();
				mapPanel.renderMapPublic(g, points);
				if (twoMapView) {
					Graphics g2 = mapPanel2.getGraphics();
					mapPanel2.renderMapPublic(g2, points2);
				}
			}
		});
		
		// This is code for the panel with the radio buttons for the cursor options
		
		JPanel cursorPanel = new JPanel();
		cursorPanel.setBounds(920, 10, 130, 183);
		cursorPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		cursorPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel_1 = new JLabel(" Cursor Options");
		cursorPanel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		cursorPanel.add(rdbtnPlaceNode);
		buttonGroup.add(rdbtnPlaceNode);
		rdbtnPlaceNode.setSelected(true);

		cursorPanel.add(rdbtnRemoveNode);
		buttonGroup.add(rdbtnRemoveNode);

		cursorPanel.add(rdbtnSelectNode);
		buttonGroup.add(rdbtnSelectNode);

		cursorPanel.add(rdbtnMakeEdge);
		buttonGroup.add(rdbtnMakeEdge);
		cursorPanel.add(rdbtnRemoveEdge);
		buttonGroup.add(rdbtnRemoveEdge);
		getContentPane().setLayout(null);


		// This is code pertaining to the panel holding the map panel.
		
		JPanel panel = new JPanel();
		panel.setLocation(10, 13);
	//	panel.setPreferredSize(new Dimension(1000, 800));
	//	panel.setSize(new Dimension(1192, 653));
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(Color.WHITE);
		
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBounds(10,10,900, 600);
		panel.add(mapPanel);
		getContentPane().add(panel);
		getContentPane().add(cursorPanel);
		getContentPane().add(nodeInfoPanel);
		
		JPanel panel2 = new JPanel();
		panel2.setLocation(465, 10);
	//	
	//	panel2.setLocation(930, 500);
		panel2.setSize(445, 600);
		panel2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel2.setBackground(Color.WHITE);
		panel2.setLayout(new BorderLayout(0, 0));
		panel2.setBounds(465, 10, 445, 600);
		mapPanel2.setBounds(2, 2, 443, 598); //TODO Figure out why this line of code is needed when it isn't needed for mapPanel!
		panel2.add(mapPanel2);
	//	getContentPane().add(panel2);
		
	//	panel.setSize(new Dimension(445, 600));
	//	getContentPane().add(panel2);
		
		mntmShowTwoMaps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				panel.setSize(new Dimension(445, 600));
				getContentPane().add(panel2);
				panel.setBackground(Color.WHITE);
				mnFile.add(mntmLoadExtraMap);
				twoMapView = true;
				mntmSaveMap.setText("Save Maps");
			}
		});
		
		mntmShowOneMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			getContentPane().remove(panel2);
				panel.setSize(new Dimension(900, 600));
				mnFile.remove(mntmLoadExtraMap);
				twoMapView = false;
				mntmSaveMap.setText("Save Map");
			}
		});
	}
}
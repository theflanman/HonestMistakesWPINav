package main.gui.frontutil;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import main.LocalMap;
import main.MapNode;
import main.gui.GUIFront;
import main.util.Constants;
import main.util.GUIFrontUtil;
import main.util.proxy.IProxyImage;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

@SuppressWarnings("serial")
public class TweenPanel extends JPanel {
	ArrayList<MapNode> localNodes;
	public ArrayList<MapNode> chosenNodes;

	private final TweenManager tweenManager = SLAnimator.createTweenManager();
	private JLabel labelMainPanel = new JLabel();
	private JLabel labelStep = new JLabel();
	private IProxyImage mapImage;
	private Runnable action;
	private boolean actionEnabled = true;
	private boolean hover = false;
	private int borderThickness = 2;
	private String panelID;
	
	private double panX;
	private double panY;
	double zoomRatio;

	String packageName;
	boolean isMapView;

	ArrayList<Polygon> buildingPolygons = new ArrayList<Polygon>();
	Polygon pCPolygon, aKPolygon, bPolygon, cCPolygon, fLPolygon, gLPolygon, hHPolygon, hHGPolygon, sHPolygon;

	/**
	 * Class for a custom panel to do drawing and tweening. This can be seperated into a seperate class file
	 * but it functions better as a private class
	 */
	public TweenPanel(ArrayList<MapNode> mapNodes, IProxyImage mapPath, 
			String panelId, String packageName){

		// determine whether anything should be painted onto this tab
		if(packageName.equals(Constants.STREET_PATH))
			this.isMapView = false;
		else
			this.isMapView = true;

		this.packageName = packageName;

		setLayout(new BorderLayout());

		GUIFront.getGlobalMap().setAllNodes(GUIFront.getGlobalMap().getMapNodes());

		this.localNodes = mapNodes;

		labelMainPanel.setFont(new Font("Sans", Font.BOLD, 90));
		labelMainPanel.setVerticalAlignment(SwingConstants.CENTER);
		labelMainPanel.setHorizontalAlignment(SwingConstants.CENTER);
		labelMainPanel.setText(panelID);

		this.mapImage = mapPath;

		if(this.isMapView){
			zoomRatio = GUIFront.getZoomHandle().getZoomAmount(); // get the initialized zoom amount
		}
		else{
			zoomRatio = 1;
			setPanX(0);
			setPanY(0);
		}

		buildingPolygons = GUIFrontUtil.initializePolygons();
		aKPolygon = buildingPolygons.get(0);
		bPolygon = buildingPolygons.get(1);
		cCPolygon = buildingPolygons.get(2);
		fLPolygon = buildingPolygons.get(3);
		gLPolygon = buildingPolygons.get(4);
		hHPolygon = buildingPolygons.get(5);
		hHGPolygon = buildingPolygons.get(6);
		pCPolygon = buildingPolygons.get(7);
		sHPolygon = buildingPolygons.get(8);

		addMouseListener(GUIFront.getPanHandle());
		addMouseMotionListener(GUIFront.getPanHandle());
		addMouseWheelListener(GUIFront.getZoomHandle());

		if(this.isMapView){		
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent me) {
					if (GUIFront.allowSetting == true && GUIFront.drawLine == false){
						// Reset the main reference point incase we are clicking away from a popup menu
						try {
							GUIFront.setMainReferencePoint(GUIFront.getTransform().inverseTransform(me.getPoint(), null));
						} catch (NoninvertibleTransformException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						// figure out where the closest map node is, set that node as a startnode the StartingNode
						MapNode node = GUIFront.backend.findNearestNode(GUIFront.getMainReferencePoint().getX() + getPanX(), 
								GUIFront.getMainReferencePoint().getY() + getPanY(), GUIFront.backend.getLocalMap());

						//check the click to see if it is inside of any of the building polygons
						//AK
						if(aKPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Atwater Kent"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(0, 0, 0, 1); // atwater kent 1
								}
							});
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(1, 0, 0, 1); // atwater kent 2
								}
							});
							popupMenu.add(new JMenuItem("Floor 3"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(2, 0, 0, 1); // atwater kent 3
								}
							});
							popupMenu.add(new JMenuItem("Basement"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(3, 0, 0, 1); // atwater kent basement
								}
							});

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());							
							return; 
						}

						//Boynton
						if(bPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Boynton Hall"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(4, 0, 0, 1); // boynton hall 1
								}
							});
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(5, 0, 0, 1); // boynton hall 2
								}
							});
							popupMenu.add(new JMenuItem("Floor 3"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(6, 0, 0, 1); // boynton hall 3
								}
							});
							popupMenu.add(new JMenuItem("Basement"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(7, 0, 0, 1); // boynton hall basement
								}
							});

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());
							return; 
						}

						//Campus Center
						if(cCPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Campus Center"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(8, 0, 0, 1); // campus center 1
								}
							});
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(9, 0, 0, 1); // campus center 2
								}
							});
							popupMenu.add(new JMenuItem("Floor 3"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(10, 0, 0, 1); // campus center 3
								}
							});

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());
							return; 
						}

						// Fuller Labs
						if(fLPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Fuller Labs"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(12, 0, 0, 1); // fuller labs 1
								}
							});	
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(13, 0, 0, 1); // fuller labs 2
								}
							});	
							popupMenu.add(new JMenuItem("Floor 3"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(14, 0, 0, 1); // fuller labs 3
								}
							});	
							popupMenu.add(new JMenuItem("Basement"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(15, 0, 0, 1); // fuller labs basement
								}
							});	
							popupMenu.add(new JMenuItem("Sub Basement"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(16, 0, 0, 1); // fuller labs sub basement
								}
							});	

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());
							return; 
						}								

						//Library
						if(gLPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Gordon Library"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(17, 0, 0, 1); // library 1
								}
							});
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(18, 0, 0, 1); // library 2
								}
							});
							popupMenu.add(new JMenuItem("Floor 3"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(19, 0, 0, 1); // library 3
								}
							});
							popupMenu.add(new JMenuItem("Basement"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(20, 0, 0, 1); // library basement
								}
							});

							popupMenu.add(new JMenuItem("Sub Basement"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(21, 0, 0, 1); // library sub basement
								}
							});

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());
							return; 
						}

						//Higgins
						if(hHPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Higgins House"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(22, 0, 0, 1); // higgins house 1
								}
							});
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(23, 0, 0, 1); // higgins house 2
								}
							});
							popupMenu.add(new JMenuItem("Floor 3"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(24, 0, 0, 1); // higgins house 3
								}
							});

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());
							return; 
						}
						
						//Higgins Garage
						if(hHGPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Higgins House Garage"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(25, 0, 0, 1); // higgins garage 1
								}
							});
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(26, 0, 0, 1); // higgins garage 2
								}
							});

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());
							return; 
						}

						//Project Center
						if(pCPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Project Center"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(27, 0, 0, 1); // project center 1
								}
							});
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(28, 0, 0, 1); // project center 2
								}
							});

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());
							return; 
						}


						//Stratton
						if(sHPolygon.contains(GUIFront.getMainReferencePoint())){
							JPopupMenu popupMenu = new JPopupMenu();

							popupMenu.add(new JMenuItem("Stratton Hall"))
							.setFont(new Font("Helvetica", Font.BOLD, 12));
							popupMenu.addSeparator();

							popupMenu.add(new JMenuItem("Floor 1"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(29, 0, 0, 1); // stratton hall 1
								}
							});
							popupMenu.add(new JMenuItem("Floor 2"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(30, 0, 0, 1); // stratton hall 2
								}
							});
							popupMenu.add(new JMenuItem("Floor 3"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(31, 0, 0, 1); // stratton hall 3
								}
							});

							popupMenu.add(new JMenuItem("Basement"))
							.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GUIFront.changeMapTo(32, 0, 0, 1); // stratton hall basement 3
								}
							});

							popupMenu.show(GUIFront.panelMap, me.getX(), me.getY());
							return; 
						}
						

						//refer to Andrew Petit if this doesn't make sense
						if(GUIFront.getGlobalMap().getChosenNodes().size() == 0){//set the start node of the globalnodes list of chosenNodes if that list is empty
							GUIFront.getGlobalMap().setStartNode(node);
							GUIFront.getGlobalMap().getChosenNodes().add(node);
							GUIFront.getGlobalMap().getAllNodes().add(node);
							GUIFront.backend.getLocalMap().setStartNode(node);//remember to set the start node of that localMap the user is currently on
							GUIFront.btnClear.setEnabled(true); //enable clear button if some node has been added
						}
						else{
							if(GUIFront.getGlobalMap().getChosenNodes().size() == 1){//if only the start node has been placed, place the end node
								GUIFront.getGlobalMap().getChosenNodes().add(node);
								GUIFront.getGlobalMap().setEndNode(node);
								GUIFront.getGlobalMap().getAllNodes().add(node);
								GUIFront.backend.getLocalMap().setEndNode(node);//remember to set the start node of that localMap the user is currently on
								GUIFront.btnRoute.setEnabled(true);
			
							}
							else{
								//this means we need to account for waypoints
								MapNode endNode = GUIFront.getGlobalMap().getEndNode();
								LocalMap localMap = endNode.getLocalMap();
								for (LocalMap localmap : GUIFront.getGlobalMap().getLocalMaps()){ //go back to the localMap we set to be the end, and re make it null as that node is no longer the globalMap's end node
									if (localMap == localmap){
										localmap.setEndNode(null);
										GUIFront.getGlobalMap().getChosenNodes().add(node);
										GUIFront.getGlobalMap().setEndNode(node);
										GUIFront.backend.getLocalMap().setEndNode(node); //re set the end node here to the new local map the user is on
									}
								}
								GUIFront.getGlobalMap().getChosenNodes().add(node);
								GUIFront.getGlobalMap().setEndNode(node);
								GUIFront.backend.getLocalMap().setEndNode(node); //re set the end node here to the new local map the user is on

							}
							repaint();
						}

						repaint();

					} else if (GUIFront.drawLine == true){
						GUIFront.btnClear.doClick();
					}
				}
			});
		}

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

	public ArrayList<MapNode> getMapNodes() {
		return localNodes;
	}

	public void setMapNodes(ArrayList<MapNode> localNodes) {
		this.localNodes = localNodes;
	}

	public Image getMapImage() {
		return mapImage.getImage(packageName);
	}

	public void setMapImage(IProxyImage mapImage) {
		this.mapImage = mapImage;
	}
	public void setColors(String scheme){
		GUIFront.setColors(GUIFront.getAllSchemes().setColorScheme(scheme));
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
	
	/**
	 * Draw Dashed Line
	 */
	public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){

        //creates a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();

        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.drawLine(x1, y1, x2, y2);

        //gets rid of the copy
        g2d.dispose();
        
	}

	@Override
	protected void paintComponent(Graphics g) {
		Color startNodeColor = GUIFront.getColors().getStartNodeColor();
		Color endNodeColor = GUIFront.getColors().getEndNodeColor();
		Color lineColor = GUIFront.getColors().getLineColor();
		Color outlineColor = GUIFront.getColors().getOutlineColor();

		super.paintComponent(g);

		Graphics2D graphics = (Graphics2D) g;

		if(this.mapImage == null) // StepByStep
			if(!GUIFront.isCurrentlyOpen()){
				GUIFront.getLblStepByStep().setVisible(false);
				GUIFront.getLblClickHere().setVisible(true);
				GUIFront.getLblDistance().setVisible(false);
				GUIFront.getScrollPane().setVisible(false);
				GUIFront.getListDirections().setVisible(false);
			} 
			else {
				GUIFront.getLblStepByStep().setVisible(true);
				GUIFront.getLblDistance().setVisible(true);
				GUIFront.getLblClickHere().setVisible(false);
				GUIFront.getScrollPane().setVisible(true);
				GUIFront.getListDirections().setVisible(true);
			}

		else {
			// Save the current transformed state incase something goes wrong
			AffineTransform saveTransform = graphics.getTransform();
			GUIFront.setTransform(new AffineTransform(saveTransform));

			// account for changes in zoom
			GUIFront.getTransform().translate(getWidth() / 2, getHeight() /2);
			GUIFront.getTransform().scale(zoomRatio, zoomRatio);
			GUIFront.getTransform().translate(-getWidth() / 2, -getHeight() / 2);

			GUIFront.getTransform().translate(getPanX(), getPanY()); // move to designated location
			graphics.setTransform(GUIFront.getTransform());
			
			// Colors start and end differently
			// Draws the map and places pre-existing node data onto the map as
			// well start and end nodes if they have been set
			graphics.drawImage(this.mapImage.getImage(packageName), 0, 0, this);

			// Sets the color of the start and end nodes to be different for each new waypoint
			if(this.isMapView){

				// if this is the campus map, draw the building polygons
				if(GUIFront.backend.getLocalMap().getMapImageName().equals(Constants.DEFAULT_MAP_IMAGE)){

					// Draw the panels over the building
					graphics.setColor(lineColor);
					graphics.setStroke(new BasicStroke (7));
					graphics.draw(aKPolygon);
					graphics.draw(bPolygon);
					graphics.draw(cCPolygon);
					graphics.draw(fLPolygon);
					graphics.draw(gLPolygon);
					graphics.draw(pCPolygon);
					graphics.draw(hHPolygon);
					graphics.draw(hHGPolygon);
					graphics.draw(sHPolygon);
				}

				// Sets the color of the start and end nodes to be different
				graphics.setStroke(new BasicStroke(1));
				graphics.setColor(startNodeColor);
				
				if(GUIFront.drawNodes){
					if(!(GUIFront.paths.isEmpty())){ //only try this if paths is not empty - otherwise this will result in errors
						if (GUIFront.paths.get(GUIFront.index).get(0) != null){ // make sure that the start node (which it should never be) is not null
							graphics.setColor(startNodeColor);
							graphics.fillOval((int) GUIFront.paths.get(GUIFront.index).get(0).getXPos() - (int)getPanX() - 5, 
									(int) GUIFront.paths.get(GUIFront.index).get(0).getYPos() - (int)getPanY() - 5, 10, 10);
							graphics.setColor(outlineColor);
							graphics.drawOval((int) GUIFront.paths.get(GUIFront.index).get(0).getXPos() - (int)getPanX() - 5, 
									(int) GUIFront.paths.get(GUIFront.index).get(0).getYPos() - (int)getPanY() - 5, 10, 10);
						}
						if (GUIFront.paths.get(GUIFront.index).get(GUIFront.paths.get(GUIFront.index).size() - 1) != null){ //make sure the end node (which it should never be) is not null
							graphics.setColor(endNodeColor);
							graphics.fillOval((int) GUIFront.paths.get(GUIFront.index).get(GUIFront.paths.get(GUIFront.index).size() - 1).getXPos() - (int)getPanX() - 5, 
									(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.paths.get(GUIFront.index).size() - 1).getYPos() - (int)getPanY() - 5, 10, 10);

							graphics.setColor(outlineColor);
							graphics.drawOval((int) GUIFront.paths.get(GUIFront.index).get(0).getXPos() - (int)getPanX() - 5, 
									(int) GUIFront.paths.get(GUIFront.index).get(0).getYPos() - (int)getPanY() - 5, 10, 10);
						}
					}
					//drawing for originally placed nodes
					if (GUIFront.getGlobalMap().getStartNode() != null){ //when globalMap start is updated place its position on the map if the localmap the user is on is where that node should be placed
						if (GUIFront.getGlobalMap().getStartNode().getLocalMap() == GUIFront.backend.getLocalMap()){
							graphics.setColor(startNodeColor);
							graphics.fillOval((int) GUIFront.backend.getLocalMap().getStartNode().getXPos() - (int)getPanX() - 5, 
									(int) GUIFront.backend.getLocalMap().getStartNode().getYPos() - (int)getPanY() - 5, 10, 10);
							graphics.setColor(outlineColor);
							graphics.drawOval((int) GUIFront.backend.getLocalMap().getStartNode().getXPos() - (int)getPanX() - 5, 
									(int) GUIFront.backend.getLocalMap().getStartNode().getYPos() - (int)getPanY() - 5, 10, 10);
						}
					}

					if(GUIFront.getGlobalMap().getEndNode() != null){ //when globalMap end is updated place its position on the map if the localMap the user is on is where that node should be placed
						if (GUIFront.getGlobalMap().getEndNode().getLocalMap() == GUIFront.backend.getLocalMap()){
							graphics.setColor(endNodeColor);
							graphics.fillOval((int) GUIFront.getGlobalMap().getEndNode().getXPos() - (int)getPanX() - 5, 
									(int) GUIFront.getGlobalMap().getEndNode().getYPos() - (int)getPanY() - 5, 10, 10);
							graphics.setColor(outlineColor);
							graphics.drawOval((int) GUIFront.getGlobalMap().getEndNode().getXPos() - (int)getPanX() - 5, 
									(int) GUIFront.getGlobalMap().getEndNode().getYPos() - (int)getPanY() - 5, 10, 10);
						}
					}
					if (GUIFront.getGlobalMap().getChosenNodes().size() > 2){ //check if there are waypoints -- if the user is on a map where one or more of these nodes should be placed than place them
						for (int i = 1; i < GUIFront.getGlobalMap().getChosenNodes().size() - 2; i++){
							if (GUIFront.getGlobalMap().getChosenNodes().get(i).getLocalMap() == GUIFront.backend.getLocalMap()){
								graphics.setColor(Color.ORANGE);
								graphics.fillOval((int) GUIFront.getGlobalMap().getChosenNodes().get(i).getXPos() - (int)getPanX() - 5, 
										(int) GUIFront.getGlobalMap().getChosenNodes().get(i).getYPos() - (int)getPanY() - 5, 10, 10);
								graphics.setColor(outlineColor);
								graphics.drawOval((int) GUIFront.getGlobalMap().getChosenNodes().get(i).getXPos() - (int)getPanX() - 5, 
										(int) GUIFront.getGlobalMap().getChosenNodes().get(i).getYPos() - (int)getPanY() - 5, 10, 10);
							}
						}
					}
				}
				
				//this is where you draw the lines
				if (GUIFront.getGlobalMap().getChosenNodes().size() >= 1) {
					for (int i = 0; i < GUIFront.thisRoute.size() - 1; i++){//basically go through the current map and draw the lines for all links between nodes in a route on that map
						double x1 = GUIFront.backend.getCoordinates(GUIFront.thisRoute).get(i)[0];
						double y1 = GUIFront.backend.getCoordinates(GUIFront.thisRoute).get(i)[1];
						double x2 = GUIFront.backend.getCoordinates(GUIFront.thisRoute).get(i + 1)[0];
						double y2 = GUIFront.backend.getCoordinates(GUIFront.thisRoute).get(i + 1)[1];
						Graphics2D g2 = (Graphics2D) g;
						g2.setColor(lineColor);
						Stroke dashed = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
						g2.setStroke(dashed);
						this.drawDashedLine(g2, (int) x1 - (int)getPanX(), (int) y1 - (int)getPanY(), (int) x2 - (int)getPanX(), (int) y2 - (int)getPanY());
					}
				} 

				if (GUIFront.drawLine2 == true){
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(2));
					g2.setColor(Color.YELLOW);
					g2.drawLine((int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2 - 1).getXPos() - (int)getPanX(), 
							(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2 - 1).getYPos() - (int)getPanY(), 
							(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2).getXPos() - (int)getPanX(), 
							(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2).getYPos() - (int)getPanY());
					/*g2.setColor(Color.BLACK);
					g2.fillOval((int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2).getXPos() - (int)getPanX(), 
							(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2).getYPos() - (int)getPanY(), 10, 10);
					g2.drawOval((int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2).getXPos() -5, 
							(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2).getYPos() + 4, 10, 10);*/
				}

				if (GUIFront.drawLine3 == true){
					Graphics2D g2 = (Graphics2D) g;
					g2.setStroke(new BasicStroke(2));
					g2.setColor(Color.YELLOW);
					g2.drawLine((int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2 + 1).getXPos() - (int)getPanX(), 
							(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2 + 1).getYPos() - (int)getPanY(), 
							(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2).getXPos() - (int)getPanX(), 
							(int) GUIFront.paths.get(GUIFront.index).get(GUIFront.index2).getYPos() - (int)getPanY());
				}
				
				
				repaint();
				graphics.setTransform(saveTransform); // reset to original transform to prevent weird border mishaps
			}
		}
	}

	public String getID(){
		return this.panelID;
	}
	public void setScale(double scaleAmt){
		this.zoomRatio = scaleAmt;
	}

	public double getPanX() {
		return panX;
	}

	public void setPanX(double panX) {
		this.panX = panX;
	}

	public double getPanY() {
		return panY;
	}

	public void setPanY(double panY) {
		this.panY = panY;
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
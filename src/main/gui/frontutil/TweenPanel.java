package main.gui.frontutil;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import main.MapNode;
import main.gui.GUIFront;
import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/** @description The panel that can expand out and compress in
 * 
 * @author Trevor
 */
public class TweenPanel extends JPanel {
	ArrayList<MapNode> localNodes;
	public ArrayList<MapNode> chosenNodes;

	private final TweenManager tweenManager = SLAnimator.createTweenManager();
	private JLabel labelMainPanel = new JLabel();
	private JLabel labelStep = new JLabel();
	private Image mapImage;
	private Runnable action;
	private boolean actionEnabled = true;
	private boolean hover = false;
	private int borderThickness = 2;
	private String panelID;

	private double panX;
	private double panY;
	static double zoomRatio;

	/** Constructor for a custom panel to do drawing and tweening.
	 * 
	 */
	public TweenPanel(ArrayList<MapNode> mapNodes, Image mapPath, String panelId){

		setLayout(new BorderLayout());

		this.localNodes = mapNodes;

		labelMainPanel.setFont(new Font("Sans", Font.BOLD, 90));
		labelMainPanel.setVerticalAlignment(SwingConstants.CENTER);
		labelMainPanel.setHorizontalAlignment(SwingConstants.CENTER);
		labelMainPanel.setText(panelID);

		this.mapImage = mapPath;

		setPanX(0);
		setPanY(0);
		zoomRatio = 1;
		
		addMouseListener(GUIFront.getPanHandle());
		addMouseMotionListener(GUIFront.getPanHandle());
		addMouseWheelListener(GUIFront.getZoomHandle());

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (GUIFront.isAllowSetting() == true){
					// figure out where the closest map node is, set that node as a startnode the StartingNode
					MapNode node = GUIFront.getBackend().findNearestNode(GUIFront.getMainReferencePoint().getX() + getPanX(), GUIFront.getMainReferencePoint().getY() + getPanY(), GUIFront.getBackend().getLocalMap());

					if(GUIFront.getGlobalMap().getChosenNodes().size() == 0){
						GUIFront.getGlobalMap().setStartNode(node);
						GUIFront.getGlobalMap().getStartNode().setLocalMap(GUIFront.getBackend().getLocalMap());
						GUIFront.getBackend().getLocalMap().setStartNode(node);
						GUIFront.getBtnClear().setEnabled(true);
					}
					else{
						MapNode endNode = GUIFront.getGlobalMap().getEndNode();
						if(endNode != null)
							GUIFront.getGlobalMap().addToMiddleNodes(endNode);

						GUIFront.getGlobalMap().setEndNode(node);
						GUIFront.getGlobalMap().getEndNode().setLocalMap(GUIFront.getBackend().getLocalMap());
						GUIFront.getBackend().getLocalMap().setEndNode(node);
					}
					GUIFront.getGlobalMap().getChosenNodes().add(node);
				}

				repaint();
			}	
		});
	}

	/** Constructor for Step by Step Directions panel. There needs to be two separate ones 
	 *  as they both don't need map images.
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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D graphics = (Graphics2D) g;

		if(this.mapImage == null) // StepByStep
			if(!GUIFront.isCurrentlyOpen()){
				GUIFront.getLabelStepByStep().setVisible(false);
				GUIFront.getLabelClickHere().setVisible(true);
				GUIFront.getLabelDistance().setVisible(false);
				GUIFront.getScrollPane().setVisible(false);
				GUIFront.getTxtAreaDirections().setVisible(false);
			} 
			else {
				GUIFront.getLabelStepByStep().setVisible(true);
				GUIFront.getLabelDistance().setVisible(true);
				GUIFront.getLabelClickHere().setVisible(false);
				GUIFront.getScrollPane().setVisible(true);
				GUIFront.getTxtAreaDirections().setVisible(true);
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

			// Scale the map relative to the panels current size and your current viewing window
			graphics.drawImage(mapImage, 0, 0, this);	

			// Test drawing of map nodes
			for(MapNode n : localNodes){
				graphics.fillOval((int)n.getXPos() - (int)getPanX() - 5, (int)n.getYPos() - (int)getPanY() - 5, 10, 10);
			}

			/* Colors start and end differently; Draws the map and places pre-existing node data onto the map as
			 * well start and end nodes if they have been set
			 */
			graphics.drawImage(this.mapImage, 0, 0, this);

			// Sets the color of the start and end nodes to be different
			graphics.setColor(Color.RED);
			for (int i = 0; i < GUIFront.getGlobalMap().getChosenNodes().size(); i++) {
				if(i == 0){
					if (GUIFront.getBackend().getLocalMap().getStartNode() != null){
						graphics.setColor(Color.RED);
						graphics.fillOval((int) GUIFront.getBackend().getLocalMap().getStartNode().getXPos() - (int)getPanX() - 5, (int) GUIFront.getBackend().getLocalMap().getStartNode().getYPos() - (int)getPanY() - 5, 10, 10);
					}
				} 
				else if(i == GUIFront.getGlobalMap().getChosenNodes().size()-1){
					if (GUIFront.getBackend().getLocalMap().getEndNode() != null){
						graphics.setColor(Color.GREEN);
						graphics.fillOval((int) GUIFront.getBackend().getLocalMap().getEndNode().getXPos() - (int)getPanX() - 5, (int) GUIFront.getBackend().getLocalMap().getEndNode().getYPos() - (int)getPanY() - 5, 10, 10);
					}
				}
				else {
					graphics.setColor(Color.ORANGE);
					graphics.fillOval((int) GUIFront.getGlobalMap().getChosenNodes().get(i).getXPos() - (int)getPanX() - 5, (int) GUIFront.getGlobalMap().getChosenNodes().get(i).getYPos() - (int)getPanY() - 5, 10, 10);
				}
			}

			// draws the line on the panel 
			if (GUIFront.setDrawLine(true)) {
				if (GUIFront.getPaths().isEmpty()) {
					for (int i = 0; i < GUIFront.getMapnodes().size() - 1; i++) {
						double x1 = GUIFront.getBackend().getCoordinates(GUIFront.getMapnodes()).get(i)[0];
						double y1 = GUIFront.getBackend().getCoordinates(GUIFront.getMapnodes()).get(i)[1];
						double x2 = GUIFront.getBackend().getCoordinates(GUIFront.getMapnodes()).get(i + 1)[0];
						double y2 = GUIFront.getBackend().getCoordinates(GUIFront.getMapnodes()).get(i + 1)[1];
						double alpha = 0.5;
						Color color = new Color(0, 1, 1, (float) alpha);
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(5));
						g2.setColor(color);
						g2.drawLine((int) x1 - (int)getPanX(), (int) y1 - (int)getPanY(), (int) x2 - (int)getPanX(), (int) y2 - (int)getPanY());
					}
					GUIFront.setDrawLine(false);
					GUIFront.setRemoveLine(true);
				} 
				else {
					for (int i = 0; i < GUIFront.getThisRoute().size() - 1; i++) {
						double x1 = GUIFront.getBackend().getCoordinates(GUIFront.getThisRoute()).get(i)[0];
						double y1 = GUIFront.getBackend().getCoordinates(GUIFront.getThisRoute()).get(i)[1];
						double x2 = GUIFront.getBackend().getCoordinates(GUIFront.getThisRoute()).get(i + 1)[0];
						double y2 = GUIFront.getBackend().getCoordinates(GUIFront.getThisRoute()).get(i + 1)[1];
						double alpha = 0.5;
						Color color = new Color(0, 1, 1, (float) alpha);
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(5));
						g2.setColor(color);
						g2.drawLine((int) x1 - (int)getPanX(), (int) y1 - (int)getPanY(), (int) x2 - (int)getPanX(), (int) y2 - (int)getPanY());
					}
					GUIFront.setDrawLine(false);
					GUIFront.setRemoveLine(true);
				}
			} 
			// removes the lines from the panel
			else if (GUIFront.isRemoveLine() == true) {
				if (GUIFront.getPaths().isEmpty()){
					for (int i = 0; i < GUIFront.getMapnodes().size() - 1; i++) {
						double x1 = GUIFront.getBackend().getCoordinates(GUIFront.getMapnodes()).get(i)[0];
						double y1 = GUIFront.getBackend().getCoordinates(GUIFront.getMapnodes()).get(i)[1];
						double x2 = GUIFront.getBackend().getCoordinates(GUIFront.getMapnodes()).get(i + 1)[0];
						double y2 = GUIFront.getBackend().getCoordinates(GUIFront.getMapnodes()).get(i + 1)[1];
						double alpha = 0.5;
						Color color = new Color(0, 1, 1, (float) alpha);
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(5));
						g2.setColor(color);
						g2.drawLine((int) x1 - (int)getPanX(), (int) y1 - (int)getPanY(), (int) x2 - (int)getPanX(), (int) y2 - (int)getPanY());
					}
					GUIFront.setDrawLine(true);
					GUIFront.setRemoveLine(false);
				} 
				else {
					for (int i = 0; i < GUIFront.getThisRoute().size() - 1; i++) {
						double x1 = GUIFront.getBackend().getCoordinates(GUIFront.getThisRoute()).get(i)[0];
						double y1 = GUIFront.getBackend().getCoordinates(GUIFront.getThisRoute()).get(i)[1];
						double x2 = GUIFront.getBackend().getCoordinates(GUIFront.getThisRoute()).get(i + 1)[0];
						double y2 = GUIFront.getBackend().getCoordinates(GUIFront.getThisRoute()).get(i + 1)[1];
						Graphics2D g2 = (Graphics2D) g;
						g2.setStroke(new BasicStroke(5));
						g2.setColor(Color.white);
						g2.drawLine((int) x1 - (int)getPanX(), (int) y1 - (int)getPanY(), (int) x2 - (int)getPanX(), (int) y2 - (int)getPanY());
					}
					GUIFront.setDrawLine(true);
					GUIFront.setRemoveLine(false);
				}
			}
			
			repaint();
			graphics.setTransform(saveTransform); // reset to original transform to prevent weird border mishaps
		}
	}

	public ArrayList<MapNode> getMapNodes() {
		return localNodes;
	}

	public void setMapNodes(ArrayList<MapNode> localNodes) {
		this.localNodes = localNodes;
	}

	public Image getMapImage() {
		return mapImage;
	}

	public void setMapImage(Image mapImage) {
		this.mapImage = mapImage;
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

	public String getID(){
		return this.panelID;
	}
	
	public static void setScale(double scaleAmt){
		zoomRatio = scaleAmt;
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

	/** Tween accessor class.
	 *  This class handles all of the relevant information regarding the target components tweening information
	 */
	public static class Accessor extends SLAnimator.ComponentAccessor {
		public static final int BORDER_THICKNESS = 100;

		/** Gets the thickness values to be used in animation
		 *  @param target The component we are creating an animation on
		 *  @param tweenType A variable used to decide which kind of animation we want to do, in this case there's only one option
		 *  @param returnValues A list of values containing the desired borderThickness to draw
		 *  @return returnVal Inidicates success or failure
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

		/** Sets the animation values to the specified 
		 *  @param target The component we are creating an animation on
		 *  @param tweenType A variable used to decide which kind of animation we want to do, in this case there's only one option
		 *  @param newValues A list of values containing the desired borderThickness to draw, with a value at index 0
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

package main.gui.frontutil;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

import javax.swing.JPanel;

import main.MapNode;

@SuppressWarnings("serial")
public class MapPanel extends JPanel implements ActionListener {
	private Image bgImage;
	private int circleSize = 10; //Circle size determines size of nodes on map. 
	private double xOffset = 0;
	private double yOffset = 0;
	private double startX, startY; //coordinates where mouse is pressed down
	private double dx, dy; //distance dragged with mouse
	ArrayList<MapNode> selectedPanelPoints = new ArrayList<MapNode>(); //currently selected points
	ArrayList<MapNode> mapPanelPoints = new ArrayList<MapNode>(); // currently loaded list of points
	String thismap;
	MapNode selectedNode;
	// Looks at the array of points, and creates graphical representation of what is currently stored.
	private void renderMapPrivate(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.blue);
		g2d.drawImage(bgImage, (int) xOffset, (int) yOffset, null);

		for(MapNode n : mapPanelPoints){
			g2d.fillOval((int) (n.getXPos() - 5 + xOffset), (int) (n.getYPos() - 5 + yOffset), circleSize, circleSize);

			for(MapNode m : n.getNeighbors()) {
				if(m.getLocalMap().getMapImageName().equals(n.getLocalMap().getMapImageName())) {
					g2d.drawLine((int) (n.getXPos() + xOffset), (int) (n.getYPos() + yOffset), (int) (m.getXPos() + xOffset), (int) (m.getYPos() + yOffset));
				}
				else {
					if(m.getAttributes().getOfficialName() != null && !m.getAttributes().getOfficialName().equals(""))
						g2d.drawString(m.getAttributes().getOfficialName(), (int) (n.getXPos() + 10 + xOffset), (int) (n.getYPos() + 10 + yOffset));
					else
						g2d.drawString("" + m.getXPos() + ", " + m.getYPos(), (int) (n.getXPos() + 10 + xOffset), (int) (n.getYPos() + 10 + yOffset));
				}//end else
			}//end for
		}//end outer for

		//Color selected nodes that are in this panel to be green.
		g2d.setPaint(Color.green);
		if(!mapPanelPoints.isEmpty()) {
			thismap = mapPanelPoints.get(0).getLocalMap().getMapImageName(); //What map is currently being displayed by this panel?

			for(MapNode n : selectedPanelPoints){
				if(thismap.equals(n.getLocalMap().getMapImageName())) { //Make sure the nodes are in this panel, not the other panel
					g2d.fillOval((int) (n.getXPos() - 5 + xOffset), (int) (n.getYPos() - 5 + yOffset), circleSize, circleSize);
				}
			}
		}

	}

	// paintComponent is what Swing calls to update the displayed graphics.
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		renderMapPrivate(g);
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	//Updates the points array to reflect a change, then calls the private method to render.
	public void renderMapPublic(Graphics g, ArrayList<MapNode> points) {
		mapPanelPoints = points;
		renderMapPrivate(g);
	}
	
	//This version added later and should be used, accommodates selection of multiple nodes.
	public void renderMapPublic(Graphics g, ArrayList<MapNode> points, MapNode selected) {
		mapPanelPoints = points;
		selectedNode = selected;
		renderMapPrivate(g);
	}

	public void renderMapPublic(Graphics g, ArrayList<MapNode> points, ArrayList<MapNode> selectedNodes, MapNode selected){
		mapPanelPoints = points;
		selectedPanelPoints = selectedNodes;
		selectedNode = selected;
		renderMapPrivate(g);
	}

	//Sets the stored background image to the map image.
	public void setBgImage(Image pic) {
		bgImage = pic;
	}

	public MapPanel() {
		//The following two listeners implement panning.
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				super.mousePressed(me);
				startX = me.getX(); //Remember where the mouse was pressed
				startY = me.getY();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent me) {
				super.mouseDragged(me);
				dx = me.getX() - startX; //Track changes in position as dragging occurs
				dy = me.getY() - startY;
				startX = me.getX();
				startY = me.getY();
				xOffset += dx; //the offset variables track the net offset from the original position the image and contents are in.
				yOffset += dy;

				repaint();
			}
		});
	}

	public double getXOffset() {
		return xOffset;
	}

	public double getYOffset() {
		return yOffset;
	}
}

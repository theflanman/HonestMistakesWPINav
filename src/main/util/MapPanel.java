package main.util;
import main.LocalMap;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.awt.event.*;
import java.io.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import main.MapNode;

public class MapPanel extends JPanel implements ActionListener {
	private Image bgImage;
	int circleSize = 10; //Circle size determines size of nodes on map. 
	ArrayList<MapNode> selectedPanelPoints = new ArrayList<MapNode>(); //currently selected points

	ArrayList<MapNode> mapPanelPoints = new ArrayList<MapNode>(); // currently loaded list of points

	MapNode selectedNode;
	// Looks at the array of points, and creates graphical representation of what is currently stored.
	private void renderMapPrivate(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(Color.blue);
		g2d.drawImage(bgImage, 0, 0, null);
		
		//		if (mapPanelPoints != null) {
		//			String currentLocal = mapPanelPoints.get(0).getLocalMap().getMapImageName();
		for(MapNode n : mapPanelPoints){
			g2d.fillOval((int) n.getXPos() - 5, (int) n.getYPos() - 5, circleSize, circleSize);
				for(MapNode m : n.getNeighbors()) {
					g2d.drawLine((int) n.getXPos(), (int) n.getYPos(), (int) m.getXPos(), (int) m.getYPos());
				}
				for(MapNode m : n.getNeighbors()) {
				if(m.getLocalMap().getMapImageName().equals(n.getLocalMap().getMapImageName())) {
					//g2d.fillOval((int) m.getXPos() - 5, (int) m.getYPos() - 5, circleSize, circleSize);
					//g2d.drawLine((int) n.getXPos(), (int) n.getYPos(), (int) m.getXPos(), (int) m.getYPos());
				}
				else {
					//g2d.drawArc((int) n.getXPos(),  (int) n.getYPos(), 10, 10, 10, 10);
					if(m.getAttributes().getOfficialName() != null && !m.getAttributes().getOfficialName().equals(""))
	  					g2d.drawString(m.getAttributes().getOfficialName(), (int) n.getXPos() + 10, (int) n.getYPos() + 10);
					else
						g2d.drawString("" + m.getXPos() + ", " + m.getYPos(), (int) n.getXPos() + 10, (int) n.getYPos() + 10);
				}//end else
				//g2d.setPaint(Color.blue);
				
				}//end for
		}//end outer for
			
		g2d.setPaint(Color.green);
		for(MapNode n : selectedPanelPoints){
			g2d.fillOval((int) n.getXPos() - 5, (int) n.getYPos() - 5, circleSize, circleSize);
			}
		}
			
			
	//}

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
		selectedPanelPoints.clear();
		renderMapPrivate(g);
	}
	
	public void renderSelectedNodes(Graphics g, ArrayList<MapNode> points, ArrayList<MapNode> selectedNodes, MapNode selected){
		mapPanelPoints = points;
		selectedPanelPoints = selectedNodes;
		selectedNode = selected;
		renderMapPrivate(g);
	}

	public void renderMapPublic(Graphics g, ArrayList<MapNode> points, MapNode selected) {
		mapPanelPoints = points;
		selectedNode = selected;
		renderMapPrivate(g);
	}

	//Sets the stored background image to the map image.
	public void setBgImage(Image pic) {
		bgImage = pic;
	}

}

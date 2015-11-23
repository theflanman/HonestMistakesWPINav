package main.util;
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
	
	ArrayList<MapNode> mapPanelPoints = new ArrayList<MapNode>(); // currently loaded list of points

	// Looks at the array of points, and creates graphical representation of what is currently stored.
	private void renderMapPrivate(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(bgImage, 0, 0, null);
		g2d.setPaint(Color.blue);
		for(MapNode n : mapPanelPoints){
			g2d.fillOval((int) n.getXPos() - 5, (int) n.getYPos() - 5, circleSize, circleSize);
				for(MapNode m : n.getNeighbors()) {
					g2d.drawLine((int) n.getXPos(), (int) n.getYPos(), (int) m.getXPos(), (int) m.getYPos());
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
	
	//Sets the stored background image to the map image.
	public void setBgImage(Image pic) {
		bgImage = pic;
	}

}

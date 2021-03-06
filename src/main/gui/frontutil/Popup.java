package main.gui.frontutil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.BorderLayout;

public class Popup extends JDialog {

	Color backgroundColor;
	
	public Popup(Color backgroundColor){
		this.backgroundColor = backgroundColor;
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JButton btnOk = new JButton("Ok");
		getContentPane().add(btnOk, BorderLayout.SOUTH);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Popup.this.dispose();
			}
		});
	}
	
	public void setToUserInstructions(){
		getContentPane().setBackground(backgroundColor);
		setUndecorated(true);
		setType(Type.UTILITY);
		setBounds(100, 100, 684, 400);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		
		// top panel
		JPanel topPanel = new JPanel();
		topPanel.setBackground(backgroundColor);
		topPanel.setForeground(Color.WHITE);
		topPanel.setBounds(0, 0, 684, 38);
		getContentPane().add(topPanel);
		topPanel.setLayout(null);
		
		// Top bar text
		JLabel lblAbout = new JLabel("Instructions");
		lblAbout.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAbout.setForeground(Color.BLACK);
		lblAbout.setBounds(15, 0, 138, 36);
		topPanel.add(lblAbout);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setForeground(Color.BLACK);
		textArea.setText(" Routing a Path:"
				+ "     \n1. Click on 2 or more points on any map (using the 'Locations' tab or by clicking on a building)."
				+ "     \n2. Click the 'Route' button."
				+ "     \n3. Click 'Next/Previous Step' to step through the total path. Click 'Next/Previous Map' to go to the next map."
				+ "     \n4. Read the Step by Step Instructions. "
				+ "     \n5. Click the 'Clear All' button to start a new path."
				+ "\n\n Street View: Click on the 'Street View' tab to see a picture of the location."
				+ "\n\n Search Bars: Type a name or attribute of a building and press 'Enter.'"
				+ "\n\n Other:"
				+ "     \n1. Back to Campus Map: Click to set the map to the main campus map."
				+ "     \n2. Change Floors: Changes the floor, if in a building."
				);
		textArea.setBounds(50, 40, 600, 300);
		textArea.setEditable(false);
		getContentPane().add(textArea);
		textArea.setColumns(10);
	}

	
	public void setToAbout(){
		getContentPane().setBackground(backgroundColor);
		setUndecorated(true);
		setType(Type.UTILITY);
		setBounds(100, 100, 684, 400);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		
		// top panel
		JPanel topPanel = new JPanel();
		topPanel.setBackground(backgroundColor);
		topPanel.setForeground(Color.WHITE);
		topPanel.setBounds(0, 0, 684, 38);
		getContentPane().add(topPanel);
		topPanel.setLayout(null);
		
		// Top bar text
		JLabel lblAbout = new JLabel("About");
		lblAbout.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAbout.setForeground(Color.BLACK);
		lblAbout.setBounds(15, 0, 138, 36);
		topPanel.add(lblAbout);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(Color.WHITE);
		textArea.setForeground(Color.BLACK);
		textArea.setText(" The Era of Navigation is a mapping software developed to "
				+ "navigate Worcester Polytechnic Institute (WPI).\n By click on one's "
				+ "current location and any desired destination, the shortest route will "
				+ "be displayed."
				+ "\n\n Team Name: Honest Mistakes"
				+ "\n     Jonas McGowan-Martin: Product Owner, Backup Product Owner"
				+ "\n     Domenic Bozzuto: Project Manager, Backup Product Manager"
				+ "\n     Nathan George: Lead Software Engineer, Backup Lead Software Engineer"
				+ "\n     Lauren Baker: Lead Graphics Engineer, Backup Lead Graphics Engineer"
				+ "\n     Trevor Valcourt: Backup Lead Graphics Engineer, Lead Graphics Engineer"
				+ "\n     Nick Gigliotti: Lead Test Engineer, Lead Software Engineer"
				+ "\n     Andrew Petit: Backup Product Manager, Product Manager"
				+ "\n     Connar Flanigan: Backup Lead Software Engineer, "
				+ "\n     Rayan Alsoby: Backup Product Owner, Product Owner"
				+ "\n\n Class: Software Engineering (CS3733) 2015 B term"
				+ "\n Professor: Wilson Wong"
				+ "\n Coach: Lukas Hunker"
				);
		textArea.setBounds(50, 40, 600, 300);
		textArea.setEditable(false);
		getContentPane().add(textArea);
		textArea.setColumns(10);
	}
	
}
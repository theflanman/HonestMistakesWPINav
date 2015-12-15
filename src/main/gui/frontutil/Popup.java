package main.gui.frontutil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window.Type;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Popup extends JDialog {

	Color backgroundColor;
	
	public Popup(Color backgroundColor){
		this.backgroundColor = backgroundColor;
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
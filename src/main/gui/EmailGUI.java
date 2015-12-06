package main.gui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;

import main.EmailSender;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTextArea;

/*
 * @author Nick Gigliotti
 * Pop-Up Window for Emailing Directions
 */
public class EmailGUI extends JDialog {

	private static final long serialVersionUID = 3115264882573369576L;
	private JTextField txtTo;
	private JTextField txtSubject;


	EmailSender email = new EmailSender();
	
	String[] toEmail;
	String subject = "Your trip at WPI"; 
	String body = "Add a custom message here to be displayed above the directions";
	String directions = GUIFront.allText; 
	int toAreaIndex = 0;
	int bodyAreaIndex = 0;

 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EmailGUI dialog = new EmailGUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EmailGUI() {
		setUndecorated(true);
		setType(Type.UTILITY);
		setBounds(100, 100, 684, 359);
		getContentPane().setLayout(null);
		
		// Cancel Button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(590, 314, 79, 29);
		getContentPane().add(btnCancel);
		
		// When the cancel button is pressed the window is no longer shown
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmailGUI.this.setVisible(false);
			}
		});
		
		// Send Button
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(496, 314, 79, 29);
		getContentPane().add(btnSend);
		
		// When the send button is pressed:
		// the test is the text fields is saved to the email contents
		// the email is sent
		// the window is no longer shown
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Emailing Directions");
				subject = txtSubject.getText();
				body = body.concat(directions);
				String[] toEmail = txtTo.getText().split(";");
				email.sendFromGMail(toEmail, subject, body);
				EmailGUI.this.setVisible(false);
			}
		});
		
		// To address text field
		txtTo = new JTextField();
		txtTo.setText("Type an email followed by a \";\" to add multiple recipents");
		txtTo.setBounds(120, 71, 530, 29);
		getContentPane().add(txtTo);
		txtTo.setColumns(10);
		
		// Waiting for a mouse press to clear the default text the first time
		// TODO Make the default text grey and then changed to black text
		txtTo.addMouseListener(new MouseAdapter() {
			  @Override
			  public void mouseClicked(MouseEvent e) {
				  if (toAreaIndex == 0) {
					  txtTo.setText("");
					  toAreaIndex ++;
				  }
			  }
			});
		
		// Subject text field
		txtSubject = new JTextField();
		txtSubject.setText(subject);
		txtSubject.setColumns(10);
		txtSubject.setBounds(120, 129, 530, 29);
		getContentPane().add(txtSubject);
		
		
		// Body/Custom Message text area
		JTextArea txtBody = new JTextArea();
		txtBody.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtBody.setText(body);
		txtBody.setLineWrap(true);
		txtBody.setBounds(120, 186, 530, 108);
		getContentPane().add(txtBody);
		
		// Waiting for a mouse press to clear the default text the first time
		// TODO Make the default text grey and then changed to black text
		txtBody.addMouseListener(new MouseAdapter() {
			  @Override
			  public void mouseClicked(MouseEvent e) {
				  if (bodyAreaIndex == 0) {
					  txtBody.setText("");
					  bodyAreaIndex ++;
				  }
			  }
			});
		
		// To label
		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(72, 75, 32, 20);
		getContentPane().add(lblTo);
		
		// Subject label
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setBounds(41, 133, 58, 20);
		getContentPane().add(lblSubject);
		
		// Message label
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setBounds(36, 186, 69, 20);
		getContentPane().add(lblMessage);
		
		// Top Bar panel
		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(65, 105, 225));
		topPanel.setForeground(Color.WHITE);
		topPanel.setBounds(0, 0, 684, 38);
		getContentPane().add(topPanel);
		topPanel.setLayout(null);
		
		// Top bar text
		JLabel lblNewLabel = new JLabel("Email Directions");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(15, 0, 138, 36);
		topPanel.add(lblNewLabel);
	}
}

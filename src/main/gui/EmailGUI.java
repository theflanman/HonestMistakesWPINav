package main.gui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import main.EmailSender;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EmailGUI() {
		getContentPane().setBackground(new Color(95, 172, 213));
		setUndecorated(true);
		setType(Type.UTILITY);
		setBounds(100, 100, 684, 359);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		
		// Cancel Button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(590, 314, 79, 29);
		getContentPane().add(btnCancel);
		
		// When the cancel button is pressed the window is no longer shown
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EmailGUI.this.dispose();
			}
		});
		
		// To address text field
		txtTo = new JTextField();
		txtTo.setBackground(Color.WHITE);
		txtTo.setForeground(Color.GRAY);
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
					  txtTo.setForeground(Color.BLACK);
					  toAreaIndex ++;
				  }
			  }
			});
		
		// Subject text field
		txtSubject = new JTextField();
		txtSubject.setBackground(Color.WHITE);
		txtSubject.setForeground(Color.BLACK);
		txtSubject.setText(subject);
		txtSubject.setColumns(10);
		txtSubject.setBounds(120, 129, 530, 29);
		getContentPane().add(txtSubject);
		
		// To label
		JLabel lblTo = new JLabel("To:");
		lblTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTo.setForeground(Color.WHITE);
		lblTo.setBounds(79, 74, 32, 20);
		getContentPane().add(lblTo);
		
		// Subject label
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(41, 133, 70, 20);
		getContentPane().add(lblSubject);
		
		// Message label
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setBounds(36, 186, 79, 20);
		getContentPane().add(lblMessage);
		
		// Top Bar panel
		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(218, 211, 203));
		topPanel.setForeground(Color.WHITE);
		topPanel.setBounds(0, 0, 684, 38);
		getContentPane().add(topPanel);
		topPanel.setLayout(null);
		
		// Top bar text
		JLabel lblNewLabel = new JLabel("Email Directions");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBounds(15, 0, 138, 36);
		topPanel.add(lblNewLabel);
		
		// Body panel
		JPanel bodyPanel = new JPanel();
		bodyPanel.setBackground(new Color(95, 172, 213));
		bodyPanel.setBounds(121, 189, 529, 98);
		getContentPane().add(bodyPanel);
		bodyPanel.setLayout(null);
		
		
		// Body/Custom Message text area
		JTextArea txtBody = new JTextArea();
		txtBody.setBackground(Color.WHITE);
		txtBody.setForeground(Color.GRAY);
		txtBody.setBounds(0, 0, 533, 93);
		bodyPanel.add(txtBody);
		txtBody.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtBody.setText(body);
		txtBody.setLineWrap(true);
		
		// Waiting for a mouse press to clear the default text the first time
		// TODO Make the default text grey and then changed to black text
		txtBody.addMouseListener(new MouseAdapter() {
			  @Override
			  public void mouseClicked(MouseEvent e) {
				  if (bodyAreaIndex == 0) {
					  txtBody.setText("");
					  txtBody.setForeground(Color.BLACK);
					  bodyAreaIndex ++;
				  }
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
					@Override
					public void actionPerformed(ActionEvent e) {
						subject = txtSubject.getText();
						body = txtBody.getText();
						body = body.concat("\n").concat(directions);
						String[] toEmail = txtTo.getText().split(";");
						email.sendFromGMail(toEmail, subject, body);
						EmailGUI.this.dispose();;
					}
				});
	}
}

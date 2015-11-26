package main.gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.junit.experimental.theories.Theories;

import main.EmailSender;

import javax.swing.JEditorPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextArea;
/*
 * @author Nick Gigliotti
 * Pop-Up Window for Emailing Directions
 */
public class EmailGUI extends JDialog {
	
	private static final long serialVersionUID = 3115264882573369576L;
	
	private JTextField txtTo;
	private JTextField txtSubject;
	
	EmailSender email = new EmailSender() ;

	String from = "EraOfNavigation";
	String pass = "HonestMistakes";
	String[] to = { "" };
	String subject = "Welcome to the Era of Navigation";
	String body = "hello" ;
	
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
		setBounds(100, 100, 1055, 578);
		getContentPane().setLayout(null);
		
		/*
		 * -------------TO Field---------------
		 */
		JPanel toPanel = new JPanel();
		toPanel.setBounds(15, 16, 1003, 39);
		getContentPane().add(toPanel);
		toPanel.setLayout(null);
		
		JLabel lblTo = new JLabel("TO:");
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblTo.setBounds(0, 0, 36, 39);
		toPanel.add(lblTo);
		
		txtTo = new JTextField();
		txtTo.setText("");
		txtTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtTo.setBounds(40, 0, 963, 39);
		toPanel.add(txtTo);
		txtTo.setColumns(10);
		
		/*
		 * ---------------SUBJECT Field-----------------
		 */
		JPanel subjectPanel = new JPanel();
		subjectPanel.setBounds(15, 71, 1003, 39);
		getContentPane().add(subjectPanel);
		subjectPanel.setLayout(null);
		
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSubject.setBounds(0, 0, 76, 36);
		subjectPanel.add(lblSubject);
		
		txtSubject = new JTextField();
		txtSubject.setText(subject);
		txtSubject.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtSubject.setBounds(86, 0, 917, 39);
		subjectPanel.add(txtSubject);
		txtSubject.setColumns(10);
		
		/*
		 * ----------------BODY Field-------------------
		 */
		JPanel bodyPanel = new JPanel();
		bodyPanel.setBounds(15, 126, 1003, 341);
		getContentPane().add(bodyPanel);
		bodyPanel.setLayout(null);
		
		JTextArea txtBody = new JTextArea();
		txtBody.setText("hello");
		txtBody.setBounds(0, 0, 1003, 341);
		bodyPanel.add(txtBody);
	}
	/*
	 * Button Bar
	 * Located on the bottom of the Window
	 */
	{
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 483, 1033, 39);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);
		/*
		 * --------------SEND BUTTON-----------------
		 */
		{
			JButton sendButton = new JButton("SEND");
			sendButton.setActionCommand("SEND");
			buttonPane.add(sendButton);
			getRootPane().setDefaultButton(sendButton);
			sendButton.addActionListener(new ActionListener() {
		        	public void actionPerformed(ActionEvent e) {
		        		System.out.println("Emailing Directions");
		        		to[0] = txtTo.getText();
		        		subject = txtSubject.getText();
		        		email.sendFromGMail(from, pass, to, subject, body);
		        		// EmailGUI.this.setVisible(false);
		        	}
		        }
		        );
		}
	}
}

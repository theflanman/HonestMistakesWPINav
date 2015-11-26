package main.gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;

import main.EmailSender;

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


	EmailSender email = new EmailSender();

	String from = "EraOfNavigation";
	String pass = "HonestMistakes";
	String[] to = { "" };
	String subject = "Welcome to the Era of Navigation";
	String body = GUIFront.allText;

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
		setBounds(100, 100, 900, 600);
		getContentPane().setLayout(null);
	}

	{
		// ----------------BODY-------------------
		JTextArea txtBody = new JTextArea();
		txtBody.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtBody.setBounds(15, 130, 848, 353);
		getContentPane().add(txtBody);
		txtBody.setText(body);

		
		// -------------SUBJECT---------------
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setBounds(15, 71, 76, 36);
		getContentPane().add(lblSubject);
		lblSubject.setFont(new Font("Tahoma", Font.BOLD, 18));

		txtSubject = new JTextField();
		txtSubject.setBounds(101, 74, 762, 39);
		getContentPane().add(txtSubject);
		txtSubject.setText(subject);
		txtSubject.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtSubject.setColumns(10);
		

		// --------------TO---------------
		JLabel lblTo = new JLabel("TO:");
		lblTo.setBounds(15, 16, 36, 39);
		getContentPane().add(lblTo);
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 18));

		txtTo = new JTextField();
		txtTo.setBounds(55, 16, 808, 39);
		getContentPane().add(txtTo);
		txtTo.setText("");
		txtTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtTo.setColumns(10);
		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 483, 1033, 39);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane);

	
		{
			// --------------SEND BUTTON-----------------
			JButton sendButton = new JButton("SEND");
			sendButton.setActionCommand("SEND");
			buttonPane.add(sendButton);
			getRootPane().setDefaultButton(sendButton);
			sendButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Emailing Directions");
					body = txtBody.getText();
					to[0] = txtTo.getText();
					subject = txtSubject.getText();
					email.sendFromGMail(from, pass, to, subject, body);
					EmailGUI.this.setVisible(false);
				}
			});
		}
	}
}

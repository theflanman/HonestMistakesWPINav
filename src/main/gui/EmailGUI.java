package main.gui;

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
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * @author Nick Gigliotti
 * Pop-Up Window for Emailing Directions
 */
public class EmailGUI extends JDialog {

	private static final long serialVersionUID = 3115264882573369576L;
	private JTextField txtTo;
	private JTextField txtSubject;

	EmailSender email = new EmailSender();
	
	String welcomeMessage = "The directions for your route are:\n\n";
	String fromEmail = "EraOfNavigation";
	String pass = "HonestMistakes";
	String[] toEmail = { "" };
	String subject = "The Era of Navigationn"; 
	String body = welcomeMessage.concat(GUIFront.allText);

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
		setBounds(100, 100, 1056, 653);
		getContentPane().setLayout(null);
		
		// Email BODY ---------------------------------
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(15, 147, 1004, 407);
		getContentPane().add(scrollPane);
		
		JTextArea txtBody = new JTextArea();
		txtBody.setFont(new Font("Tahoma", Font.PLAIN, 18));
		scrollPane.setViewportView(txtBody);
		txtBody.setText(body);
		
		// Email TO -----------------------------------
		JPanel panelTo = new JPanel();
		panelTo.setBounds(15, 16, 1004, 37);
		getContentPane().add(panelTo);
		panelTo.setLayout(null);
		
		JLabel lblTo = new JLabel("TO:");
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblTo.setBounds(0, 0, 86, 37);
		panelTo.add(lblTo);
		
		txtTo = new JTextField();
		txtTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtTo.setBounds(42, 2, 962, 32);
		panelTo.add(txtTo);
		txtTo.setColumns(10);
		
		// Email SUBJECT -----------------------------
		JPanel paneSubject = new JPanel();
		paneSubject.setBounds(15, 79, 1004, 37);
		getContentPane().add(paneSubject);
		paneSubject.setLayout(null);
		
		JLabel lblSubject = new JLabel("SUBJECT:");
		lblSubject.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblSubject.setBounds(0, 0, 91, 37);
		paneSubject.add(lblSubject);
		
		txtSubject = new JTextField();
		txtSubject.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtSubject.setBounds(94, 2, 910, 32);
		paneSubject.add(txtSubject);
		txtSubject.setText(subject);
		txtSubject.setColumns(10);
		
		JPanel panelButton = new JPanel();
		panelButton.setBounds(15, 563, 1004, 34);
		getContentPane().add(panelButton);
		panelButton.setLayout(null);
		
		JButton btnSend = new JButton("SEND");
		btnSend.setBounds(931, 0, 73, 29);
		panelButton.add(btnSend);
		getRootPane().setDefaultButton(btnSend);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				body = txtBody.getText();
				toEmail[0] = txtTo.getText();
				subject = txtSubject.getText();
				email.sendFromGMail(fromEmail, pass, toEmail, subject, body);
				EmailGUI.this.setVisible(false);
			}
		});
	}

}

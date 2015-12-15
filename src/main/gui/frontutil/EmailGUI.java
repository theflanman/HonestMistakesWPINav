package main.gui.frontutil;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.gui.GUIFront;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;

/**
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

	public static void main(String[] args) {
		try {
			EmailGUI dialog = new EmailGUI(null, null, null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EmailGUI(Color background1, Color background2, Color button1, Color button2) {
		getContentPane().setBackground(background2);
		setUndecorated(true);
		setType(Type.UTILITY);
		setBounds(100, 100, 684, 359);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);

		// Cancel Button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(591, 277, 79, 29);
		btnCancel.setBackground(button1);
		getContentPane().add(btnCancel);

		// When the cancel button is pressed the window is no longer shown
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmailGUI.this.dispose();
			}
		});

		// To address text field
		txtTo = new JTextField();
		txtTo.setBackground(Color.WHITE);
		txtTo.setForeground(Color.GRAY);
		txtTo.setText("Type an email followed by a \";\" to add multiple recipents");
		txtTo.setBounds(120, 44, 530, 29);
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
		txtSubject.setBounds(120, 97, 530, 29);
		getContentPane().add(txtSubject);

		// To label
		JLabel lblTo = new JLabel("To:");
		lblTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTo.setForeground(Color.WHITE);
		lblTo.setBounds(79, 47, 32, 20);
		getContentPane().add(lblTo);

		// Subject label
		JLabel lblSubject = new JLabel("Subject:");
		lblSubject.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setBounds(41, 100, 70, 20);
		getContentPane().add(lblSubject);

		// Message label
		JLabel lblMessage = new JLabel("Message:");
		lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMessage.setForeground(Color.WHITE);
		lblMessage.setBounds(36, 153, 79, 20);
		getContentPane().add(lblMessage);

		// Body panel
		JPanel bodyPanel = new JPanel();
		bodyPanel.setBackground(background2);
		bodyPanel.setBounds(121, 153, 529, 98);
		getContentPane().add(bodyPanel);
		bodyPanel.setLayout(null);


		// Body/Custom Message text area
		JTextArea txtBody = new JTextArea();
		txtBody.setBounds(0, 0, 533, 93);
		bodyPanel.add(txtBody);
		txtBody.setBackground(Color.WHITE);
		txtBody.setForeground(Color.GRAY);
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
		btnSend.setBounds(497, 277, 79, 29);
		btnSend.setBackground(button2);
		getContentPane().add(btnSend);

		// When the send button is pressed:
		// the test is the text fields is saved to the email contents
		// the email is sent
		// the window is no longer shown
		btnSend.addActionListener(new ActionListener() {
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

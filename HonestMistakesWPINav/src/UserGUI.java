import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.border.EtchedBorder;

public class UserGUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtEnterARoom;
	private JTextField txtEnterARoom_1;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserGUI frame = new UserGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UserGUI() {
		setTitle("Navigate WPI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1282, 770);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		txtEnterARoom = new JTextField();
		txtEnterARoom.setText("Enter a room here as your start.");
		GridBagConstraints gbc_txtEnterARoom = new GridBagConstraints();
		gbc_txtEnterARoom.insets = new Insets(0, 0, 5, 5);
		gbc_txtEnterARoom.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterARoom.gridx = 0;
		gbc_txtEnterARoom.gridy = 1;
		contentPane.add(txtEnterARoom, gbc_txtEnterARoom);
		txtEnterARoom.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblDirectionsFormat = new JLabel(" Directions format  ");
		GridBagConstraints gbc_lblDirectionsFormat = new GridBagConstraints();
		gbc_lblDirectionsFormat.insets = new Insets(0, 0, 5, 0);
		gbc_lblDirectionsFormat.gridx = 0;
		gbc_lblDirectionsFormat.gridy = 0;
		panel.add(lblDirectionsFormat, gbc_lblDirectionsFormat);
		
		JRadioButton rdbtnDirFormatMap = new JRadioButton("Map");
		buttonGroup.add(rdbtnDirFormatMap);
		GridBagConstraints gbc_rdbtnDirFormatMap = new GridBagConstraints();
		gbc_rdbtnDirFormatMap.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDirFormatMap.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnDirFormatMap.gridx = 0;
		gbc_rdbtnDirFormatMap.gridy = 1;
		panel.add(rdbtnDirFormatMap, gbc_rdbtnDirFormatMap);
		
		JRadioButton rdbtnDirFormatStpByStp = new JRadioButton("Step-by-step");
		buttonGroup.add(rdbtnDirFormatStpByStp);
		GridBagConstraints gbc_rdbtnDirFormatStpByStp = new GridBagConstraints();
		gbc_rdbtnDirFormatStpByStp.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDirFormatStpByStp.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnDirFormatStpByStp.gridx = 0;
		gbc_rdbtnDirFormatStpByStp.gridy = 2;
		panel.add(rdbtnDirFormatStpByStp, gbc_rdbtnDirFormatStpByStp);
		
		JRadioButton rdbtnDirFormatBoth = new JRadioButton("Both");
		rdbtnDirFormatBoth.setSelected(true);
		buttonGroup.add(rdbtnDirFormatBoth);
		GridBagConstraints gbc_rdbtnDirFormatBoth = new GridBagConstraints();
		gbc_rdbtnDirFormatBoth.anchor = GridBagConstraints.WEST;
		gbc_rdbtnDirFormatBoth.gridx = 0;
		gbc_rdbtnDirFormatBoth.gridy = 3;
		panel.add(rdbtnDirFormatBoth, gbc_rdbtnDirFormatBoth);
		
		txtEnterARoom_1 = new JTextField();
		txtEnterARoom_1.setText("Enter a room here as your destination.");
		GridBagConstraints gbc_txtEnterARoom_1 = new GridBagConstraints();
		gbc_txtEnterARoom_1.insets = new Insets(0, 0, 5, 5);
		gbc_txtEnterARoom_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterARoom_1.gridx = 0;
		gbc_txtEnterARoom_1.gridy = 2;
		contentPane.add(txtEnterARoom_1, gbc_txtEnterARoom_1);
		txtEnterARoom_1.setColumns(10);
		
		JButton btnCalculateRoute = new JButton("Calculate route");
		GridBagConstraints gbc_btnCalculateRoute = new GridBagConstraints();
		gbc_btnCalculateRoute.insets = new Insets(0, 0, 5, 5);
		gbc_btnCalculateRoute.gridx = 0;
		gbc_btnCalculateRoute.gridy = 3;
		contentPane.add(btnCalculateRoute, gbc_btnCalculateRoute);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 2;
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 4;
		contentPane.add(panel_1, gbc_panel_1);
		
		JTextPane txtpnTextDirections = new JTextPane();
		txtpnTextDirections.setText("Text Directions");
		txtpnTextDirections.setToolTipText("");
		GridBagConstraints gbc_txtpnTextDirections = new GridBagConstraints();
		gbc_txtpnTextDirections.gridheight = 4;
		gbc_txtpnTextDirections.fill = GridBagConstraints.BOTH;
		gbc_txtpnTextDirections.gridx = 2;
		gbc_txtpnTextDirections.gridy = 1;
		contentPane.add(txtpnTextDirections, gbc_txtpnTextDirections);
	}

}

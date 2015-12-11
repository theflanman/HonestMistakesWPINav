package main.gui.frontutil;

import javax.swing.*;

import java.awt.*;

@SuppressWarnings("serial")
/**
 * 
 * @author Rayan Alsoby
 *
 */
public class SplashLoad extends JFrame{


	private JLabel imglabel;
	private ImageIcon img;
	@SuppressWarnings("unused")
	private static JProgressBar pbar;
	public Thread t = null;

	/*
	 * constructor for splash screen settings: dimensions, colors, etc..
	 */
	public SplashLoad(){
		//set the Jframe 
		super("Splash");
		this.setBackground(Color.WHITE);
		this.setForeground(Color.white);
		setSize(450, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		//image label setting
		img = new ImageIcon(("src/data/splash/splashscreen.png"));
		imglabel = new JLabel(img);
		add(imglabel);
		imglabel.setBounds(0, 0, 450, 300);
		imglabel.setBackground(Color.white);
		setLayout(null);
		/*
		//progress bar settings
		pbar = new JProgressBar();
		pbar.setMinimum(0);
		pbar.setMaximum(100);
		pbar.setStringPainted(true);
		pbar.setForeground(ColorSchemes.defaultBlue);
		add(pbar);
		pbar.setPreferredSize(new Dimension(450, 20));
		pbar.setBounds(0, 300, 450, 20);

		//fake loading progress bar 1 to 100
		Thread t = new Thread() {
			public void run() {
				int i = 0;
				while (i <= 100) {
					pbar.setValue(i);
					try {
						sleep(10);
					} catch (InterruptedException ex) {
						Logger.getLogger(SplashLoad.class.getName()).log(Level.SEVERE, null, ex);
					}
					i++;
				}
			}
		};
		t.start();*/
	}
}

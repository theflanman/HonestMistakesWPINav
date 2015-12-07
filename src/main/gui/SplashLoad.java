package main.gui;

import javax.swing.*;

import main.util.Constants;

import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
@SuppressWarnings("serial")
public class SplashLoad extends JFrame{
	
	
    private JLabel imglabel;
    private ImageIcon img;
    private static JProgressBar pbar;
    public Thread t = null;
    
	public SplashLoad(){
		  super("Splash");
	        this.setBackground(Color.WHITE);
	        this.setForeground(Color.white);
		  	setSize(900, 600);
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setUndecorated(true);
	        img = new ImageIcon(("src/data/splash/splashfinal.png"));
	        imglabel = new JLabel(img);
	        add(imglabel);
	        setLayout(null);
	        pbar = new JProgressBar();
	        pbar.setMinimum(0);
	        pbar.setMaximum(100);
	        pbar.setStringPainted(true);
	        pbar.setForeground(ColorSchemes.defaultBlue);
	        imglabel.setBounds(0, 0, 900, 600);
	        imglabel.setBackground(Color.white);
	        add(pbar);
	        pbar.setPreferredSize(new Dimension(500, 30));
	        pbar.setBounds(0, 580, 900, 20);

	        Thread t = new Thread() {
	 
	            public void run() {
	                int i = 0;
	                while (i <= 100) {
	                    pbar.setValue(i);
	                    try {
	                        sleep(90);
	                    } catch (InterruptedException ex) {
	                        Logger.getLogger(SplashLoad.class.getName()).log(Level.SEVERE, null, ex);
	                    }
	                    i++;
	                }
	            }
	        };
	        t.start();
	}
}

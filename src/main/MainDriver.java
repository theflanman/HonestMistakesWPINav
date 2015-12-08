package main;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.tweenengine.Tween;
import main.gui.GUIFront;
import main.gui.SplashLoad;
import main.util.Constants;

public class MainDriver {
	
	/**
	 * Main driver to initialize map information and startup the Main GUI
	 * @param args Command-Line arguments required of a main function
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException{		

		File[] localMapList = new File(Constants.IMAGES_PATH).listFiles(); // gets a list of localmap filenames
		
		String[] localMapNames = new File(Constants.IMAGES_PATH).list();
		
		// Setup tween stuff
		Tween.registerAccessor(GUIFront.TweenPanel.class, new GUIFront.TweenPanel.Accessor());
		SLAnimator.start();
		
		//UIManager.put("nimbusBase", new Color(95, 172, 213));
		//UIManager.put("nimbusBlueGrey", new Color(184, 217, 144));
		//UIManager.put("control", new Color(96, 164, 79));
		
		// Change the theme away from the standard swing one
	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        if ("Nimbus".equals(info.getName())) {
	            UIManager.setLookAndFeel(info.getClassName());
	            break;
	        }
	    }
	    
	    //initiate the splash screen and create a delay before the program launches
	    SplashLoad s=new SplashLoad();
        s.setVisible(true);
        Thread t=Thread.currentThread();
        try {
			Thread.sleep(1100);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Launches the main application
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new GUIFront(localMapList.length, localMapList);
					
					//close the splash screen when the loading is done
					if(GUIFront.backend.splashFlag){
			        s.dispose();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}
}
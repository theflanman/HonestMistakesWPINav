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
import main.gui.MainSplash;
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
		new MainSplash();

		// Setup tween stuff
		Tween.registerAccessor(GUIFront.TweenPanel.class, new GUIFront.TweenPanel.Accessor());
		SLAnimator.start();
		
		// Change the theme away from the standard swing one
	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        if ("Nimbus".equals(info.getName())) {
	            UIManager.setLookAndFeel(info.getClassName());
	            break;
	        }
	    }
		
		// Launches the main application
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					new GUIFront(localMapList.length, localMapList);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});	
	}

}

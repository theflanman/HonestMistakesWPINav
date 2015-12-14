package main;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import aurelienribon.slidinglayout.SLAnimator;
import aurelienribon.tweenengine.Tween;
import main.gui.GUIFront;
import main.gui.frontutil.ColorSchemes;
import main.gui.frontutil.ColorSetting;
import main.gui.frontutil.SplashLoad;
import main.gui.frontutil.TweenPanel;
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
	
    static ColorSetting colorScheme;
    static ColorSchemes allSchemes;



	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException{		

		File[] localMapList = new File(Constants.IMAGES_PATH).listFiles(); // gets a list of localmap filenames
		
		// Setup tween stuff
		Tween.registerAccessor(TweenPanel.class, new TweenPanel.Accessor());
		SLAnimator.start();
			
		// Change the theme away from the standard swing one
	    /*for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        if ("Nimbus".equals(info.getName())) {
	            UIManager.setLookAndFeel(info.getClassName());
	            break;
	        }
	    }*/

		allSchemes = new ColorSchemes();
		colorScheme = allSchemes.setColorScheme("Default Campus");
	    //initiate the splash screen and create a delay before the program launches
	    SplashLoad s=new SplashLoad();
        s.setVisible(true);

	    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");

		// Launches the main application
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new GUIFront(localMapList.length, localMapList);

			    //    new GUIFront(localMapList.length, localMapList);

					//close the splash screen when the loading is done
					if(GUIFront.backend.splashFlag)
						s.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}
}
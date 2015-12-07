package main.gui;
import javax.swing.ImageIcon;

import main.util.Constants;

public class MainSplash {
	

	  SplashLoad screen;

	  public MainSplash() {
	    // initialize the splash screen
	//    splashScreenInit();
	    // do something here to simulate the program doing something that
	    // is time consuming
	    for (int i = 0; i <= 100; i++)
	    {
	      for (long j=0; j<500000; ++j)
	      {
	        String poop = " " + (j + i);
	      }
	      // run either of these two -- not both
	      screen.setProgress("Yo " + i, i);  // progress bar with a message
	      //screen.setProgress(i);           // progress bar with no message
	    }
	  //  splashScreenDestruct();
	   // System.exit(0/
	    }

	 


}

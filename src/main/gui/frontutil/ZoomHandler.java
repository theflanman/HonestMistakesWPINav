package main.gui.frontutil;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/** @description Handles all zoom functionality
 * A class to handle zooming based on scrolling of the mouse wheel. 
 * Current implementation allows for between 50% and 200% zoom. Anything less than 50%
 * with the current map sizes makes the images disappear.
 * TODO: Potentially add double click functionality ? 
 * TODO: Potentially add button functionality ?
 * 
 * @author Gatrie
 */
public class ZoomHandler implements MouseWheelListener {
	double zoomAmount;

	public ZoomHandler(){
		this.zoomAmount = 1; // the amount to zoom
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		int direction = mwe.getWheelRotation();

		// moving up, so zoom in (no greater than 100%)
		if(direction < 0){ 
			if(zoomAmount <= (.9 + .001))
				zoomAmount += 0.1;
		} 
		// moving down, zoom out (no less than 0%)
		else { 
			if(zoomAmount >= 0.5)
				zoomAmount -= 0.1;
		}

		TweenPanel.setScale(zoomAmount);
	}

}

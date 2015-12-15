package main.gui.frontutil;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import main.gui.GUIFront;

/**
 * A class to handle zooming based on scrolling of the mouse wheel. 
 * Current implementation allows for between 50% and 200% zoom. Anything less than 50%
 * with the current map sizes makes the images disappear.
 * TODO: Potentially add double click functionality ? 
 * TODO: Potentially add button functionality ?
 * 
 * @author Gatrie
 */
public class ZoomHandler implements MouseWheelListener {

	private double zoomAmount;

	public ZoomHandler(){
		this.setZoomAmount(0.7); // default zoom amount
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		int direction = mwe.getWheelRotation();

		if(direction < 0){ // moving up, so zoom in	(no greater than 100%)
			if(getZoomAmount() <= (1.1 + .001))
				setZoomAmount(getZoomAmount() + 0.1);
		} 
		else { // moving down, zoom out (no less than 50%)
			if(getZoomAmount() >= 0.4)
				setZoomAmount(getZoomAmount() - 0.1);
		}

		GUIFront.panelMap.setScale(getZoomAmount());
	}

	public double getZoomAmount() {
		return zoomAmount;
	}

	public void setZoomAmount(double zoomAmount) {
		this.zoomAmount = zoomAmount;
	}

}
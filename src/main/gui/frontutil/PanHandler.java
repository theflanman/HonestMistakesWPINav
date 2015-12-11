package main.gui.frontutil;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import main.MapNode;
import main.gui.GUIFront;

/**
 * Handles events related to panning the map image efficiently. 
 * Created with reference to code at: http://web.eecs.utk.edu/
 * @author Trevor
 */
public class PanHandler implements MouseListener, MouseMotionListener {
	double startX, startY; // reference points of original transformation
	AffineTransform startTransform; // original state of transformation

	@Override
	public void mouseDragged(MouseEvent me) {
		// now we want to start in reference to the initial transformation of THIS object, ie startTransform
		try {
			GUIFront.setMainReferencePoint(startTransform.inverseTransform(me.getPoint(), null));
		} catch (NoninvertibleTransformException e){
			e.printStackTrace();
		}

		// Now figure out the difference
		double distanceMovedX = GUIFront.getMainReferencePoint().getX() - startX;
		double distanceMovedY = GUIFront.getMainReferencePoint().getY() - startY;

		// reset the start points to the clicked point (remember, this is stored in mainReferencePoint)
		startX = GUIFront.getMainReferencePoint().getX();
		startY = GUIFront.getMainReferencePoint().getY();

		GUIFront.panelMap.setPanX(GUIFront.panelMap.getPanX() + distanceMovedX);
		GUIFront.panelMap.setPanY(GUIFront.panelMap.getPanY() + distanceMovedY);

		// Update the map node locations relative to the map image
		for(MapNode n : GUIFront.backend.getLocalMap().getMapNodes()){
			n.setXPos(n.getXPos() + distanceMovedX);
			n.setYPos(n.getYPos() + distanceMovedY);

		}
	}

	/**
	 * Will save the point clicked at and the state of the initial transformation as
	 * panning is likely to occur.
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		/**
		 * Suppose that T:U->V is a linear transformation. If there is a function S:V->U such that
		 *	S*T=I   T*S=I, then T is invertible.
		 *	
		 *	Check to make sure the current transformation is invertible and get that point
		 */
		try {
			GUIFront.setMainReferencePoint(GUIFront.getTransform().inverseTransform(me.getPoint(), null));
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}

		// save the starting points and initial transformation
		startX = GUIFront.getMainReferencePoint().getX();
		startY = GUIFront.getMainReferencePoint().getY();
		startTransform = GUIFront.getTransform();
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

}

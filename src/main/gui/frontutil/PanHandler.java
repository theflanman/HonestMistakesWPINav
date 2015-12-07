package main.gui.frontutil;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import main.MapNode;
import main.gui.GUIFront;

/** @description Handles all panning functionality
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

		GUIFront.getPanelMap().setPanX(GUIFront.getPanelMap().getPanX() + distanceMovedX);
		GUIFront.getPanelMap().setPanY(GUIFront.getPanelMap().getPanY() + distanceMovedY);

		// Update the map node locations relative to the map image
		for(MapNode n : GUIFront.getBackend().getLocalMap().getMapNodes()){
			n.setXPos(n.getXPos() + distanceMovedX);
			n.setYPos(n.getYPos() + distanceMovedY);
		}

		/**
		 * Handles events related to panning the map image efficiently. 
		 * Created with reference to code at: http://web.eecs.utk.edu/
		 * @author Trevor
		 */
		MapNode tmpStart, tmpEnd; // temporary variables for clarity
		if(GUIFront.getGlobalMap().getStartNode() != null){
			tmpStart = GUIFront.getGlobalMap().getStartNode();
			tmpStart.setXPos(tmpStart.getXPos() + distanceMovedX);
			tmpStart.setYPos(tmpStart.getYPos() + distanceMovedY);
			GUIFront.getGlobalMap().setStartNode(tmpStart);
		} 
		if (GUIFront.getGlobalMap().getEndNode() != null){
			tmpEnd = GUIFront.getGlobalMap().getEndNode();
			tmpEnd.setXPos(tmpEnd.getXPos() + distanceMovedX);
			tmpEnd.setYPos(tmpEnd.getYPos() + distanceMovedY);
			GUIFront.getGlobalMap().setEndNode(tmpEnd);
		}

		for (MapNode mapnode : GUIFront.getGlobalMap().getMiddleNodes()){
			MapNode tmpMiddle;
			if (mapnode != null){
				tmpMiddle = mapnode;
				tmpMiddle.setXPos(tmpMiddle.getXPos() + distanceMovedX);
				tmpMiddle.setYPos(tmpMiddle.getYPos() + distanceMovedY);
			}
		}
	}

	/** Will save the point clicked at and the state of the initial transformation as
	 *  panning is likely to occur.
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
	public void mouseReleased(MouseEvent me) {}
	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
}

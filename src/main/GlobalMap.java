package main;

import java.util.ArrayList;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class GlobalMap {

	private ArrayList<MapNode> mapNodes;
	private ArrayList<LocalMap> localMaps;
	private ArrayList<MapNode> path;
	
	public void generateStepByStep() {
		StepbyStep currentStepByStep = new StepbyStep(path);
		currentStepByStep.printDirection();
	}
	
	public void navigate(MapNode startNode, MapNode endNode) {
		
	}
	
}

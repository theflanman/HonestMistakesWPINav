package main;

/** TODO write this
 * 
 * @author Connor Flanigan
 *
 */

public class GlobalMap {

	private MapNode[] mapNodes;
	private LocalMap[] localMaps;
	private MapNode[] path;
	
	public void generateStepByStep() {
		StepbyStep currentStepByStep = new StepbyStep(path);
		currentStepByStep.printDirection();
	}
	
	public void navigate(MapNode startNode, MapNode endNode) {
		
	}
	
}

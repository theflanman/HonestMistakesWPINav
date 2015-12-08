package main.gui;

import java.awt.Color;
/**
 * 
 * @author Rayan Alsoby
 * a class to create a color setting object, which makes us to give information about different components of the gui 
 */
public class ColorSetting {
	
	private Color mainBackColor;
	private Color sideBarColor;
	private Color routeButtonColor;
	private Color otherButtonsColor;
	private Color lineColor;
	private Color startNodeColor;
	private Color endNodeColor;
	private Color outlineColor;

	public ColorSetting(){
		mainBackColor = new Color(95, 172, 213);
		sideBarColor = new Color(95, 172, 213);
		routeButtonColor = new Color(95, 172, 213);
		otherButtonsColor = new Color(95, 172, 213);
		lineColor = new Color(95, 172, 213);
		startNodeColor = new Color(95, 172, 213);
		endNodeColor = new Color(95, 172, 213);
		outlineColor = new Color(95, 172, 213);
	}
	
	public ColorSetting(Color routeButtonColor, Color otherButtonsColor, Color mainBackColor, Color sideBarColor,
			Color lineColor, Color startNodeColor, Color endNodeColor, Color outlineColor) {
		this.routeButtonColor = routeButtonColor;
		this.otherButtonsColor = otherButtonsColor;
		this.mainBackColor = mainBackColor;
		this.sideBarColor = sideBarColor;
		this.lineColor = lineColor;
		this.startNodeColor = startNodeColor;
		this.endNodeColor = endNodeColor;
		this.outlineColor = outlineColor; 
	}
	
	public Color getRouteButtonColor() {
		return routeButtonColor;
	}
	public void setRouteButtonColor(Color routeButtonColor) {
		this.routeButtonColor = routeButtonColor;
	}
	public Color getOtherButtonsColor() {
		return otherButtonsColor;
	}
	public void setOtherButtonsColor(Color otherButtonsColor) {
		this.otherButtonsColor = otherButtonsColor;
	}
	public Color getMainBackColor() {
		return mainBackColor;
	}
	public void setMainBackColor(Color mainBackColor) {
		this.mainBackColor = mainBackColor;
	}
	public Color getSideBarColor() {
		return sideBarColor;
	}
	public void setSideBarColor(Color sideBarColor) {
		this.sideBarColor = sideBarColor;
	}
	public Color getLineColor() {
		return lineColor;
	}
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	public Color getStartNodeColor() {
		return startNodeColor;
	}
	public void setStartNodeColor(Color startNodeColor) {
		this.startNodeColor = startNodeColor;
	}
	public Color getEndNodeColor() {
		return endNodeColor;
	}
	public void setEndNodeColor(Color endNodeColor) {
		this.endNodeColor = endNodeColor;
	}
	public Color getOutlineColor() {
		return outlineColor;
	}
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}
	
}

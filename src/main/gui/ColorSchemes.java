package main.gui;

import java.awt.Color;
import java.util.HashMap;

public class ColorSchemes {
	Color defaultBlue = new Color(95, 172, 213);
	Color defaultDarkGreen = new Color(96, 164, 79);
	Color defaultLightGreen = new Color(184, 217, 144);
	Color defaultLight = new Color(218, 211, 203);
	Color defaultRed = new Color(223, 106, 73);
	
	
	
	private HashMap<String, ColorSetting> colorSchemes;
	ColorSetting defaultCampus = new ColorSetting();
	


	public ColorSchemes(){
		this.colorSchemes = new HashMap<String, ColorSetting>();
		defaultCampus.setRouteButtonColor(defaultBlue);
		defaultCampus.setOtherButtonsColor(defaultRed);
		defaultCampus.setLineColor(defaultBlue);
		defaultCampus.setMainBackColor(defaultLightGreen);
		defaultCampus.setSideBarColor(defaultDarkGreen);
		defaultCampus.setStartNodeColor(defaultDarkGreen);
		defaultCampus.setEndNodeColor(defaultRed);
		
		colorSchemes.put("Default Campus", defaultCampus);

		
		

	}
	


	
	
	public HashMap<String, ColorSetting> getColorSchemes(){
		return this.colorSchemes;
	}
	
	
	
	
	
	public ColorSetting setColorScheme(String scheme){	
		System.out.println(this.getColorSchemes().get(scheme).getLineColor().toString());

		return this.getColorSchemes().get(scheme);
	}
	

}

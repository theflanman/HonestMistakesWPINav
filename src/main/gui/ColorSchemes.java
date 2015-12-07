package main.gui;

import java.awt.Color;
import java.util.HashMap;

public class ColorSchemes {
	//Default Campus
	Color defaultBlue = new Color(95, 172, 213);
	Color defaultDarkGreen = new Color(96, 164, 79);
	Color defaultLightGreen = new Color(184, 217, 144);
	Color defaultLight = new Color(218, 211, 203);
	Color defaultRed = new Color(223, 106, 73);
	Color defaultBlack = new Color(0,0,0);
	//Greyscale
	Color darkestGrey = new Color(64, 60, 58);
	Color darkerGrey = new Color(115, 114, 111);
	Color neturalGrey = new Color(166, 165, 164);
	Color lighterGrey = new Color(217, 216, 215);
	Color lightestGrey = new Color(242, 242, 240);
	
	//WPI colors
	Color wpiGrey = new Color(169, 176, 183);
	Color wpiRed = new Color(172, 43, 55);
	
	
	
	private HashMap<String, ColorSetting> colorSchemes;
	ColorSetting defaultCampus = new ColorSetting();
	ColorSetting greyscale = new ColorSetting();
	ColorSetting wpiDefault = new ColorSetting();


	public ColorSchemes(){
		this.colorSchemes = new HashMap<String, ColorSetting>();
		//set default campus
		defaultCampus.setRouteButtonColor(defaultBlue);
		defaultCampus.setOtherButtonsColor(defaultRed);
		defaultCampus.setLineColor(defaultBlue);
		defaultCampus.setMainBackColor(defaultLightGreen);
		defaultCampus.setSideBarColor(defaultBlue);
		defaultCampus.setStartNodeColor(defaultDarkGreen);
		defaultCampus.setEndNodeColor(defaultRed);
		defaultCampus.setOutlineColor(defaultBlack);		
		colorSchemes.put("Default Campus", defaultCampus);
		//set greyscale
		greyscale.setRouteButtonColor(lightestGrey);
		greyscale.setOtherButtonsColor(darkerGrey);
		greyscale.setLineColor(lightestGrey);
		greyscale.setMainBackColor(darkerGrey);
		greyscale.setSideBarColor(lighterGrey);
		greyscale.setStartNodeColor(darkestGrey);
		greyscale.setEndNodeColor(lightestGrey);
		greyscale.setOutlineColor(defaultBlack);
		colorSchemes.put("Greyscale", greyscale);
		
		wpiDefault.setRouteButtonColor(wpiGrey);
		wpiDefault.setOtherButtonsColor(wpiGrey);
		wpiDefault.setLineColor(defaultBlack);
		wpiDefault.setMainBackColor(wpiRed);
		wpiDefault.setSideBarColor(wpiRed);
		wpiDefault.setStartNodeColor(wpiGrey);
		wpiDefault.setEndNodeColor(wpiRed);
		wpiDefault.setOutlineColor(defaultBlack);
		colorSchemes.put("WPI Default", wpiDefault);

		

	}
	


	
	
	public HashMap<String, ColorSetting> getColorSchemes(){
		return this.colorSchemes;
	}
	
	
	
	
	
	public ColorSetting setColorScheme(String scheme){	
		System.out.println(this.getColorSchemes().get(scheme).getLineColor().toString());

		return this.getColorSchemes().get(scheme);
	}
	

}

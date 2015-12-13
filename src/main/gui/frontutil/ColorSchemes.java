package main.gui.frontutil;

import java.awt.Color;
import java.util.HashMap;

/**
 * @author Rayan Alsoby
 * class that contains the different created color schemes
 */
public class ColorSchemes {
	//Default Campus
	static Color defaultBlue = new Color(95, 172, 213);
	Color defaultDarkGreen = new Color(96, 164, 79);
	Color defaultLightGreen = new Color(184, 217, 144);
	Color defaultLight = new Color(218, 211, 203);
	Color defaultRed = new Color(223, 106, 73);	
	Color defaultBlack = new Color(0,0,0);
	
	// Alt Default Campus
	
	//Greyscale
	Color darkestGrey = new Color(64, 60, 58);
	Color darkerGrey = new Color(115, 114, 111);
	Color neturalGrey = new Color(166, 165, 164);
	Color lighterGrey = new Color(217, 216, 215);
	Color lightestGrey = new Color(242, 242, 240);

	//WPI colors
	Color wpiGrey = new Color(169, 176, 183);
	Color wpiRed = new Color(172, 43, 55);
	
	//Flower Power
	Color flowerPink1 = new Color(255, 44, 161);
	Color flowerPink2 = new Color(232, 40, 226);
	Color flowerPink3 = new Color(214, 56, 255);
	Color flowerPurple = new Color(149, 40, 232);
	Color flowerBlue = new Color(115, 41, 255);
	
	//All Blue
	Color darkestBlue = new Color(2,30,129);
	Color darkerBlue = new Color(2,83,190);
	Color neturalBlue = new Color(25,128,236);
	Color lighterBlue = new Color(72,187,252);
	Color lightestBlue = new Color(147,226,255);


	private HashMap<String, ColorSetting> colorSchemes;
	ColorSetting defaultCampus = new ColorSetting();
	ColorSetting altDefaultCampus = new ColorSetting();
	ColorSetting greyscale = new ColorSetting();
	ColorSetting wpiDefault = new ColorSetting();
	ColorSetting flowerPower = new ColorSetting();
	ColorSetting allBlue = new ColorSetting();
	

	public ColorSchemes(){
		//initialize different color schemes and add them to the list of color schemes
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
		
		//set alternative default campus
		altDefaultCampus.setRouteButtonColor(defaultBlue);
		altDefaultCampus.setOtherButtonsColor(defaultRed);
		altDefaultCampus.setLineColor(defaultBlue);
		altDefaultCampus.setMainBackColor(defaultLightGreen);
		altDefaultCampus.setSideBarColor(neturalGrey);
		altDefaultCampus.setStartNodeColor(defaultDarkGreen);
		altDefaultCampus.setEndNodeColor(defaultRed);
		altDefaultCampus.setOutlineColor(defaultBlack);		
		colorSchemes.put("Alt Default Campus", altDefaultCampus);

		//set greyscale
		greyscale.setRouteButtonColor(lightestGrey);
		greyscale.setOtherButtonsColor(darkerGrey);
		greyscale.setLineColor(defaultBlack);
		greyscale.setMainBackColor(lighterGrey);
		greyscale.setSideBarColor(darkerGrey);
		greyscale.setStartNodeColor(darkestGrey);
		greyscale.setEndNodeColor(lightestGrey);
		greyscale.setOutlineColor(defaultBlack);
		colorSchemes.put("Greyscale", greyscale);

		//set WPI crimson grey 
		wpiDefault.setRouteButtonColor(wpiGrey);
		wpiDefault.setOtherButtonsColor(wpiGrey);
		wpiDefault.setLineColor(defaultBlack);
		wpiDefault.setMainBackColor(wpiRed);
		wpiDefault.setSideBarColor(wpiRed);
		wpiDefault.setStartNodeColor(wpiGrey);
		wpiDefault.setEndNodeColor(wpiRed);
		wpiDefault.setOutlineColor(defaultBlack);
		colorSchemes.put("WPI Default", wpiDefault);
		
		//set flower power
		flowerPower.setRouteButtonColor(flowerPink1);
		flowerPower.setOtherButtonsColor(flowerPink2);
		flowerPower.setLineColor(flowerPurple);
		flowerPower.setMainBackColor(flowerPink3);
		flowerPower.setSideBarColor(flowerPurple);
		flowerPower.setStartNodeColor(flowerPink3);
		flowerPower.setEndNodeColor(flowerBlue);
		flowerPower.setOutlineColor(defaultBlack);
		colorSchemes.put("Flower Power", flowerPower);
		
		//set All Blue
		allBlue.setRouteButtonColor(lightestBlue);
		allBlue.setOtherButtonsColor(neturalBlue);
		allBlue.setLineColor(neturalBlue);
		allBlue.setMainBackColor(lighterBlue);
		allBlue.setSideBarColor(neturalBlue);
		allBlue.setStartNodeColor(lightestBlue);
		allBlue.setEndNodeColor(darkerBlue);
		allBlue.setOutlineColor(darkestBlue);
		colorSchemes.put("All Blue", allBlue);
		
	}

	public HashMap<String, ColorSetting> getColorSchemes(){
		return this.colorSchemes;
	}

	/**
	 * @param scheme a string representing the chosen color scheme
	 * @return a color setting from the hash map based on the selected scheme
	 */
	public ColorSetting setColorScheme(String scheme){	
		return this.getColorSchemes().get(scheme);
	}


}

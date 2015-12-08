package main.util;

public class GeneralUtil {

	public GeneralUtil() {
	}

	public static String removeExtension(String path){
		path = path.replace(".", ":");
		
		String newPath = path.split(":")[0].trim();
		
		return newPath;
	}
	
	/**
	 * 
	 * @param path to jpg
	 * @return a string from the package data.streetimages
	 */
	public static String convertToStreetPath(String path){
		return path.replace("images", "streetimages");
		
	}
}

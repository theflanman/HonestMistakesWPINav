package main.util;

/** Utilities for Saving
 * 
 * @author NathanGeorge
 */
public class SaveUtil {

	public SaveUtil() {
	}

	public static String removeExtension(String path){
		path = path.replace(".", ":");
		
		String newPath = path.split(":")[0].trim();
		
		return newPath;
	}
}

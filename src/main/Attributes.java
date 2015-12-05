package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Attributes implements Serializable {
	private HashMap<String, String> possibleEntries;
	private String officialName = "none";
	private ArrayList<String> aliases = new ArrayList<String>();
	private boolean isOutside = false;
	private boolean isBikeable = false;
	private boolean isHandicapped = false;
	private boolean isStairs = false;
	private boolean isPOI = false;
	private Types type = Types.OTHER;
	
	public Attributes() {
		this.possibleEntries = new HashMap<String, String>();
		
		possibleEntries.put("waterfountain", "waterFountain");
		possibleEntries.put("water fountain", "waterFountain");
		possibleEntries.put("Waterfountain", "waterFountain");
		possibleEntries.put("WaterFountain", "waterFountain");
		possibleEntries.put("Water Fountain", "waterFountain");
		possibleEntries.put("Water fountain", "waterFountain");
		possibleEntries.put("Bubbler", "waterFountain");
		possibleEntries.put("bubbler", "waterFountain");
		possibleEntries.put("food", "foodLocation");
		possibleEntries.put("foodLocation", "foodLocation");
		possibleEntries.put("Resturant", "foodLocation");
		possibleEntries.put("resturant", "foodLocation");
		possibleEntries.put("Food", "foodLocaion");
		possibleEntries.put("FoodLocation", "foodLocation");
		possibleEntries.put("foodlocation", "foodLocation");
		possibleEntries.put("Food", "foodLocation");
		possibleEntries.put("BathRoom", "bathRoom");
		possibleEntries.put("bathroom", "bathRoom");
		possibleEntries.put("bathRoom", "bathRoom");
		possibleEntries.put("Bath Room", "bathRoom");
		possibleEntries.put("bath room", "bathRoom");
		possibleEntries.put("Bath room", "bathRoom");
		possibleEntries.put("bath Room", "bathRoom");
		possibleEntries.put("lavatory", "bathRoom");
		possibleEntries.put("Lavatory", "bathRoom");
		possibleEntries.put("restRoom", "bathRoom");
		possibleEntries.put("restroom", "bathRoom");
		possibleEntries.put("Restroom", "bathRoom");
		possibleEntries.put("RestRoom", "bathRoom");
		possibleEntries.put("Rest Room", "bathRoom");
		possibleEntries.put("rest room", "bathRoom");
		possibleEntries.put("rest Room", "bathRoom");
		possibleEntries.put("Rest room", "bathRoom");	
	}
	public String getOfficialName() {
		return officialName;
	}
	public void setOfficialName(String officialName) {
		this.officialName = officialName;
	}
	public ArrayList<String> getAliases() {
		return aliases;
	}
	public void setAliases(ArrayList<String> aliases) {
		this.aliases = aliases;
	}
	public void removeAlias(String alias) {
		if(this.aliases.contains(alias)) {
			this.aliases.remove(this.aliases.indexOf(alias));
		}
	}
	public boolean isOutside() {
		return isOutside;
	}
	public void setOutside(boolean isOutside) {
		this.isOutside = isOutside;
	}
	public boolean isBikeable() {
		return isBikeable;
	}
	public void setBikeable(boolean isBikeable) {
		this.isBikeable = isBikeable;
	}
	public boolean isHandicapped() {
		return isHandicapped;
	}
	public void setHandicapped(boolean isHandicapped) {
		this.isHandicapped = isHandicapped;
	}
	public boolean isStairs() {
		return isStairs;
	}
	public void setStairs(boolean isStairs) {
		this.isStairs = isStairs;
	}
	public boolean isPOI() {
		return isPOI;
	}
	public void setPOI(boolean isPOI) {
		this.isPOI = isPOI;
	}
	public Types getType() {
		return type;
	}
	public void setType(Types type) {
		this.type = type;
	}
	public HashMap<String, String> getPossibleEntries() {
		return possibleEntries;
	}
}

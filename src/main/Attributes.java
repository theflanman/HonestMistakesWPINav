	package main;
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.io.Serializable;

	public class Attributes implements Serializable {
		HashMap<String, String> possibleEntries;
		String officialName;
		ArrayList<String> aliases = new ArrayList<String>();
		boolean isOutside;
		boolean isBikeable;
		boolean isHandicapped;
		boolean isStairs;
		boolean isPOI;
		public enum Types {
			FOOD, OFFICE, CLASSROOM, WATERFOUNTAIN, BATHROOM, PARKING, WALKING, DOOR, ELEVATOR, LAB, OTHER
		}
		Types type;
		public Attributes() {
			officialName = "";
			this.aliases = aliases;
			this.isOutside = isOutside;
			this.isBikeable = isBikeable;
			this.isHandicapped = isHandicapped;
			this.isStairs = isStairs;
			this.isPOI = isPOI;
			this.possibleEntries = new HashMap<String, String>();
			possibleEntries.put("waterfountain", "WATERFOUNTAIN");
			possibleEntries.put("water fountain", "WATERFOUNTAIN");
			possibleEntries.put("Waterfountain", "WATERFOUNTAIN");
			possibleEntries.put("WaterFountain", "WATERFOUNTAIN");
			possibleEntries.put("Water Fountain", "WATERFOUNTAIN");
			possibleEntries.put("Water fountain", "WATERFOUNTAIN");
			possibleEntries.put("Bubbler", "WATERFOUNTAIN");
			possibleEntries.put("bubbler", "WATERFOUNTAIN");
			possibleEntries.put("food", "FOOD");
			possibleEntries.put("foodLocation", "FOOD");
			possibleEntries.put("Resturant", "FOOD");
			possibleEntries.put("resturant", "FOOD");
			possibleEntries.put("Food", "FOOD");
			possibleEntries.put("FoodLocation", "FOOD");
			possibleEntries.put("foodlocation", "FOOD");
			possibleEntries.put("Food", "FOOD");
			possibleEntries.put("BathRoom", "BATHROOM");
			possibleEntries.put("bathroom", "BATHROOM");
			possibleEntries.put("bathRoom", "BATHROOM");
			possibleEntries.put("Bath Room", "BATHROOM");
			possibleEntries.put("bath room", "BATHROOM");
			possibleEntries.put("Bath room", "BATHROOM");
			possibleEntries.put("bath Room", "BATHROOM");
			possibleEntries.put("lavatory", "BATHROOM");
			possibleEntries.put("Lavatory", "BATHROOM");
			possibleEntries.put("restRoom", "BATHROOM");
			possibleEntries.put("restroom", "BATHROOM");
			possibleEntries.put("Restroom", "BATHROOM");
			possibleEntries.put("RestRoom", "BATHROOM");
			possibleEntries.put("Rest Room", "BATHROOM");
			possibleEntries.put("rest room", "BATHROOM");
			possibleEntries.put("rest Room", "BATHROOM");
			possibleEntries.put("Rest room", "BATHROOM");	
		}

	public ArrayList<String> getAliases() {
		return aliases;
	}
	public void removeAlias(String alias) {
		if(this.aliases.contains(alias)) {
			this.aliases.remove(this.aliases.indexOf(alias));
		}
	}
	public String getOfficialName() {
		return officialName;
	}
	public void setOfficialName(String officialName) {
		this.officialName = officialName;
	}
	public void setAliases(ArrayList<String> aliases) {
		this.aliases = aliases;
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

package main;
import java.util.ArrayList;

public class Attributes {
	String officialName;
	ArrayList<String> aliases = new ArrayList<String>();
	boolean isOutside;
	boolean isBikeable;
	boolean isHandicapped;
	boolean isStairs;
	boolean isPOI;
	public enum LocationType{
		foodLocation, office, classroom, waterFountain, bathroom, parking, walking, door, elevator, laboratory, other
		}
	LocationType type;
	public Attributes(String officialName, ArrayList<String> aliases, boolean isOutside, boolean isBikeable,
			boolean isHandicapped, boolean isStairs, boolean isPOI) {
		super();
		this.officialName = officialName;
		this.aliases = aliases;
		this.isOutside = isOutside;
		this.isBikeable = isBikeable;
		this.isHandicapped = isHandicapped;
		this.isStairs = isStairs;
		this.isPOI = isPOI;
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
	public LocationType getType() {
		return type;
	}
	public void setType(LocationType type) {
		this.type = type;
	}
}

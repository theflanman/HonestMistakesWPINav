package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Attributes implements Serializable{

		String officialName;
		ArrayList<String> aliases = new ArrayList<String>();
		boolean isOutside;
		boolean isBikeable;
		boolean isHandicapped;
		boolean isStairs;
		boolean isPOI;
		
		public Attributes() {
			officialName = "";
		}
		
		public enum Types {
			FOOD, OFFICE, CLASSROOM, WATERFOUNTAIN, BATHROOM, PARKING, WALKING, DOOR, ELEVATOR, LAB, OTHER
		}
		
		Types type;
		
		public Types getType() {
			return type;
		}
		
		public void setType(Types type) {
			this.type = type;
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
		
		public void addAlias(String newalias) {
			this.aliases.add(newalias);
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
		
	
}

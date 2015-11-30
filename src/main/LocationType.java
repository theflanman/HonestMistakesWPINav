package main;
/*
public enum LocationType{
	foodLocation, 
	office, 
	classRoom, 
	waterFountain, 
	bathRoom, parking, 
	walking, 
	door, 
	elevator, 
	laboratory, 
	other
	}
*/
public enum LocationType{
	foodLocation ("foodLocation"),
	office ("office"),
	classRoom ("classRoom"),
	waterFountain ("waterFountain"),
	bathRoom ("bathRoom"),
	parking ("parking"),
	walking ("walking"),
	door ("door"),
	elevator ("elevator"),
	laboratory ("laboratory"),
	other ("other");
	
	private final String name;
	
	private LocationType(String s){
		this.name= s;
	}
	
	public static LocationType parseType(String s){
		if(s.equals("foodLocation")){
			return LocationType.foodLocation;
		} else if (s.equals("office")){
			return LocationType.office;
		} else if (s.equals("classRoom")){
			return LocationType.classRoom;	
		} else if (s.equals("waterFountain")){
			return LocationType.waterFountain;
		} else if (s.equals("bathRoom")){
			return LocationType.bathRoom;
		} else if (s.equals("parking")){
			return LocationType.parking;
		} else if (s.equals("walking")){
			return LocationType.walking;
		} else if (s.equals("door")){
			return LocationType.door;
		} else if (s.equals("elevator")){
			return LocationType.elevator;
		} else if (s.equals("laboratory")){
			return LocationType.laboratory;	
		}else {
			return LocationType.other;
		}
	}
	
	public String toString(){
		return this.name;
	}
}
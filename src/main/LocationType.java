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
	
	public String toString(){
		return this.name;
	}
}
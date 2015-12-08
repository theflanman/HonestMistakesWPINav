package main;

public enum Types{
	FOOD ("foodLocation"),
	OFFICE ("office"),
	CLASSROOM ("classRoom"), 
	WATERFOUNTAIN ("waterFountain"),
	BATHROOM ("bathRoom"),
	PARKING ("parking"),
	WALKING ("walking"), 
	DOOR ("door"), 
	ELEVATOR ("elevator"),
	LAB ("laboratory"),
	OTHER ("other");

	
	private final String name;
	
	private Types(String s){
		this.name= s;
	}
	
	public static Types parseType(String s){
		if(s.equals("foodLocation")){
			return Types.FOOD;
		} else if (s.equals("office")){
			return Types.OFFICE;
		} else if (s.equals("classRoom")){
			return Types.CLASSROOM;	
		} else if (s.equals("waterFountain")){
			return Types.WATERFOUNTAIN;
		} else if (s.equals("bathRoom")){
			return Types.BATHROOM;
		} else if (s.equals("parking")){
			return Types.PARKING;
		} else if (s.equals("walking")){
			return Types.WALKING;
		} else if (s.equals("door")){
			return Types.DOOR;
		} else if (s.equals("elevator")){
			return Types.ELEVATOR;
		} else if (s.equals("laboratory")){
			return Types.LAB;	
		}else {
			return Types.OTHER;
		}
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
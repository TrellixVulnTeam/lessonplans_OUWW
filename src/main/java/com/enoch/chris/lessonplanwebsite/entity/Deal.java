package com.enoch.chris.lessonplanwebsite.entity;

public enum Deal {
	 NONE("N"), ALL("A"), HALF_PRICE("H");

	private String shortName;
	
	private Deal(String shortName) {
		this.shortName = shortName;
	}
	
	public String getShortName() {
        return shortName;
    }
	
	public static Deal fromShortName(String shortName) {
        switch (shortName) {
        case "N":
            return Deal.NONE;
 
        case "A":
            return Deal.ALL;
 
        case "H":
            return Deal.HALF_PRICE;
 
        default:
            throw new IllegalArgumentException("ShortName [" + shortName
                    + "] not supported.");
        }
    }
	
	

}

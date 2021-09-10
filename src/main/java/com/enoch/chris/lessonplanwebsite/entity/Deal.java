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
	
	/**
	 * This method can be used with {@link com.enoch.chris.lessonplanwebsite.entity.converter.DealConverter} to
	 * convert enums to shorter values that can be stored in the database in order to save memory.
	 * @param shortName
	 * @return
	 */
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

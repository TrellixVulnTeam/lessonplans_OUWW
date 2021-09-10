package com.enoch.chris.lessonplanwebsite.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.enoch.chris.lessonplanwebsite.entity.Deal;

/**
 * Converts the shortened names (stored in the database) to the full enum names that are used in Java code. 
 * Using short names saves space in the database.
 * @author chris
 *
 */
@Converter(autoApply = true)
public class DealConverter implements AttributeConverter<Deal, String>{

	@Override
	public String convertToDatabaseColumn(Deal deal) {
		return deal.getShortName();
	}

	@Override
	public Deal convertToEntityAttribute(String dbData) {
		return Deal.fromShortName(dbData);
	}
	

}

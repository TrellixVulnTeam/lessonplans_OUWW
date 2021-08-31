package com.enoch.chris.lessonplanwebsite.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.enoch.chris.lessonplanwebsite.entity.Deal;

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

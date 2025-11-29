package lixco.com.common;


import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateSerializerSQL implements JsonSerializer<Date>{
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	@Override
	public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(dateFormat.format(date));
	}

}

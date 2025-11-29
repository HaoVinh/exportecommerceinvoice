package trong.lixco.com.api;


import java.lang.reflect.Type;
import java.sql.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class DateDeserializerSQL  implements JsonDeserializer<Date>{
	@Override
	public Date deserialize(JsonElement dateStr, Type typeOfT, JsonDeserializationContext context){
		try {
			//cái này ưu tiên đầu tiên nha
            return Date.valueOf(dateStr.getAsString());
        } catch (Exception e) {
        }
        return null;
	}
}

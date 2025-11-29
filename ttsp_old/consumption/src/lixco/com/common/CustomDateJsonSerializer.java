package lixco.com.common;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CustomDateJsonSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}");
    StringBuilder messages;
    public CustomDateJsonSerializer(StringBuilder messages){
    	this.messages=messages;
    }
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        String asString = json.getAsString();
        try {
            if (DATE_PATTERN.matcher(asString).matches()) {
                return getDateFormat().parse(asString);
            } else if (DATE_TIME_PATTERN.matcher(asString).matches()) {
                return getDateTimeFormat().parse(asString);
            } else {
            	messages.append("Could not parse to date: " + json);
            	return null;
            }
        } catch (Exception e) {
        	Logger.getLogger(CustomDateJsonSerializer.class.getName()).log(Level.SEVERE, null, e);
        }
		return null;
    }

    private static DateFormat getDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(UTC_TIME_ZONE);
        return simpleDateFormat;
    }

    private static DateFormat getDateTimeFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(UTC_TIME_ZONE);
        return dateFormat;
    }

    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        Calendar calendar = Calendar.getInstance(UTC_TIME_ZONE);
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        String dateFormatted;
        if (hours == 0 && minutes == 0 && seconds == 0) {
            dateFormatted = getDateFormat().format(date);
        } else {
            dateFormatted = getDateTimeFormat().format(date);
        }
        return new JsonPrimitive(dateFormatted);
    }
}
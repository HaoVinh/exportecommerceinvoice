package lixco.com.commom_ejb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import lixco.com.annotation.JsonRequired;

public class AnnotatedDeserializer<T> implements JsonDeserializer<T>{

	StringBuilder messages;
	public AnnotatedDeserializer() {
	}
	public AnnotatedDeserializer(StringBuilder messages) {
		this.messages=messages;
	}

	public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        T pojo = new Gson().fromJson(je, type);

        Field[] fields = pojo.getClass().getDeclaredFields();
        for (Field f : fields)
        {
        	Annotation annotation=f.getAnnotation(JsonRequired.class);
            if (annotation != null)
            {
            	JsonRequired jsonRequired=(JsonRequired)annotation;
                try
                {
                    f.setAccessible(true);
                    Object value=f.get(pojo);
                    if (value == null)
                    {
                    	messages.append("Missing field in JSON: " + f.getName());
                    	return null;
                    }
                    if(!jsonRequired.defaultType()){
                    	 Class t = f.getType();
                    	 if(t == boolean.class && Boolean.FALSE.equals(value)){
                    		 messages.append("Never assigns a default value field:" + f.getName());
                    		return null;
                    	 }
                    	 else if(t == char.class && ((Character) value).charValue() == 0){
                    		 messages.append("Never assigns a default value field:" + f.getName());
                     		return null;
                    	 }
                    	 else if(t.isPrimitive() && ((Number) value).doubleValue() == 0){
                    		 messages.append("Never assigns a default value field:" + f.getName());
                     		return null;
                    	 }
                    	 else if(value == null){
                    		 messages.append("Never assigns a default value field:" + f.getName());
                     		return null;
                    	 }
                    }
                }
                catch (IllegalArgumentException ex)
                {
                    Logger.getLogger(AnnotatedDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (IllegalAccessException ex)
                {
                    Logger.getLogger(AnnotatedDeserializer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return pojo;

    }

}

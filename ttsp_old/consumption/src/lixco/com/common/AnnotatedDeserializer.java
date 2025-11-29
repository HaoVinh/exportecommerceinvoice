package lixco.com.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lixco.com.annotation.JsonRequired;

import com.google.common.primitives.Primitives;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class AnnotatedDeserializer<T> implements JsonDeserializer<T>{

	StringBuilder messages;
	public AnnotatedDeserializer() {
	}
	public AnnotatedDeserializer(StringBuilder messages) {
		this.messages=messages;
	}
	public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) {
		try{
			T pojo = JsonParserUtil.getGson().fromJson(je, type);
			pojo=checkRequired(pojo);
			return pojo;
		}catch (Exception e) {
		}
		return null;
	}
	private T checkRequired(T target) {
		try{
		    List<Field> invalidFields =new ArrayList<Field>();
		    findMissingAndInvalidFields(target, invalidFields);
		    if (!invalidFields.isEmpty()) {
		    	messages.append("Missing JSON required fields: ");
		    	messages.append(invalidFields.stream().map(n -> n.getDeclaringClass().getName()+":"+n.getName()).collect(Collectors.joining(",", "{", "}")));
		    	return null;
		    }
		}catch (Exception e) {
		}
		return target;
	}
	private void findMissingAndInvalidFields(Object target, List<Field> invalidFields) {
		try{
		    for (Field field : target.getClass().getDeclaredFields()) {
		    	Annotation annotation=field.getAnnotation(JsonRequired.class);
		    	field.setAccessible(true);
	        	Object fieldValue=field.get(target);
		        if (annotation != null) {
		        	JsonRequired jsonRequired=(JsonRequired)annotation;
		            if (fieldValue == null) {
		                invalidFields.add(field);
		                continue;
		            }
		            if(!jsonRequired.defaultType()){
                   	 Class t = field.getType();
                   	 if(t == boolean.class  && Boolean.FALSE.equals(fieldValue)){
                   		invalidFields.add(field);
                   		continue;
                   	 }
                   	 else if(t==String.class && "".equals(fieldValue)){
                   		invalidFields.add(field);
                   		continue;
                   	 }
                   	 else if(t == char.class && ((Character) fieldValue).charValue() == 0){
                   		invalidFields.add(field);
                   		continue;
                   	 }
                   	 else if(t.isPrimitive() && ((Number) fieldValue).doubleValue() == 0){
                   		invalidFields.add(field);
                   		continue;
                   	 }else if(Collection.class.isAssignableFrom(field.getType()) && ((Collection)fieldValue).size()==0){
                   		invalidFields.add(field);
                   		continue;
                   	 }
                   }
		        }
		        if (!isPrimitive(field) && !isPY(field)) {
	            	if (Collection.class.isAssignableFrom(field.getType())) {
	            		for(Object p:(Collection)fieldValue){
	            			 findMissingAndInvalidFields(p, invalidFields);
	            		}
	            	}else{
	            	   findMissingAndInvalidFields(fieldValue, invalidFields);
	            	}
	           }
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	private boolean isPrimitive(Field target) {
		try{
		    for (Class<?> primitiveClass : Primitives.allPrimitiveTypes()) {
		        if (primitiveClass.equals(target.getType())) {
		            return true;
		        }
		    }
		    for(Class<?> primitiveClass2:Primitives.allWrapperTypes()){
		    	 if (primitiveClass2.equals(target.getType())) {
			            return true;
			        }
		    }
		}catch(Exception e){
		}
	    return false;
	}
	private boolean isPY(Field target){
		try{
			if(Date.class.equals(target.getType())||String.class.equals(target.getType())){
				return true;
			}
		}catch (Exception e) {
		}
		return false;
	}

//	public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
//    {
//        T pojo = JsonParserUtil.getGson().fromJson(je, type);
//
//        Field[] fields = pojo.getClass().getDeclaredFields();
//        for (Field f : fields)
//        {
//        	Annotation annotation=f.getAnnotation(JsonRequired.class);
//            if (annotation != null)
//            {
//            	JsonRequired jsonRequired=(JsonRequired)annotation;
//                try
//                {
//                    f.setAccessible(true);
//                    Object value=f.get(pojo);
//                    if (value == null)
//                    {
//                    	throw new JsonParseException("Missing field in JSON: " + f.getName());
//                    }
//                    if(!jsonRequired.defaultType()){
//                    	 Class t = f.getType();
//                    	 if(t == boolean.class && Boolean.FALSE.equals(value)){
//                    		messages.append("Never assigns a default value field:" + f.getName());
//                    		return null;
//                    	 }
//                    	 else if(t == char.class && ((Character) value).charValue() == 0){
//                    		 messages.append("Never assigns a default value field:" + f.getName());
//                     		return null;
//                    	 }
//                    	 else if(t.isPrimitive() && ((Number) value).doubleValue() == 0){
//                    		 messages.append("Never assigns a default value field:" + f.getName());
//                     		return null;
//                    	 }
//                    	 else if(value == null){
//                    		 messages.append("Never assigns a default value field:" + f.getName());
//                     		return null;
//                    	 }
//                    }
//                }
//                catch (IllegalArgumentException ex)
//                {
//                    Logger.getLogger(AnnotatedDeserializer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                catch (IllegalAccessException ex)
//                {
//                    Logger.getLogger(AnnotatedDeserializer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//        return pojo;
//
//    }

}

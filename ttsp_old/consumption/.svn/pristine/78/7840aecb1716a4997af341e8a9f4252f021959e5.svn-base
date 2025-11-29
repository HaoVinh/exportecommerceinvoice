package lixco.com.common.einvoice;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JsonParserUtil2 {
	public JsonParserUtil2() {
	}

	private static final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer2())
			.registerTypeAdapter(Date.class, new DateSerializer2()).create();

	public static Gson getGson() {
		return gson;
	}

	public static String parseStringValue(String data, String elementName) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonEle = jsonParser.parse(data);
		return jsonEle.getAsJsonObject().get(elementName).getAsString();

	}

	public static String parseStringValue(String data, String objectName, String elementName) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonEle = jsonParser.parse(data);
		return jsonEle.getAsJsonObject().getAsJsonObject(objectName).get(elementName).getAsString();
	}

	public static JsonArray parseStringValueArray(String data, String objectName, String elementName) {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonEle = jsonParser.parse(data);
		JsonObject j = jsonEle.getAsJsonObject().getAsJsonObject(objectName);
		JsonArray a = j.get(elementName).getAsJsonArray();
		return a;
	}

	public static <T> void convertJsonArrayToList(String data, String objectName, String elementName, List<T> result,
			Class<T> l) throws UnsupportedEncodingException {
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonEle = jsonParser.parse(data);
		JsonObject j = jsonEle.getAsJsonObject().getAsJsonObject(objectName);
		JsonArray a = j.get(elementName).getAsJsonArray();
		if (a != null) {
			for (int i = 0; i < a.size(); i++) {
				JsonObject jobj = a.get(i).getAsJsonObject();
				result.add(gson.fromJson(jobj, l));
			}
		}
	}




	
}

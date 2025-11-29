package trong.lixco.com.apitaikhoan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import trong.lixco.com.util.DataResponseAPI;
import trong.lixco.com.util.DateJsonDeserializer;
import trong.lixco.com.util.DateJsonSerializer;
import trong.lixco.com.util.StaticPath;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MenuDataService {
	public static final String NAME = "menu";
	static Gson gson;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
		gsonBuilder.registerTypeAdapter(Date.class, new DateJsonSerializer());
		gsonBuilder.registerTypeAdapter(Date.class, new DateJsonDeserializer());
		gson = gsonBuilder.create();
	}

	/**
	 * 
	 * @param method timtheochuongtrinh
	 * @param param idprogram
	 * @return danh sach form
	 */
	public static MenuData[] dsmenutheochuongtrinh(String param) {
		try {
			String link="?cm=dsmenutheochuongtrinh&dt=" + param;
			String data = process(link);
			DataResponseAPI ketqua = gson.fromJson(data, DataResponseAPI.class);
			MenuData[] menuDatas = gson.fromJson(ketqua.getDt(), MenuData[].class);
			return menuDatas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	

	private static String process(String link) throws Exception {
		URL url = new URL(StaticPath.getPathAPI() + NAME + link);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("token", StaticPath.getTokenacc());
		conn.setRequestProperty("Content-type", "application/json");
		conn.setUseCaches(false);
		conn.setDoInput(true);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		}
	}
	
}

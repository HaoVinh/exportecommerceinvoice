package lixco.com.hddt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import lixco.com.einvoice_entity.ConfigEInvoice;
import lixco.com.einvoice_service.EInvoiceService;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MisaInvoice {
	final static String INSERTINVOICE = "v3sainvoice/code";
	final static String DELETEINVOICE = "v3sainvoice/code";
	final static String GETINVOICE = "v3sainvoice/code";

	public static String insertInvoice(String jsonInputString, EInvoiceService eInvoiceService) throws Exception {
		try {
			ConfigEInvoice configHDDT = eInvoiceService.getConfigEInvoice();
			URL url = new URL(configHDDT.getUrl() + INSERTINVOICE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + configHDDT.getToken());
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("TaxCode", configHDDT.getTax_code());
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
				os.flush();
				os.close();
			}
			BufferedReader br = null;
			if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			try {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				if (conn.getResponseCode() == 401) {
					try {
						getToken(configHDDT, eInvoiceService);
						return insertInvoice(jsonInputString, eInvoiceService);
					} catch (Exception e2) {
						return e2.getMessage();
					}
				} else {
					String result = "{\"code\":" + conn.getResponseCode() + ",\"thamso\":" + response.toString() + "}";
					return result;

				}
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
			// conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			String result = "{\"code\":" + 404 + ",\"thamso\":{\"success\":\"error\",\"error\":\"\"}}";
			return result;
		}
	}

	public static String getInvoice(String guiid, EInvoiceService eInvoiceService) throws Exception {
		ConfigEInvoice configHDDT = eInvoiceService.getConfigEInvoice();
		try {
			URL url = new URL(configHDDT.getUrl() + GETINVOICE + "/" + guiid);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + configHDDT.getToken());
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("TaxCode", configHDDT.getTax_code());
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				String result = "{\"code\":" + conn.getResponseCode() + ",\"thamso\":" + response.toString() + "}";
				return result;
			}catch (Exception e) {
				e.printStackTrace();
				String result = "{\"code\":" + 404 + ",\"thamso\":{\"success\":\"error\",\"error\":\""
						+ e.getLocalizedMessage() + "\"}}";
				String tokenMess =getToken(configHDDT, eInvoiceService);
				if(tokenMess==null){
				return result;
				}return tokenMess;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			String result = "{\"code\":" + 404 + ",\"thamso\":{\"success\":\"error\",\"error\":\"\"}}";
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			String tokenMess = getToken(configHDDT, eInvoiceService);
			if(tokenMess==null){
			String result = "{\"code\":" + 401 + ",\"thamso\":{\"success\":\"error\",\"error\":\""
					+ e.getLocalizedMessage() + "\"}}";
			return result;}
			return tokenMess;
		} catch (Exception e) {
			e.printStackTrace();
			String result = "{\"code\":" + 404 + ",\"thamso\":{\"success\":\"error\",\"error\":\""
					+ e.getLocalizedMessage() + "\"}}";
			return result;
		}
	}

	public static String deleteInvoice(String RefID, EInvoiceService eInvoiceService) throws Exception {
		ConfigEInvoice configHDDT = eInvoiceService.getConfigEInvoice();
		try {
			URL url = new URL(configHDDT.getUrl() + DELETEINVOICE + "?id=" + RefID);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Authorization", "Bearer " + configHDDT.getToken());
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("TaxCode", configHDDT.getTax_code());
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				String result = "{\"code\":" + conn.getResponseCode() + ",\"thamso\":" + response.toString() + "}";
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
			String result = "{\"code\":" + 404 + ",\"thamso\":{\"success\":\"error\",\"error\":\"\"}}";
			return result;
		}
	}

	private static String getToken(ConfigEInvoice configHDDT, EInvoiceService eInvoiceService) throws Exception {
		
		URL url = new URL(configHDDT.getUrl() + "oauth");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-type", "x-www-form-urlencoded");
		conn.setRequestProperty("TaxCode", configHDDT.getTax_code());
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		String jsonInputString = configHDDT.getGrant_type();
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
			os.flush();
			os.close();
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			String jsontoken = response.toString();
			JsonObject jsonObject = new JsonParser().parse(jsontoken).getAsJsonObject();
			String token = jsonObject.get("access_token").getAsString();
			configHDDT.setToken(token);
			eInvoiceService.updateConfigEInvocie(configHDDT);
			return "Đã cập nhật lại token. Vui lòng thực hiện lại thao tác";
		}
	}

	
	private static String getTokenTest(ConfigEInvoice configHDDT, EInvoiceService eInvoiceService) throws Exception {
		configHDDT = eInvoiceService.selectConfigEInvoiceById(2);
		URL url = new URL(configHDDT.getUrl() + "oauth");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-type", "x-www-form-urlencoded");
		conn.setRequestProperty("TaxCode", configHDDT.getTax_code());
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		String jsonInputString = configHDDT.getGrant_type();
		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
			os.flush();
			os.close();
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			String jsontoken = response.toString();
			JsonObject jsonObject = new JsonParser().parse(jsontoken).getAsJsonObject();
			String token = jsonObject.get("access_token").getAsString();
			configHDDT.setToken(token);
			eInvoiceService.updateConfigEInvocie(configHDDT);
			return "Đã cập nhật lại token. Vui lòng thực hiện lại thao tác";
		}
	}
	public static String insertInvoiceTest(String jsonInputString, EInvoiceService eInvoiceService) throws Exception {
		try {
			ConfigEInvoice configHDDT = eInvoiceService.getConfigEInvoice();
			URL url = new URL(configHDDT.getUrl() + INSERTINVOICE);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + configHDDT.getToken());
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("TaxCode", configHDDT.getTax_code());
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
				os.flush();
				os.close();
			}
			BufferedReader br = null;
			if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			try {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				if (conn.getResponseCode() == 401) {
					try {
						getToken(configHDDT, eInvoiceService);
						return insertInvoice(jsonInputString, eInvoiceService);
					} catch (Exception e2) {
						return e2.getMessage();
					}
				} else {
					String result = "{\"code\":" + conn.getResponseCode() + ",\"thamso\":" + response.toString() + "}";
					return result;

				}
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
			// conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			String result = "{\"code\":" + 404 + ",\"thamso\":{\"success\":\"error\",\"error\":\"\"}}";
			return result;
		}
	}

	
}

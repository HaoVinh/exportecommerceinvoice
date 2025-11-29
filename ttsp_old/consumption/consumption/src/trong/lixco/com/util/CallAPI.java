package trong.lixco.com.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class CallAPI {
	// static final String URLDMS="http://192.168.0.226:67/api/donhangnpp";
	// static final String TOKENDMS="54b6eac7-54trongbb-43a9-8882-9327442dffe8";
	//

	public static String dsdonHangDMS(String taikhoan, Date sDate, Date eDate) throws Exception {
		String tungay = MyUtil.chuyensangStr(sDate);
		if ("".equals(tungay))
			tungay = " ";
		String denngay = MyUtil.chuyensangStr(eDate);
		if ("".equals(denngay))
			denngay = " ";
		String urlParameters = "cm=taidanhsach&dt=" + taikhoan + "," + tungay + "," + denngay;
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		URL url = new URL(StaticPath.pathDMS);
		if (StaticPath.pathDMS.contains("https")) {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket quar
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		} else {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket quar
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

	public static String xacnhandonHangDMS(long iddonhang) throws Exception {
		String urlParameters = "cm=xacnhandonhang&dt=" + iddonhang;
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		URL url = new URL(StaticPath.pathDMS);
		if (StaticPath.pathDMS.contains("https")) {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket qua
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		} else {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket qua
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

	public static String boxacnhandonHangDMS(long iddonhang) throws Exception {
		String urlParameters = "cm=boxacnhandonhang&dt=" + iddonhang;
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		URL url = new URL(StaticPath.pathDMS);
		if (StaticPath.pathDMS.contains("https")) {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket qua
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		} else {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket qua
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
}

package trong.lixco.com.api;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CallAPI {
	private OkHttpClient client;
	private static String token;

	public CallAPI() {
		client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.MINUTES).writeTimeout(10, TimeUnit.MINUTES)
				.readTimeout(10, TimeUnit.MINUTES).build();
	}

	private static class LazyHolder {
		private static final CallAPI instance = new CallAPI();
	}

	public static CallAPI getInstance(String token) {
		CallAPI.token=token;
		return LazyHolder.instance;
	}

	public Call dangNhap(String url, String jsonAccountAPI) {
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
			RequestBody formBody = RequestBody.create(jsonAccountAPI, mediaType);
			Request request = new Request.Builder().url(url).post(formBody)
					.addHeader("Content-Type", "application/json; charset=utf-8").build();
			Call call = client.newCall(request);
			return call;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Call getMethodGet(String url, List<String> params, List<String> values) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(url);
			HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
			for (int i = 0; i < params.size(); i++) {
				urlBuilder.addQueryParameter(params.get(i), values.get(i));
			}
			String urlRender = urlBuilder.build().toString();
			Request request = new Request.Builder().addHeader("Content-Type", "application/json; charset=utf-8")
					.addHeader("Authorization", "Bearer " + token).url(urlRender).build();
			Call call = client.newCall(request);
			return call;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Call getMethodPost(String url, String dataJson) {
		try {
			RequestBody formBody = RequestBody.create(dataJson, MediaType.parse("application/json; charset=utf-8"));
			Request request = new Request.Builder().url(url).post(formBody)
					.addHeader("Authorization", "Bearer " + token).build();
			Call call = client.newCall(request);
			return call;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Call getMethodPut(String url, String dataJson) {
		try {
			RequestBody formBody = RequestBody.create(dataJson, MediaType.parse("application/json; charset=utf-8"));
			Request request = new Request.Builder().url(url).put(formBody)
					.addHeader("Authorization", "Bearer " + token).build();
			Call call = client.newCall(request);
			return call;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public Call getMethodDelete(String url, String dataJson) {
		try {
			RequestBody formBody = RequestBody.create(dataJson, MediaType.parse("application/json; charset=utf-8"));
			Request request = new Request.Builder().url(url).delete(formBody)
					.addHeader("Authorization", "Bearer " + token).build();
			Call call = client.newCall(request);
			return call;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

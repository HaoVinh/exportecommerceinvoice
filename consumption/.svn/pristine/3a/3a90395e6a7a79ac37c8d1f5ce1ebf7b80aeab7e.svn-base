package trong.lixco.com.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lixco.com.common.JsonParserUtil;
import lixco.com.entity.KeHoachGiaoHang;
import okhttp3.Call;
import okhttp3.Response;
import trong.lixco.com.api.DefinedName;
import trong.lixco.com.api.SecurityConfig;
import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.service.AccountAPIService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class KeHoachGiaoHangProccess {

	public static List<KeHoachGiaoHang> dasxtheonhomhang(boolean lay_hcm, boolean lay_bd, String chinhanh,
			AccountAPIService accountAPIService, Date nDate) {
		try {
			Gson gson = JsonParserUtil.getGson();

			String token = "";
			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
			if (accountAPI == null) {
				System.out.println("Không có tài khoản đăng nhập API.");
				return new ArrayList<KeHoachGiaoHang>();
			}
			String path = null;
			if ("BD".equals(chinhanh)) {
				path = StaticPath.getPathBD();
			} else if ("BN".equals(chinhanh)) {
				path = StaticPath.getPathBN();
			} else if ("HCM".equals(chinhanh)) {
				path = StaticPath.getPathHCM();
			}
			if (path != null) {
				String[] tokentime = new String[] { accountAPI.getToken(), accountAPI.getTimetoken() + "" };
				if (tokentime != null
						&& (tokentime[1] != null && new Date().getTime() < Long.parseLong(tokentime[1]) && tokentime[0] != null)) {
					token = tokentime[0];
				} else {
					dangnhapAPIdongbo(accountAPIService, gson, path, chinhanh);
				}
				// Goi ham du bao ton tai chi nhanh
				List<String> params = new ArrayList<String>();
				params.add(DefinedName.PARAM_NDATE);
				params.add(DefinedName.PARAM_LAYHCM);
				params.add(DefinedName.PARAM_LAYBD);
				List<String> values = new ArrayList<String>();
				values.add(MyUtil.chuyensangStrApi(nDate));
				values.add(lay_hcm+"");
				values.add(lay_bd+"");

				String methodName = "kehoachgiaohang";
				Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodGet(
						path + "/api/data/" + methodName, params, values);
				Response response = call.execute();
				if (response.isSuccessful()) {
					String data = response.body().string();
					if ("".equals(data)) {
						System.out.println("Không có dữ liệu " + chinhanh);
					} else {
						JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
						// List<Object[]> results =
						// jsonToListObjectArray(jsonObject);
						// return results;
						KeHoachGiaoHang[] arrDetail = gson.fromJson(jsonObject.get("data"), KeHoachGiaoHang[].class);
						List<KeHoachGiaoHang> keHoachGiaoHangs = new ArrayList<>(Arrays.asList(arrDetail));
						return keHoachGiaoHangs;
					}
				} else {
					if (response.code() == 401) {
						dangnhapAPIdongbo(accountAPIService, gson, path, chinhanh);
					} else {
						System.out.println("Xảy ra lỗi " + response.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<KeHoachGiaoHang>();
	}

	private static void dangnhapAPIdongbo(AccountAPIService accountAPIService, Gson gson, String path, String chinhanh)
			throws IOException {
		AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
		String[] tokentime = new String[2];
		Call call = trong.lixco.com.api.CallAPI.getInstance("").getMethodPost(path + "/api/account/login",
				gson.toJson(accountAPI));
		Response response = call.execute();
		if (response.isSuccessful()) {
			String data = response.body().string();
			JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
			tokentime[0] = jsonObject.get("access_token").getAsString();
			tokentime[1] = jsonObject.get("expires_in").getAsString();
			SecurityConfig.saveToken(accountAPIService, tokentime[0], Long.parseLong(tokentime[1]), chinhanh);
			System.out.println("Đã đăng nhập API " + chinhanh + ". Vui lòng thực hiện lại thao tác");
		} else {
			System.out.println("Tài khoản đăng nhập API " + chinhanh + " không đúng hoặc lỗi " + response.toString());
			return;
		}
	}

}

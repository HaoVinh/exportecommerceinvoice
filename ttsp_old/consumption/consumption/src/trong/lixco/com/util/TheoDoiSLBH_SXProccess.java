package trong.lixco.com.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.JsonParserUtil;
import lixco.com.reportInfo.SoLieuBaoCaoTongHop;
import okhttp3.Call;
import okhttp3.Response;
import trong.lixco.com.api.DefinedName;
import trong.lixco.com.api.SecurityConfig;
import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.info.SLBH;
import trong.lixco.com.service.AccountAPIService;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TheoDoiSLBH_SXProccess {
	public static List<Object[]> tonghopsolieu(List<SoLieuBaoCaoTongHop> list) {
		try {
			if (list.size() > 0) {
				List<SLBH> listResult = new ArrayList<>();
				// X - noi dia, E - noi dia (Sieu Thi), 1 - Sieu thi gia cong, T
				// - Xuat khau, V - Hang tra lai

				for (SoLieuBaoCaoTongHop p : list) {
					try {
						// Cai dat kenh kh moi
						String kenhkh_moi = p.getCustomer_channel_name();
						if (kenhkh_moi != null) {
							if (kenhkh_moi.equals("KÊNH HORECA") || kenhkh_moi.equals("KÊNH ONLINE")
									|| kenhkh_moi.equals("KÊNH SIÊU THỊ")) {
								kenhkh_moi = "KÊNH MT";
							}
						}
						// cai dat nhom kh
						String nhomkh = "";
						if ("KÊNH SIÊU THỊ".equals(p.getCustomer_channel_name())) {
							String[] sieuthilix = { "E", "5", "%", ")" };
							for (int i = 0; i < sieuthilix.length; i++) {
								if (sieuthilix[i].equals(p.getIe_categories_code())) {
									nhomkh = "SIÊU THỊ LIX";
									break;
								}
							}
							String[] sieuthioem = { "1", "(", "^" };
							for (int i = 0; i < sieuthioem.length; i++) {
								if (sieuthioem[i].equals(p.getIe_categories_code())) {
									nhomkh = "SIÊU THỊ OEM";
									break;
								}
							}
						} else if ("KÊNH XUẤT KHẨU".equals(p.getCustomer_channel_name())) {
							nhomkh = p.getCustomer_type_name();
						} else {
							nhomkh = p.getNhomkhachhang();
						}

						SLBH item = new SLBH(p.getArea_code(), p.getArea_name(), p.getCity_code(), p.getCity_name(),
								p.getCustomer_name(), p.getCustomer_type_code(), p.getCustomer_code(),
								p.getProduct_type_code(), p.getProduct_type_name(), p.getProduct_code(),
								p.getProduct_name(), MyMath.roundCustom(p.getQuantity() * p.getFactor(), 2),
								MyMath.roundCustom(p.getTotal_amount(), 2), p.getMonth_and_year(),
								p.getIe_categories_code(), p.isTypep() ? "XUẤT KHẨU" : "NỘI ĐỊA",
								p.getTypept() == 1 ? "BỘT GIẶT" : "NTRL", p.getYear(), p.getPcom_code(),
								p.getLoinhuantong(), p.getFactor(), p.getSpecification(), p.getCustomer_type_name(),
								p.getCustomer_channel_name(), p.getPcom_name(), p.getPbrand_name(), p.getQuarter(),
								MyMath.roundCustom(p.getTd_quantity() * p.getFactor(), 2), MyMath.roundCustom(
										p.getBd_quantity() * p.getFactor(), 2), MyMath.roundCustom(p.getBn_quantity()
										* p.getFactor(), 2), MyMath.roundCustom(p.getTd_total_amount(), 2),
								MyMath.roundCustom(p.getBd_total_amount(), 2), MyMath.roundCustom(
										p.getBn_total_amount(), 2), p.getMaspcombo() == null ? "" : p.getMaspcombo(),
								p.getTotal_amount_nt(), p.getSohd(), p.getQuocgia(), kenhkh_moi, nhomkh);
						listResult.add(item);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// Phan nhom
				// Nhóm 1: Phân nhóm theo "LOẠI SP1" (Bột giặt, NTRL)

				List<Object[]> giatris = new ArrayList<Object[]>();

				Map<String, List<SLBH>> groupByLoaiSP1 = listResult.stream().collect(
						Collectors.groupingBy(SLBH::getLoaiSp1));
				groupByLoaiSP1.forEach((loaiSp1, sublist_bot_nuoc) -> {

					String bot_nuoc = loaiSp1;

					Map<String, List<SLBH>> groupByKenhKH = sublist_bot_nuoc.stream().collect(
							Collectors.groupingBy(SLBH::getKenhKhachHang));
					groupByKenhKH.forEach((kenhKH, sublist_kenhKH) -> {
						String kenh_khachhang = kenhKH;// KÊNH GT; KÊNH MT, KÊNH
														// XK

							if ("KÊNH MT".equals(kenh_khachhang)) {
								List<SLBH> nhomLix_MT = new ArrayList<SLBH>();
								List<SLBH> nhomLix_OBMT = new ArrayList<SLBH>();
								Map<String, List<SLBH>> groupByNhomKH = sublist_kenhKH.stream().collect(
										Collectors.groupingBy(SLBH::getNhomKhachHang));
								groupByNhomKH.forEach((nhomKH, sublist_nhomKH) -> {

									if (nhomKH.equals("ONLINE") || nhomKH.equals("HORECA")
											|| nhomKH.equals("SIÊU THỊ LIX")) {
										nhomLix_MT.addAll(sublist_nhomKH);
									} else if (nhomKH.equals("SIÊU THỊ OEM")) {
										nhomLix_OBMT.addAll(sublist_nhomKH);
									}
								});
								double totalSoLuong_MT = nhomLix_MT.stream().mapToDouble(SLBH::getSoLuong).sum();
								Object[] item_MT = { bot_nuoc, "MT", totalSoLuong_MT / 1000 };
								giatris.add(item_MT);
								double totalSoLuong_OB = nhomLix_OBMT.stream().mapToDouble(SLBH::getSoLuong).sum();
								Object[] item_OB = { bot_nuoc, "OB-MT", totalSoLuong_OB / 1000 };
								giatris.add(item_OB);
							} else {
								String kenh = "";
								if ("KÊNH GT".equals(kenh_khachhang)) {
									kenh = "GT";
								} else if ("KÊNH XUẤT KHẨU".equals(kenh_khachhang)) {
									kenh = "XK";
								} else {
									kenh = kenh_khachhang;
								}
								double totalSoLuong = sublist_kenhKH.stream().mapToDouble(SLBH::getSoLuong).sum();
								Object[] item = { bot_nuoc, kenh, totalSoLuong / 1000 };
								giatris.add(item);
							}

						});
				});

				return giatris;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Object[]>();
	}

	public static List<Object[]> toncuoitheonhom(String chinhanh, AccountAPIService accountAPIService, int month,
			int year) {
		try {
			Gson gson = JsonParserUtil.getGson();

			String token = "";
			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
			if (accountAPI == null) {
				System.out.println("Không có tài khoản đăng nhập API.");
				return new ArrayList<Object[]>();
			}
			String path = null;
			if ("BD".equals(chinhanh)) {
				path = StaticPath.getPathBD();
			} else if ("BN".equals(chinhanh)) {
				path = StaticPath.getPathBN();
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
				params.add(DefinedName.PARAM_MONTH);
				params.add(DefinedName.PARAM_YEAR);
				List<String> values = new ArrayList<String>();
				values.add(month + "");
				values.add(year + "");

				String methodName = "toncuoitheonhom";
				Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodGet(
						path + "/api/data/" + methodName, params, values);
				Response response = call.execute();
				if (response.isSuccessful()) {
					String data = response.body().string();
					if ("".equals(data)) {
						System.out.println("Không có dữ liệu " + chinhanh);
					} else {
						JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
						List<Object[]> results = jsonToListObjectArray(jsonObject);
						return results;
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
		return new ArrayList<Object[]>();
	}

	public static List<Object[]> dasxtheonhomhang(String chinhanh, AccountAPIService accountAPIService, Date sDate,
			Date eDate) {
		try {
			Gson gson = JsonParserUtil.getGson();

			String token = "";
			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
			if (accountAPI == null) {
				System.out.println("Không có tài khoản đăng nhập API.");
				return new ArrayList<Object[]>();
			}
			String path = null;
			if ("BD".equals(chinhanh)) {
				path = StaticPath.getPathBD();
			} else if ("BN".equals(chinhanh)) {
				path = StaticPath.getPathBN();
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
				params.add(DefinedName.PARAM_SDATE);
				params.add(DefinedName.PARAM_EDATE);
				List<String> values = new ArrayList<String>();
				values.add(MyUtil.chuyensangStrApi(sDate));
				values.add(MyUtil.chuyensangStrApi(eDate));

				String methodName = "dasxtheonhomhang";
				Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodGet(
						path + "/api/data/" + methodName, params, values);
				Response response = call.execute();
				if (response.isSuccessful()) {
					String data = response.body().string();
					if ("".equals(data)) {
						System.out.println("Không có dữ liệu " + chinhanh);
					} else {
						JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
						List<Object[]> results = jsonToListObjectArray(jsonObject);
						return results;
						// ExpectedInventoryReqInfo[] arrDetail =
						// gson.fromJson(jsonObject.get("data"),
						// ExpectedInventoryReqInfo[].class);
						// List<Object[]> chiTietDuBaoTons = new
						// ArrayList<>(Arrays.asList(Object[]));
						// return chiTietDuBaoTons;
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
		return new ArrayList<Object[]>();
	}

	public static List<Object[]> tonghopsolieu2(List<Object[]> dataList) {
		Map<String, Double> mergedData = new HashMap<>();

		// Duyệt danh sách và gộp nhóm
		for (Object[] row : dataList) {
			String phanloai = (String) row[0];
			String kenhmoi = (String) row[1];
			double soluong = parseDouble(row[2]);

			// Tạo key duy nhất dựa trên phanloai và kenhmoi
			String key = phanloai + "##" + kenhmoi;
			mergedData.put(key, mergedData.getOrDefault(key, 0.0) + soluong);
		}

		// Chuyển Map về List<Object[]>
		List<Object[]> resultList = new ArrayList<>();
		for (Map.Entry<String, Double> entry : mergedData.entrySet()) {
			String[] keyParts = entry.getKey().split("##");
			resultList.add(new Object[] { keyParts[0], keyParts[1], entry.getValue()/1000 });
		}

		return resultList;
	}
	 private static double parseDouble(Object value) {
	        if (value instanceof Double) {
	            return (Double) value;
	        } else if (value instanceof Number) {
	            return ((Number) value).doubleValue();
	        } else {
	            try {
	                return Double.parseDouble(value.toString());
	            } catch (NumberFormatException e) {
	                throw new IllegalArgumentException("Không thể chuyển đổi giá trị: " + value);
	            }
	        }
	    }

	public static List<Object[]> xuathang(String chinhanh, AccountAPIService accountAPIService, String paramjson) {
		try {
			Gson gson = JsonParserUtil.getGson();

			String token = "";
			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, chinhanh);
			if (accountAPI == null) {
				System.out.println("Không có tài khoản đăng nhập API.");
				return new ArrayList<Object[]>();
			}
			String path = null;
			if ("BD".equals(chinhanh)) {
				path = StaticPath.getPathBD();
			} else if ("BN".equals(chinhanh)) {
				path = StaticPath.getPathBN();
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
				params.add(DefinedName.PARAM_JSON2);
				List<String> values = new ArrayList<String>();
				values.add(paramjson);

				String methodName = "xuathang";
				Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodGet(
						path + "/api/data/" + methodName, params, values);
				Response response = call.execute();
				if (response.isSuccessful()) {
					String data = response.body().string();
					if ("".equals(data)) {
						System.out.println("Không có dữ liệu " + chinhanh);
					} else {
						JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
						List<Object[]> results = jsonToListObjectArray(jsonObject);
						return results;
						// SoLieuBaoCaoTongHop[] arrDetail =
						// gson.fromJson(jsonObject.get("data"),
						// SoLieuBaoCaoTongHop[].class);
						// List<SoLieuBaoCaoTongHop> results = new
						// ArrayList<>(Arrays.asList(arrDetail));
						// return results;
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
		return new ArrayList<Object[]>();
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

	private static List<Object[]> jsonToListObjectArray(JsonObject jsonObject) {
		List<Object[]> result = new ArrayList<>();

		// Kiểm tra trường "data" có tồn tại không
		if (jsonObject.has("data")) {
			JsonArray dataArray = jsonObject.getAsJsonArray("data");

			// Duyệt qua từng mảng con trong dataArray
			for (int i = 0; i < dataArray.size(); i++) {
				JsonArray rowArray = dataArray.get(i).getAsJsonArray();

				// Chuyển JsonArray thành Object[]
				Object[] row = new Object[rowArray.size()];
				for (int j = 0; j < rowArray.size(); j++) {
					if (rowArray.get(j).isJsonPrimitive()) {
						// Tự động chuyển kiểu dữ liệu (String, int, double)
						if (rowArray.get(j).getAsJsonPrimitive().isString()) {
							row[j] = rowArray.get(j).getAsString();
						} else if (rowArray.get(j).getAsJsonPrimitive().isNumber()) {
							row[j] = rowArray.get(j).getAsNumber();
						} else if (rowArray.get(j).getAsJsonPrimitive().isBoolean()) {
							row[j] = rowArray.get(j).getAsBoolean();
						}
					} else {
						row[j] = rowArray.get(j).toString();
					}
				}

				// Thêm Object[] vào danh sách
				result.add(row);
			}
		}

		return result;
	}

	public static List<Object[]> groupAndSumAsList(List<Object[]> data) {
		// Sử dụng Map để nhóm dữ liệu
		Map<String, double[]> groupedData = new HashMap<>();

		for (Object[] row : data) {
			String name = (String) row[0]; // Lấy tên (LIX, OB, ...)
			double value1 = 0;
			double value2 = 0;

			if (row[1] instanceof BigDecimal) {
				value1 = ((BigDecimal) row[1]).doubleValue();
			} else if (row[1] instanceof Number) {
				value1 = ((Number) row[1]).doubleValue();
			}

			if (row[2] instanceof BigDecimal) {
				value2 = ((BigDecimal) row[2]).doubleValue();
			} else if (row[2] instanceof Number) {
				value2 = ((Number) row[2]).doubleValue();
			}
			// double value1 = ((BigDecimal) row[1]).doubleValue(); // Lấy số
			// lượng (value1)
			// double value2 = ((BigDecimal) row[2]).doubleValue(); // Lấy số
			// (value2)

			// Nếu đã tồn tại trong Map, cộng dồn giá trị
			if (groupedData.containsKey(name)) {
				double[] currentValues = groupedData.get(name);
				currentValues[0] += value1; // Cộng dồn số lượng
				currentValues[1] += value2; // Cộng dồn số
			} else {
				// Nếu chưa tồn tại, thêm mới
				groupedData.put(name, new double[] { value1, value2 });
			}
		}

		// Chuyển đổi từ Map sang List<Object[]>
		List<Object[]> result = new ArrayList<>();
		for (Map.Entry<String, double[]> entry : groupedData.entrySet()) {
			result.add(new Object[] { entry.getKey(), entry.getValue()[0], entry.getValue()[1] });
		}

		return result;

	}
}

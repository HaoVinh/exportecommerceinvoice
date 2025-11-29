package trong.lixco.com.util;

public class MyStringUtil {
	public static String replaceD(String searchText) {
		if (searchText != null) {
			if (searchText.contains("Đ")
					|| searchText.contains("đ")) {
//				searchText = searchText.replace("D", "_");
				searchText = searchText.replace("Đ", "_");
//				searchText = searchText.replace("d", "_");
				searchText = searchText.replace("đ", "_");
			}
			return searchText;
		} else {
			return null;
		}

	}
}

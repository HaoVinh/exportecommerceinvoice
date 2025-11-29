package trong.lixco.com.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.service.AccountAPIService;

//Tạo privatekey, public key
public class SecurityConfig {
	public static byte[] getPublicKey() throws IOException {
		InputStream in = SecurityConfig.class.getResourceAsStream("public.key");
		if (in != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			byte[] publicKeyBytes = baos.toByteArray();
			return publicKeyBytes;
		} else {
			System.out.println("Không tồn tại hoặc không thể đọc file public.key");
			return null;
		}
	}

	public static byte[] getPrivateKey() throws IOException {
		InputStream in = SecurityConfig.class.getResourceAsStream("private.key");
		if (in != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			byte[] publicKeyBytes = baos.toByteArray();
			return publicKeyBytes;
		} else {
			System.out.println("Không tồn tại hoặc không thể đọc file public.key");
			return null;
		}
	}

	public static void saveToken(AccountAPIService accountAPIService, String token, long timetoken, String chinhanh) {
		try {
			long idAcc = 0;
			if ("BD".equals(chinhanh)) {
				idAcc = 1;
			} else if ("BN".equals(chinhanh)) {
				idAcc = 2;
			} else if ("HCM".equals(chinhanh)) {
				idAcc = 3;
			} else if ("QLCL".equals(chinhanh)) {
				idAcc = 4;// chương trình QLCL
			}
			accountAPIService.updateToken(idAcc, token, timetoken);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static AccountAPI getTokenTime(AccountAPIService accountAPIService, String chinhanh) throws IOException {
		long idAcc = 0;
		if ("BD".equals(chinhanh)) {
			idAcc = 1;
		} else if ("BN".equals(chinhanh)) {
			idAcc = 2;
		} else if ("HCM".equals(chinhanh)) {
			idAcc = 3;
		} else if ("QLCL".equals(chinhanh)) {
			idAcc = 4;// chương trình QLCL
		}
		AccountAPI accountAPI = accountAPIService.findById(idAcc);

		return accountAPI;

	}

}

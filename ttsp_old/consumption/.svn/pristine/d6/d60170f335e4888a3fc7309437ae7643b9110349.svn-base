package trong.lixco.com.api;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.service.AccountAPIService;

//Tạo privatekey, public key
public class SecurityConfig {
//	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
//		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//		keyPairGen.initialize(2048);
//		KeyPair pair = keyPairGen.generateKeyPair();
//		PrivateKey privateKey1 = pair.getPrivate();
//		PublicKey publicKey1 = pair.getPublic();
//		try {
//
//			// ghi privatekey
//			try (FileOutputStream fos = new FileOutputStream(getPathkey() + "\\private.key")) {
//				fos.write(privateKey1.getEncoded());
//			}
//			// ghi publickey ra file
//			try (FileOutputStream fos = new FileOutputStream(getPathkey() + "\\public.key")) {
//				fos.write(publicKey1.getEncoded());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("Keys generated");
//
//	}
//
//	static final String FILENAME_PROPERTIES = "Params.properties";

//	public static String getPathkey() throws IOException {
//		Properties prop = new Properties();
//		InputStream in = SecurityConfig.class.getResourceAsStream(FILENAME_PROPERTIES);
//		prop.load(in);
//		String pathkey = prop.getProperty("pathkey");
//		return pathkey;
//	}
	public static byte[] getPublicKey() throws IOException {
		InputStream in = SecurityConfig.class.getResourceAsStream("public.key");
		if (in != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024]; // Bộ đệm 1KB (có thể điều chỉnh
											// kích thước theo nhu cầu)

			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			byte[] publicKeyBytes = baos.toByteArray();
//			System.out.println("Kich thuoc file: "+publicKeyBytes.length);
			return publicKeyBytes;
			// publicKeyBytes chứa dữ liệu đọc từ InputStream
		} else {
			System.out.println("Không tồn tại hoặc không thể đọc file public.key");
			// Xử lý trường hợp InputStream không tồn tại hoặc không thể đọc
			// được
			return null;
		}
	}
	public static byte[] getPrivateKey() throws IOException {
		InputStream in = SecurityConfig.class.getResourceAsStream("private.key");
		if (in != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024]; // Bộ đệm 1KB (có thể điều chỉnh
											// kích thước theo nhu cầu)

			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			byte[] publicKeyBytes = baos.toByteArray();
//			System.out.println("Kich thuoc file: "+publicKeyBytes.length);
			return publicKeyBytes;
			// publicKeyBytes chứa dữ liệu đọc từ InputStream
		} else {
			System.out.println("Không tồn tại hoặc không thể đọc file public.key");
			// Xử lý trường hợp InputStream không tồn tại hoặc không thể đọc
			// được
			return null;
		}
	}
	
	public static void saveToken(AccountAPIService accountAPIService, String token, long timetoken,String chinhanh) {
		try {
			long idAcc = 0;
			if ("BD".equals(chinhanh)) {
				idAcc = 1;
			} else if ("BN".equals(chinhanh)) {
				idAcc = 2;
			} else if ("HCM".equals(chinhanh)) {
				idAcc = 3;
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
		}
		AccountAPI accountAPI = accountAPIService.findById(idAcc);

		return accountAPI;

	}

}

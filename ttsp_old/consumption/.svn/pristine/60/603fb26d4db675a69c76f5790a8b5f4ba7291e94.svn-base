package trong.lixco.com.api;

import io.jsonwebtoken.Jwts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.service.AccountAPIService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Named
public class AccountHanlder {
	public static final int THOIGIANSONGTOKEN = 43200;// 30 ngay
	@Inject
	private AccountAPIService accountAPIService;
	private Gson gson = new Gson();

	public void processPost(String data, StringBuilder respContent, ResponseBuilder responseBuilder) {
		login(data, respContent, responseBuilder);
	}

	private void login(String data, StringBuilder respContent, ResponseBuilder responseBuilder) {
		if (data != null && !data.equals("")) {
			AccountAPI accountAPI = gson.fromJson(data, AccountAPI.class);
			if (accountAPI != null) {
				String userName = accountAPI.getUsername();
				String passWord = accountAPI.getPassword();
				if (userName != null && !userName.equals("") && passWord != null && !passWord.equals("")) {
					// kiểm tra username và password có đúng hay không
					boolean accountCheck = accountAPIService.findUSerPass(userName, passWord);
					if (accountCheck) {
						// đúng
						// tạo token cho user
						try {
							PrivateKey privateKey = getPrivateKey();
							Instant now = Instant.now();
							LocalDateTime current = LocalDateTime.now();
							Date date = Date.from(current.plusMinutes(THOIGIANSONGTOKEN).atZone(ZoneId.systemDefault())
									.toInstant());
							Map<String, Object> claims = new HashMap<String, Object>();
							claims.put("userName", userName);

							String jwtToken = Jwts.builder().setClaims(claims).setSubject("LIXCO")
									.setId(UUID.randomUUID().toString()).setIssuedAt(Date.from(now))
									.setExpiration(date).signWith(privateKey).compact();

							JsonObject jsonObject = new JsonObject();
							jsonObject.addProperty("access_token", jwtToken);
							jsonObject.addProperty("expires_in", date.getTime());
							jsonObject.addProperty("token_type", SecurityConstanst.TONKEN_PREFIX);
							responseBuilder.status(Response.Status.OK);
							respContent.append(jsonObject);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						// sai thong tin dang nhap
						String str = CommonService.FormatResponse(-1, MessagesAPI.USER_PASS_INCORRECT);
						respContent.append(str);
					}
				} else {
					// Ten dang nhap, mat khau khong dung dinh dang
					String str = CommonService.FormatResponse(-1, MessagesAPI.REQUEST_INVALID);
					respContent.append(str);
				}
			} else {
				// Loi dinh dang json ten dn, mat khau
				String str = CommonService.FormatResponse(-1, MessagesAPI.REQUEST_INVALID);
				respContent.append(str);
			}
		} else {
			// Khong co du lieu dang nhap
			String str = CommonService.FormatResponse(-1, MessagesAPI.REQUEST_INVALID);
			respContent.append(str);
		}
	}

	// get privatekey hệ thống
	private PrivateKey getPrivateKey() throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
		// đọc privatekey
		byte[] privateKeyBytes = SecurityConfig.getPrivateKey();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
		return privateKey;
	}
}

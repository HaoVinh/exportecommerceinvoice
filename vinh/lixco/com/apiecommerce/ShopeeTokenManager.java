
package vinh.lixco.com.apiecommerce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lixco.com.entity.ShopeeToken;
import lixco.com.service.ShopeeTokenService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Path("/tokenshopee")
public class ShopeeTokenManager {
	private static final Logger LOGGER = Logger.getLogger(ShopeeTokenManager.class.getName());
	private static final long PARTNER_ID = 2030160L;
	private static final String PARTNER_KEY = "shpk6b6e5843507275485253585072656c56587a6d6e4374477347647954714b";
	private static final long SHOP_ID = 773456943L;
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String BASE_URL = "https://partner.shopeemobile.com";

	@Inject
	private ShopeeTokenService shopeeTokenService;

	private ShopeeToken currentToken;

//	
//	public synchronized void generateTokenFromAuthCode(String code) {
//		try {
//			// Check if token already exists and auth code was used
//			if (currentToken != null && currentToken.isUsedAuthCode()) {
//				LOGGER.info("Authorization code already used. Using existing token.");
//				return;
//			}
//
//			long timestamp = System.currentTimeMillis() / 1000L;
//			String path = "/api/v2/auth/token/get";
//			String sign = generateHmacSHA256(PARTNER_ID + path + timestamp, PARTNER_KEY);
//
//			String urlStr = BASE_URL + path + "?partner_id=" + PARTNER_ID + "&timestamp=" + timestamp + "&sign=" + sign ;
//
//			Map<String, Object> payloadMap = new HashMap<>();
//			payloadMap.put("code", code);
//			payloadMap.put("shop_id", SHOP_ID);
//			payloadMap.put("partner_id", PARTNER_ID);
//			
//			String payload = MAPPER.writeValueAsString(payloadMap);
//
//			System.out.println("Request URL: " + urlStr);
//			System.out.println("Payload: " + payload);
//
//			JsonNode json = sendPostRequest(urlStr, payload);
//			String accessToken = json.path("access_token").asText();
//			String refreshToken = json.path("refresh_token").asText();
//			long expireIn = json.path("expire_in").asLong();
//			long expireTime = timestamp + expireIn - 60;
//
//			ShopeeToken token = new ShopeeToken();
//			token.setAccessToken(accessToken);
//			token.setRefreshToken(refreshToken);
//			token.setExpireTime(expireTime);
//			token.setUsedAuthCode(true);
//
//			shopeeTokenService.create(token);
//			this.currentToken = token;
//			System.out.println("✅ Token mới đã tạo từ code auth: " + accessToken);
//		} catch (Exception e) {
//			throw new RuntimeException("❌ Lỗi tạo token từ code auth: " + e.getMessage(), e);
//		}
//	}
//	
	
//	private String generateHmacSHA256(String data, String key) throws Exception {
//		Mac mac = Mac.getInstance("HmacSHA256");
//		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//		mac.init(secretKey);
//		byte[] hashBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
//		StringBuilder sb = new StringBuilder();
//		for (byte b : hashBytes) {
//			sb.append(String.format("%02x", b));
//		}
//		return sb.toString();
//	}
//
////
//	public static void main(String[] args) {
//		ShopeeTokenManager tokenManager = new ShopeeTokenManager();
//		String authCode = "4175496d666a6542746674777a526f72";
//		tokenManager.generateTokenFromAuthCode(authCode);
//
//	}
}
package vinh.lixco.com.apiecommerce;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lixco.com.entity.TikTokToken;
import lixco.com.service.TikTokTokenService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@ApplicationScoped
public class TikTokTokenManager {
    private static final Logger LOGGER = Logger.getLogger(TikTokTokenManager.class.getName());

    private static final String APP_KEY = "6gm54lge178ke";
    private static final String APP_SECRET = "f54f72703a82039bb14f11e225154739fdf5dd58";

    // API URL
    private static final String TOKEN_URL = "https://auth.tiktok-shops.com/api/v2/token/refresh";
    private static final String INITIAL_TOKEN_URL = "https://auth.tiktok-shops.com/api/v2/token/get";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Inject
    private TikTokTokenService tikTokTokenService;

    private TikTokToken currentToken;

    public synchronized String getAccessToken() {
        try {
            long now = System.currentTimeMillis() / 1000L;
            currentToken = tikTokTokenService.findById(1);

//            if (currentToken == null) {
//                initToken("ROW_SfT0ewAAAACcm1DPZz9DKY7JILx6Krt46WLaMizFTeokqFsRkgVQHYpprEPemjDUhs5ZTAlrXTuxeNwFfQBqMxTk-rKlBEbq");
//            }

            if (now >= currentToken.getExpireTime() || isTokenExpiringSoon()) {
                refreshToken();
            }

            return currentToken.getAccessToken();

        } catch (Exception e) {
            LOGGER.severe("Error getting TikTok AccessToken: " + e.getMessage());
            throw new RuntimeException("Failed to get AccessToken", e);
        }
    }

    /**
     * Refresh Access Token
     */
    private void refreshToken() {
        try {
            String refreshToken = currentToken.getRefreshToken();
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                throw new RuntimeException("RefreshToken is null. Cần initToken(authCode) lại.");
            }

            String urlStr = TOKEN_URL
                    + "?app_key=" + APP_KEY
                    + "&app_secret=" + APP_SECRET
                    + "&refresh_token=" + refreshToken
                    + "&grant_type=refresh_token";

            LOGGER.info("Attempting to refresh token with URL: " + urlStr);

            JsonNode json = sendGetRequest(urlStr);

            JsonNode data = json.path("data");
            if (json.path("code").asInt() != 0 || data.isMissingNode()) {
                throw new RuntimeException("TikTok API error: " + json.path("message").asText());
            }

            String newAccessToken = data.path("access_token").asText();
            String newRefreshToken = data.path("refresh_token").asText();
            long expireIn = data.path("access_token_expire_in").asLong();
            long newExpireTime = System.currentTimeMillis() / 1000L + expireIn - 60;

            TikTokToken existingToken = tikTokTokenService.findById(1);
            if (existingToken == null) {
                throw new RuntimeException("TikTokToken not found in DB.");
            }

            existingToken.setAccessToken(newAccessToken);
            existingToken.setRefreshToken(newRefreshToken);
            existingToken.setExpireTime(newExpireTime);
            tikTokTokenService.update(existingToken);
            currentToken = existingToken;

            LOGGER.info("Token refreshed successfully.");

        } catch (Exception e) {
            LOGGER.severe("Failed to refresh TikTok token: " + e.getMessage());
            throw new RuntimeException("Refresh token failed", e);
        }
    }

    /**
     * Check nếu token còn dưới 5 phút sẽ refresh
     */
    private boolean isTokenExpiringSoon() {
        long currentTime = System.currentTimeMillis() / 1000L;
        return currentToken != null && currentToken.getExpireTime() - currentTime < 300;
    }

    /**
     * Gửi GET request và parse JSON
     */
    private JsonNode sendGetRequest(String urlStr) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            InputStream inputStream = (responseCode == 200)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }

            return MAPPER.readTree(responseBuilder.toString());

        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}

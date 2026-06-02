package vinh.lixco.com.apiecommerce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lazada.lazop.api.LazopClient;
import com.lazada.lazop.api.LazopRequest;
import com.lazada.lazop.api.LazopResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SignatureUtil {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SIGN_METHOD = "sha256"; // Lazada yêu cầu chữ thường

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureUtil.class);

    /**
     * Tạo signature cho Lazada Open Platform (HMAC-SHA256, hex lowercase)
     *
     * @param apiPath   Ví dụ: "/auth/token/create" (bắt buộc, không được rỗng)
     * @param params    Map các tham số (app_key, code, timestamp, sign_method, ...)
     * @param appSecret App Secret của bạn
     * @return chữ ký hex lowercase hoặc null nếu lỗi
     */
    public static String generateSignature(String apiPath, Map<String, String> params, String appSecret) {
        if (apiPath == null || apiPath.isEmpty()) {
            LOGGER.error("apiPath is required (e.g. /auth/token/create)");
            return null;
        }

        // Sắp xếp key theo alphabet tăng dần
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // Build chuỗi ký: apiPath + key1value1 + key2value2 + ...
        StringBuilder stringToSign = new StringBuilder(apiPath);
        for (String key : keys) {
            String value = params.get(key);
            if (value != null && !value.isEmpty()) {
                stringToSign.append(key).append(value);
            }
        }

        String base = stringToSign.toString();
        LOGGER.debug("String to sign: {}", base);

        return getSignature(base, appSecret);
    }

    /**
     * Hàm HMAC-SHA256 đã có của bạn (giữ nguyên, vì đúng)
     * base = chuỗi cần ký
     * secret = appSecret
     */
    public static String getSignature(String base, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            sha256Hmac.init(secretKey);
            byte[] hash = sha256Hmac.doFinal(base.getBytes(StandardCharsets.UTF_8));
            return byteArrayToHexString(hash);
        } catch (Exception e) {
            LOGGER.error("Failed to generate signature", e);
            return null;
        }
    }

    /**
     * Hex encode lowercase (đã có của bạn, chuẩn cho Lazada)
     */
    private static String byteArrayToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String stmp = Integer.toHexString(b & 0xFF);
            if (stmp.length() == 1) {
                sb.append('0');
            }
            sb.append(stmp);
        }
        return sb.toString(); // lowercase
    }

    // Ví dụ sử dụng trong main (cho /auth/token/create)
    public static void main(String[] args) {
    	// Thay bằng giá trị thật của bạn
        String appKey    = "133487";                              // App Key từ Lazada Open Platform
        String appSecret = "RZTQdjz5VUdnpQ81koTwuQ0lrBJlnepC";   // App Secret
        String authCode  = "0_133487_rpeG3Y2dtbX0gA55LzALUdJk1118";         // Code từ redirect callback (bắt đầu bằng 0_...)

        // Domain đặc biệt cho auth token (không dùng api.lazada.vn/rest)
        String serverUrl = "https://auth.lazada.com/rest";

        try {
            // Khởi tạo client với serverUrl, appKey, appSecret
            LazopClient client = new LazopClient(serverUrl, appKey, appSecret);

            // Tạo request cho API lấy token
            LazopRequest request = new LazopRequest("/auth/token/create");

            // Thêm tham số bắt buộc: code
            request.addApiParameter("code", authCode);


            // Gọi API
            LazopResponse response = client.execute(request);

            // In ra response body (JSON chứa access_token, refresh_token, expires_in, ...)
            System.out.println("Response Body:");
            System.out.println(response.getBody());

            // Parse JSON nếu cần (dùng Gson hoặc Jackson)
            // Ví dụ: {"access_token":"50000xxx...","refresh_token":"50000yyy...","expires_in":604800,...}

        } catch (Exception e) {
            System.err.println("Lỗi khi gọi API: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
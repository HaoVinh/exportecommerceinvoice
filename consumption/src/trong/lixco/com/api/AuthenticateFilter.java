package trong.lixco.com.api;

import io.jsonwebtoken.Jwts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/api/data/*")
public class AuthenticateFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String header = request.getHeader(SecurityConstanst.HEADER_STRING);
		if (header == null || !header.startsWith(SecurityConstanst.TONKEN_PREFIX)) {
			response.setStatus(401);
			String content = CommonModel.toJson(-1, "Tonken invalid", "");
			CommonModel.out(content, response);
			return;
		}
		String tonken = request.getHeader(SecurityConstanst.HEADER_STRING).replace(SecurityConstanst.TONKEN_PREFIX, "")
				.trim();
		try {
			// lấy token để kiểm tra
			PublicKey publicKey = getPublicKey();
			Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(tonken);
		} catch (Exception e) {
			response.setStatus(401);
			String content = CommonModel.toJson(-1, "Tonken invalid: " + e.getMessage(), "");
			response.setHeader("Content-Type", "application/json; charset=utf-8");
			CommonModel.out(content, response);
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
//	// get publickey hệ thống
//	private PublicKey getPublicKey()
//			throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
//		// đọc publickey
//		File publicKeyFile = new File(SecurityConfig.getPathkey() + "\\public.key");
//		byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
//		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
//		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
//		return publicKey;
//	}
	// get publickey hệ thống
	private PublicKey getPublicKey() throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
		// đọc publickey
		byte[] publicKeyBytes = SecurityConfig.getPublicKey();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		return publicKey;
	}
}

package trong.lixco.com.webservice;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@javax.servlet.annotation.WebFilter("/apitest/*")
public class WebFilterAPI implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException,
			IOException {
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		boolean status = false;
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Map<String, String> map = new HashMap<String, String>();
		Enumeration headerNames = request.getHeaderNames();

		String queryString = request.getRequestURL().toString();
		
		if (queryString.contains("login")) {
			chain.doFilter(req, res);
		} else {
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				if ("token".equals(key)) {
					
				}

			}
			if (status) {
				chain.doFilter(req, res);
			} else {
				try {
//					String mes = CommonModel.toJson(-1, "Khong xac thuc duoc, vui long dang nhap lai.");
//					CommonModel.out(mes, response);
				} catch (Exception e) {

				}
			}
		}

	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
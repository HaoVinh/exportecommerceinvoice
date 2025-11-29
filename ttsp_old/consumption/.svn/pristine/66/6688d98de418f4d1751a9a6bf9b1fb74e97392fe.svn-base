package trong.lixco.com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class MyReaderHTML {
	    public static String readHTMLFromResource(String resourcePath) {
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        ExternalContext externalContext = facesContext.getExternalContext();
	        try {
	            // Mở tệp tài nguyên từ resourcePath
	            InputStream inputStream = externalContext.getResourceAsStream(resourcePath);
	            if (inputStream != null) {
	                // Đọc dữ liệu từ inputStream thành chuỗi
	                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8));
	                StringBuilder htmlContent = new StringBuilder();
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    htmlContent.append(line);
	                }
	                reader.close();
	                return htmlContent.toString();
	            } else {
	                System.out.println("Không thể tìm thấy tài nguyên: " + resourcePath);
	                return null;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    public static String readHTMLFromResource(ServletContext servletContext,String resourcePath) {
	        try {
	            // Mở tệp tài nguyên từ resourcePath
	            InputStream inputStream = servletContext.getResourceAsStream(resourcePath);
	            if (inputStream != null) {
	                // Đọc dữ liệu từ inputStream thành chuỗi
	                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8));
	                StringBuilder htmlContent = new StringBuilder();
	                String line;
	                while ((line = reader.readLine()) != null) {
	                    htmlContent.append(line);
	                }
	                reader.close();
	                return htmlContent.toString();
	            } else {
	                System.out.println("Không thể tìm thấy tài nguyên: " + resourcePath);
	                return null;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


}

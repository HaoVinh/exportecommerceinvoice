package trong.lixco.com.api;

import java.util.Set;

import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class AppConfig extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new java.util.HashSet<>();
		addRestResourceClasses(resources);
		return resources;
	}

	private void addRestResourceClasses(Set<Class<?>> resources) {
		// add all resources classes
		resources.add(AccountServlet.class);
		resources.add(DongBoServlet.class);
		resources.add(SanPhamServlet.class);
		resources.add(DuBaoTonServlet.class);
		resources.add(LuanChuyenServlet.class);
		resources.add(KhachHangServlet.class);
		resources.add(SanPhamServlet.class);
		resources.add(InvoiceServlet.class);
		resources.add(YeuCauKiemTraHangHoaServlet.class);
		resources.add(InvoiceTempServlet.class);
		
	}
}

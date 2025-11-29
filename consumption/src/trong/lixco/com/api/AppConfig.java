package trong.lixco.com.api;

import java.util.Set;

import javax.ws.rs.core.Application;

//import vinh.lixco.com.apiecommerce.LazadaAPIServlet;
//import vinh.lixco.com.apiecommerce.TikTokAPIServlet;
import vinh.lixco.com.apiecommerce.LazadaAPIServlet;
import vinh.lixco.com.apiecommerce.ShopeeAPIServlet;
import vinh.lixco.com.apiecommerce.TikTokAPIServlet;

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
		resources.add(SanPhamServlet2.class);
		resources.add(InvoiceServlet.class);
		resources.add(YeuCauKiemTraHangHoaServlet.class);
		resources.add(XacNhanKiemTraServlet.class);
		resources.add(TonKhoServlet.class);
		resources.add(GoodsReceiptNoteServlet.class);
//		
		resources.add(ProductServlet.class);
		resources.add(SLBH_SXServlet.class);
		resources.add(KeHoachGiaoHangServlet.class);
		resources.add(InvoiceTempServlet.class);//thêm
		resources.add(LoginServlet.class);
		resources.add(HoaDonServlet.class);
		resources.add(LazadaAPIServlet.class);
		resources.add(TikTokAPIServlet.class);
		resources.add(ShopeeAPIServlet.class);
	}
}

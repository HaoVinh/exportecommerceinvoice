package lixco.com.interfaces;

import java.util.List;

import lixco.com.entity.Customer;
import lixco.com.reportInfo.BangKeHoaDon;
import lixco.com.reportInfo.ChiTietCongNo;
import lixco.com.reportInfo.CongNo;
import lixco.com.reportInfo.CongNoToiHanThanhToan;
import lixco.com.reportInfo.SoDuCongNo;
import lixco.com.reportInfo.TongKetTheoSanPham;
import lixco.com.reportInfo.TongKetTieuThuKhachHang;

public interface IReportKHService {
	public int inthongketheosanpham(String json,List<TongKetTheoSanPham> list,List<Customer> customers);
	public int inthongketheosanpham2(String json,List<TongKetTheoSanPham> list,List<Customer> customers);
	public int inthongketheokhachhangdvt(String json, List<TongKetTieuThuKhachHang> list,List<Customer> customers);
	public int bangkehoadon(String json, List<BangKeHoaDon> list,List<Customer> customers) ;
	public int congno(String json, List<CongNo> list,List<Customer> customers) ;
	public int chitietcongno(String json, List<ChiTietCongNo> list,List<Customer> customers);
	public int congnotoihanthanhtoan(String json, List<CongNoToiHanThanhToan> list,List<Customer> customers);
	public List<SoDuCongNo> soducongno(String json,long idCus);
	public int soducongno2(String json, List<SoDuCongNo> list,List<Customer> customers);
	}

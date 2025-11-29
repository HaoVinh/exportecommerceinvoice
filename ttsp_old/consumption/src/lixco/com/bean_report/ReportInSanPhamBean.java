package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import lixco.com.common.FormatHandler;
import lixco.com.entity.Area;
import lixco.com.entity.Car;
import lixco.com.entity.CarType;
import lixco.com.entity.City;
import lixco.com.entity.Contract;
import lixco.com.entity.Customer;
import lixco.com.entity.IECategories;
import lixco.com.entity.PaymentMethod;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IAreaService;
import lixco.com.interfaces.ICarService;
import lixco.com.interfaces.ICarTypeService;
import lixco.com.interfaces.ICityService;
import lixco.com.interfaces.IContractService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IPaymentMethodService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.HanThanhToan;
import lixco.com.reportInfo.LoaiBaoCaoInSanPham;
import trong.lixco.com.bean.AbstractBean;

@Named
@ViewScoped
public class ReportInSanPhamBean extends AbstractBean {
	private static final long serialVersionUID = -8870786800128625218L;
	@Inject
	private Logger logger;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IProductService productService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IIECategoriesService iECategoriesService;
	@Inject
	private IReportService reportService;
	@Inject
	private IContractService contractService;
	@Inject
	private ICityService cityService;
	@Inject
	private ICarService carService;
	@Inject
	private ICarTypeService carTypeService;
	@Inject
	private IAreaService areaService;
	@Inject
	private IPaymentMethodService paymentMethodService;
	private Customer customerSearch;
	private Product productSearch;
	private IECategories iECategoriesSearch;
	private List<IECategories> listIECategories;
	private ProductType productTypeSearch;
	private List<ProductType> listProductType;
	private Contract contractSearch;
	private City citySearch;
	private PaymentMethod paymentMethodSearch;
	private List<PaymentMethod> listPaymentMethod;
	private int typep;//xuất khẩu hay nội địa
	private Area areaSearch;
	private List<Area> listArea;
	private Car carSearch;
	private CarType carTypeSearch;
	private List<CarType> listCarType;
	private boolean reUpdateReponsity;
	private Date fromDateSearch;
	private Date toDateSearch;
	private List<HanThanhToan> listHanThanhToan;
	private List<LoaiBaoCaoInSanPham> listLoaiBaoCaoInSanPham;
	private LoaiBaoCaoInSanPham loaiBaoCaoInSanPhamSelect;
	private String title;
	@Override
	protected void initItem() {
		try{
			listArea=new ArrayList<>();
			areaService.selectAll(listArea);
			listPaymentMethod=new ArrayList<>();
			paymentMethodService.selectAll(listPaymentMethod);
			listIECategories=new ArrayList<>();
			iECategoriesService.selectAll(listIECategories);
			listCarType=new ArrayList<>();
			carTypeService.selectAll(listCarType);
			listProductType=new ArrayList<>();
			productTypeService.selectAll(listProductType);
			initListHanThanhToan();
			initListLoaiBaoCaoInSanPham();
			title="Báo cáo in sản phẩm";
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.initItem:"+e.getMessage(),e);
		}
	}
	
	public void initListLoaiBaoCaoInSanPham(){
		try{
			listLoaiBaoCaoInSanPham=new ArrayList<>();
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(1, "1.TK xuất khẩu theo sản phẩm"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(2, "2.TK xuất theo k/hàng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(3, "3.TK xuất theo thành phố"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(4, "4.TK xuất theo Brand Name"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(5, "5.Bảng kê hóa đơn"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(6, "6.Công nợ"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(7, "7.Chi tiết công nợ"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(8, "8.Thống kê theo tháng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(9, "9.Tồn kho tháng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(10, "10.Tồn kho sản phẩm"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(11, "11.Thẻ kho sản phẩm"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(12, "12.Doanh thu KH theo tháng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(13, "13.Bảng kê chi tiết hóa đơn"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(14, "14.Nhập xuất ngày"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(15, "15.Bảng kê lượng vận chuyển"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(16, "16.Bảng kê chi phí vc xe lix thuê"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(17, "17.Bảng kê chi tiết CP thuê xe v/c"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(18, "18.Xem doanh thu và sản lượng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(19, "19.Bảng kê chi phí xe khách hàng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(20, "20.Bảng kê đơn hàng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(21, "21.Số liệu họp giao ban"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(22, "22.In chi phí vận chuyển"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(23, "23.Báo cáo lever"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(24, "24.Lấy số liệu BC tổng hợp"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(25,"25.TK sp chưa giao hàng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(28,"25_1.TK sp chưa giao hàng"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(26, "26.In lũy kế nhập xuất"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(27, "27.TK khách hàng xuất theo ngày"));
			listLoaiBaoCaoInSanPham.add(new LoaiBaoCaoInSanPham(29, "29.Hàng chậm luân chuyển"));
		}catch (Exception e) {
		}
	}
	public String includeViewPage(){
		PrimeFaces current=PrimeFaces.current();
		try{
			if(loaiBaoCaoInSanPhamSelect!=null){
				int code=loaiBaoCaoInSanPhamSelect.getId();
				current.executeScript("PF('tabViewVar').select(1);");
				title=loaiBaoCaoInSanPhamSelect.getName();
				switch (code) {
				case 1:
					return "/pages/baocao/tk_xuat_theo_san_pham.xhtml";
				case 2:
					return "/pages/baocao/tk_xuat_theo_khach_hang.xhtml";
				case 3:
					return "/pages/baocao/tk_xuat_theo_thanh_pho.xhtml";
				case 4:
					return "/pages/baocao/tk_xuat_theo_brand_name.xhtml";
				case 5:
					return "/pages/baocao/bang_ke_hoa_don.xhtml";
				case 6:
					return "/pages/baocao/congno.xhtml";
				case 7:
					return "/pages/baocao/chi_tiet_cong_no.xhtml";
				case 8:
					return "/pages/baocao/tk_theo_thang.xhtml";
				case 9:
					return "/pages/baocao/ton_kho_thang.xhtml";
				case 10:
					return "/pages/baocao/ton_kho_san_pham.xhtml";
				case 11:
					return "/pages/baocao/the_kho_san_pham.xhtml";
				case 12:
					return "/pages/baocao/doanh_thu_khach_hang_theo_thang.xhtml";
				case 13:
					return "/pages/baocao/bk_chi_tiet_hoa_don.xhtml";
				case 14:
					return "/pages/baocao/in_nhap_xuat_ngay.xhtml";
				case 15:
					return "/pages/baocao/bang_ke_khoi_luong_van_chuyen.xhtml";
				case 21:
					return "/pages/baocao/lay_so_lieu_bao_cao_hgb.xhtml";
				case 23:
					return "/pages/baocao/bao_cao_lever.xhtml";
				case 24:
					return "/pages/baocao/lay_so_lieu_nc_tong_hop.xhtml";
				case 25:
					return "/pages/baocao/thong_ke_san_pham_chua_giao.xhtml";
				case 26:
					return "/pages/baocao/luy_ke_nhap_xuat.xhtml";
				case 28:
					return "/pages/baocao/bang_ke_san_pham_chua_giao.xhtml";
				case 29:
					return "/pages/baocao/hang_cham_luan_chuyen.xhtml";
				default:
					return "/pages/baocao/default.xhtml";
				}
			}
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.includeViewPage:"+e.getMessage(),e);
		}
		return "/pages/baocao/default.xhtml";
	}
	
	private void initListHanThanhToan(){
		try{
			listHanThanhToan=new ArrayList<>();
			listHanThanhToan.add(new HanThanhToan("dtt","Đã Thanh toán"));
			listHanThanhToan.add(new HanThanhToan("cdhtt","Chưa đến hạn thanh toán"));
			listHanThanhToan.add(new HanThanhToan("dkhtt","Đến kỳ hạn thanh toán"));
		}catch (Exception e) {
		}
	}
	public List<City> completeCity(String text){
		try{
			List<City> list=new ArrayList<>();
			cityService.complete(FormatHandler.getInstance().converViToEn(text), areaSearch.getId(), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.completeCity:"+e.getMessage(),e);
		}
		return null;
	}
	public List<Product> completeProduct(String text){
		try{
			List<Product> list=new ArrayList<Product>();
			productService.completeByProductType(FormatHandler.getInstance().converViToEn(text),productTypeSearch==null ?0 :productTypeSearch.getId(), list);
			return list;
		}catch (Exception e) {
			logger.error("ReportInSanPhamBean.completeProduct:"+e.getMessage(),e);
		}
		return null;
	}
	@Override
	protected Logger getLogger() {
		return logger;
	}
	
	public Customer getCustomerSearch() {
		return customerSearch;
	}

	public void setCustomerSearch(Customer customerSearch) {
		this.customerSearch = customerSearch;
	}

	public Product getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
	}

	public IECategories getiECategoriesSearch() {
		return iECategoriesSearch;
	}

	public void setiECategoriesSearch(IECategories iECategoriesSearch) {
		this.iECategoriesSearch = iECategoriesSearch;
	}

	public List<IECategories> getListIECategories() {
		return listIECategories;
	}

	public void setListIECategories(List<IECategories> listIECategories) {
		this.listIECategories = listIECategories;
	}

	public ProductType getProductTypeSearch() {
		return productTypeSearch;
	}

	public void setProductTypeSearch(ProductType productTypeSearch) {
		this.productTypeSearch = productTypeSearch;
	}

	public Contract getContractSearch() {
		return contractSearch;
	}

	public void setContractSearch(Contract contractSearch) {
		this.contractSearch = contractSearch;
	}

	public City getCitySearch() {
		return citySearch;
	}

	public void setCitySearch(City citySearch) {
		this.citySearch = citySearch;
	}

	public PaymentMethod getPaymentMethodSearch() {
		return paymentMethodSearch;
	}

	public void setPaymentMethodSearch(PaymentMethod paymentMethodSearch) {
		this.paymentMethodSearch = paymentMethodSearch;
	}

	public List<PaymentMethod> getListPaymentMethod() {
		return listPaymentMethod;
	}

	public void setListPaymentMethod(List<PaymentMethod> listPaymentMethod) {
		this.listPaymentMethod = listPaymentMethod;
	}


	public int getTypep() {
		return typep;
	}
	public void setTypep(int typep) {
		this.typep = typep;
	}
	public Area getAreaSearch() {
		return areaSearch;
	}

	public void setAreaSearch(Area areaSearch) {
		this.areaSearch = areaSearch;
	}

	public Car getCarSearch() {
		return carSearch;
	}

	public void setCarSearch(Car carSearch) {
		this.carSearch = carSearch;
	}

	public CarType getCarTypeSearch() {
		return carTypeSearch;
	}

	public void setCarTypeSearch(CarType carTypeSearch) {
		this.carTypeSearch = carTypeSearch;
	}

	public List<CarType> getListCarType() {
		return listCarType;
	}

	public void setListCarType(List<CarType> listCarType) {
		this.listCarType = listCarType;
	}

	public boolean isReUpdateReponsity() {
		return reUpdateReponsity;
	}

	public void setReUpdateReponsity(boolean reUpdateReponsity) {
		this.reUpdateReponsity = reUpdateReponsity;
	}
	public List<Area> getListArea() {
		return listArea;
	}
	public void setListArea(List<Area> listArea) {
		this.listArea = listArea;
	}
	public Date getFromDateSearch() {
		return fromDateSearch;
	}
	public void setFromDateSearch(Date fromDateSearch) {
		this.fromDateSearch = fromDateSearch;
	}
	public Date getToDateSearch() {
		return toDateSearch;
	}
	public void setToDateSearch(Date toDateSearch) {
		this.toDateSearch = toDateSearch;
	}
	public List<ProductType> getListProductType() {
		return listProductType;
	}
	public void setListProductType(List<ProductType> listProductType) {
		this.listProductType = listProductType;
	}
	public List<HanThanhToan> getListHanThanhToan() {
		return listHanThanhToan;
	}
	public void setListHanThanhToan(List<HanThanhToan> listHanThanhToan) {
		this.listHanThanhToan = listHanThanhToan;
	}
	public List<LoaiBaoCaoInSanPham> getListLoaiBaoCaoInSanPham() {
		return listLoaiBaoCaoInSanPham;
	}
	public void setListLoaiBaoCaoInSanPham(List<LoaiBaoCaoInSanPham> listLoaiBaoCaoInSanPham) {
		this.listLoaiBaoCaoInSanPham = listLoaiBaoCaoInSanPham;
	}
	public LoaiBaoCaoInSanPham getLoaiBaoCaoInSanPhamSelect() {
		return loaiBaoCaoInSanPhamSelect;
	}
	public void setLoaiBaoCaoInSanPhamSelect(LoaiBaoCaoInSanPham loaiBaoCaoInSanPhamSelect) {
		this.loaiBaoCaoInSanPhamSelect = loaiBaoCaoInSanPhamSelect;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}

package lixco.com.bean_report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Product;
import lixco.com.entity.ProductType;
import lixco.com.interfaces.IGoodsReceiptNoteService;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IProductTypeService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.HangChamLuanChuyen;
import lixco.com.reportInfo.TonKhoSanPham;
import lixco.com.reportInfo.TonKhoThang;
import lixco.com.reqInfo.ChiTietDuBaoTon;
import lixco.com.reqInfo.InfoInventory;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Call;
import okhttp3.Response;

import org.jboss.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.api.DefinedName;
import trong.lixco.com.api.SecurityConfig;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.StaticPath;
import trong.lixco.com.util.ToolReport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Named
@ViewScoped
public class ReportHangChamLuanChuyenBNBean extends AbstractBean {
	private static final long serialVersionUID = -8870786800128625218L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private IProductService productService;
	@Inject
	private IProductTypeService productTypeService;
	@Inject
	private IInventoryService inventoryService;
	@Getter
	@Setter
	private Date ngaytinhton;
	private Product productSearch;
	private ProductType productTypeSearch;
	private List<ProductType> listProductType;
	private int typep;
	private List<TonKhoThang> listTonKhoThang;
	private List<TonKhoThang> listTonKhoThangFilter;
	private FormatHandler formatHandler;
	private InfoInventory infoInventory;
	private String title = "";

	@Inject
	AccountAPIService accountAPIService;

	@Override
	protected void initItem() {
		try {
			ngaytinhton = new Date();
			listProductType = new ArrayList<>();
			productTypeService.selectAll(listProductType);
			formatHandler = FormatHandler.getInstance();
		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.initItem:" + e.getMessage(), e);
		}
	}

	@Inject
	IGoodsReceiptNoteService goodsReceiptNoteService;

	public void hangChamLuanChuyenExcel() {
		try {
			JsonObject json = new JsonObject();
			json.addProperty("ngaytinhton", MyUtil.chuyensangStrApi(ngaytinhton));
			json.addProperty("product_type_id", productTypeSearch == null ? 0 : productTypeSearch.getId());
			json.addProperty("product_id", productSearch == null ? 0 : productSearch.getId());

			int month = ToolTimeCustomer.getMonthM(ngaytinhton);
			int year = ToolTimeCustomer.getYearM(ngaytinhton);

			List<HangChamLuanChuyen> hangChamLuanChuyens = new ArrayList<HangChamLuanChuyen>();
			/*
			 * lay ton kho thang
			 */
			List<TonKhoSanPham> tonkhothangs = inventoryService.tonkhongay(ngaytinhton, productSearch == null ? 0
					: productSearch.getId(), productTypeSearch == null ? 0 : productTypeSearch.getId());
			Set<String> maspparams = new HashSet<String>();
			for (int i = 0; i < tonkhothangs.size(); i++) {
				if (tonkhothangs.get(i).getKg_closing_balance() != 0) {
					HangChamLuanChuyen hcLuanChuyen = new HangChamLuanChuyen();
					hcLuanChuyen.setKho("BN");
					hcLuanChuyen.setLoaisp(tonkhothangs.get(i).getProduct_type_name());
					hcLuanChuyen.setIdsp(tonkhothangs.get(i).getProduct_id());
					hcLuanChuyen.setMasp(tonkhothangs.get(i).getProduct_code());
					hcLuanChuyen.setTensp(tonkhothangs.get(i).getProduct_name());
					hcLuanChuyen.setNam(year);
					hcLuanChuyen.setThang(month);
					hcLuanChuyen.setSlton(tonkhothangs.get(i).getKg_closing_balance());
					maspparams.add(hcLuanChuyen.getMasp());
					hangChamLuanChuyens.add(hcLuanChuyen);
				}
			}

			/*
			 * Cai dat ngay sx gan nhat
			 */
			List<String> masps = new ArrayList<String>(maspparams);
			List<Object[]> ngaysxgannhats = goodsReceiptNoteService.ngaysxgannhatBN(masps);
			List<HangChamLuanChuyen> hangChamLuanChuyenDeletes = new ArrayList<HangChamLuanChuyen>();
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < hangChamLuanChuyens.size(); i++) {
				for (int j = 0; j < ngaysxgannhats.size(); j++) {
					if (hangChamLuanChuyens.get(i).getMasp().equals(Objects.toString(ngaysxgannhats.get(j)[0], ""))) {
						String ngaysxStr = (String) ngaysxgannhats.get(j)[1];
						Date ngaysx = null;
						try {
							ngaysx=sf.parse(ngaysxStr);
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (hangChamLuanChuyens.get(i).getNgaysxgannhat() == null) {
							hangChamLuanChuyens.get(i).setNgaysxgannhat(ngaysx);
						} else {
							if (hangChamLuanChuyens.get(i).getNgaysxgannhat().before(ngaysx)) {
								hangChamLuanChuyens.get(i).setNgaysxgannhat(ngaysx);
							} else {
								ngaysxgannhats.get(j)[1] = hangChamLuanChuyens.get(i).getNgaysxgannhat();
							}
						}
					}
				}
				if (hangChamLuanChuyens.get(i).getNgaysxgannhat() == null) {
					for (int j = 0; j < hangChamLuanChuyens.size(); j++) {
						if (hangChamLuanChuyens.get(i).getMasp().equals(hangChamLuanChuyens.get(j).getMasp())) {
							if (hangChamLuanChuyens.get(j).getNgaysxgannhat() != null) {
								hangChamLuanChuyens.get(i).setNgaysxgannhat(
										hangChamLuanChuyens.get(j).getNgaysxgannhat());
							}
						}
					}
				}
				if (hangChamLuanChuyens.get(i).getSlton() == 0) {
					hangChamLuanChuyenDeletes.add(hangChamLuanChuyens.get(i));
				}
			}
			hangChamLuanChuyens.removeAll(hangChamLuanChuyenDeletes);
			// xuat du lieu
			if (hangChamLuanChuyens.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "MÃ SP", "SẢN PHẨM", "SL TỒN CUỐI(Kg)", "KHO", "SỐ THÁNG ĐÃ SX", "NGÀY SX GẦN NHẤT",
						"LOẠI SP" };
				results.add(title);

				LocalDate date1 = new LocalDate(ngaytinhton);
				for (int i = 0; i < hangChamLuanChuyens.size(); i++) {
					int numberOfMonths = 0;
					if (hangChamLuanChuyens.get(i).getNgaysxgannhat() != null) {
						LocalDate date2 = new LocalDate(hangChamLuanChuyens.get(i).getNgaysxgannhat());
						Months monthsBetween = Months.monthsBetween(date1, date2);
						numberOfMonths = monthsBetween.getMonths();

					}
					Object[] row = { hangChamLuanChuyens.get(i).getMasp(), hangChamLuanChuyens.get(i).getTensp(),
							hangChamLuanChuyens.get(i).getSlton(), hangChamLuanChuyens.get(i).getKho(),
							Math.abs(numberOfMonths), hangChamLuanChuyens.get(i).getNgaysxgannhat(),
							hangChamLuanChuyens.get(i).getLoaisp() };
					results.add(row);
				}

				StringBuilder title2 = new StringBuilder();
				title2.append("HangChamLuanChuyen_" + MyUtil.chuyensangddMMyyStr(ngaytinhton));
				ToolReport.printReportExcelRaw(results, title2.toString());
			} else {
				warning("Không có dữ liệu");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ReportTonKhoThangBean.search:" + e.getMessage(), e);
		}
	}

	

	public List<Product> completeProduct(String text) {
		try {
			List<Product> list = new ArrayList<>();
			productService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("ReportTonKhoThangBean.completeProduct:" + e.getMessage(), e);
		}
		return null;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public Product getProductSearch() {
		return productSearch;
	}

	public void setProductSearch(Product productSearch) {
		this.productSearch = productSearch;
	}

	public ProductType getProductTypeSearch() {
		return productTypeSearch;
	}

	public void setProductTypeSearch(ProductType productTypeSearch) {
		this.productTypeSearch = productTypeSearch;
	}

	public List<ProductType> getListProductType() {
		return listProductType;
	}

	public void setListProductType(List<ProductType> listProductType) {
		this.listProductType = listProductType;
	}

	public int getTypep() {
		return typep;
	}

	public void setTypep(int typep) {
		this.typep = typep;
	}

	public List<TonKhoThang> getListTonKhoThang() {
		return listTonKhoThang;
	}

	public void setListTonKhoThang(List<TonKhoThang> listTonKhoThang) {
		this.listTonKhoThang = listTonKhoThang;
	}

	public List<TonKhoThang> getListTonKhoThangFilter() {
		return listTonKhoThangFilter;
	}

	public void setListTonKhoThangFilter(List<TonKhoThang> listTonKhoThangFilter) {
		this.listTonKhoThangFilter = listTonKhoThangFilter;
	}

	public FormatHandler getFormatHandler() {
		return formatHandler;
	}

	public void setFormatHandler(FormatHandler formatHandler) {
		this.formatHandler = formatHandler;
	}

	public InfoInventory getInfoInventory() {
		return infoInventory;
	}

	public void setInfoInventory(InfoInventory infoInventory) {
		this.infoInventory = infoInventory;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

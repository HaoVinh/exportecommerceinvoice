package lixco.com.bean_report;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.commom_ejb.MyMath;
import lixco.com.common.FormatHandler;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Car;
import lixco.com.entity.CarType;
import lixco.com.entity.Customer;
import lixco.com.entity.Product;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IInventoryService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.BangKeKhoiLuongVanChuyen;
import lixco.com.reportInfo.BangKeSanPhamChuaGiao;
import lixco.com.reqInfo.ExpectedInventoryReqInfo;
import lixco.com.service.CustomerService;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.ToolReport;

import com.google.gson.JsonObject;

@Named
@ViewScoped
public class ReportThongKeSanPhamChuaGiaoBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private IReportService reportService;
	@Inject
	private IProductService productService;
	@Inject
	private ICustomerService customerService;
	@Getter
	@Setter
	private Date fromDateSearch;
	@Getter
	@Setter
	private Date toDateSearch;

	@Override
	protected void initItem() {
		try {
			int month = ToolTimeCustomer.getMonthCurrent();
			int year = ToolTimeCustomer.getYearCurrent();
			fromDateSearch = ToolTimeCustomer.getDateMinCustomer(month, year);
			toDateSearch = ToolTimeCustomer.getDateMaxCustomer(month, year);
		} catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.initItem:" + e.getMessage(), e);
		}
	}

	public List<Customer> completeCustomer(String text) {
		try {
			List<Customer> list = new ArrayList<>();
			customerService.complete(FormatHandler.getInstance().converViToEn(text), list);
			return list;
		} catch (Exception e) {
			logger.error("BangKeKhoiLuongVanChuyenBean.completeCustomer:" + e.getMessage(), e);
		}
		return null;
	}

	@Inject
	private IInventoryService inventoryService;

	public void reportSPchuaGiaoExcel() {
		Notify notify = new Notify(FacesContext.getCurrentInstance());
		try {
			List<Object[]> list = new ArrayList<>();
			JsonObject json = new JsonObject();
			json.addProperty("from_date", ToolTimeCustomer.convertDateToString(fromDateSearch, "yyyy-MM-dd"));
			json.addProperty("to_date", ToolTimeCustomer.convertDateToString(toDateSearch, "yyyy-MM-dd"));
			inventoryService.getSPChuaGiao(JsonParserUtil.getGson().toJson(json), list);
			if (list.size() > 0) {
				List<Object[]> listResult = new ArrayList<>();
				Object[] title = { "Mã KH", "Tên KH", "Mã SP", "Tên SP", "Số Lượng chưa giao(thùng)","PO" };
				listResult.add(title);
				for (Object[] p : list) {

//					if ("K0004".equals(Objects.toString(p[1], null))&&"HT016".equals(Objects.toString(p[8], null))){
//						
//						double sldh=Double.parseDouble(Objects.toString(p[3], "0"));
//						double sldhdx= Double.parseDouble(Objects
//								.toString(p[4], "0"));
//						double sldhkm=Double.parseDouble(Objects.toString(p[5], "0"));
//						double sldhkmdx= Double.parseDouble(Objects
//								.toString(p[6], "0"));
//						System.out.println(sldh+" \t"+sldhdx+"\t"+sldhkm+"\t"+sldhkmdx);
//						System.out.println(p.toString());
//					}
					

					double slchuagiao = (Double.parseDouble(Objects.toString(p[3], "0")) - Double.parseDouble(Objects
							.toString(p[4], "0")))
							+ (Double.parseDouble(Objects.toString(p[5], "0")) - Double.parseDouble(Objects.toString(
									p[6], "0")));
					if(slchuagiao!=0){
					Object[] row = { Objects.toString(p[8], null), Objects.toString(p[9], null),
							Objects.toString(p[1], null), Objects.toString(p[2], null), MyMath.round(slchuagiao),Objects.toString(p[10], null) };
					listResult.add(row);
					}

				}
				StringBuilder title2 = new StringBuilder();
				title2.append("bang_ke_so_luong_chua_giao");
				ToolReport.printReportExcelRaw(listResult, title2.toString());
			} else {
				notify.warning("Không có dữ liệu");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

}

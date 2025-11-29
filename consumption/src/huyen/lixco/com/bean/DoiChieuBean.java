package huyen.lixco.com.bean;

import huyen.lixco.com.service.HoaDonC2Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lixco.com.entity.HoaDonC2;
import lixco.com.entity.HoaDonChiTietC2;
import lixco.com.entity.Invoice;
import lombok.Getter;
import lombok.Setter;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

import trong.lixco.com.bean.AbstractBean;

import com.google.gson.Gson;

@Named
@ViewScoped
public class DoiChieuBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Getter
	@Setter
	private List<HoaDonC2> hoaDonC2 = new ArrayList<>();
	@Getter
	@Setter
	private List<HoaDonChiTietC2> hoaDonChiTiets = new ArrayList<HoaDonChiTietC2>();
	@Inject
	private HoaDonC2Service doiChieuService;
	@Getter
	@Setter
	private Date fromDateSearch;
	@Getter
	@Setter
	private Date toDateSearch;

	@Getter
	@Setter
	private long idInvoice;
	@Getter
	@Setter
	private String voucherCodeSearch;

	@Getter
	@Setter
	private String poNoSearch;
	@Getter
	@Setter
	private String orderVoucherSearch;
	@Getter
	@Setter
	private String orderCodeSearch;
	@Getter
	@Setter
	private int paymentSearch;

	final String INSERTINVOICE = "v3sainvoice/code";
	final String DELETEINVOICE = "v3sainvoice/code";
	final String GETINVOICE = "v3sainvoice/code";

	@Getter
	@Setter
	private int monthDms;
	@Getter
	@Setter
	private int yearDms;

	@Getter
	@Setter
	private HoaDonChiTietC2 selectedHoaDonChiTiet;
	@Getter
	@Setter
	private HoaDonC2 selectedhoaDon;
	


	@Getter
	@Setter
	private String voucherCode2Input;
	
	Gson gson;
	@Getter
	List<Boolean> cotdsdonhang = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true);
	final String TENBANG = "DSHOADON";



	@Override
	protected void initItem() {

	}

	public void nextOrPrev(int next) {

	}

	@Getter
	@Setter
	private String stextStr;

	// public void search() {
	// try {
	// listInvoiceDTO4 = new ArrayList<>();
	// listInvoiceDTO4 = hoaDonService.findByVoucherCode(voucherCodeSearch,
	// fromDateSearch, toDateSearch);
	// if (listInvoiceDTO4 == null || listInvoiceDTO4.isEmpty()) {
	// Notify notify = new Notify(FacesContext.getCurrentInstance());
	// notify.warning("Không tìm thấy hóa đơn nào phù hợp!");
	// }
	// } catch (Exception e) {
	// logger.error("InvoiceBean.search: " + e.getMessage(), e);
	// }
	// }

	@Override
	protected Logger getLogger() {
		return logger;
	}

	
}



package lixco.com.bean;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.HeThong;
import lixco.com.entity.KhoaDuLieu;
import lixco.com.service.HeThongService;
import lixco.com.service.KhoaDuLieuService;
import lombok.Getter;
import lombok.Setter;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.util.MyUtil;

@Named
@ViewScoped
public class KhoaDuLieuBean extends AbstractBean {
	private static final long serialVersionUID = 1233308831878395451L;
	@Inject
	private Logger logger;
	@Getter
	@Setter
	private int month;
	@Getter
	@Setter
	private int year;
	@Inject
	private KhoaDuLieuService khoaDuLieuService;
	@Getter
	@Setter
	private KhoaDuLieu khoaDuLieuCrud;
	@Getter
	@Setter
	private List<KhoaDuLieu> khoaDuLieus;
	@Inject
	HeThongService heThongService;
	@Getter
	@Setter
	HeThong heThong;

	@Override
	protected void initItem() {
		try {
			month = ToolTimeCustomer.getMonthCurrent();
			year = ToolTimeCustomer.getYearCurrent();
			khoaDuLieuCrud = new KhoaDuLieu();
			khoaDuLieuCrud.setKmonth(month);
			khoaDuLieuCrud.setKyear(year);
			search();

			heThong = heThongService.findById(1l);
		} catch (Exception e) {
			noticeError(e.getLocalizedMessage());
		}
	}

	public void search() {
		khoaDuLieus = khoaDuLieuService.findAll();
	}

	public void updateHeThong() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (heThong != null) {
				if (allowUpdate(null)) {
					heThong.setNote(heThong.getNote()+";"+getAccount().getUserName()+"_"+MyUtil.chuyensangStrHH(new Date()));
					heThongService.update(heThong);
					success();
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện.','error',2000);");
				}
			} else {
				warning("Chưa cấu hình cài đặt hệ thống.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {

			if (khoaDuLieuCrud.getKmonth() != 0 && khoaDuLieuCrud.getKyear() != 0) {
				KhoaDuLieu khoaDuLieuOld = khoaDuLieuService.findByMonthYear(khoaDuLieuCrud.getKmonth(),
						khoaDuLieuCrud.getKyear());
				if (khoaDuLieuOld == null) {
					if (allowSave(null)) {
						khoaDuLieuCrud.setId(0);
						khoaDuLieuCrud = khoaDuLieuService.create(khoaDuLieuCrud);
						success();
					} else {
						current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện.','error',2000);");
					}
				} else {
					if (allowUpdate(null)) {
						khoaDuLieuOld.setKkhoa(khoaDuLieuCrud.isKkhoa());
						String khoaStr = "khoa";
						if (!khoaDuLieuOld.isKkhoa())
							khoaStr = "mokhoa";
						khoaStr += "(" + MyUtil.chuyensangStrHH(new Date()) + ")";
						khoaDuLieuOld.setNote(khoaDuLieuOld.getNote() + ";" + getAccount().getUserName() + "-"
								+ khoaStr);
						khoaDuLieuService.update(khoaDuLieuOld);
						success();
					} else {
						current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện.','error',2000);");
					}

				}
				search();
			}
		} catch (Exception e) {
			logger.error("CityBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}
}

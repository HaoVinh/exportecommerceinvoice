package lixco.com.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import lixco.com.common.SessionHelper;
import lixco.com.entity.Driver;
import lixco.com.service.DriverService;
import lombok.Getter;
import lombok.Setter;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.util.ToolReport;

@Named
@ViewScoped
public class DriverBean extends AbstractBean {
	private static final long serialVersionUID = 1L;
	@Inject
	private Logger logger;
	@Inject
	private DriverService driverService;
	@Getter
	@Setter
	private Driver driverCrud;
	@Getter
	@Setter
	private Driver driverSelect;
	@Getter
	@Setter
	private List<Driver> listDriver;

	@Override
	protected void initItem() {
		try {
			search();
			createNew();
		} catch (Exception e) {
			logger.error("DriverBean.initItem:" + e.getMessage(), e);
		}
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void search() {
		try {
			listDriver = driverService.selectAll();
		} catch (Exception e) {
			logger.error("DriverBean.search:" + e.getMessage(), e);
		}
	}

	public void saveOrUpdate() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (driverCrud != null) {
				String name = driverCrud.getName();
				if (name != null && !"".equals(name)) {
					if (driverCrud.getId() == 0) {
						// check code đã tồn tại chưa
						if (allowSave(null)) {
							if (driverService.insert(driverCrud) != -1) {
								current.executeScript("swaldesigntimer('Thành công!', 'Thêm thành công!','success',2000);");
								listDriver.add(0, driverCrud.clone());
							} else {
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Thêm thất bại!','error',2000);");
							}

						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện','error',2000);");
						}
					} else {
						// check code update đã tồn tại chưa
						if (allowUpdate(null)) {
							if (driverService.update(driverCrud) != -1) {
								current.executeScript("swaldesigntimer('Thành công!', 'Cập nhật thành công!','success',2000);");
								listDriver.set(listDriver.indexOf(driverCrud), driverCrud);
							} else {
								current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Cập nhật thất bại!','error',2000);");
							}
						} else {
							current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện!','error',2000);");
						}
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo', 'Thông tin không đầy đủ, điền đủ thông tin chứa(*)','warning',2000);");
				}
			}
		} catch (Exception e) {
			logger.error("DriverBean.saveOrUpdate:" + e.getMessage(), e);
		}
	}

	public void showEdit() {
		try {
			driverCrud = driverSelect;
		} catch (Exception e) {
			logger.error("DriverBean.showEdit:" + e.getMessage(), e);
		}
	}

	public void createNew() {
		driverCrud = new Driver();
	}

	public void delete() {
		PrimeFaces current = PrimeFaces.current();
		try {
			if (driverSelect != null) {
				if (allowDelete(new Date())) {
					if (driverService.deleteById(driverSelect.getId()) != -1) {
						current.executeScript("swaldesigntimer('Thành công!', 'Xóa thành công!','success',2000);");
						listDriver.remove(driverSelect);
					} else {
						current.executeScript("swaldesigntimer('Xảy ra lỗi!', 'Không xóa được!','error',2000);");
					}
				} else {
					current.executeScript("swaldesigntimer('Cảnh báo!', 'Tài khoản này không có quyền thực hiện hoặc tháng đã khoá!','error',2000);");
				}
			} else {
				current.executeScript("swaldesigntimer('Cảnh báo!', 'Chưa chọn dòng để xóa!','warning',2000);");
			}
		} catch (Exception e) {
			logger.error("DriverBean.delete:" + e.getMessage(), e);
		}
	}

	public void exportExcel() {
		try {
			if (listDriver.size() > 0) {
				List<Object[]> results = new ArrayList<Object[]>();
				Object[] title = { "id", "ten", "dienthoai", "cccd", "ghichu" };
				results.add(title);
				for (Driver p : listDriver) {
					Object row[] = { p.getId(), p.getName(), p.getPhone(), p.getCccd(), p.getNote() };
					results.add(row);
				}
				String titleEx = "dmlaixe";
				ToolReport.printReportExcelRaw(results, titleEx);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package trong.lixco.com.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lixco.com.entity.KhoaDuLieu;
import lixco.com.service.KhoaDuLieuService;
import net.sf.jasperreports.engine.JRParameter;

import org.jboss.logging.Logger;
import org.primefaces.PrimeFaces;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.account.servicepublics.AccountServicePublic;
import trong.lixco.com.account.servicepublics.AccountServicePublicProxy;
import trong.lixco.com.account.servicepublics.LockTableServicePublic;
import trong.lixco.com.account.servicepublics.LockTableServicePublicProxy;
import trong.lixco.com.info.PrivateConfig;
import trong.lixco.com.util.UrlPermission;

public abstract class AbstractBean implements Serializable {
	private static final long serialVersionUID = 9154488968915975475L;
	@Inject
	protected AuthorizationManager authorizationManager;
	private Account account;
	private PrivateConfig cf;
	AccountServicePublic accountServicePublic;

	private LockTableServicePublic lockTableServicePublic;
	@Inject
	KhoaDuLieuService khoaDuLieuService;

	String taikhoanadmin[] = { "Administrator", "Nguyễn Thị Thúy Vân", "Vũ Quang Đấu","Cao Thị Thanh Bình" };

	@PostConstruct
	public void init() {
		lockTableServicePublic = new LockTableServicePublicProxy();
		accountServicePublic = new AccountServicePublicProxy();
		initItem();
		getAccount();
	}

	protected abstract void initItem();

	protected abstract Logger getLogger();

	private void getSession() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = req.getSession();
		account = (Account) session.getAttribute("account");
		if (account != null)
			try {
				cf = new PrivateConfig(account.getPrivateConfig());
			} catch (Exception e) {
			}

	}

	public void writeLogInfo(String message) {
		if (account == null)
			getSession();
		getLogger().info("(" + account.getUserName() + "): " + message);
	}

	public void writeLogError(String message) {
		if (account == null)
			getSession();
		getLogger().error("(" + account.getUserName() + "): " + message);
	}

	public void writeLogWarning(String message) {
		try {
			if (account == null)
				getSession();
			getLogger().fatal("(" + account.getUserName() + "): " + message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<UrlPermission> getUrlPermissions() {
		return authorizationManager.getUrlPermissions();
	}

	// Kiem tra thang da khoa chua
	public boolean lock(Date date) {
		try {
			KhoaDuLieu lt;
			int month = (date.getMonth() + 1);
			int year = (date.getYear() + 1900);
			lt = khoaDuLieuService.findByMonthYear(month, year);
			if (lt != null) {
				return lt.isKkhoa();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean lock(int month, int year) {
		try {
			KhoaDuLieu lt = khoaDuLieuService.findByMonthYear(month, year);
			if (lt != null) {
				return lt.isKkhoa();
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public boolean allowSave(Date date) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isSave()) {
				if (date != null) {
					if (lock(date) == false) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isSave();
			}
		} else {
			return false;
		}
	}

	public boolean allowSave(Date date, String nameMemCreate) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isSave()) {
				if (date != null) {
					if (lock(date) == false && allowUser(nameMemCreate)) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isSave();
			}
		} else {
			return false;
		}
	}

	public boolean allowUpdate(Date date, String nameMemCreate) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isUpdate()) {
				if (date != null) {
					if (lock(date) == false && allowUser(nameMemCreate)) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isUpdate();
			}
		} else {
			return false;
		}
	}

	public boolean allowDelete(Date date, String nameMemCreate) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isDelete()) {
				if (date != null) {
					if (lock(date) == false) {
						if (allowUser(nameMemCreate)) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isDelete();
			}
		} else {
			return false;
		}
	}

	public boolean allowUpdate(Date date) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isUpdate()) {
				if (date != null) {
					if (lock(date) == false) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isUpdate();
			}
		} else {
			return false;
		}
	}

	public boolean allowDelete(Date date) {
		UrlPermission up = authorizationManager.getPermission();
		if (up != null) {
			if (up.isDelete()) {
				if (date != null) {
					if (lock(date) == false) {
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return up.isDelete();
			}
		} else {
			return false;
		}
	}

	public Map<String, Object> installConfigPersonReport() {
		Map<String, Object> importParam = new HashMap<String, Object>();
		Locale locale = new Locale("vi", "VI");
		importParam.put(JRParameter.REPORT_LOCALE, locale);
		String image = FacesContext.getCurrentInstance().getExternalContext()
				.getRealPath("/resources/gfx/lixco_logo.png");
		importParam.put("logo", image);
		if (cf == null)
			getSession();
		if (cf != null) {
			if (",".equals(cf.getDecimalSeparator()))
				importParam.put("REPORT_LOCALE", new Locale("vi", "VN"));
			importParam.put("formatNumber", cf.getParttenNumber());
			importParam.put("formatDate", cf.getFormatDate());
		}
		return importParam;

	}

	public int getMax(List<Integer> list) {
		int max = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) > max) {
				max = list.get(i);
			}
		}
		return max;
	}

	public AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public PrivateConfig getCf() {
		if (cf == null)
			getSession();
		return cf;
	}

	public void setCf(PrivateConfig cf) {
		this.cf = cf;
	}

	boolean admin;

	public Account getAccount() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = req.getSession();
		account = (Account) session.getAttribute("account");
		String db = (String) session.getAttribute("database");
		try {
			if (account != null) {
				admin = account.isAdmin();
				return accountServicePublic.findById(account.getId());
			} else {
				return new Account();
			}
		} catch (Exception e) {
			return null;
		}

	}

	public String getDatabase() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = req.getSession();
		String db = (String) session.getAttribute("database");
		return db;

	}

	public String getDatabaseDH() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = req.getSession();
		String db = (String) session.getAttribute("database");
		if (db.equals("HO CHI MINH") || db.equals("BINH DUONG")) {
			return "BINH DUONG";
		}
		return db;
	}

	public void notice(String content) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo!", content);
		PrimeFaces.current().dialog().showMessageDynamic(message);
		PrimeFaces.current().ajax().update("messages");
	}
	public void info(String content) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", content));
		if (findComponentById("messages") != null) {
			PrimeFaces.current().ajax().update("messages");
		}
	}
	public void warning(String content) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Thông báo", content));
		if (findComponentById("messages") != null) {
			PrimeFaces.current().ajax().update("messages");
		}
	}

	public void error(String content) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thông báo lỗi", content));
		if (findComponentById("messages") != null) {
			PrimeFaces.current().ajax().update("messages");
		}
	}

	public static UIComponent findComponentById(String componentId) {
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		return root.findComponent(componentId);
	}

	public void success() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", "Thành công."));
		PrimeFaces.current().ajax().update("messages");
	}

	public void success(String messages) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Thông báo", messages));
		PrimeFaces.current().ajax().update("messages");
	}

	public void successJs() {
		PrimeFaces.current().executeScript("swaldesigntimer('Thành công!', 'Thành công!','success',2000);");
	}

	public void noticeError(String content) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Thông báo!", content);
		PrimeFaces.current().dialog().showMessageDynamic(message);
		PrimeFaces.current().ajax().update("messages");
	}

	public boolean isAdmin() {
		return admin;
	}

	public void thongbao(String value) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("swaldesignclose2('Thành công!', '" + value + "','success',2000);");
	}

	public void canhbao(String value) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("swaldesignclose2('Cảnh báo!', '" + value + "','warning');");
	}

	public void executeScript(String script) {
		PrimeFaces.current().executeScript(script);
	}

	public void showDialog(String name) {
		PrimeFaces.current().executeScript("PF('" + name + "').show();");
	}

	public void showDialogErrorinfor() {
		PrimeFaces.current().executeScript("PF('dldulieukhongnapduoc').show();");
		PrimeFaces.current().ajax().update("iddldulieukhongnapduoc");
	}

	public void hideDialog(String name) {
		PrimeFaces.current().executeScript("PF('" + name + "').hide();");
	}

	public String getCreateByUser() {
		try {
			return getAccount().getMember().getName();
		} catch (Exception e) {
			return "";
		}

	}

	public boolean allowUser(String nameMemCreate) {
		Account account = getAccount();
		if (account != null) {
			if (account.isAdmin()) {
				// Neu tai khoan admin he thong
				return true;
			} else {
				if (nameMemCreate == null) {
					return true;
				} else {
					if (account.getMember() != null) {
						if (nameMemCreate == null || "".equals(nameMemCreate)) {
							return true;
						} else {
							if (nameMemCreate.equals(account.getMember().getName())) {
								return true;
							} else {
								// kiem tra neu tai khoan admin chuong trinh cho
								// phep cap nhat
								for (int i = 0; i < taikhoanadmin.length; i++) {
									if (taikhoanadmin[i].equals(account.getMember().getName())) {
										return true;
									}
								}
								return false;
							}
						}
					} else {
						error("Tài khoản không có nhân viên!!!");
						return false;
					}
				}
			}
		} else {
			error("Không tìm thấy tài khoản đăng nhập, vui lòng đăng nhập lại.");
			return false;
		}

	}

	public boolean allowUserAdmin() {
		Account account = getAccount();
		if (account != null) {
			if (account.isAdmin()) {
				// Neu tai khoan admin he thong
				return true;
			} else {
				// kiem tra neu tai khoan admin chuong trinh cho
				// phep cap nhat
				for (int i = 0; i < taikhoanadmin.length; i++) {
					if (taikhoanadmin[i].equals(account.getMember().getName())) {
						return true;
					}
				}
				return false;
			}
		} else {
			error("Không tìm thấy tài khoản đăng nhập, vui lòng đăng nhập lại.");
			return false;
		}

	}

	public void updateform(String id) {
		PrimeFaces.current().ajax().update(id);
	}
}

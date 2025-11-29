package trong.lixco.com.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lixco.com.entity.YeuCauKiemTraHang;
import lixco.com.entity.YeuCauKiemTraHangDetail;
import lixco.com.entityapi.ThongBaoDTO;
import lixco.com.service.YeuCauKiemTraHangDetailService;
import lixco.com.service.YeuCauKiemTraHangService;
import trong.lixco.com.apitaikhoan.AccountData;
import trong.lixco.com.apitaikhoan.AccountDataService;
import trong.lixco.com.entity.AccountDatabase;
import trong.lixco.com.entity.AccountEmailConfirm;
import trong.lixco.com.entity.AccountEmailSend;
import trong.lixco.com.info.DataSendMail;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.AccountEmailConfirmService;
import trong.lixco.com.service.AccountEmailSendService;
import trong.lixco.com.service.MailManagerService;
import trong.lixco.com.service.ServerMailLixService;
import trong.lixco.com.util.MyMailUtil;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.NameSytem;
import trong.lixco.com.util.StaticPath;

@WebServlet(name = "XNKetQuaServlet", urlPatterns = { "/XNKetQuaServlet/*" })
public class XNKetQuaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	@Inject
	AccountEmailConfirmService accountEmailConfirmService;
	@Inject
	YeuCauKiemTraHangService yeuCauKiemTraHangService;

	protected void processRequestPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			session.setAttribute("tieude", "");
			session.setAttribute("maphieu", "");
			session.setAttribute("tennvxacnhan", "");
			session.setAttribute("ngayxacnhan", "");
			String sessionIdFromEmail = request.getParameter("sessionIdFromEmail");
			String idphieu = request.getParameter("idphieu");
			if (sessionIdFromEmail != null && idphieu != null) {

				/*
				 * Start kiem tra tai khoan xac nhan tu mail
				 */
				AccountEmailConfirm acCf = accountEmailConfirmService.findUId(sessionIdFromEmail);
				if (acCf != null) {
					accountEmailConfirmService.delete(acCf.getId());
					YeuCauKiemTraHang yc = yeuCauKiemTraHangService.findById(Long.parseLong(idphieu));
					if (yc != null) {
						Date now = new Date();
						session.setAttribute("tieude", "XÁC NHẬN KẾT QUẢ KIỂM TRA");
						session.setAttribute("maphieu", yc.getRequestCode());
						session.setAttribute("tennvxacnhan", acCf.getNameEmp());
						session.setAttribute("ngayxacnhan", MyUtil.chuyensangStrHH(now));

						// cap nhat da tiep nhan va ngay tiep nhan
						if (yc.isTpkcsxacnhankq() == false) {
							yc.setTpkcsxacnhankqDate(now);
							yc.setTpkcsxacnhankqName(acCf.getNameEmp());
							yeuCauKiemTraHangService.updateXNKetQua(yc);
							// Cai dat gui mail cho BLD
							setPathLink(request);
							guiMailBLD(yc, request.getServletContext());
						}

						String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/"
								+ NameSytem.NAMEPROGRAM + "/YCKTHHSuccess.jsf";
						response.sendRedirect(url);
					} else {
						session.setAttribute("idphieu", idphieu);
						String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/"
								+ NameSytem.NAMEPROGRAM + "/YCKTHHError.jsf";
						response.sendRedirect(url);
					}

				} else {
					String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/"
							+ NameSytem.NAMEPROGRAM + "/403confirmmail.html";
					response.sendRedirect(url);
				}

				/*
				 * End kiem tra tai khoan xac nhan tu mail
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Inject
	MailManagerService mailManagerService;
	@Inject
	ServerMailLixService serverMailLixService;
	@Inject
	AccountEmailSendService accountEmailSendService;
	@Inject
	YeuCauKiemTraHangDetailService yeuCauKiemTraHangDetailService;
	static final String CONFIRM_BLDDUYETKQ = "/XNKetQuaBLDServlet";

	public void guiMailBLD(YeuCauKiemTraHang yeuCauKiemTraHangCrud, ServletContext servletContext) {
		List<YeuCauKiemTraHangDetail> yeuCauKiemTraHangDetails = yeuCauKiemTraHangDetailService
				.findByYeuCauKiemTraHang(yeuCauKiemTraHangCrud.getId());
		// 3: nhom BLD
		List<AccountEmailSend> accountEmailSends = accountEmailSendService.findTypeAcc(3);
		for (int i = 0; i < accountEmailSends.size(); i++) {
			AccountData accountData = AccountDataService.laytaikhoan(accountEmailSends.get(i).getCodeEmp());
			if (accountData != null) {
				AccountEmailConfirm acc = new AccountEmailConfirm();
				acc.setUid(UUID.randomUUID().toString());
				acc.setUser(accountData.getUserName());
				acc.setPass(accountData.getPassword());
				acc.setCodeEmp(accountData.getMember().getCode());
				acc.setNameEmp(accountData.getMember().getName());
				accountEmailConfirmService.create(acc);
				String linkConfirm = StaticPath.getLinkConfirmEmail() + CONFIRM_BLDDUYETKQ + "?sessionIdFromEmail="
						+ acc.getUid() + "&idphieu=" + yeuCauKiemTraHangCrud.getId();
				boolean result = MyMailUtil.sendMailConfirmYCKT(yeuCauKiemTraHangCrud, yeuCauKiemTraHangDetails,
						accountEmailSends.get(i).getEmail(), linkConfirm, mailManagerService, serverMailLixService,
						new DataSendMail("", " duyệt kết quả kiểm tra ", "", ""), servletContext,"Xác nhận phiếu yêu cầu kiểm tra hàng hóa");
				if (result) {
					System.out.println("Đã gửi mail BLD duyệt kết quả kiểm tra.");
				} else {
					System.out.println("Lỗi: Gửi mail BLD duyệt kết quả kiểm tra không thành công.");
				}
			}
		}
	}

	public void setPathLink(HttpServletRequest request) {
		String path;
		String pathlocal;

		String pathqlcl;

		AccountDatabase accdb = accountDatabaseService.findByName("hethong");
		boolean check = checkAddressLocal(request);
		if (check) {
			path = accdb.getAddress();
		} else {
			path = accdb.getAddressPublic();
		}
		StaticPath.setPath(path);
		StaticPath.setTokenacc(accdb.getToken());

		AccountDatabase accdblocal = accountDatabaseService.findByName("consumption");
		if (check) {
			pathlocal = accdblocal.getAddress();
		} else {
			pathlocal = accdblocal.getAddressPublic();
		}
		StaticPath.setPathLocal(pathlocal);
		StaticPath.setLinkConfirmEmail(accdblocal.getLinkConfirmEmail());

		AccountDatabase accdbqlcl = accountDatabaseService.findByName("qlcl");
		if (check) {
			pathqlcl = accdbqlcl.getAddress();
		} else {
			pathqlcl = accdbqlcl.getAddressPublic();
		}
		StaticPath.setPathQLCL(pathqlcl);
		StaticPath.setTokenQLCL(accdbqlcl.getToken());

	}

	public boolean checkAddressLocal(HttpServletRequest request) {
		try {

			String ipAddress = request.getHeader("X-FORWARDED-FOR");// ip
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				boolean temp = ipAddress.contains("192.168.");
				if (temp == false) {
					temp = ipAddress.contains("127.0.0.1");
				}
				return temp;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequestPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		processRequestPost(request, response);
	}

}

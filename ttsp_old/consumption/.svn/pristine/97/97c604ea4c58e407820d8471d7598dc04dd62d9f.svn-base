package trong.lixco.com.servlet;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lixco.com.entity.YeuCauKiemTraHang;
import lixco.com.service.YeuCauKiemTraHangService;
import trong.lixco.com.entity.AccountEmailConfirm;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.AccountEmailConfirmService;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.NameSytem;

@WebServlet(name = "XNKetQuaBLDServlet", urlPatterns = { "/XNKetQuaBLDServlet/*" })
public class XNKetQuaBLDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
						session.setAttribute("tieude", "XÁC NHẬN KẾT QUẢ PHIẾU YÊU CẦU KIỂM TRA");
						session.setAttribute("maphieu", yc.getRequestCode());
						session.setAttribute("tennvxacnhan", acCf.getNameEmp());
						session.setAttribute("ngayxacnhan", MyUtil.chuyensangStrHH(now));

						// cap nhat da tiep nhan va ngay tiep nhan
						if (yc.isBldxacnhan() == false) {
							yc.setBldxacnhanDate(now);
							yeuCauKiemTraHangService.updateXNKetQuaBLD(yc);
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

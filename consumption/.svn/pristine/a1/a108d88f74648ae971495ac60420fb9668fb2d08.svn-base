package trong.lixco.com.servlet;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lixco.com.entity.Car;
import lixco.com.entity.Invoice;
import lixco.com.entity.InvoiceDetail;
import lixco.com.entity.Product;
import lixco.com.entity.YeuCauKiemTraHang;
import lixco.com.entityapi.DataAPI;
import lixco.com.entityapi.InvoiceAsyncDTO;
import lixco.com.entityapi.InvoiceDTO;
import lixco.com.entityapi.InvoiceDetailDTO;
import lixco.com.entityapi.ThongBaoDTO;
import lixco.com.hddt.ThongBao;
import lixco.com.service.YeuCauKiemTraHangService;
import okhttp3.Call;
import okhttp3.Response;

import org.apache.http.HttpRequest;
import org.primefaces.PrimeFaces;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.account.servicepublics.AccountServicePublic;
import trong.lixco.com.account.servicepublics.AccountServicePublicProxy;
import trong.lixco.com.account.servicepublics.SingleSignOn;
import trong.lixco.com.api.JsonParserUtil;
import trong.lixco.com.api.SecurityConfig;
import trong.lixco.com.apitaikhoan.AccountData;
import trong.lixco.com.apitaikhoan.AccountDataService;
import trong.lixco.com.bean.AuthorizationManager;
import trong.lixco.com.entity.AccountAPI;
import trong.lixco.com.entity.AccountDatabase;
import trong.lixco.com.entity.AccountEmailConfirm;
import trong.lixco.com.service.AccountAPIService;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.service.AccountEmailConfirmService;
import trong.lixco.com.service.DongBoService;
import trong.lixco.com.util.MyUtil;
import trong.lixco.com.util.NameSytem;
import trong.lixco.com.util.StaticPath;

@WebServlet(name = "TNYeuCauServlet", urlPatterns = { "/TNYeuCauServlet/*" })
public class TNYeuCauServlet extends HttpServlet {
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
						session.setAttribute("tieude", "TIẾP NHẬN PHIẾU YÊU CẦU KIỂM TRA HÀNG HÓA");
						session.setAttribute("maphieu", yc.getRequestCode());
						session.setAttribute("tennvxacnhan", acCf.getNameEmp());
						session.setAttribute("ngayxacnhan", MyUtil.chuyensangStrHH(now));

						// cap nhat da tiep nhan va ngay tiep nhan
						if (yc.isKcsdatiepnhan() == false) {
							yc.setNgaytiepnhan(now);
							yc.setNguoitiepnhan(acCf.getNameEmp());
							yeuCauKiemTraHangService.updateTiepNhan(yc);
							// Cai dat nhan vien nhan thong bao chuong trinh
							// QLCL
							setPathLink(request);
							ThongBaoDTO thongBaoDTO=new ThongBaoDTO();
							thongBaoDTO.setCreatedDate(now);
							thongBaoDTO.setChuongtrinh("TTSP");
							thongBaoDTO.setTieude("Phiếu yêu cầu kiểm tra hàng hóa "+yc.getRequestCode());
							thongBaoDTO.setLinktruycap("pages/kiemtrasanpham/kiemtrasanpham.htm");
							guiThongBaoQLCL(thongBaoDTO);
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
	AccountAPIService accountAPIService;
	@Inject
	DongBoService dongBoService;
	
	int guithongbao=0;
	final String CHUONGTRINH="QLCL";
	public void guiThongBaoQLCL(ThongBaoDTO thongBaoDTO) {
		guithongbao+=1;
		PrimeFaces current = PrimeFaces.current();
		try {
			Gson gson = JsonParserUtil.getGson();

			StringBuffer message = new StringBuffer();
			String token = "";

			AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, CHUONGTRINH);
			if (accountAPI == null) {
				System.out.println("Lỗi gửi thông báo, không có tài khoản đăng nhập API.");
				return;
			}

			String[] tokentime = new String[] { accountAPI.getToken(), accountAPI.getTimetoken() + "" };
			if (tokentime != null
					&& (tokentime[1] != null && new Date().getTime() < Long.parseLong(tokentime[1]) && tokentime[0] != null)) {
				token = tokentime[0];
			} else {
				dangnhapAPIdongbo(gson);
				if(guithongbao==1){
					guiThongBaoQLCL(thongBaoDTO);
				}
			}
			
			// Goi ham dong bo
			Call call = trong.lixco.com.api.CallAPI.getInstance(token).getMethodPost(StaticPath.getPathQLCL() + "/api/data/tbyeucaukiemtrahh",
					gson.toJson(thongBaoDTO));
			Response response = call.execute();

			String data = response.body().string();
			JsonObject jdata = gson.fromJson(data, JsonObject.class);
			String err = jdata.get("err").getAsString();
			String msg = jdata.get("msg").getAsString();
			message.append(msg);
			if (response.isSuccessful()) {
				if (err.equals("-1")) {
					System.out.println("Gửi thông báo lỗi:"+msg);
				} else {
					System.out.println("Gửi thông báo: đã gửi thành công");
				}
			} else {
				if (response.code() == 401) {
					dangnhapAPIdongbo(gson);
					if(guithongbao==1){
						guiThongBaoQLCL(thongBaoDTO);
					}
				} else {
					System.out.println("Gửi thông báo lỗi :"+msg);
				}
			}

		} catch (Exception e) {
			current.executeScript("swaldesigntimer('Xảy ra lỗi!', '" + e.getMessage() + "','error',2000);");
		}
	}

	private void dangnhapAPIdongbo(Gson gson) throws IOException {
		AccountAPI accountAPI = SecurityConfig.getTokenTime(accountAPIService, CHUONGTRINH);
		String[] tokentime = new String[2];
		Call call = trong.lixco.com.api.CallAPI.getInstance("").getMethodPost(
				StaticPath.getPathQLCL() + "/api/account/login", gson.toJson(accountAPI));
		Response response = call.execute();
		if (response.isSuccessful()) {
			String data = response.body().string();
			JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
			tokentime[0] = jsonObject.get("access_token").getAsString();
			tokentime[1] = jsonObject.get("expires_in").getAsString();
			SecurityConfig.saveToken(accountAPIService, tokentime[0], Long.parseLong(tokentime[1]), CHUONGTRINH);
			System.out.println("Gửi thông báo: Đã liên kết đến hệ thống " + response.toString() + ". Vui lòng thực hiện chuyển lại");
		} else {
			System.out
					.println("Lỗi gửi thông báo: Tài khoản đăng nhập API " + CHUONGTRINH + " không đúng hoặc lỗi " + response.toString());
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

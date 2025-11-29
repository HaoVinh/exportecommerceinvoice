package trong.lixco.com.api;

import io.jsonwebtoken.io.IOException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lixco.com.common.JsonParserUtil;
import lixco.com.entity.YeuCauKiemTraHang;
import lixco.com.entity.YeuCauKiemTraHangDetail;
import lixco.com.entityapi.YeuCauKiemTraHangDTO;
import lixco.com.service.YeuCauKiemTraHangDetailService;
import lixco.com.service.YeuCauKiemTraHangService;
import lixco.com.service.YeuCauKiemTraHangTHService;
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
import trong.lixco.com.util.StaticPath;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("data")
public class YeuCauKiemTraHangHoaServlet {
	@Context
	private HttpServletRequest request;
	@Inject
	YeuCauKiemTraHangTHService yeuCauKiemTraHangTHService;

	@GET
	@Path("danhsachphieuyeucau")
	@Produces("application/json; charset=utf-8")
	public Response findDanhSachPhieu(@QueryParam(DefinedName.PARAM_SDATE) String sDate,
			@QueryParam(DefinedName.PARAM_EDATE) String eDate) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			Date startDate = MyUtil.chuyensangStrApiDate(sDate);
			Date endDate = MyUtil.chuyensangStrApiDate(eDate);
			List<YeuCauKiemTraHangDTO> yeuCauKiemTraHangDTOs = yeuCauKiemTraHangTHService.searchAPI(startDate, endDate);
			if (yeuCauKiemTraHangDTOs.size() > 0) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.add("data", JsonParserUtil.getGson().toJsonTree(yeuCauKiemTraHangDTOs));
				responseBuilder.status(Response.Status.OK);
				result.append(jsonObject);
			} else {
				result.append(CommonModel.toJson(1, DefinedName.NO_CONTENT));
				responseBuilder.status(Response.Status.NO_CONTENT);
			}
		} catch (Exception e) {
			result.append("Xảy ra lỗi: " + e.getMessage());
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	@POST
	@Path("luuketquaphieuyeucaukt")
	@Produces("application/json; charset=utf-8")
	public Response luuKetQuaPhieuYeuCauKiemTra(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				Gson gson = JsonParserUtil.getGson();
				YeuCauKiemTraHangDTO yeuCauKiemTraHangDTO = gson.fromJson(data, YeuCauKiemTraHangDTO.class);
				luuKetQuaKiemTra(yeuCauKiemTraHangDTO, result, responseBuilder);
				setPathLink(request);
				YeuCauKiemTraHang yc = yeuCauKiemTraHangService.findById(yeuCauKiemTraHangDTO.getId());
				if (yc.isDakiemtra()) {
					if (yc.isDaguimailTPKCS() == false) {
						guiMailTPKCS(yc, request.getServletContext());
					}
				}
			} else {
				result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result.toString()).build();
	}

	@Inject
	YeuCauKiemTraHangService yeuCauKiemTraHangService;

	private void luuKetQuaKiemTra(YeuCauKiemTraHangDTO yeuCauKiemTraHangDTO, StringBuilder result,
			ResponseBuilder responseBuilder) {
		try {
			StringBuilder errors = new StringBuilder();
			YeuCauKiemTraHang yc = yeuCauKiemTraHangService.findById(yeuCauKiemTraHangDTO.getId());
			if (yc.isBldxacnhan()) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("err", -1);
				jsonObject.addProperty("msg", "BLĐ đã duyệt kết quả.");
				responseBuilder.status(Response.Status.OK);
				result.append(jsonObject);
			} else {
				if (yc.isDaguimailTPKCS() == false) {
					setPathLink(request);
				}
				int res = yeuCauKiemTraHangTHService.updateKetQuaKiemTra(yeuCauKiemTraHangDTO, errors);
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("err", res);
				jsonObject.addProperty("msg", errors.toString());
				responseBuilder.status(Response.Status.OK);
				result.append(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseBuilder.status(Response.Status.BAD_REQUEST);
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
	@Inject
	AccountEmailConfirmService accountEmailConfirmService;
	static final String CONFIRM_XACNHANKETQUA = "/XNKetQuaServlet";

	public void guiMailTPKCS(YeuCauKiemTraHang yeuCauKiemTraHangCrud, ServletContext servletContext) {
		List<YeuCauKiemTraHangDetail> yeuCauKiemTraHangDetails = yeuCauKiemTraHangDetailService
				.findByYeuCauKiemTraHang(yeuCauKiemTraHangCrud.getId());
		// 1: truong phòng KCS (tiep nhan yeu cau kiem tra, xac nhan ket qua
		// kiem tra)
		List<AccountEmailSend> accountEmailSends = accountEmailSendService.findTypeAcc(1);
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
				String linkConfirm = StaticPath.getLinkConfirmEmail() + CONFIRM_XACNHANKETQUA + "?sessionIdFromEmail="
						+ acc.getUid() + "&idphieu=" + yeuCauKiemTraHangCrud.getId();
				boolean result = MyMailUtil.sendMailConfirmYCKT(yeuCauKiemTraHangCrud, yeuCauKiemTraHangDetails,
						accountEmailSends.get(i).getEmail(), linkConfirm, mailManagerService, serverMailLixService,
						new DataSendMail("", " xác nhận kết quả kiểm tra", "", ""), servletContext,"Xác nhận kết quả kiểm tra hàng hóa");
				if (result) {
					yeuCauKiemTraHangService.updateSendMailTPKCS(yeuCauKiemTraHangCrud.getId());
					System.out.println("Đã gửi mail yêu cầu P. Quản lý chất lượng xác nhận kết quả kiểm tra.");
				} else {
					System.out
							.println("Lỗi: Gửi mail yêu cầu P. Quản lý chất lượng xác nhận kết quả không thành công.");
				}
			}
		}
	}

	@Inject
	AccountDatabaseService accountDatabaseService;

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
}

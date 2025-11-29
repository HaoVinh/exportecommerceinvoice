package trong.lixco.com.servlet;

import java.io.Closeable;
import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import trong.lixco.com.account.servicepublics.Account;
import trong.lixco.com.account.servicepublics.AccountServicePublic;
import trong.lixco.com.account.servicepublics.AccountServicePublicProxy;
import trong.lixco.com.account.servicepublics.SingleSignOn;
import trong.lixco.com.bean.AuthorizationManager;
import trong.lixco.com.entity.AccountDatabase;
import trong.lixco.com.service.AccountDatabaseService;
import trong.lixco.com.util.StaticPath;

@WebServlet(name = "authorServlet", urlPatterns = { "/authorServlet/*" })
public class AuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Inject
	private AuthorizationManager authorizationManager;
	@Inject
	private AccountDatabaseService accountDatabaseService;
	private String path;
	private String pathlocal;
	private String pathcenter;
	private String pathDMS;
	private String tokenDMS;

	private String pathBD;
	private String tokenBD;
	private String pathBN;
	private String tokenBN;
	private String pathHCM;
	private String tokenHCM;

	private String pathDMS_MienNam;
	private String tokenDMS_MienNam;

	@Override
	public void init() throws ServletException {
		super.init();
		System.out.println("[AuthorServlet] initialized");
	}

	protected void processRequestPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			String idStr = request.getParameter("id");
			String database = request.getParameter("database");
			if (idStr != null && database != null) {
				session.setAttribute("database", database);
				setPathLink(request);
				AccountServicePublic accountServicePublic = new AccountServicePublicProxy();
				String loginURL = path + "/account/pages/Start.jsf";
				System.out.println("loginURL: " + loginURL);
				SingleSignOn singleSignOn = accountServicePublic.findSSOById(Long.parseLong(idStr));
				if (singleSignOn != null) {
					Account account = accountServicePublic.findById(Long.parseLong(singleSignOn.getValue()));
					accountServicePublic.deleteSSO(singleSignOn);
					if (account != null) {
						// Kiem tra User nay co cho phep truy cap chuong
						// trinh hay khong
						// Neu cho phep thi cai dat bo quyen cho cho user
						boolean allow = authorizationManager.isAllowed(account);
						if (allow) {
							System.out.println("homeURL: " + pathlocal + "/consumption/pages/home.jsf");
							session.setAttribute("account", account);
							response.sendRedirect(pathlocal + "/consumption/pages/home.jsf");

						}
					}
				} else {
					response.sendRedirect(loginURL);
				}
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setPathLink(HttpServletRequest request) {
		AccountDatabase accdb = accountDatabaseService.findByName("hethong");
		boolean check = checkAddressPublic(request);
		if (!check) {
			path = accdb.getAddress();
		} else {
			path = accdb.getAddressPublic();
		}
		StaticPath.setPath(path);
		StaticPath.setTokenacc(accdb.getToken());

		AccountDatabase accdblocal = accountDatabaseService.findByName("consumption");
		if (!check) {
			pathlocal = accdblocal.getAddress();
		} else {
			pathlocal = accdblocal.getAddressPublic();
		}
		StaticPath.setPathLocal(pathlocal);
		StaticPath.setLinkConfirmEmail(accdblocal.getLinkConfirmEmail());

		AccountDatabase accdbcenter = accountDatabaseService.findByName("trungtam");
		if (!check) {
			pathcenter = accdbcenter.getAddress();
		} else {
			pathcenter = accdbcenter.getAddressPublic();
		}
		StaticPath.setPathCenter(pathcenter);

		AccountDatabase accdbDMS = accountDatabaseService.findByName("dms");
		if (!check) {
			pathDMS = accdbDMS.getAddress();
			tokenDMS = accdbDMS.getToken();
		} else {
			pathDMS = accdbDMS.getAddressPublic();
			tokenDMS = accdbDMS.getToken();
		}
		StaticPath.setPathDMS(pathDMS);
		StaticPath.setTokenDMS(tokenDMS);

		AccountDatabase accdbBD = accountDatabaseService.findByName("BD");
		if (accdbBD != null) {
			pathBD = accdbBD.getAddress();
			tokenBD = accdbBD.getToken();
			StaticPath.setPathBD(pathBD);
			StaticPath.setTokenBD(tokenBD);
		}

		AccountDatabase accdbBN = accountDatabaseService.findByName("BN");
		if (accdbBN != null) {
			pathBN = accdbBN.getAddress();
			tokenBN = accdbBN.getToken();
			StaticPath.setPathBN(pathBN);
			StaticPath.setTokenBN(tokenBN);
		}
		AccountDatabase accdbHCM = accountDatabaseService.findByName("HCM");
		if (accdbHCM != null) {
			pathHCM = accdbHCM.getAddress();
			tokenHCM = accdbHCM.getToken();
			StaticPath.setPathHCM(pathHCM);
			StaticPath.setTokenHCM(tokenHCM);
		}

		AccountDatabase accdbDMS_MienNam = accountDatabaseService.findByName("dms_miennam");
		if (accdbDMS_MienNam != null) {
			if (!check) {
				pathDMS_MienNam = accdbDMS_MienNam.getAddress();
				tokenDMS_MienNam = accdbDMS_MienNam.getToken();
			} else {
				pathDMS_MienNam = accdbDMS_MienNam.getAddressPublic();
				tokenDMS_MienNam = accdbDMS_MienNam.getToken();
			}
			StaticPath.setPathDMS_MienNam(pathDMS_MienNam);
			StaticPath.setTokenDMS_MienNam(tokenDMS_MienNam);
		}
	}

	public boolean checkAddressPublic(HttpServletRequest request) {
		try {
			String url = request.getRequestURL().toString();
			String host = request.getServerName();
			boolean status = host.contains("lixco.com");
			System.out.println("checkAddressPublic: " + host + " -> " + status + " url: " + url);
			return status;
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

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
			}
		}
	}

}

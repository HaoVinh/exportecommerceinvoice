package trong.lixco.com.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletContext;

import trong.lixco.com.entity.MailManager;
import trong.lixco.com.entity.ServerMailLix;
import trong.lixco.com.info.DataSendMail;
import trong.lixco.com.service.MailManagerService;
import trong.lixco.com.service.ServerMailLixService;
import lixco.com.entity.YeuCauKiemTraHang;
import lixco.com.entity.YeuCauKiemTraHangDetail;

public class MyMailUtil {
	public static boolean sendMailConfirmYCKT(YeuCauKiemTraHang yeuCauKiemTraHang,
			List<YeuCauKiemTraHangDetail> yeuCauKiemTraHangDetails, String email, String linkConfirm,
			MailManagerService mailManagerService, ServerMailLixService serverMailLixService,
			DataSendMail dataSendMail, ServletContext servletContext, String tieude) {
		try {
			MailManager mailManager = mailManagerService.getMailinfoLix();
			final String mailadmin = mailManager.getMailadmin();
			final String passmailadmin = mailManager.getPassmailadmin();

			Properties props = System.getProperties();
			List<ServerMailLix> serverMailLixs = serverMailLixService.configServerMail();
			for (ServerMailLix serverMailLix : serverMailLixs) {
				props.put(serverMailLix.getKey(), serverMailLix.getValue());
			}
			Authenticator pa = null;

			if (mailadmin != null && passmailadmin != null) {
				props.put("mail.smtp.auth", "true");
				pa = new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailadmin, passmailadmin);
					}
				};
			} // else: no authentication
			Session session = Session.getInstance(props, pa);
			// — Create a new message –
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(mailadmin));

			// msg.setRecipients(Message.RecipientType.TO,
			// InternetAddress.parse(email, false));

			// — Set the subject and body text –
			msg.setSubject(MimeUtility.encodeText("[YCKTHH][" + yeuCauKiemTraHang.getRequestCode()
					+ "] Yêu cầu kiểm tra hàng hóa", "utf-8", "B"));
			String pathFileResource = "/resources/mauhtml/MailXNKiemTra.html";
			String htmlStr = "";
			if (servletContext == null) {
				htmlStr = MyReaderHTML.readHTMLFromResource(pathFileResource);
			} else {
				htmlStr = MyReaderHTML.readHTMLFromResource(servletContext, pathFileResource);
			}
			String html = caiDatGiaTri(htmlStr, yeuCauKiemTraHang, yeuCauKiemTraHangDetails, linkConfirm, dataSendMail,
					tieude);
			Multipart multipart = new MimeMultipart("alternative");
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(html, "text/html; charset=utf-8");
			multipart.addBodyPart(htmlPart);
			msg.setContent(multipart);
			// — Set some other header information –

			msg.setHeader("X-Mailer", "LOTONtechEmail");
			msg.setSentDate(new Date());
			msg.saveChanges();

			// — Send the message –
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("trong-nguyenvan@lixco.com", false));

			Transport.send(msg);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String caiDatGiaTri(String htmlStr, YeuCauKiemTraHang yeuCauKiemTraHang,
			List<YeuCauKiemTraHangDetail> chitiets, String linkConfirm, DataSendMail dataSendMail, String tieude) {
		StringBuffer strBff = new StringBuffer(htmlStr);
		strBff = replaceBuff(strBff, "${tieude}", tieude);
		strBff = replaceBuff(strBff, "${sophieu}", yeuCauKiemTraHang.getRequestCode());
		strBff = replaceBuff(strBff, "${ngay}", MyUtil.chuyensangStr(yeuCauKiemTraHang.getRequestDate()));
		strBff = replaceBuff(strBff, "${nhanvientao}", yeuCauKiemTraHang.getCreatedBy());
		StringBuffer ctBuff = new StringBuffer();
		for (int i = 0; i < chitiets.size(); i++) {
			ctBuff.append("<tr>");
			ctBuff.append("<td>");
			ctBuff.append("<b>" + chitiets.get(i).getProduct().getProduct_code() + "</b>-"
					+ chitiets.get(i).getProduct().getProduct_name());
			ctBuff.append("</td>");
			ctBuff.append("<td>");
			ctBuff.append(chitiets.get(i).getLohang());
			ctBuff.append("</td>");
			ctBuff.append("<td>");
			ctBuff.append(MyUtil.dinhdangso2sole(chitiets.get(i).getQuantity()
					/ chitiets.get(i).getProduct().getSpecification())
					+ " (Thùng)");
			ctBuff.append("</td>");
			ctBuff.append("<td>");
			ctBuff.append(chitiets.get(i).getNguongoc() == 1 ? "Hàng lưu kho"
					: chitiets.get(i).getNguongoc() == 2 ? "Hàng trả về"
							: chitiets.get(i).getNguongoc() == 3 ? "Hàng đổi trả" : "");
			ctBuff.append("</td>");
			ctBuff.append("<td>");
			ctBuff.append(chitiets.get(i).getTinhtrang());
			ctBuff.append("</td>");
			ctBuff.append("<td>");
			if (yeuCauKiemTraHang.isDakiemtra()) {
				ctBuff.append("Kết quả: <b>" + (chitiets.get(i).isKiemtradat() ? "Đạt" : "Không đạt") + "</b><br>"
						+ "Ngày gia hạn: <b>" + MyUtil.chuyensangStr(chitiets.get(i).getGiahanluukho()) + "</b><br>"
						+ "TC hóa lý sinh: <b>"
						+ (chitiets.get(i).getTieuchuan() == null ? "" : chitiets.get(i).getTieuchuan()) + "</b><br>"
						+ "Nguyên nhân: <b>" + MyUtil.removeNull(chitiets.get(i).getNguyennhan()) + "</b><br>"
						+ "Hướng giải quyết: <b>" + MyUtil.removeNull(chitiets.get(i).getHuonggiaiquyet()) + "<b>");
			}
			ctBuff.append("</td>");
			ctBuff.append("</tr>");
		}
		strBff = replaceBuff(strBff, "${chitiet}", ctBuff.toString());
		strBff = replaceBuff(strBff, "${hrefStep}", "Nhấp vào<a href=" + linkConfirm
				+ " style='font-size: 1.2em;font-weight: bold;'>  đây  <a/> để " + dataSendMail.getParam1() + ".");
		return strBff.toString();
	}

	private static StringBuffer replaceBuff(StringBuffer strBff, String oldText, String newText) {
		int start = strBff.indexOf(oldText);
		if (start != -1) {
			int end = start + oldText.length();
			strBff = strBff.replace(start, end, newText == null ? "" : newText);
		}
		return strBff;
	}
}

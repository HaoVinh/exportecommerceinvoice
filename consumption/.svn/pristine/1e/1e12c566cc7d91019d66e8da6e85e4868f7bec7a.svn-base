package lixco.com.req;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lixco.com.common.CommonModel;
import lixco.com.common.CommonService;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.interfaces.IIEInvoiceService;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Named
public class IEInvoiceHandler extends AbstractHandler {
	@Inject
	private Logger logger;
	@Inject
	private IIEInvoiceService iEInvoiceService;

	@Override
	protected int authenticate(String data, ResponseBuilder responseBuilder, StringBuilder authenErrorMsg) {
		return 0;
	}

	public void processGetQueryData(String cmd, String data, ResponseBuilder responseBuilder,
			StringBuilder respContent) {
		int err = -1;
		try {
			switch (cmd) {
			case "list_vfs":
				err = searchProvidedFox(data, responseBuilder, respContent);
				break;
			case "detail_vfs":
				err = getListDetailProviedFox(data, responseBuilder, respContent);
				break;
			default:
				responseBuilder.status(Response.Status.NOT_ACCEPTABLE);
				respContent.append(CommonService.FormatResponse(err, "Not support"));
				break;
			}
		} catch (Exception e) {
			logger.error("IEInvoiceHandler.processPostFormData:" + e.getMessage(), e);
		}
	}

	private int getListDetailProviedFox(String data, ResponseBuilder responseBuilder, StringBuilder respContent) {
		int ret = -1;
		try {
			StringBuilder messages = new StringBuilder();
			int code = authenticate(data, responseBuilder, messages);
			if (code != 0) {
				respContent.append(CommonModel.toJson(ret, messages.toString()));
				responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
			} else {
				List<Object[]> list = new ArrayList<>();
				int ck = iEInvoiceService.getListIEInvoiceDetailProvidedFox(data, list);
				if (ck == -1) {
					ret = -1;
					responseBuilder.status(Response.Status.BAD_REQUEST);
					respContent.append(CommonModel.toJson(ret, messages.toString()));
				} else {
					JsonArray jsonArrr = new JsonArray();
					JsonObject item = new JsonObject();
					for (Object[] p : list) {
//						id, masp, soluong, dongia, sotien, maso, dongiant, sotiennt, cont_no, cont_20_40, order_no,
//						soluongxk, sotienntxk, soconts, noiden, batch_code
						item.addProperty("id", Objects.toString(p[0], null));
						item.addProperty("masp", Objects.toString(p[1], null));
						item.addProperty("soluong", Double.parseDouble(Objects.toString(p[2], "0")));
						item.addProperty("dongia", Double.parseDouble(Objects.toString(p[3], "0")));
						item.addProperty("sotien", Double.parseDouble(Objects.toString(p[4], "0")));
						item.addProperty("maso", Objects.toString(p[5], null));
						item.addProperty("dongiant", Double.parseDouble(Objects.toString(p[6], "0")));
						item.addProperty("sotiennt", Double.parseDouble(Objects.toString(p[7], "0")));
						item.addProperty("cont_no", Objects.toString(p[8], null));
						item.addProperty("cont_20_40", Objects.toString(p[9], null));
						item.addProperty("order_no", Objects.toString(p[10], null));
						item.addProperty("soluongxk", Double.parseDouble(Objects.toString(p[11], "0")));
						item.addProperty("sotienntxk", Double.parseDouble(Objects.toString(p[12], "0")));
						item.addProperty("soconts", Integer.parseInt(Objects.toString(p[13], "0")));
						item.addProperty("noiden", Objects.toString(p[14], null));
						item.addProperty("batch_code", Objects.toString(p[15], null));
						jsonArrr.add(item);
					}
					ret = 0;
					responseBuilder.status(Response.Status.OK);
					Gson gson = new GsonBuilder().serializeNulls().create();
					respContent.append(CommonService.FormatResponse2(ret, "", "list_ie_invoice_detail", jsonArrr));

				}
			}
		} catch (Exception e) {
			ret = -1;
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			respContent.append(CommonModel.toJson(ret, "IEInvoiceHandler.getListDetailProviedFox:"));
			logger.error("IEInvoiceHandler.getListDetailProviedFox:" + e.getMessage(), e);
		}
		return ret;
	}

	public void processPostFormData(String cmd, String data, ResponseBuilder responseBuilder,
			StringBuilder respContent) {
		int err = -1;
		try {
			switch (cmd) {
			case "list_vfs":
				err = searchProvidedFox(data, responseBuilder, respContent);
				break;
			case "detail_vfs":
				err = getListDetailProviedFox(data, responseBuilder, respContent);
				break;
			default:
				responseBuilder.status(Response.Status.NOT_ACCEPTABLE);
				respContent.append(CommonService.FormatResponse(err, "Not support"));
				break;
			}
		} catch (Exception e) {
			logger.error("IEInvoiceHandler.processPostFormData:" + e.getMessage(), e);
		}
	}

	private int searchProvidedFox(String data, ResponseBuilder responseBuilder, StringBuilder respContent) {
		int ret = -1;
		try {
			StringBuilder messages = new StringBuilder();
			int code = authenticate(data, responseBuilder, messages);
			if (code != 0) {
				respContent.append(CommonModel.toJson(ret, messages.toString()));
				responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
			} else {
				List<Object[]> list = new ArrayList<>();
				int ck = iEInvoiceService.searchProvidedFox(data, list);
				if (ck == -1) {
					ret = -1;
					responseBuilder.status(Response.Status.BAD_REQUEST);
					respContent.append(CommonModel.toJson(ret, messages.toString()));
				} else {
					JsonArray jsonArrr = new JsonArray();
					JsonObject item = new JsonObject();
					for (Object[] p : list) {
//						id, maxn, soct, makh, ngay, makho, soxe, hesothue, htthanhtoa, sohd, dathtoan, tigia,
//						bill_no, tokhai_no, port_no, etd, donvitien, idhoadon, matk, mabx, ngoaigio, htbocxep,
//						dagiaohang, shippedper, term_deliv, load_date, portoftran, freight, ref_no, taixe, ghichu, shipmark
						item.addProperty("id", Objects.toString(p[0], null));
						item.addProperty("maxn", Objects.toString(p[1], null));
						item.addProperty("soct", Objects.toString(p[2], null));
						item.addProperty("makh", Objects.toString(p[3], null));
						item.addProperty("ngay",
								ToolTimeCustomer.convertDateToString((Date) p[4], "yyyy-MM-dd HH:mm:ss"));
						item.addProperty("makho", Objects.toString(p[5], null));
						item.addProperty("soxe", Objects.toString(p[6], null));
						item.addProperty("hesothue", Double.parseDouble(Objects.toString(p[7])));
						item.addProperty("htthanhtoa", Objects.toString(p[8], null));
						item.addProperty("sohd", Objects.toString(p[9], null));
						item.addProperty("dathtoan", Boolean.parseBoolean(Objects.toString(p[10])));
						item.addProperty("tigia", Double.parseDouble(Objects.toString(p[11])));
						item.addProperty("bill_no", Objects.toString(p[12], null));
						item.addProperty("tokhai_no", Objects.toString(p[13], null));
						item.addProperty("port_no", Objects.toString(p[14], null));
						item.addProperty("etd",
								ToolTimeCustomer.convertDateToString((Date) p[15], "yyyy-MM-dd HH:mm:ss"));
						item.addProperty("donvitien", Objects.toString(p[16], null));
						item.addProperty("idhoadon", Objects.toString(p[17], null));
						item.addProperty("matk", Objects.toString(p[18], null));
						item.addProperty("mabx", Objects.toString(p[19], null));
						item.addProperty("ngoaigio", Boolean.parseBoolean(Objects.toString(p[20])));
						item.addProperty("htbocxep", Objects.toString(p[21], null));
						item.addProperty("dagiaohang", Boolean.parseBoolean(Objects.toString(p[22])));
						item.addProperty("shippedper", Objects.toString(p[23], null));
						item.addProperty("term_deliv", Objects.toString(p[24], null));
						item.addProperty("load_date",
								ToolTimeCustomer.convertDateToString((Date) p[25], "yyyy-MM-dd HH:mm:ss"));
						item.addProperty("portoftran", Objects.toString(p[26], null));
						item.addProperty("freight", Objects.toString(p[27], null));
						item.addProperty("ref_no", Objects.toString(p[28], null));
						item.addProperty("taixe", Objects.toString(p[29], null));
						item.addProperty("ghichu", Objects.toString(p[30], null));
						item.addProperty("shipmark", Objects.toString(p[31], null));
						jsonArrr.add(item);
					}
					ret = 0;
					responseBuilder.status(Response.Status.OK);
					respContent.append(CommonService.FormatResponse(ret, "", "list_ie_invoice", jsonArrr));

				}
			}
		} catch (Exception e) {
			ret = -1;
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			respContent.append(CommonModel.toJson(ret, "IEInvoiceHandler.searchProvidedFox:"));
			logger.error("IEInvoiceHandler.searchProvidedFox:" + e.getMessage(), e);
		}
		return ret;
	}
}

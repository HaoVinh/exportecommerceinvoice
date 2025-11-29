package trong.lixco.com.api;

import io.jsonwebtoken.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import lixco.com.common.JsonParserUtil;
import lixco.com.entityapi.CustomerAsyncDTO;
import lixco.com.entityapi.DataAPI;
import lixco.com.entityapi.DeliveryPricingAsyncDTO;
import lixco.com.entityapi.PricingProgramAsyncDTO;
import lixco.com.entityapi.ProductAsyncDTO;
import lixco.com.entityapi.PromotionProgramAsyncDTO;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IDeliveryPricingService;
import lixco.com.interfaces.IPricingProgramService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IPromotionProgramService;
import lixco.com.service.DeliveryPricingService;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("data/dongbo")
public class DongBoServlet {
	@Inject
	private Logger logger;
	@Context
	private HttpServletRequest request;

	@Inject
	IPricingProgramService pricingProgramService;
	@Inject
	IPromotionProgramService promotionProgramService;

	@POST
	@Path("ctkhuyenmai")
	@Produces("application/json; charset=utf-8")
	public Response ctkhuyenmai(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				Gson gson = JsonParserUtil.getGson();
				DataAPI dataAPI = gson.fromJson(data, DataAPI.class);
				PromotionProgramAsyncDTO programAsyncDTO = gson.fromJson(dataAPI.getData(),
						PromotionProgramAsyncDTO.class);
				dongbokhuyenmai(programAsyncDTO, result, responseBuilder);
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

	private void dongbokhuyenmai(PromotionProgramAsyncDTO promotionProgramAsyncDTO, StringBuilder result,
			ResponseBuilder responseBuilder) {
		String msg = "";
		int err = 0;
		try {
			StringBuilder errors = new StringBuilder();
			promotionProgramService.dongbokhuyenmai(promotionProgramAsyncDTO, errors);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("err", 0);
			jsonObject.addProperty("msg", errors.toString());
			responseBuilder.status(Response.Status.OK);
			result.append(jsonObject);
			return;
		} catch (Exception e) {
			err = -1;
			msg = e.getMessage();
		}
		result.append(CommonService.FormatResponse(err, msg));
	}

	@POST
	@Path("ctdongia")
	@Produces("application/json; charset=utf-8")
	public Response ctdongia(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				Gson gson = JsonParserUtil.getGson();
				DataAPI dataAPI = gson.fromJson(data, DataAPI.class);
				PricingProgramAsyncDTO pricingProgramAsyncDTO = gson.fromJson(dataAPI.getData(),
						PricingProgramAsyncDTO.class);
				dongbodongia(pricingProgramAsyncDTO, result, responseBuilder);
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

	private void dongbodongia(PricingProgramAsyncDTO pricingProgramAsyncDTO, StringBuilder result,
			ResponseBuilder responseBuilder) {
		String msg = "";
		int err = 0;
		try {
			StringBuilder errors = new StringBuilder();
			pricingProgramService.dongbodongia(pricingProgramAsyncDTO, errors);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("err", 0);
			jsonObject.addProperty("msg", errors.toString());
			responseBuilder.status(Response.Status.OK);
			result.append(jsonObject);
			return;
		} catch (Exception e) {
			err = -1;
			msg = e.getMessage();
		}
		result.append(CommonService.FormatResponse(err, msg));
	}

	@POST
	@Path("khachhang")
	@Produces("application/json; charset=utf-8")
	public Response khachhang(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				Gson gson = JsonParserUtil.getGson();
				DataAPI dataAPI = gson.fromJson(data, DataAPI.class);
				CustomerAsyncDTO customerAsyncDTO = gson.fromJson(dataAPI.getData(), CustomerAsyncDTO.class);
				dongbokhachhang(customerAsyncDTO, result, responseBuilder);
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
	ICustomerService customerService;

	private void dongbokhachhang(CustomerAsyncDTO customerAsyncDTO, StringBuilder result,
			ResponseBuilder responseBuilder) {
		String msg = "";
		int err = 0;
		try {
			StringBuilder errors = new StringBuilder();
			customerService.dongbokhachhang(customerAsyncDTO, errors);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("err", 0);
			jsonObject.addProperty("msg", errors.toString());
			responseBuilder.status(Response.Status.OK);
			result.append(jsonObject);
			return;
		} catch (Exception e) {
			err = -1;
			msg = e.getMessage();
		}
		result.append(CommonService.FormatResponse(err, msg));
	}

	@Inject
	IProductService productService;

	@POST
	@Path("sanpham")
	@Produces("application/json; charset=utf-8")
	public Response sanpham(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				Gson gson = JsonParserUtil.getGson();
				DataAPI dataAPI = gson.fromJson(data, DataAPI.class);
				ProductAsyncDTO productAsyncDTO = gson.fromJson(dataAPI.getData(), ProductAsyncDTO.class);
				dongbosanpham(productAsyncDTO, result, responseBuilder);
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

	private void dongbosanpham(ProductAsyncDTO productAsyncDTO, StringBuilder result, ResponseBuilder responseBuilder) {
		String msg = "";
		int err = 0;
		try {
			StringBuilder errors = new StringBuilder();
			productService.dongbosanpham(productAsyncDTO, errors);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("err", 0);
			jsonObject.addProperty("msg", errors.toString());
			responseBuilder.status(Response.Status.OK);
			result.append(jsonObject);
			return;
		} catch (Exception e) {
			err = -1;
			msg = e.getMessage();
		}
		result.append(CommonService.FormatResponse(err, msg));
	}

	@POST
	@Path("dongiavc")
	@Produces("application/json; charset=utf-8")
	public Response dongiavc(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				Gson gson = JsonParserUtil.getGson();
				DataAPI dataAPI = gson.fromJson(data, DataAPI.class);
				DeliveryPricingAsyncDTO deliveryPricingAsyncDTO = gson.fromJson(dataAPI.getData(),
						DeliveryPricingAsyncDTO.class);
				dongbodongiavc(deliveryPricingAsyncDTO, result, responseBuilder);
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
	IDeliveryPricingService deliveryPricingService;

	private void dongbodongiavc(DeliveryPricingAsyncDTO deliveryPricingAsyncDTO, StringBuilder result,
			ResponseBuilder responseBuilder) {
		String msg = "";
		int err = 0;
		try {
			StringBuilder errors = new StringBuilder();
			deliveryPricingService.dongbodongiavc(deliveryPricingAsyncDTO, errors);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("err", 0);
			jsonObject.addProperty("msg", errors.toString());
			responseBuilder.status(Response.Status.OK);
			result.append(jsonObject);
			return;
		} catch (Exception e) {
			err = -1;
			msg = e.getMessage();
		}
		result.append(CommonService.FormatResponse(err, msg));
	}

}

package lixco.com.req;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;

import com.google.gson.JsonObject;

import lixco.com.common.CommonModel;
import lixco.com.common.CommonService;
import lixco.com.common.HolderParser;
import lixco.com.common.JsonParserUtil;
import lixco.com.common.ToolTimeCustomer;
import lixco.com.entity.Customer;
import lixco.com.entity.DeliveryPricing;
import lixco.com.entity.IECategories;
import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.entity.PricingProgram;
import lixco.com.entity.PricingProgramDetail;
import lixco.com.entity.Product;
import lixco.com.entity.PromotionOrderDetail;
import lixco.com.entity.PromotionProgramDetail;
import lixco.com.entity.Warehouse;
import lixco.com.interfaces.ICustomerPricingProgramService;
import lixco.com.interfaces.ICustomerPromotionProgramService;
import lixco.com.interfaces.ICustomerService;
import lixco.com.interfaces.IDeliveryPricingService;
import lixco.com.interfaces.IIECategoriesService;
import lixco.com.interfaces.IOrderLixService;
import lixco.com.interfaces.IPricingProgramDetailService;
import lixco.com.interfaces.IProcessLogicOrderService;
import lixco.com.interfaces.IProductService;
import lixco.com.interfaces.IPromotionProgramDetailService;
import lixco.com.interfaces.IPromotionalPricingService;
import lixco.com.interfaces.IWarehouseService;
import lixco.com.reqInfo.CustomerPricingProgramReqInfo;
import lixco.com.reqInfo.CustomerPromotionProgramReqInfo;
import lixco.com.reqInfo.OrderLixData;
import lixco.com.reqInfo.OrderLixData.OrderDetailData;
import lixco.com.reqInfo.OrderSearchInfo;
import lixco.com.reqInfo.OrderSearchInfo.OrderInfo;
import lixco.com.reqInfo.OrderSearchInfo.Page;
import lixco.com.reqInfo.PricingProgramDetailReqInfo;
import lixco.com.reqInfo.ProductReqInfo;
import lixco.com.reqInfo.PromotionalPricingReqInfo;
import lixco.com.reqInfo.WrapOrderDetailNppReqInfo;
import lixco.com.reqInfo.WrapOrderNppReqInfo;

@Named
public class OrderLixHandler extends AbstractHandler {
	@Inject
	private Logger logger;
	@Inject
	private IProcessLogicOrderService processLogicOrderService;
	@Inject
	private IProductService productService;
	@Inject
	private ICustomerService customerService;
	@Inject
	private IDeliveryPricingService deliveryPricingService;
	@Inject
	private ICustomerPricingProgramService customerPricingProgramService;
	@Inject
	private ICustomerPromotionProgramService customerPromotionProgramService;
	@Inject
	private IPricingProgramDetailService pricingProgramDetailService;
	@Inject
	private IPromotionProgramDetailService promotionProgramDetailService;
	@Inject
	private IPromotionalPricingService promotionalPricingService;
	@Inject
	private IIECategoriesService iECategoriesService;
	@Inject
	private IWarehouseService warehouseService;
	@Inject
	private IOrderLixService orderLixService;

	public void processGetQueryData(String cmd, String data, ResponseBuilder responseBuilder,
			StringBuilder respContent) {
		int err = -1;
		try {
			switch (cmd) {
			case "search":
				err = search(data, responseBuilder, respContent);
				break;
			case "detail":
				err = search(data, responseBuilder, respContent);
				break;
			default:
				responseBuilder.status(Response.Status.NOT_ACCEPTABLE);
				respContent.append(CommonService.FormatResponse(err, "Not support"));
				break;
			}
		} catch (Exception e) {
			logger.error("OrderLixHandler.processPostFormData:" + e.getMessage(), e);
		}
	}

	private int search(String data, ResponseBuilder responseBuilder, StringBuilder respContent) {
		int res=-1;
		try{
			
		}catch (Exception e) {
			logger.error("OrderLixHandler.search:"+e.getMessage(),e);
		}
		return res;
	}
	public int getListDetail(String data, ResponseBuilder responseBuilder, StringBuilder respContent) {
		int res=-1;
		try{
			/*{ order_info:{from_date:'',to_date:'',customer_id:0,order_code:'',voucher_code:'',ie_categories_id:0,po_no:'',delivered:-1,status:-1}, page:{page_index:0, page_size:0}}*/
			JsonObject json=new JsonObject();
//			JsonParserUtil.getGson(type, messages)
//			orderLixService.search(json, page, list);
		}catch (Exception e) {
			logger.error("OrderLixHandler.getListDetail:"+e.getMessage(),e);
		}
		return res;
	}
	public void processPostFormData(String cmd, String data, ResponseBuilder responseBuilder,
			StringBuilder respContent) {
		int err = -1;
		try {
			switch (cmd) {
			case "save_update_order_npp":
				err=saveOrUpdate(data, responseBuilder, respContent);
				break;
			case "del_order_npp":
				err=deleteOrderNpp(data,responseBuilder,respContent);
				break;
			case "save_update_detail_npp":
				err=saveOrUpdateDetailNPP(data,responseBuilder,respContent);
				break;
			case "del_detail_npp":
				err=deleteOrderDetailNpp(data,responseBuilder,respContent);
				break;
			default:
				responseBuilder.status(Response.Status.NOT_ACCEPTABLE);
				respContent.append(CommonService.FormatResponse(err, "Not support"));
				break;
			}
		} catch (Exception e) {
			logger.error("OrderLixHandler.processPostFormData:" + e.getMessage(), e);
		}
	}
	private int deleteOrderDetailNpp(String data, ResponseBuilder responseBuilder, StringBuilder respContent) {
		int ret=-1;
		try{
			//{npp_order_detail_id:0}
			StringBuilder messages=new StringBuilder();
			int code=authenticate(data, responseBuilder, messages);
			if(code!=0){
				respContent.append(CommonModel.toJson(code, messages.toString()));
				responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
			}else{
				JsonObject json=JsonParserUtil.getGson().fromJson(data,JsonObject.class);
				HolderParser hNppOrderdetailId=JsonParserUtil.getValueNumber(json, "npp_order_detail_id", null);
				long nppOrderDetailId=Long.parseLong(Objects.toString(hNppOrderdetailId.getValue()));
				if(nppOrderDetailId<=0){
					ret=-1;
					respContent.append(CommonModel.toJson(ret, "npp_order_detail_id not exsist"));
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
					return ret;
				}
				ret=processLogicOrderService.deleteNppOrderDetail(nppOrderDetailId, messages);
				if(ret==-1){
					responseBuilder.status(Response.Status.BAD_REQUEST);
					respContent.append(CommonModel.toJson(ret, messages.toString()));
				}else{
					responseBuilder.status(Response.Status.OK);
					respContent.append(CommonModel.toJson(ret, "deleted successfully"));
				}
			}
		}catch (Exception e) {
			ret=-1;
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			respContent.append(CommonModel.toJson(ret,"OrderLixHandler.deleteOrderDetailNpp:" + e.getMessage()));
			logger.error("OrderLixHandler.deleteOrderDetailNpp:"+e.getMessage(),e);
		}
		return ret;
	}

	private int saveOrUpdateDetailNPP(String data, ResponseBuilder responseBuilder, StringBuilder respContent) {
		int ret=-1;
		try{
//			{id:0,created_date:'',created_by:'',product_code:'', box_quantity:0,promotion_forms:0}
			StringBuilder messages = new StringBuilder();
			int code=authenticate(data, responseBuilder, messages);
			if(code!=0){
				respContent.append(CommonModel.toJson(code, messages.toString()));
				responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
			}else{
				OrderDetailData orderDetailData = JsonParserUtil.getGson(OrderDetailData.class, messages).fromJson(data,
						OrderDetailData.class);
				if(orderDetailData==null){
					ret = -1;
					respContent.append(CommonModel.toJson(ret, messages.toString()));
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
					return ret;
				}
				OrderLix orderLix=orderLixService.selectOnlyId(orderDetailData.getOrder_id());
				if(orderLix ==null){
					ret=-1;
					respContent.append("npp_order_id not found");
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
					return ret;
				}
				OrderDetail detail = new OrderDetail();
				detail.setBox_quantity(orderDetailData.getBox_quantity());
				ProductReqInfo pr=new ProductReqInfo();
				productService.selectByCode(orderDetailData.getProduct().getProduct_code(),pr);
				Product product=pr.getProduct();
				if (product == null) {
					ret = -1;
					respContent.append(CommonModel.toJson(ret, "product not found product_code:" +orderDetailData.getProduct().getProduct_code()));
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
					return ret;
				}
				detail.setProduct(product);
				double quantity=BigDecimal.valueOf(orderDetailData.getBox_quantity()).multiply(BigDecimal.valueOf(product.getSpecification())).doubleValue();
				detail.setQuantity(quantity);
				detail.setCreated_date(orderDetailData.getCreated_date());
				detail.setCreated_by(orderDetailData.getCreated_by());
				detail.setLast_modifed_by(orderDetailData.getLast_modifed_by());
				detail.setLast_modifed_date(orderDetailData.getLast_modifed_date());
				detail.setPromotion_forms(orderDetailData.getPromotion_forms());
				detail.setOrder_lix(orderLix);
				detail.setNpp_order_detail_id(orderDetailData.getId());
				WrapOrderDetailNppReqInfo t=new WrapOrderDetailNppReqInfo();
				t.setOrder_detail(detail);
				// cập nhật đơn giá sản phẩm
				PricingProgram pricingProgram=orderLix.getPricing_program();
				if(pricingProgram!=null){
					PricingProgramDetailReqInfo dt = new PricingProgramDetailReqInfo();
					pricingProgramDetailService.findSettingPricingChild(pricingProgram.getId(),
							detail.getProduct().getId(), dt,detail.getOrder_lix().getOrder_date());
					if (dt.getPricing_program_detail() == null) {
						pricingProgramDetailService.findSettingPricing(pricingProgram.getId(),
								detail.getProduct().getId(), dt);
					}
					PricingProgramDetail ppd = dt.getPricing_program_detail();
					detail.setUnit_price(ppd == null ? 0 : ppd.getUnit_price());
					detail.setUnit_price_goc(ppd == null ? 0 : ppd.getUnit_price());
				}
				// tính sản phẩm khuyến mãi.
				if (detail.getPromotion_forms() != 0 && orderLix.getPromotion_program()!=null) {
					List<PromotionOrderDetail> listPromotionOrderDetail=new ArrayList<>();
					//{product_id:0,promotion_product_id:0,promotion_program_id:0,promotion_form:0}
					JsonObject js = new JsonObject();
					js.addProperty("product_id", detail.getProduct().getId());
					js.addProperty("promotion_program_id", orderLix.getPromotion_program().getId());
					js.addProperty("promotion_form", detail.getPromotion_forms());
					List<PromotionProgramDetail> listPromotionProgramDetail = new ArrayList<>();
					promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(js),
							listPromotionProgramDetail);
					for (PromotionProgramDetail ppd : listPromotionProgramDetail) {
						PromotionOrderDetail pod = new PromotionOrderDetail();
						pod.setOrder_detail(detail);
						pod.setProduct(ppd.getPromotion_product());
						// tim nạp đơn giá sản phẩm khuyến mãi
						double quantityPM = BigDecimal.valueOf(detail.getBox_quantity()).multiply(BigDecimal.valueOf(ppd.getPromotion_quantity())).divide(BigDecimal.valueOf(ppd.getBox_quatity())).doubleValue();
						pod.setQuantity(quantityPM);
						pod.setCreated_date(detail.getCreated_date());
						pod.setCreated_by(detail.getCreated_by());
						pod.setOrder_detail(detail);
						// tim nạp đơn giá sản phẩm khuyến mãi
						PromotionalPricingReqInfo ppr = new PromotionalPricingReqInfo();
						JsonObject jpp = new JsonObject();
						jpp.addProperty("date", ToolTimeCustomer.convertDateToString(orderLix.getDelivery_date(), "dd/MM/yyyy"));
						jpp.addProperty("product_id", ppd.getPromotion_product().getId());
						promotionalPricingService.findSettingPromotionalPricing(JsonParserUtil.getGson().toJson(jpp), ppr);
						if (ppr.getPromotional_pricing() != null) {
							pod.setUnit_price(ppr.getPromotional_pricing().getUnit_price());
						}
						listPromotionOrderDetail.add(pod);
					}
					t.setList_promotion_order_detail(listPromotionOrderDetail);
				}
				ret=processLogicOrderService.saveOrUpdateDetailNpp(t, messages);
				if(ret==-1){
					responseBuilder.status(Response.Status.BAD_REQUEST);
					respContent.append(CommonModel.toJson(ret, messages.toString()));
				}else{
					responseBuilder.status(Response.Status.OK);
					JsonObject result=new JsonObject();
					result.addProperty("id",detail.getId());
					respContent.append(CommonService.FormatResponse(ret, "", "order_detail", result));
				}
			}
			
		}catch (Exception e) {
			ret=-1;
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			respContent.append(CommonService.FormatResponse(ret,"OrderLixHandler.saveOrUpdateDetailNPP:" + e.getMessage()));
			logger.error("OrderLixHandler.saveOrUpdateDetailNPP:" + e.getMessage(), e);
		}
		return ret;
	}

	private int deleteOrderNpp(String data, ResponseBuilder responseBuilder, StringBuilder respContent){
		int ret=-1;
		try{/*{npp_order_id:0}*/
			StringBuilder messages=new StringBuilder();
			int code=authenticate(data, responseBuilder, messages);
			if(code!=0){
				respContent.append(CommonModel.toJson(code, messages.toString()));
				responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
			}else{
				JsonObject json=JsonParserUtil.getGson().fromJson(data, JsonObject.class);
				HolderParser hNppOrderId=JsonParserUtil.getValueNumber(json, "npp_order_id", null);
				long nppOrderId=Long.parseLong(Objects.toString(hNppOrderId.getValue()));
				if(nppOrderId<=0){
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
					ret=-1;
					respContent.append(CommonService.FormatResponse(ret,"npp_order_id not exists"));
					return ret;
				}
				ret=processLogicOrderService.deleteNppOrder(nppOrderId, messages);
				if(ret==-1){
					respContent.append(CommonModel.toJson(ret, messages.toString()));
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
				}else{
					respContent.append(CommonModel.toJson(ret,"deleted successfully"));
					responseBuilder = Response.status(Response.Status.OK);
				}
			}
		}catch (Exception e) {
			ret=-1;
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			respContent.append(CommonModel.toJson(ret,"OrderLixHandler.deleteOrderLix:" + e.getMessage()));
			logger.error("OrderLixHandler.deleteOrderLix:"+e.getMessage(),e);
		}
		return ret;
	}

	private int saveOrUpdate(String data, ResponseBuilder responseBuilder, StringBuilder respContent) {
		int ret = -1;
		try {
			StringBuilder messages = new StringBuilder();
			int code = authenticate(data, responseBuilder, messages);
			if (code != 0) {
				respContent.append(CommonModel.toJson(ret, messages.toString()));
				responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
			} else {
				//{ id:0, order_code:'', created_date:'' created_by:'',last_modifed_date:'', last_modifed_by:'', order_date:'',
				//delivery_date:'', customer:{ customer_code:'' },
				//list_order_detail:[{id:0,created_date:'',created_by:'',product_code:'', box_quantity:0,promotion_forms:0}] }
				OrderLixData orderLixData = JsonParserUtil.getGson(OrderLixData.class, messages).fromJson(data,
						OrderLixData.class);
				if (orderLixData == null) {
					ret = -1;
					respContent.append(CommonModel.toJson(ret, messages.toString()));
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
					return ret;
				}
				OrderLix orderLix = new OrderLix();
				List<OrderDetail> listDetail = new ArrayList<OrderDetail>();
				List<PromotionOrderDetail> listPromotionOrderDetail = new ArrayList<>();
				WrapOrderNppReqInfo info = new WrapOrderNppReqInfo(orderLix, listDetail, listPromotionOrderDetail);
				orderLix.setNpp_order_id(orderLixData.getId());
				orderLix.setVoucher_code(orderLixData.getOrder_code());
				orderLix.setCreated_date(orderLixData.getCreated_date());
				orderLix.setCreated_by(orderLixData.getCreated_by());
				orderLix.setLast_modifed_by(orderLixData.getLast_modifed_by());
				orderLix.setLast_modifed_date(orderLixData.getLast_modifed_date());
				orderLix.setOrder_date(orderLixData.getOrder_date());
				orderLix.setDelivery_date(orderLixData.getDelivery_date());
				Customer customer=customerService.selectOnlyId(orderLixData.getCustomer().getCustomer_code());
				if (customer == null) {
					respContent.append(CommonModel.toJson(ret, "customer not found!"));
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
					return ret;
				}
				orderLix.setCustomer(customer);
				//cập nhật danh mục nhập xuất là xuất nội đia
				IECategories ie= iECategoriesService.selectByCodeOld("X");
				if(ie==null){
					ret=-1;
					respContent.append(CommonModel.toJson(ret, "iecategories not found!"));
					responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
					return ret;
				}
				orderLix.setIe_categories(ie);
				Warehouse w=warehouseService.selectByCodeOld("F");
				if(w==null){
					ret=-1;
					respContent.append(CommonModel.toJson(ret, "warehouse not found!"));
					responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
					return ret;
				}
				orderLix.setWarehouse(w);
				// {order_date:'',customer_id:0} 
				// cập nhật chương trình đơn giá cho đơn hàng
				JsonObject json = new JsonObject();
				json.addProperty("date",ToolTimeCustomer.convertDateToString(orderLixData.getDelivery_date(), "dd/MM/yyyy"));
				json.addProperty("customer_id", customer.getId());
				CustomerPricingProgramReqInfo t = new CustomerPricingProgramReqInfo();
				customerPricingProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t);
				//cập nhật chương trình khuyến mãi cho khách hàng
				CustomerPromotionProgramReqInfo t1 = new CustomerPromotionProgramReqInfo();
				customerPromotionProgramService.selectForCustomer(JsonParserUtil.getGson().toJson(json), t1);
				info.setOrder_lix(orderLix);
				for (OrderDetailData d : orderLixData.getList_order_detail()) {
					OrderDetail detail = new OrderDetail();
					detail.setBox_quantity(d.getBox_quantity());
					ProductReqInfo pr=new ProductReqInfo();
					productService.selectByCode(d.getProduct().getProduct_code(),pr);
					Product product=pr.getProduct();
					if (product == null) {
						ret = -1;
						respContent.append(CommonModel.toJson(ret, "product not found product_code:" + d.getProduct().getProduct_code()));
						responseBuilder = Response.status(Response.Status.BAD_REQUEST);
						return ret;
					}
					detail.setProduct(product);
					double quantity=BigDecimal.valueOf(d.getBox_quantity()).multiply(BigDecimal.valueOf(product.getSpecification())).doubleValue();
					detail.setQuantity(quantity);
					detail.setCreated_date(d.getCreated_date());
					detail.setCreated_by(d.getCreated_by());
					detail.setLast_modifed_by(d.getLast_modifed_by());
					detail.setLast_modifed_date(d.getLast_modifed_date());
					detail.setPromotion_forms(d.getPromotion_forms());
					detail.setOrder_lix(orderLix);
					detail.setNpp_order_detail_id(d.getId());
					listDetail.add(detail);
				}
				if (listDetail.size() == 0) {
					ret = -1;
					respContent.append(CommonModel.toJson(ret, "list_order_detail is empty!"));
					responseBuilder = Response.status(Response.Status.BAD_REQUEST);
				}
				// nạp chương trình đơn giá
				if (t.getCustomer_pricing_program() != null) {
					PricingProgram pricingProgram = t.getCustomer_pricing_program().getPricing_program();
					orderLix.setPricing_program(pricingProgram);
					// cập nhật đơn giá sản phẩm trong đơn hàng
					for (OrderDetail d : listDetail) {
						// cập nhật đơn giá sản phẩm
						PricingProgramDetailReqInfo dt = new PricingProgramDetailReqInfo();
						pricingProgramDetailService.findSettingPricingChild(pricingProgram.getId(),
								d.getProduct().getId(), dt,d.getOrder_lix().getOrder_date());
						if (dt.getPricing_program_detail() == null) {
							pricingProgramDetailService.findSettingPricing(pricingProgram.getId(),
									d.getProduct().getId(), dt);
						}
						PricingProgramDetail ppd = dt.getPricing_program_detail();
						d.setUnit_price(ppd == null ? 0 : ppd.getUnit_price());
						d.setUnit_price_goc(ppd == null ? 0 : ppd.getUnit_price());
					}
				} else {
					orderLix.setPricing_program(null);
					for (OrderDetail d : listDetail) {
						// cập nhật đơn giá sản phẩm
						d.setUnit_price(0);
						d.setUnit_price_goc(0);
					}
				}
				//nạp chương trình khuyến mãi 
				if (t1.getCustomer_promotion_program() != null) {
					orderLix.setPromotion_program(t1.getCustomer_promotion_program().getPromotion_program());
					long promotionProgramId = orderLix.getPromotion_program().getId();
					for (OrderDetail d : listDetail) {
						// tính sản phẩm khuyến mãi.
						if (d.getPromotion_forms() != 0) {
							//{product_id:0,promotion_product_id:0,promotion_program_id:0,promotion_form:0}
							JsonObject js = new JsonObject();
							js.addProperty("product_id", d.getProduct().getId());
							js.addProperty("promotion_program_id", promotionProgramId);
							js.addProperty("promotion_form", d.getPromotion_forms());
							List<PromotionProgramDetail> listPromotionProgramDetail = new ArrayList<>();
							promotionProgramDetailService.selectBy(JsonParserUtil.getGson().toJson(js),
									listPromotionProgramDetail);
							for (PromotionProgramDetail ppd : listPromotionProgramDetail) {
								PromotionOrderDetail pod = new PromotionOrderDetail();
								pod.setOrder_detail(d);
								pod.setProduct(ppd.getPromotion_product());
								// tim nạp đơn giá sản phẩm khuyến mãi
								double quantity = BigDecimal.valueOf(d.getBox_quantity()).multiply(BigDecimal.valueOf(ppd.getPromotion_quantity())).divide(BigDecimal.valueOf(ppd.getBox_quatity())).doubleValue();
								pod.setQuantity(quantity);
								pod.setCreated_date(d.getCreated_date());
								pod.setCreated_by(d.getCreated_by());
								pod.setOrder_detail(d);
								// tim nạp đơn giá sản phẩm khuyến mãi
								PromotionalPricingReqInfo ppr = new PromotionalPricingReqInfo();
								JsonObject jpp = new JsonObject();
								jpp.addProperty("date", ToolTimeCustomer.convertDateToString(orderLix.getDelivery_date(), "dd/MM/yyyy"));
								jpp.addProperty("product_id", ppd.getPromotion_product().getId());
								promotionalPricingService.findSettingPromotionalPricing(JsonParserUtil.getGson().toJson(jpp), ppr);
								if (ppr.getPromotional_pricing() != null) {
									pod.setUnit_price(ppr.getPromotional_pricing().getUnit_price());
								}
								listPromotionOrderDetail.add(pod);
							}
						}
					}
				} else {
					orderLix.setPromotion_program(null);
				}
				// tìm địa điểm đơn giá giao hàng
				JsonObject json1 = new JsonObject();
				json1.addProperty("customer_id", customer.getId());
				json1.addProperty("disable", 0);
				List<DeliveryPricing> list = new ArrayList<>();
				deliveryPricingService.seletcBy(JsonParserUtil.getGson().toJson(json1), list);
				// nạp địa điểm giao hàng và đơn giá giao hàng
				if (list.size() > 0) {
					orderLix.setDelivery_pricing(list.get(0));
				} else {
					orderLix.setDelivery_pricing(null);
				}
				int ck=processLogicOrderService.saveOrUpdate(info, messages);
				if(ck==-1){
					ret=-1;
					responseBuilder.status(Response.Status.BAD_REQUEST);
					respContent.append(CommonModel.toJson(ret, messages.toString()));
				}else{
					ret=0;
					responseBuilder.status(Response.Status.OK);
					JsonObject result=new JsonObject();
					result.addProperty("id",orderLix.getId());
					respContent.append(CommonService.FormatResponse(ret, "", "order_lix", result));
				}
			}
		} catch (Exception e) {
			ret=-1;
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
			respContent.append(CommonModel.toJson(ret,"OrderLixHandler.insertOrderLix:" + e.getMessage()));
			logger.error("OrderLixHandler.insertOrderLix:" + e.getMessage(), e);
		}
		return ret;
	}

	@Override
	protected int authenticate(String data, ResponseBuilder responseBuilder, StringBuilder authenErrorMsg) {
		try {
			// continue
			responseBuilder.status(Response.Status.OK);
		} catch (Exception e) {
			responseBuilder.status(Response.Status.UNAUTHORIZED);
			authenErrorMsg.append("authentication error");
			logger.error("OrderLixHandler.authenticate:" + e.getMessage(), e);
		}
		return 0;
	}
}

package lixco.com.reqInfo;

import java.util.List;

import lixco.com.entity.OrderDetail;
import lixco.com.entity.PromotionOrderDetail;

public class WrapOrderDetailNppReqInfo {
	private OrderDetail order_detail=null;
	private List<PromotionOrderDetail> list_promotion_order_detail;
	
	public WrapOrderDetailNppReqInfo() {
	}
	
	public WrapOrderDetailNppReqInfo(OrderDetail order_detail, List<PromotionOrderDetail> list_promotion_order_detail) {
		this.order_detail = order_detail;
		this.list_promotion_order_detail = list_promotion_order_detail;
	}
	public OrderDetail getOrder_detail() {
		return order_detail;
	}
	public void setOrder_detail(OrderDetail order_detail) {
		this.order_detail = order_detail;
	}
	public List<PromotionOrderDetail> getList_promotion_order_detail() {
		return list_promotion_order_detail;
	}
	public void setList_promotion_order_detail(List<PromotionOrderDetail> list_promotion_order_detail) {
		this.list_promotion_order_detail = list_promotion_order_detail;
	}
}

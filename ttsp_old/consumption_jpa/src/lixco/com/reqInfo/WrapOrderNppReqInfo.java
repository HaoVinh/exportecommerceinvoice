package lixco.com.reqInfo;

import java.util.List;

import lixco.com.entity.OrderDetail;
import lixco.com.entity.OrderLix;
import lixco.com.entity.PromotionOrderDetail;

public class WrapOrderNppReqInfo {
	private OrderLix order_lix;
	private List<OrderDetail> list_order_detail;
	private List<PromotionOrderDetail> list_promotion_order_detail;
	
	public WrapOrderNppReqInfo() {
	}
	
	public WrapOrderNppReqInfo(OrderLix order_lix, List<OrderDetail> list_order_detail,
			List<PromotionOrderDetail> list_promotion_order_detail) {
		this.order_lix = order_lix;
		this.list_order_detail = list_order_detail;
		this.list_promotion_order_detail = list_promotion_order_detail;
	}

	public OrderLix getOrder_lix() {
		return order_lix;
	}
	public void setOrder_lix(OrderLix order_lix) {
		this.order_lix = order_lix;
	}
	public List<OrderDetail> getList_order_detail() {
		return list_order_detail;
	}
	public void setList_order_detail(List<OrderDetail> list_order_detail) {
		this.list_order_detail = list_order_detail;
	}
	public List<PromotionOrderDetail> getList_promotion_order_detail() {
		return list_promotion_order_detail;
	}
	public void setList_promotion_order_detail(List<PromotionOrderDetail> list_promotion_order_detail) {
		this.list_promotion_order_detail = list_promotion_order_detail;
	}
}

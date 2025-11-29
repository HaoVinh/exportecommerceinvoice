package lixco.com.reqInfo;


import lixco.com.entity.OrderDetail;

public class WrapOrderDetailLixReqInfo implements Cloneable{
	private OrderDetail order_detail=null;
	private double export_quantity;//số lượng thùng xuất
	private double export_unit_quantity;//số lượng xuất đơn vị tính
	private double total_unit_quantity;//số lượng xuất đơn vị tính
	private double real_quantity;//số thùng thực xuất
	private double remain_quantity;//sô lượng còn lại chưa xuất
	
	public WrapOrderDetailLixReqInfo() {
	}
	public WrapOrderDetailLixReqInfo(OrderDetail order_detail) {
		this.order_detail = order_detail;
	}

	public OrderDetail getOrder_detail() {
		return order_detail;
	}
	public void setOrder_detail(OrderDetail order_detail) {
		this.order_detail = order_detail;
	}
	
	public double getExport_quantity() {
		return export_quantity;
	}
	public void setExport_quantity(double export_quantity) {
		this.export_quantity = export_quantity;
	}
	public double getReal_quantity() {
		return real_quantity;
	}
	public void setReal_quantity(double real_quantity) {
		this.real_quantity = real_quantity;
	}
	public double getRemain_quantity() {
		return remain_quantity;
	}
	public void setRemain_quantity(double remain_quantity) {
		this.remain_quantity = remain_quantity;
	}
	public double getExport_unit_quantity() {
		return export_unit_quantity;
	}
	public void setExport_unit_quantity(double export_unit_quantity) {
		this.export_unit_quantity = export_unit_quantity;
	}
	
	public double getTotal_unit_quantity() {
		return total_unit_quantity;
	}
	public void setTotal_unit_quantity(double total_unit_quantity) {
		this.total_unit_quantity = total_unit_quantity;
	}
	@Override
	public WrapOrderDetailLixReqInfo clone() throws CloneNotSupportedException {
		try{
			WrapOrderDetailLixReqInfo cloned=new WrapOrderDetailLixReqInfo();
			cloned.setOrder_detail(order_detail==null ? null : order_detail.clone());
			cloned.setReal_quantity(real_quantity);
			cloned.setExport_quantity(export_quantity);
			cloned.setRemain_quantity(remain_quantity);
			return cloned;
		}catch (Exception e) {
		}
		return null;
	}
	
	
}

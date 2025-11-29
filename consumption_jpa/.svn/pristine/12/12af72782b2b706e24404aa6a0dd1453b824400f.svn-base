package lixco.com.reqInfo;

import java.util.ArrayList;
import java.util.List;

import lixco.com.entity.ExportBatchOD;
import lixco.com.entity.OrderDetail;

public class WrapOrderDetailReqInfo  implements Cloneable{
	private OrderDetail order_detail =null;
	private List<ExportBatchOD> list_export_batch =null;
	public WrapOrderDetailReqInfo() {
	}
	public WrapOrderDetailReqInfo(OrderDetail order_detail, List<ExportBatchOD> list_export_batch) {
		this.order_detail = order_detail;
		this.list_export_batch = list_export_batch;
	}
	public OrderDetail getOrder_detail() {
		return order_detail;
	}
	public void setOrder_detail(OrderDetail order_detail) {
		this.order_detail = order_detail;
	}
	public List<ExportBatchOD> getList_export_batch() {
		return list_export_batch;
	}
	public void setList_export_batch(List<ExportBatchOD> list_export_batch) {
		this.list_export_batch = list_export_batch;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((order_detail == null) ? 0 : order_detail.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WrapOrderDetailReqInfo other = (WrapOrderDetailReqInfo) obj;
		if (order_detail == null) {
			if (other.order_detail != null)
				return false;
		} else if (!order_detail.equals(other.order_detail))
			return false;
		return true;
	}
	@Override
	public WrapOrderDetailReqInfo clone() throws CloneNotSupportedException {
		WrapOrderDetailReqInfo w=new WrapOrderDetailReqInfo();
		w.setOrder_detail(order_detail==null ? null : order_detail.clone());
		if(list_export_batch !=null){
			List<ExportBatchOD> list=new ArrayList<ExportBatchOD>();
			w.setList_export_batch(list);
			for(ExportBatchOD ex:list_export_batch){
				list.add(ex.clone());
			}
		}
		return w;
	}
	
}

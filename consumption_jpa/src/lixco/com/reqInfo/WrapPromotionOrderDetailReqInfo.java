package lixco.com.reqInfo;

import java.util.ArrayList;
import java.util.List;

import lixco.com.entity.ExportBatchPOD;
import lixco.com.entity.PromotionOrderDetail;

public class WrapPromotionOrderDetailReqInfo implements Cloneable{
	private PromotionOrderDetail promotion_order_detail=null;
	private List<ExportBatchPOD> list_export_batch=null;
	public WrapPromotionOrderDetailReqInfo() {
	}
	
	public WrapPromotionOrderDetailReqInfo(PromotionOrderDetail promotion_order_detail,List<ExportBatchPOD> list_export_batch) {
		this.promotion_order_detail = promotion_order_detail;
		this.list_export_batch = list_export_batch;
	}

	public PromotionOrderDetail getPromotion_order_detail() {
		return promotion_order_detail;
	}
	public void setPromotion_order_detail(PromotionOrderDetail promotion_order_detail) {
		this.promotion_order_detail = promotion_order_detail;
	}
	public List<ExportBatchPOD> getList_export_batch() {
		return list_export_batch;
	}
	public void setList_export_batch(List<ExportBatchPOD> list_export_batch) {
		this.list_export_batch = list_export_batch;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((promotion_order_detail == null) ? 0 : promotion_order_detail.hashCode());
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
		WrapPromotionOrderDetailReqInfo other = (WrapPromotionOrderDetailReqInfo) obj;
		if (promotion_order_detail == null) {
			if (other.promotion_order_detail != null)
				return false;
		} else if (!promotion_order_detail.equals(other.promotion_order_detail))
			return false;
		return true;
	}
	@Override
	public WrapPromotionOrderDetailReqInfo clone() throws CloneNotSupportedException {
		WrapPromotionOrderDetailReqInfo w=new WrapPromotionOrderDetailReqInfo();
		w.setPromotion_order_detail(this.promotion_order_detail==null ? null :promotion_order_detail.clone());
		if(list_export_batch !=null){
			List<ExportBatchPOD> list=new ArrayList<ExportBatchPOD>();
			w.setList_export_batch(list);
			for(ExportBatchPOD ex:list_export_batch){
				list.add(ex.clone());
			}
		}
		return w;
	}
	
}

package lixco.com.reqInfo;

import java.util.List;

import lixco.com.entity.GoodsReceiptNote;
import lixco.com.entity.GoodsReceiptNoteDetail;

public class WrapListGoodsReceiptNoteDetailReqInfo {
	private List<GoodsReceiptNoteDetail> list_goods_receipt_note_detail;
	private GoodsReceiptNote goods_receipt_note;
	private String member_name;
	
	public WrapListGoodsReceiptNoteDetailReqInfo() {
	}
	public WrapListGoodsReceiptNoteDetailReqInfo(List<GoodsReceiptNoteDetail> list_goods_receipt_note_detail,
			String member_name) {
		this.list_goods_receipt_note_detail = list_goods_receipt_note_detail;
		this.member_name = member_name;
	}
	public WrapListGoodsReceiptNoteDetailReqInfo(List<GoodsReceiptNoteDetail> list_goods_receipt_note_detail,
			GoodsReceiptNote goods_receipt_note, String member_name) {
		this.list_goods_receipt_note_detail = list_goods_receipt_note_detail;
		this.goods_receipt_note = goods_receipt_note;
		this.member_name = member_name;
	}
	public List<GoodsReceiptNoteDetail> getList_goods_receipt_note_detail() {
		return list_goods_receipt_note_detail;
	}
	public void setList_goods_receipt_note_detail(List<GoodsReceiptNoteDetail> list_goods_receipt_note_detail) {
		this.list_goods_receipt_note_detail = list_goods_receipt_note_detail;
	}
	public GoodsReceiptNote getGoods_receipt_note() {
		return goods_receipt_note;
	}
	public void setGoods_receipt_note(GoodsReceiptNote goods_receipt_note) {
		this.goods_receipt_note = goods_receipt_note;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
}

package lixco.com.reqInfo;

import lixco.com.entity.GoodsReceiptNoteDetail;

public class GoodsReceiptNoteDetailReqInfo {
	private GoodsReceiptNoteDetail goods_receipt_note_detail=null;
	private String member_name;

	public GoodsReceiptNoteDetailReqInfo() {
	}

	public GoodsReceiptNoteDetailReqInfo(GoodsReceiptNoteDetail goods_receipt_note_detail, String member_name) {
		this.goods_receipt_note_detail = goods_receipt_note_detail;
		this.member_name = member_name;
	}

	public GoodsReceiptNoteDetailReqInfo(GoodsReceiptNoteDetail goods_receipt_note_detail) {
		this.goods_receipt_note_detail = goods_receipt_note_detail;
	}

	public GoodsReceiptNoteDetail getGoods_receipt_note_detail() {
		return goods_receipt_note_detail;
	}

	public void setGoods_receipt_note_detail(GoodsReceiptNoteDetail goods_receipt_note_detail) {
		this.goods_receipt_note_detail = goods_receipt_note_detail;
	}

	public String getMember_name() {
		return member_name;
	}

	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	
}

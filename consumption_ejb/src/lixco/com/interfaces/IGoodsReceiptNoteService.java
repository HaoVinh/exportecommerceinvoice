package lixco.com.interfaces;

import java.util.Date;
import java.util.List;

import lixco.com.commom_ejb.PagingInfo;
import lixco.com.entity.GoodsReceiptNote;
import lixco.com.reqInfo.GoodsReceiptNoteReqInfo;
import lixco.com.reqfox.NhapSP;

public interface IGoodsReceiptNoteService {
	public int search(String json,List<GoodsReceiptNote> list);
	public int insert(GoodsReceiptNoteReqInfo t);
	public int update(GoodsReceiptNoteReqInfo t);
	public int selectById(long id,GoodsReceiptNoteReqInfo t);
	public GoodsReceiptNote findById(long id);
	public int selectByCode(String code,GoodsReceiptNoteReqInfo t);
	public int deleteById(long id);
	/*phần nap*/
	public GoodsReceiptNote checkReceiptNoteExists(String voucherCode,String batchCode,Date importDate,long customerId,long warehouseId,long iECategoriesId);
	/*phần foxpro */
	public int selectByFoxPro(long receiptId,List<NhapSP> list);
	public int updateNhapSPByFox(List<NhapSP> list);
	public List<String> getListNhapSPIdFox(long receiptId);
	public String getIdFoxNhapSP(long detailID);
	public int selectByIdToExcel(List<Long> receiptIds, List<Object[]> list);
	public List<Object[]> ngaysxgannhat(List<String> masps);
	public List<Object[]> ngaysxgannhatBN(List<String> masps);


}

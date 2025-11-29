package lixco.com.interfaces;

import java.util.List;

import lixco.com.entity.Batch;
import lixco.com.entity.ExportBatch;
import lixco.com.reqInfo.BatchReqInfo;
import lixco.com.reqInfo.ExportBatchODFake;
import lixco.com.reqInfo.ExportBatchODFake.BatchFake;
import lixco.com.reqInfo.ExportBatchPODFake;
import lixco.com.reqInfo.ExportBatchPODFake.BatchPODFake;

public interface IBatchService {
	public int insert(BatchReqInfo t);
	public int update(BatchReqInfo t);
	public int deleteById(long id);
	public int selectById(long id,BatchReqInfo t);
	public BatchFake selectByIdFake(long id);
	public BatchPODFake selectByIdPODFake(long id);
	public int exportAutoBatch(long productId,double quantity);
	public int exportManualBatch(List<Batch> list);
	public double getQuantityRemaining(long productId);
	public int selectByCode(String batch_code,BatchReqInfo t);
	public int exportBatchByInvoiceDetail(long productId, long invoiceDetailId,List<Batch> listBatch);
	public int exportBatchByOrderDetail(long productId, long orderDetailId,List<BatchFake> listBatch);
	public int exportBatchByPromotionOrderDetail(long productId, long promotionOrderDetailId,List<BatchPODFake> listBatch);

}

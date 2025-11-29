package lixco.com.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import lixco.com.commom_ejb.ToolTimeCustomer;
import lixco.com.entity.InvoiceDelete;
import lixco.com.entity.InvoiceTemp;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class InvoiceDeleteService {
	@Inject
	private EntityManager em;
	@Inject
	private Logger logger;
	@Resource
	private SessionContext ct;
	
	public int insert(InvoiceDelete p) {
		int res = -1;
		try {
			if (p != null) {
				em.persist(p);
				if (p.getId() > 0) {
					res = 0;
				}
			}
		} catch (Exception e) {
			logger.error("InvoiceDeleteService.insert:" + e.getMessage(), e);
		}
		return res;
	}
	/**
     * Tìm kiếm trường idref (String) của các hóa đơn trong phạm vi ngày (invoice_date)
     * được xác định bởi monthS và yearS.
     * * @param monthS Tháng tìm kiếm (1-12)
     * @param yearS Năm tìm kiếm
     * @return List<String> Danh sách các idref tương ứng
     */
    public List<String> findAllIdRefsByInvoiceDate(int monthS, int yearS) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        
        // 1. Thay đổi kiểu trả về của CriteriaQuery sang String (kiểu dữ liệu của idref)
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<InvoiceDelete> root = cq.from(InvoiceDelete.class);

        // **Lưu ý:** Loại bỏ .fetch() vì ta đang chỉ chọn một trường đơn lẻ (idref)

        // 2. Xác định phạm vi ngày tìm kiếm (từ đầu tháng đến cuối tháng của monthS/yearS)
        Date startDate = getStartOfMonth(monthS, yearS); // Giả định hàm này tồn tại
        Date endDate = getEndOfMonth(monthS, yearS);   // Giả định hàm này tồn tại
        
        // 3. Chọn trường idref
        cq.select(root.get("idref").as(String.class)); 

        // 4. Điều kiện tìm kiếm: Sử dụng trường "invoice_date"
        cq.where(cb.between(root.get("invoice_date"), startDate, endDate));

        // 5. Tạo TypedQuery với kiểu trả về là String
        TypedQuery<String> query = em.createQuery(cq);
        
        return query.getResultList();
    }
    
    // *****************************************************************
    // CÁC HÀM TIỆN ÍCH GIẢ ĐỊNH (Thay thế cho ToolTimeCustomer của bạn)
    // *****************************************************************
    
    private Date getStartOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        // Month trong Calendar là 0-indexed (0=tháng 1, 11=tháng 12)
        cal.set(year, month - 1, 1, 0, 0, 0); 
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date getEndOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // Đặt ngày cuối cùng của tháng, và giờ là 23:59:59.999
        cal.set(year, month - 1, lastDay, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

}

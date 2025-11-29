package lixco.com.loaddata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class InvoiceDMS {
	    private long id; // idinvoice
	    private String maCt; // mact
	    private String voucherCode; // vcinvoice
	    private Date invoiceDate; // invDate
	    private String customerCode; // cusCode
	    private String codeIe; // codeie
	    private String ai; // ai
	    private double taxValue; // tax_value
	    private String orderVoucher; // order_voucher
	    private String note; // noteinvoice
	    private String lookupCode; // lk
	    private String refId; // refId

	    private List<InvoiceDetailDMS> details; // Danh sách các chi tiết hóa đơn

	    // Constructors, Getters, Setters...
	    // Cần có setter cho details để thêm chi tiết vào
	    public void addDetail(InvoiceDetailDMS detail) {
	        if (this.details == null) {
	            this.details = new ArrayList<>();
	        }
	        this.details.add(detail);
	    }

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			InvoiceDMS other = (InvoiceDMS) obj;
			if (id != other.id)
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (id ^ (id >>> 32));
			return result;
		}
	
	
}

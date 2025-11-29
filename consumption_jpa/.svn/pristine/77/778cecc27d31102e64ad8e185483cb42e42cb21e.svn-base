package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import lixco.com.entity.Invoice;
import lombok.Data;

@Data
public class InvoiceDTO {
	private long idtc;
	private String created_by;
	private Date invoice_date;// ngày hóa đơn
	private List<InvoiceDetailDTO> invoiceDetailDTOs;
	private String makhachhang;// makh cai dat cho hoa don den
	private String po;
	private String soxe;
	private String manoiden;
	private double hesothue;
	private String mathukho;

	public InvoiceDTO(Invoice invoice, List<InvoiceDetailDTO> invoiceDetailDTOs, String po, String manoiden,
			String makhachhang) {
		this.idtc = invoice.getId();
		this.created_by = invoice.getCreated_by();
		this.invoice_date = invoice.getInvoice_date();
		this.invoiceDetailDTOs = invoiceDetailDTOs;
		this.makhachhang = makhachhang;
		this.po = po;
		this.soxe = invoice.getCar() != null ? invoice.getCar().getLicense_plate() : "";
		this.manoiden = manoiden;
		this.hesothue = invoice.getTax_value();
		this.mathukho = invoice.getStocker() != null ? invoice.getStocker().getStocker_code() : "";
	}

}

package lixco.com.einvoice_entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="einvoicedata")
@Entity
public class EInvoiceData {
	@Id
	@GeneratedValue(strategy =IDENTITY)
	private long id;
	private int company_id;
	private String inv_series;
	private String inv_template_no;
	private String inv_type_code;
	private String organizationUnitID;
	private String invoiceTemplateID;
	private int InvoiceType;// kiểu hóa đơn //_Default = 0, //_01GTKT = 1, //_02GTTT
	// = 2, //_03XKNB = 3, //_04HGDL = 4, //_06KPTQ = 6,
	// //_07TVT = 7, //_01BLP = 9, //_02BLP = 9
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public String getInv_series() {
		return inv_series;
	}
	public void setInv_series(String inv_series) {
		this.inv_series = inv_series;
	}
	public String getInv_template_no() {
		return inv_template_no;
	}
	public void setInv_template_no(String inv_template_no) {
		this.inv_template_no = inv_template_no;
	}
	public String getInv_type_code() {
		return inv_type_code;
	}
	public void setInv_type_code(String inv_type_code) {
		this.inv_type_code = inv_type_code;
	}
	public String getOrganizationUnitID() {
		return organizationUnitID;
	}
	public void setOrganizationUnitID(String organizationUnitID) {
		this.organizationUnitID = organizationUnitID;
	}
	public String getInvoiceTemplateID() {
		return invoiceTemplateID;
	}
	public void setInvoiceTemplateID(String invoiceTemplateID) {
		this.invoiceTemplateID = invoiceTemplateID;
	}
	public int getInvoiceType() {
		return InvoiceType;
	}
	public void setInvoiceType(int invoiceType) {
		InvoiceType = invoiceType;
	}
	
}

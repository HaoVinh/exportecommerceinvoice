package lixco.com.einvoice_data;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EInvoiceV3 {
	String RefID;
	String OrganizationUnitID;// id don
	String InvoiceTemplateID;// id mau,hoa don
	boolean IsInheritFromOldTemplate = true;
	int EditVersion=0;
	// thông tin khách hàng
	String AccountObjectID = null;// id khách hàng
	String AccountObjectName="";// tên đơn vị
	String AccountObjectAddress="";// địa
								// chỉ
	String AccountObjectTaxCode="";// mã số thuế người mua hàng
	String AccountObjectBankAccount="";// số tài khoản ngân hàng
	String AccountObjectBankName="";// tên ngân hàng
	String ContactName="";// họ tên người mua hàng
	String ReceiverName="";
	String ReceiverEmail = "";
	String ReceiverEmailCC = "";
	String ReceiverEmailBCC = "";
	String ReceiverMobile ="";

	// thông tin chung của hóa đơn
	String PaymentMethod="";// hình thức thành toán
	int InvoiceType = 1;// kiểu hóa đơn //_Default = 0, //_01GTKT = 1, //_02GTTT
						// = 2, //_03XKNB = 3, //_04HGDL = 4, //_06KPTQ = 6,
						// //_07TVT = 7, //_01BLP = 9, //_02BLP = 9
	String InvTemplateNo="";// mẫu số hóa đơn
	String InvSeries="";// ký hiệu hóa đơn
	String InvTemplateNoSeries="";
	String InvNo = "<Chưa cấp số>";// số hóa đơn
	Date InvDate;// ngày hóa đơn
	int PublishStatus = 0;// trạng thái phát hành

	// thông tin thanh toán và tiền
	String CurrencyCode="";// thông tin tiền tệ của hóa đơn
	double ExchangeRate=0.0;// tỷ giá quy đổi
	double TotalSaleAmountOC=0.0;// tổng tiền bán hàng nguyên tệ
	double TotalSaleAmount=0.0;// tổng tiền bán hàng quy đổi
	double TotalDiscountAmountOC=0.0;// tổng tiền chiết khấu nguyên tệ
	double TotalDiscountAmount=0.0;// tổng tiền chiết khấu quy đổi
	double TotalVATAmountOC=0.0;// tổng tiền VAT nguyên tệ
	double TotalVATAmount=0.0;// tổng tiền VAT quy đổi
	double TotalAmountOC=0.0;// tổng tiền thanh toán nguyên tệ
	double TotalAmount=0.0;// tổng tiền thanh toán quy đổi
	double VATRate=0.0;// thuế suất

	double TotalAmountWithVAT=0.0;// Lấy tổng tiền thanh toán quy đổi

	// thông tin trạng thái hóa đơn
	int SendInvoiceStatus;
	int EInvoiceStatus;
	int PaymentStatus;

	String CurrencyID="";// id loai tien (kieu: guid)
	String TransactionID="";// ma tra cuu
	boolean IsInvoiceDeleted;// co la hoa don xo bo hay khong (true: co;
								// false:khong)
	Date CreatedDate;// ngay tao
	String CreatedBy="";// nguoi tao;
	Date ModifiedDate;// ngay sua
	String ModifiedBy="";// nguoi sua
	String DeletedReason="";// Lý do xóa bỏ
	int CompanyID = 0;// id cong ty
	String CompanyTaxCode="";// ma so thue cong ty
	int TypeChangeInvoice = 0;// loại hoá đơn điều chỉnh 0: hoá đơn điều chỉnh
								// tăng; 1: hoá đơn điều chỉnh giảm; 2: hoá đơn
								// điều chỉnh thông tin
	String CustomData=null;// custom data

	// Phieu xuat kho
	String ContractCode="";
	String ContractDate;
	String ContractOwner="";
	String JournalMemo="";
	String TransporterName="";
	String Transport="";
	String ListFromStockName=" ";
	String ListToStockName="";
	String TransportContractCode="";
	String deliveryType;// Kiểu phiếu Giá trị: 1: phiếu nội bộ, 2: phiếu đại lý
	String StockOutFullName="";
	String StockInFullName="";

	// Bo sung
	int SourceType = 0;
	String AccountObjectCode = "";
	boolean IsInvoiceReceipted = false;
	double ExchangeRateOperation = 0.0;
	double TotalAmountWithoutVAT = 0.0;
	double TotalSaleAmountOther = 0.0;
	double TotalVATAmountOther = 0.0;
	double TotalAmountWithVATOC = 0.0;
	String VATRateOther = "";

	Date PaymentExpirationDate = null;

	int PaymentRule = 0;
	boolean IsInvoiceDiscount = false;
	int TypeDiscount = 0;
	int BusinessArea = 0;
	String ReceiptCode = "";
	String ReceiptName = "";
	int ApproveStep = 0;
	boolean IsImport = false;
	
	int sendToTaxStatus=0;
	String InvoiceCode;
	String messageCode;
	String messageRefNo;
	String messageRefDate;
	int errorInvoiceStatus;
	boolean isTaxReduction;
	boolean IsTaxReduction43;
	String orgOrganizationUnitID;
	String orgRefID;
	String orgInvNo;
	String orgInvDate;
	String orgTransactionID;
	String orgInvTemplateNo;
	String orgInvSeries;
	int entityState;
 
	List<EInvoiceDetailV3> InvoiceDetails;// chi tiet hoa don

}

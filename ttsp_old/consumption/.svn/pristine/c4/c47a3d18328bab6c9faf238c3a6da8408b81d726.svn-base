package lixco.com.einvoice_data;

import java.util.Date;
public class EInvoice {
	private String RefID;//9//PK ID hóa đơn GUID (not null)(Khi thêm mới thêm mới hóa đơn/phiếu xuất kho yêu cầu phải sinh chuỗi guid mới. Khi update tì truyền vào RefID bản ghi cần update)
	private int RefType;//9//	Loại hóa đơn (Not Null) 0: Hóa đơn GTGT; 1:Hóa đơn bán hàng; 2: Hóa đơn điều chỉnh tăng;
	private String AccountObjectID;//9	//ID Khách hàng GUID
	private String AccountObjectTaxCode;//9//Mã số thuế khách hàng
	private String AccountObjectName;//9//Tên khách hàng (maxlength = 128)
	private String AccountObjectAddress;//9//	Địa chỉ khách hàng (maxlength = 255)	
	private String ContactName;//9//	Tên người mua hàng (maxlength = 128
	private String ReceiverEmail;//9//	Email người nhận (maxlength = 255)
	private String ReceiverMobile;//9//Số điện thoại người nhận (maxlength = 50)
	private String PaymentMethod;//9//Phương thức thanh toán (maxlength = 50)
	private String AccountObjectBankAccount;//9//Số tài khoản ngân hàng (maxlength = 50)
	private String AccountObjectBankName;//9//	Tên ngân hàng (maxlength = 128)
	private String InvTemplateNo;//9//Mẫu số hóa đơn(Not null) (maxlength = 25)
	/*Loại mẫu hóa đơn
      01GTKT, 01GTKT0: Hóa đơn giá trị gia tăng;
      02GTTT, 02GTTT0: Hóa đơn bán hàng;
     03XKNB, 03XKNB0: Phiếu xuất kho kiêm vận chuyển hàng hóa nội bộ;
      04HGDL, 04HGDL0: Phiếu xuất kho gửi bán hàng đại lý;
      07KPTQ, 07KPTQ0: Hóa đơn bán hàng (dành cho tổ chức, cá nhân trong khu phi thuế quan);
      01BLP, 01BLP: Biên lai thu phí, lệ phí không có mệnh giá*/
	private String InvTypeCode;//9//Loại mẫu hóa đơn (Not null)
	private String InvSeries;//9//Ký hiệu hóa đơn(Not null) (maxlength = 20) AB/1//9E	Có
	private String InvNo;//9//Số hóa đơn(Not null)0000007 Nếu chưa có số: <Chưa cấp số>
	private String InvTemplateNoSeries;//9//Ghép ký hiệu hóa đơn và mẫu số hóa đơn dùng cho việc binding lên form gửi email	
	private double VATRate;//9//Thuế suất GTGT của cả hóa đơn (lấy Max(VATRate))
	private double TotalSaleAmountOC;//9//tổng tiền hàng nguyên tệ (NOT NULL), mặc định là 0)
	private double TotalDiscountAmountOC;//9//Tổng tiền chiết khấu nguyên tệ (NOT NULL), mặc định là 0
	private double TotalVATAmountOC;//9//Tổng tiền thuế GTGT nguyên tệ (NOT NULL, mặc định là 0)	
	private double TotalAmountOC;//9//Tổng tiền thanh toán nguyên tệ (NOT NULL, mặc định là 0)	644600	
	private Date InvDate;//9//	Ngày lập hóa đơn (Not Null) 201//9-10-30T00:00:00+07:00
	private String CurrencyID;//9//	ID loại tiền	
	private String CurrencyCode;//9//	loại tiền VND	
	private double ExchangeRate;//9//	Tỷ giá hối đoái 1		
//	private double ExchangeRateOperation;//	phép tính quy đổi : 0: nhân, 1: chia	
	private double TotalSaleAmount;//9//	Tổng tiền hàng quy đổi (NOT NULL)
	private double TotalDiscountAmount;//9// (NOT NULL, mặc định là 0)	Tổng tiền chiết khấu quy đổi	
	private double TotalAmountWithoutVAT;// (NOT NULL, mặc định là 0)	Tổng tiền hàng trước thuế	
	private double TotalVATAmount;//9//Tổng tiền thuế GTGT Quy đổi	
	private double TotalAmount;//9//Tổng tiền thanh toán quy đổi	
	private String TransactionID;//9//	Mã tra cứu trên MISA MEINVOICE	75FGH18D7	
	private int PublishStatus;//9//Trạng thái phát hành hóa đơn (Not Null) 0: chưa phát hành,1: đang phát hành,2: phát hành lỗi,3: đã phát hành
	private boolean IsInvoiceDeleted;//9//Có là hóa đơn xóa bỏ không? true: là có, false: là không	false
//	private int SentTimes;//	Số lần gửi hóa đơn cho khách hàng	
//	private boolean IsInvoiceReceipted;//Người mua đã nhận được hóa đơn chưa true: là đã nhận, false: là chưa nhận 
	private Date CreatedDate;//9//	Ngày tạo bản ghi	201//9-10-30T00:00:00+07:00	
	private String CreatedBy;//9//	Người tạo bản ghi	Sơn	
	private Date ModifiedDate;//9//	Ngày sửa bản ghi	201//9-10-30T00:00:00+07:00	
	private String ModifiedBy;//9//	Người sửa bản ghi	Tuấn Anh	
	private int EInvoiceStatus;//9//Trạng thái hóa đơn (Not Null) Giá trị: 0: hóa đơn gốc, 1: hóa đơn thay thế, 2: hóa đơn xóa bỏ	 
	private int SendInvoiceStatus;//9//	Trạng thái gửi email		
//	private int ConvertTimes;//	Ngày chuyển đổi		
	private String ReceiverName;//9//	Tên người nhận Email
	private String DeletedReason;//9//	Lý do xóa bỏ	Do lỗi phần mềm	
	private int CompanyID;//9//Mã công ty (Not null)	2256
//	private String CompanyName;//	Tên công ty	Công ty cổ phần Misa(Not null)
	private String CompanyTaxCode;//9//	Mã số thuế công ty (Not null) 0101243150-675
//	private Date InvDateComputer;//Ngày lập hóa đơn máy tính	201//9-10-30T00:00:00+07:00	
//	private String OrgRefID;//ID của hóa đơn bị thay thế, điều chỉnh
//	private String OrgInvNo;//Số của hóa đơn bị thay thế, điều chỉnh	00000012	
//	private String OrgTransactionID;//Mã tra cứu của hóa đơn bị thay thế, điều chỉnh	Q3FXH473J	
//	private String OrgInvTemplateNo;//	Mẫu số của hóa đơn bị thay thế, hoặc điều chỉnh	01GTKT0/002	
//	private String OrgInvSeries;//	Ký hiệu của hóa đơn bị thay thế, hoặc điều chỉnh	AB/19E	
//	private Date OrgInvDate;//Ngày của hóa đơn bị thay thế, hoặc điều chỉnh	2019-10-30T00:00:00+07:00	
//	private String AttachFileName;//	tên file biên bản điều chỉnh	bien_ban_dieu_chinh	
//	private String AttachFileNameOld;//	tên file biên bản điều chỉnh cũ khi thay đổi bb đính kèm		
	private String AttachFilePath;//	nội dung file đính kèm bb điều chỉnh		
//	private String ChangeReason;//	lý do điều chỉnh	Do lỗi đánh máy	
	private int TypeChangeInvoice;//9//	loại hoá đơn điều chỉnh 0: hoá đơn điều chỉnh tăng,1: hoá đơn điều chỉnh giảm,	2: hoá đơn điều chỉnh thông tin
//	private Date DeletedDate;//	Ngày xóa bỏ	2019-10-30T00:00:00+07:00	
	private double TotalAmountWithVAT;//9//	Lấy tổng tiền thanh toán quy đổi
//	private int IsDelete;//Hóa đơn đã xóa bỏ chưa: 0 - chưa xoá bỏ, 1- đã xoá bỏ		
//	private Date RefDate;//Ngày xóa bỏ	2019-10-30T00:00:00+07:00	
//	private String ContractCode;//Số của hợp đồng	HD123456	
//	private Date ContractDate;//	Ngày hợp đồng	2019-10-30T00:00:00+07:00	
//	private String ContractOwner;//Người điều lệnh Nguyễn Văn Hà
//	private String JournalMemo;//Nội dung điều động	Chuyển lưu tồn kho	
//	private String TransporterName;//Người vận chuyển	Đỗ Xuân Phú	
//	private String Transport;//Phương tiện vận chuyển	Ô tô	
//	private String ListFromStockName;//	Tên kho xuất	Kho Cầu Giấy	
//	private String ListToStockName;//	Tên kho nhập	Kho Phố Cổ	
//	private String TransportContractCode;//	mã liên lạc vận chuyển	MLL2256	
//	private int DeliveryType;//	Kiểu phiếu Giá trị: 1: phiếu nội bộ, 2: phiếu đại lý
//	private boolean IsImport;//	Có phải là hóa đơn nhập khẩu không? Mặc định là: false
//	private int TypeDiscount;//Loại tỷ lệ chiết khấu Giá trị: 0: không chiết khấu, 1: chiết khấu theo mặt hàng, 2: chiết khấu theo tổng hóa đơn Mặc định là: 0
	private String CustomData;//9//Lưu thông tin các trường đặc thù	{"CustomField1":"Hàng dễ vỡ","CustomField2":"20"}	
//	private Date PaymentExpirationDate;//Hạn thanh toán	2019-10-30T00:00:00+07:00	
//	private int PaymentStatus;//	Trạng thái thanh toán 0: chưa thanh toán; 1: Chờ xác nhận; 2: đã gửi xác nhận; 3: đã thanh toán; 4: đã hủy; 5: tạm hủy đơn hàng Mặc định là: 0
//	private int PaymentRule;//	Điều khoản	0	
//	private Date PublishDate;//	Thời gian phát hành hóa đơn	2019-10-30T00:00:00+07:00	
	private int EditVersion;//9//	Mã đánh dấu phiên bản của hóa đơn (Dùng khi update, cộng thêm 1 mỗi lần update)	0	
//	private String ReceiptCode;//	Mã loại phí (dùng cho mẫu biên llai)	Phí đăng kiểm ...	
//	private boolean IsMoreVATRate;//	Là hoá đơn nhiều thuế suất : 0- không, 1- có	0	
//	private int BusinessArea;//	LĨnh vực kinh doanh hoá đơn theo mẫu hoá đơn lập 0 - GTGT thường, 1 - vé máy bay, 2 - vận tải, 3 - khách sạn, 4 - khách sạn chứa TTĐB, 5 - vé máy bay không có phí dịch vụ, 6 - kinh doanh xe, 7 - bán thuốc, 8 -khách sạn không có phòng trọ, ngày đến, ngày đi có TTĐB, 9 - khách sạn không có phòng trọ, ngày đến, ngày đi, 10 - Đăng kiểm xe cơ giới, 11 - Xăng dầu, 12 - Nhà sách, 13 - Đại lý hàng hải, 14 - Nhà sách không có xuất bản phí, 15 - Biên lai thu phí, lệ phí, 16 - Hóa đơn không dòng chi tiết, 17 - Bất động sản, 18 - hoàn vé máy bay, 19 - hoàn vé máy bay,	0	
//	private boolean IsInvoiceDiscount;//là hoá đơn chỉ chứa dòng hàng hoá chiết khấu : 0- không, 1- có
	private int EntityState;//1
	
	String OrganizationUnitID="44e52d74-4da4-11ec-940a-005056a6f699";//id don vi
	String InvoiceTemplateID= "7f6f553b-b942-48f3-a055-31f535976ada";//id mau hoa don
	boolean IsInheritFromOldTemplate= true;
	private int InvoiceType=1;//kieu hoa don
	
	
	public String getRefID() {
		return RefID;
	}
	public void setRefID(String refID) {
		RefID = refID;
	}
	public int getRefType() {
		return RefType;
	}
	public void setRefType(int refType) {
		RefType = refType;
	}
	public String getAccountObjectID() {
		return AccountObjectID;
	}
	public void setAccountObjectID(String accountObjectID) {
		AccountObjectID = accountObjectID;
	}
	public String getAccountObjectTaxCode() {
		return AccountObjectTaxCode;
	}
	public void setAccountObjectTaxCode(String accountObjectTaxCode) {
		AccountObjectTaxCode = accountObjectTaxCode;
	}
	public String getAccountObjectName() {
		return AccountObjectName;
	}
	public void setAccountObjectName(String accountObjectName) {
		AccountObjectName = accountObjectName;
	}
	public String getAccountObjectAddress() {
		return AccountObjectAddress;
	}
	public void setAccountObjectAddress(String accountObjectAddress) {
		AccountObjectAddress = accountObjectAddress;
	}
	public String getContactName() {
		return ContactName;
	}
	public void setContactName(String contactName) {
		ContactName = contactName;
	}
	public String getReceiverEmail() {
		return ReceiverEmail;
	}
	public void setReceiverEmail(String receiverEmail) {
		ReceiverEmail = receiverEmail;
	}
	public String getReceiverMobile() {
		return ReceiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		ReceiverMobile = receiverMobile;
	}
	public String getPaymentMethod() {
		return PaymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		PaymentMethod = paymentMethod;
	}
	public String getAccountObjectBankAccount() {
		return AccountObjectBankAccount;
	}
	public void setAccountObjectBankAccount(String accountObjectBankAccount) {
		AccountObjectBankAccount = accountObjectBankAccount;
	}
	public String getAccountObjectBankName() {
		return AccountObjectBankName;
	}
	public void setAccountObjectBankName(String accountObjectBankName) {
		AccountObjectBankName = accountObjectBankName;
	}
	public String getInvTemplateNo() {
		return InvTemplateNo;
	}
	public void setInvTemplateNo(String invTemplateNo) {
		InvTemplateNo = invTemplateNo;
	}
	public String getInvTypeCode() {
		return InvTypeCode;
	}
	public void setInvTypeCode(String invTypeCode) {
		InvTypeCode = invTypeCode;
	}
	public String getInvSeries() {
		return InvSeries;
	}
	public void setInvSeries(String invSeries) {
		InvSeries = invSeries;
	}
	public String getInvNo() {
		return InvNo;
	}
	public void setInvNo(String invNo) {
		InvNo = invNo;
	}
	public String getInvTemplateNoSeries() {
		return InvTemplateNoSeries;
	}
	public void setInvTemplateNoSeries(String invTemplateNoSeries) {
		InvTemplateNoSeries = invTemplateNoSeries;
	}
	public double getVATRate() {
		return VATRate;
	}
	public void setVATRate(double vATRate) {
		VATRate = vATRate;
	}
	public double getTotalSaleAmountOC() {
		return TotalSaleAmountOC;
	}
	public void setTotalSaleAmountOC(double totalSaleAmountOC) {
		TotalSaleAmountOC = totalSaleAmountOC;
	}
	public double getTotalDiscountAmountOC() {
		return TotalDiscountAmountOC;
	}
	public void setTotalDiscountAmountOC(double totalDiscountAmountOC) {
		TotalDiscountAmountOC = totalDiscountAmountOC;
	}
	public double getTotalVATAmountOC() {
		return TotalVATAmountOC;
	}
	public void setTotalVATAmountOC(double totalVATAmountOC) {
		TotalVATAmountOC = totalVATAmountOC;
	}
	public double getTotalAmountOC() {
		return TotalAmountOC;
	}
	public void setTotalAmountOC(double totalAmountOC) {
		TotalAmountOC = totalAmountOC;
	}
	public Date getInvDate() {
		return InvDate;
	}
	public void setInvDate(Date invDate) {
		InvDate = invDate;
	}
	public String getCurrencyID() {
		return CurrencyID;
	}
	public void setCurrencyID(String currencyID) {
		CurrencyID = currencyID;
	}
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public double getExchangeRate() {
		return ExchangeRate;
	}
	public void setExchangeRate(double exchangeRate) {
		ExchangeRate = exchangeRate;
	}
	public double getTotalSaleAmount() {
		return TotalSaleAmount;
	}
	public void setTotalSaleAmount(double totalSaleAmount) {
		TotalSaleAmount = totalSaleAmount;
	}
	public double getTotalDiscountAmount() {
		return TotalDiscountAmount;
	}
	public void setTotalDiscountAmount(double totalDiscountAmount) {
		TotalDiscountAmount = totalDiscountAmount;
	}
	public double getTotalAmountWithoutVAT() {
		return TotalAmountWithoutVAT;
	}
	public void setTotalAmountWithoutVAT(double totalAmountWithoutVAT) {
		TotalAmountWithoutVAT = totalAmountWithoutVAT;
	}
	public double getTotalVATAmount() {
		return TotalVATAmount;
	}
	public void setTotalVATAmount(double totalVATAmount) {
		TotalVATAmount = totalVATAmount;
	}
	public double getTotalAmount() {
		return TotalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		TotalAmount = totalAmount;
	}
	public String getTransactionID() {
		return TransactionID;
	}
	public void setTransactionID(String transactionID) {
		TransactionID = transactionID;
	}
	public int getPublishStatus() {
		return PublishStatus;
	}
	public void setPublishStatus(int publishStatus) {
		PublishStatus = publishStatus;
	}
	public boolean isIsInvoiceDeleted() {
		return IsInvoiceDeleted;
	}
	public void setIsInvoiceDeleted(boolean isInvoiceDeleted) {
		IsInvoiceDeleted = isInvoiceDeleted;
	}
	public Date getCreatedDate() {
		return CreatedDate;
	}
	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public Date getModifiedDate() {
		return ModifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		ModifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return ModifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}
	public int getEInvoiceStatus() {
		return EInvoiceStatus;
	}
	public void setEInvoiceStatus(int eInvoiceStatus) {
		EInvoiceStatus = eInvoiceStatus;
	}
	public int getSendInvoiceStatus() {
		return SendInvoiceStatus;
	}
	public void setSendInvoiceStatus(int sendInvoiceStatus) {
		SendInvoiceStatus = sendInvoiceStatus;
	}
	public String getReceiverName() {
		return ReceiverName;
	}
	public void setReceiverName(String receiverName) {
		ReceiverName = receiverName;
	}
	public String getDeletedReason() {
		return DeletedReason;
	}
	public void setDeletedReason(String deletedReason) {
		DeletedReason = deletedReason;
	}
	public int getCompanyID() {
		return CompanyID;
	}
	public void setCompanyID(int companyID) {
		CompanyID = companyID;
	}
	public String getCompanyTaxCode() {
		return CompanyTaxCode;
	}
	public void setCompanyTaxCode(String companyTaxCode) {
		CompanyTaxCode = companyTaxCode;
	}
	public String getAttachFilePath() {
		return AttachFilePath;
	}
	public void setAttachFilePath(String attachFilePath) {
		AttachFilePath = attachFilePath;
	}
	public int getTypeChangeInvoice() {
		return TypeChangeInvoice;
	}
	public void setTypeChangeInvoice(int typeChangeInvoice) {
		TypeChangeInvoice = typeChangeInvoice;
	}
	public double getTotalAmountWithVAT() {
		return TotalAmountWithVAT;
	}
	public void setTotalAmountWithVAT(double totalAmountWithVAT) {
		TotalAmountWithVAT = totalAmountWithVAT;
	}
	public String getCustomData() {
		return CustomData;
	}
	public void setCustomData(String customData) {
		CustomData = customData;
	}
	public int getEditVersion() {
		return EditVersion;
	}
	public void setEditVersion(int editVersion) {
		EditVersion = editVersion;
	}
	public int getEntityState() {
		return EntityState;
	}
	public void setEntityState(int entityState) {
		EntityState = entityState;
	}
	public String getOrganizationUnitID() {
		return OrganizationUnitID;
	}
	public void setOrganizationUnitID(String organizationUnitID) {
		OrganizationUnitID = organizationUnitID;
	}
	public String getInvoiceTemplateID() {
		return InvoiceTemplateID;
	}
	public void setInvoiceTemplateID(String invoiceTemplateID) {
		InvoiceTemplateID = invoiceTemplateID;
	}
	public boolean isIsInheritFromOldTemplate() {
		return IsInheritFromOldTemplate;
	}
	public void setIsInheritFromOldTemplate(boolean isInheritFromOldTemplate) {
		IsInheritFromOldTemplate = isInheritFromOldTemplate;
	}
	public int getInvoiceType() {
		return InvoiceType;
	}
	public void setInvoiceType(int invoiceType) {
		InvoiceType = invoiceType;
	}
	
}

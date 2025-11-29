package lixco.com.einvoice_data;


public class EInvoiceDetail {
	private String RefDetailID;//9//Khóa chính (Not Null)	49e59629-144b-c705-73e6-38617f7626a2
	private String RefID;//9//Khóa ngoại kết nối với Hóa đơn (Not null)	5f2b48ff-b866-1a88-c158-25d265f01734
	private String InventoryItemID;//9//ID của hàng hóa (Not Null)	2b586137-1245-7dd5-f723-54727cffc7b2
	private String InventoryItemCode;//9//Mã hàng hóa (Not Null)	MH3232	
	private String InventoryItemName;//9//Tên hàng hóa (Not Null) Áo phông đen
	private String Description;//9//	Ghi chú		
	private String UnitName;//9//	Đơn vị tính	Cái	
	private double Quantity;//9//	Số lượng	5	
//	private double InWards;//	Thực nhập	3	
	private double UnitPrice;//9//	Đơn giá	500000	
	private double AmountOC;//9//	Thành tiền nguyên tệ	500	
	private double Amount;//9//	Thành tiền quy đổi Amount = AmountOC (Phép tính quy đổi) ExchangeRate Phép tính quy đổi : nhân hoặc chia	2000000	
	private double DiscountRate;//9//	Tỷ lệ chiết khấu	0	
	private double DiscountAmountOC;//9//	Tiền chiết khấu nguyên tệ	0	
	private double DiscountAmount;//9//	Tiền chiết khấu quy đổi DiscountAmount = DiscountAmountOC (Phép tính quy đổi) ExchangeRate Phép tính quy đổi : nhân hoặc chia	0	
	private double VATRate;//9//	Thuế GTGT	10	
	private double VATAmountOC;//9//	Tiền GTGT nguyên tệ	40	
	private double VATAmount;//9//	Tiền GTGT quy đổi VATAmount = VATAmountOC * ExchangeRate Phép tính quy đổi : nhân hoặc chia	800000	
	private int SortOrder;//9//	Số thứ tự	2	
	private boolean IsPromotion;//9//	Hàng khuyến mãi	false	
	private boolean IsTemp;//9//false
	private int CompanyID;//9//	Mã công ty (Not Null)	2256
	private int InventoryItemType;//9//	Kiểu hàng hóa Giá trị: 0: Vật tư hàng hóa, 1: thành phẩm, 2: dịch vụ, 3: mô tả, 4: là hàng hoá chiết khấu	0	
//	private double UnitAfterTax;//Đơn giá sau thuế	20000	
//	private double AmountAfterTax;//	Thành tiền sau thuế	3000000	
//	private double IsDescription;//	Là dòng diễn giải mô tả	false	có
//	private double OutWards;//	Thực xuất	5	
	private int SortOrderView;//9//	Số thứ tự	3	
	private int EntityState;//9//1
//	private String EngineNumber;//Số máy (Đối với hóa đơn kinh doanh xe)		
//	private String ChassisNumber;//	Số khung (Đối với hóa đơn kinh doanh xe)		
//	private String LotNo;//	Số lô ( với hóa đơn thuốc)		
//	private Date ExpireDate;//	Hạn sử dụng (với hóa đơn thuốc)	2019-10-30T00:00:00+07:00	
//	private String SerialNumber;//	Quy cách		
//	private String InventoryItemNote;//	Ghi chú		
//	private String PublishFee;//	xuất bản phí với hoá đơn nhà sách		
//	private String CustomData;//	trường mở rộng của detail hàng hoá	theo định dạng: {"CustomField1Detail":"Hàng dễ vỡ","CustomField2Detail":"20","CustomField3Detail":"50000"}
	public String getRefDetailID() {
		return RefDetailID;
	}
	public void setRefDetailID(String refDetailID) {
		RefDetailID = refDetailID;
	}
	public String getRefID() {
		return RefID;
	}
	public void setRefID(String refID) {
		RefID = refID;
	}
	public String getInventoryItemID() {
		return InventoryItemID;
	}
	public void setInventoryItemID(String inventoryItemID) {
		InventoryItemID = inventoryItemID;
	}
	public String getInventoryItemCode() {
		return InventoryItemCode;
	}
	public void setInventoryItemCode(String inventoryItemCode) {
		InventoryItemCode = inventoryItemCode;
	}
	public String getInventoryItemName() {
		return InventoryItemName;
	}
	public void setInventoryItemName(String inventoryItemName) {
		InventoryItemName = inventoryItemName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getUnitName() {
		return UnitName;
	}
	public void setUnitName(String unitName) {
		UnitName = unitName;
	}
	public double getQuantity() {
		return Quantity;
	}
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	public double getUnitPrice() {
		return UnitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		UnitPrice = unitPrice;
	}
	public double getAmountOC() {
		return AmountOC;
	}
	public void setAmountOC(double amountOC) {
		AmountOC = amountOC;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public double getDiscountRate() {
		return DiscountRate;
	}
	public void setDiscountRate(double discountRate) {
		DiscountRate = discountRate;
	}
	public double getDiscountAmountOC() {
		return DiscountAmountOC;
	}
	public void setDiscountAmountOC(double discountAmountOC) {
		DiscountAmountOC = discountAmountOC;
	}
	public double getDiscountAmount() {
		return DiscountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		DiscountAmount = discountAmount;
	}
	public double getVATRate() {
		return VATRate;
	}
	public void setVATRate(double vATRate) {
		VATRate = vATRate;
	}
	public double getVATAmountOC() {
		return VATAmountOC;
	}
	public void setVATAmountOC(double vATAmountOC) {
		VATAmountOC = vATAmountOC;
	}
	public double getVATAmount() {
		return VATAmount;
	}
	public void setVATAmount(double vATAmount) {
		VATAmount = vATAmount;
	}
	public int getSortOrder() {
		return SortOrder;
	}
	public void setSortOrder(int sortOrder) {
		SortOrder = sortOrder;
	}
	public boolean isIsPromotion() {
		return IsPromotion;
	}
	public void setIsPromotion(boolean isPromotion) {
		IsPromotion = isPromotion;
	}
	public boolean isIsTemp() {
		return IsTemp;
	}
	public void setIsTemp(boolean isTemp) {
		IsTemp = isTemp;
	}
	public int getCompanyID() {
		return CompanyID;
	}
	public void setCompanyID(int companyID) {
		CompanyID = companyID;
	}
	public int getInventoryItemType() {
		return InventoryItemType;
	}
	public void setInventoryItemType(int inventoryItemType) {
		InventoryItemType = inventoryItemType;
	}
	public int getSortOrderView() {
		return SortOrderView;
	}
	public void setSortOrderView(int sortOrderView) {
		SortOrderView = sortOrderView;
	}
	public int getEntityState() {
		return EntityState;
	}
	public void setEntityState(int entityState) {
		EntityState = entityState;
	}
}

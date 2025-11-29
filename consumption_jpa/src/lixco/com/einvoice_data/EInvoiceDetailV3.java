package lixco.com.einvoice_data;

import lombok.Data;

@Data
public class EInvoiceDetailV3 {
	String RefDetailID;
	String RefID;
	int SortOrder= 1;
	int SortOrderView= 1;
	String InventoryItemID= "";
	int InventoryItemType= 0;
	String InventoryItemCode= "";
	String InventoryItemName= "";
	String Description= "";
	String UnitName= "";
	double Quantity= 1.0;			
	double UnitPrice= 0.0;
	transient double AmountOCBefore= 0.0;
	double AmountOC= 0.0;
	double Amount= 0.0;
	
	double VATRate= 0.0;
	double VATAmountOC= 0.0;
	double VATAmount= 0.0;
	
	double InWards= 0.0;
	
	double DiscountRate= 0.0;
	double DiscountAmountOC= 0.0;
	double DiscountAmount= 0.0;
	boolean IsPromotion= false;
	boolean IsTemp= false;
	double UnitAfterTax= 0.0;
	double AmountAfterTax= 0.0;
	String IsDescription= null;
	double OutWards= 0.0;
	String InventoryItemNote= null;
	String CustomData= "{}";
	String CustomDataDetail= null;
	int EntityState= 1;
	int CompanyID;
	
}

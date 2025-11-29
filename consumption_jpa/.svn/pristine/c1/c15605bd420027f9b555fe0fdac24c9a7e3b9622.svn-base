package lixco.com.entityapi;

import java.util.Date;
import java.util.List;

import lixco.com.entity.InvoiceDetail;
import lombok.Data;

@Data
public class InvoiceDetailDTO5 {
    private long id;
    private Date createdDate;
    private String createdBy;
    private long createdById;
    private Date lastModifiedDate;
    private String lastModifiedBy;

    private Long productId;
    private String productName;
    private String productCode;
    private String productdh_code;

    private double quantity;
    private double real_quantity;
    private double unitPrice;
    private double unitPriceSS;
    private boolean chenhlechgiakm;
    private double total;
    private double realQuantity;

    private String note;
    private String productDhCode;
    private Long detailOwnId;

    private double foreignUnitPrice;
    private double totalForeignAmount;

    private Long invoiceId;
    private boolean exOrder;
    private String noteBatchCode;

    private Long invoiceDetailOwnId;

    private String refDetailID;
    private String refID;
    private String refDetailVCNBID;
    private String refIDVCNB;
    private String idfox;

    private long idcthdxuatkhau;
    private boolean naponline;

    private long idchitietdonhang;
    private long idchitietdonhangkm;

    private String act;
    private String useraction;

    private boolean hdsuagia;
    private String napdulieu;

    private double slpallet;


    public InvoiceDetailDTO5(InvoiceDetail invoiceDetail) {
        if (invoiceDetail != null) {
            this.id = invoiceDetail.getId();
            this.createdDate = invoiceDetail.getCreated_date();
            this.createdBy = invoiceDetail.getCreated_by();
            this.createdById = invoiceDetail.getCreated_by_id();
            this.lastModifiedDate = invoiceDetail.getLast_modifed_date();
            this.lastModifiedBy = invoiceDetail.getLast_modifed_by();

            this.productId = invoiceDetail.getProduct() != null ? invoiceDetail.getProduct().getId() : null;
            this.productName = invoiceDetail.getProduct() != null ? invoiceDetail.getProduct().getProduct_name() : null;
            this.productCode = invoiceDetail.getProduct() != null ? invoiceDetail.getProduct().getProduct_code() : null;

            this.quantity = invoiceDetail.getQuantity();
            this.unitPrice = invoiceDetail.getUnit_price();
            this.unitPriceSS = invoiceDetail.getUnit_price_ss();
            this.chenhlechgiakm = invoiceDetail.isChenhlechgiakm();
            this.total = invoiceDetail.getTotal();
            this.realQuantity = invoiceDetail.getReal_quantity();

            this.note = invoiceDetail.getNote();
            this.productDhCode = invoiceDetail.getProductdh_code();
            this.detailOwnId = invoiceDetail.getDetail_own_id();

            this.foreignUnitPrice = invoiceDetail.getForeign_unit_price();
            this.totalForeignAmount = invoiceDetail.getTotal_foreign_amount();

            this.invoiceId = invoiceDetail.getInvoice() != null ? invoiceDetail.getInvoice().getId() : null;
            this.exOrder = invoiceDetail.isEx_order();
            this.noteBatchCode = invoiceDetail.getNote_batch_code();

            this.invoiceDetailOwnId = invoiceDetail.getInvoice_detail_own() != null ? invoiceDetail.getInvoice_detail_own().getId() : null;

            this.refDetailID = invoiceDetail.getRefDetailID();
            this.refID = invoiceDetail.getRefID();
            this.refDetailVCNBID = invoiceDetail.getRefDetailVCNBID();
            this.refIDVCNB = invoiceDetail.getRefIDVCNB();
            this.idfox = invoiceDetail.getIdfox();

            this.idcthdxuatkhau = invoiceDetail.getIdcthdxuatkhau();
            this.naponline = invoiceDetail.isNaponline();

            this.idchitietdonhang = invoiceDetail.getIdchitietdonhang();
            this.idchitietdonhangkm = invoiceDetail.getIdchitietdonhangkm();

            this.act = invoiceDetail.getAct();
            this.useraction = invoiceDetail.getUseraction();

            this.hdsuagia = invoiceDetail.isHdsuagia();
            this.napdulieu = invoiceDetail.getNapdulieu();

            this.slpallet = invoiceDetail.getSlpallet();
        }
    }

}

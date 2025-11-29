package trong.lixco.com.info;
import java.io.Serializable;
import java.util.Date;

public class FlatInvoiceRow implements Serializable {
    private Long idinvoice;
    private String mact;
    private String vcinvoice;
    private Date invDate;
    private String cusCode;
    private String codeie;
    private String ai;
    private String productCode;
    private double qud;
    private double unitPrice;
    private double ttd;
    private double taxValue;
    private String orderVoucher;
    private String noteinvoice;
    private double qudthung;
    private double bi;
    private String ci;
    private String lk;
    private String pdcode;
    private String idref;

    public FlatInvoiceRow() {}

    /** Map theo thứ tự cột trong SELECT */
    public static FlatInvoiceRow from(Object[] r) {
        FlatInvoiceRow x = new FlatInvoiceRow();
        int i = 0;
        x.setIdinvoice(toLong(r[i++]));
        x.setMact(toStr(r[i++]));
        x.setVcinvoice(toStr(r[i++]));
        x.setInvDate(toDate(r[i++]));
        x.setCusCode(toStr(r[i++]));
        x.setCodeie(toStr(r[i++]));
        x.setAi(toStr(r[i++]));
        x.setProductCode(toStr(r[i++]));
        x.setQud(toDbl(r[i++]));
        x.setUnitPrice(toDbl(r[i++]));
        x.setTtd(toDbl(r[i++]));
        x.setTaxValue(toDbl(r[i++]));
        x.setOrderVoucher(toStr(r[i++]));
        x.setNoteinvoice(toStr(r[i++]));
        x.setQudthung(toDbl(r[i++]));
        x.setBi(toDbl(r[i++]));
        x.setCi(toStr(r[i++]));
        x.setLk(toStr(r[i++]));
        x.setPdcode(toStr(r[i++]));
        x.setIdref(toStr(r[i++]));
        return x;
    }

    // -------- helpers ----------
    private static String toStr(Object o) { return o == null ? "" : String.valueOf(o); }
    private static Long toLong(Object o) {
        if (o == null) return null;
        if (o instanceof Number) return ((Number) o).longValue();
        return Long.valueOf(o.toString());
    }
    private static double toDbl(Object o) {
        if (o == null) return 0d;
        if (o instanceof Number) return ((Number) o).doubleValue();
        try { return Double.parseDouble(o.toString()); } catch (Exception ignored) { return 0d; }
    }
    private static Date toDate(Object o) {
        if (o == null) return null;
        if (o instanceof Date) return (Date) o;
        if (o instanceof java.sql.Timestamp) return new Date(((java.sql.Timestamp) o).getTime());
        if (o instanceof java.sql.Date) return new Date(((java.sql.Date) o).getTime());
        try {
            return javax.xml.bind.DatatypeConverter.parseDateTime(o.toString()).getTime();
        } catch (Exception ignored) {
            return null;
        }
    }

    // -------- getters/setters ----------
    public Long getIdinvoice() { return idinvoice; }
    public void setIdinvoice(Long idinvoice) { this.idinvoice = idinvoice; }
    public String getMact() { return mact; }
    public void setMact(String mact) { this.mact = mact; }
    public String getVcinvoice() { return vcinvoice; }
    public void setVcinvoice(String vcinvoice) { this.vcinvoice = vcinvoice; }
    public Date getInvDate() { return invDate; }
    public void setInvDate(Date invDate) { this.invDate = invDate; }
    public String getCusCode() { return cusCode; }
    public void setCusCode(String cusCode) { this.cusCode = cusCode; }
    public String getCodeie() { return codeie; }
    public void setCodeie(String codeie) { this.codeie = codeie; }
    public String getAi() { return ai; }
    public void setAi(String ai) { this.ai = ai; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public double getQud() { return qud; }
    public void setQud(double qud) { this.qud = qud; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public double getTtd() { return ttd; }
    public void setTtd(double ttd) { this.ttd = ttd; }
    public double getTaxValue() { return taxValue; }
    public void setTaxValue(double taxValue) { this.taxValue = taxValue; }
    public String getOrderVoucher() { return orderVoucher; }
    public void setOrderVoucher(String orderVoucher) { this.orderVoucher = orderVoucher; }
    public String getNoteinvoice() { return noteinvoice; }
    public void setNoteinvoice(String noteinvoice) { this.noteinvoice = noteinvoice; }
    public double getQudthung() { return qudthung; }
    public void setQudthung(double qudthung) { this.qudthung = qudthung; }
    public double getBi() { return bi; }
    public void setBi(double bi) { this.bi = bi; }
    public String getCi() { return ci; }
    public void setCi(String ci) { this.ci = ci; }
    public String getLk() { return lk; }
    public void setLk(String lk) { this.lk = lk; }
    public String getPdcode() { return pdcode; }
    public void setPdcode(String pdcode) { this.pdcode = pdcode; }
    public String getIdref() { return idref; }
    public void setIdref(String idref) { this.idref = idref; }
}


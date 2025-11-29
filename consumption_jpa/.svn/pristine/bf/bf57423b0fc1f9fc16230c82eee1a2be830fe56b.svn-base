package lixco.com.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lixco.com.entityapi.CustomerDTO;
import lixco.com.entityapi.CustomerTypesDTO;
@Entity
@Table(name="customer")
public class Customer implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_date;
	private String created_by;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_modifed_date;
	private String last_modifed_by;
	@Column(unique=true)
	private String customer_code;//mã khách hàng
	private String customer_name;// tên khách hàng
	private String tax_code;//mã số thuế
	private String sotaikhoan;
	private String address;//địa chỉ
	private String addressOld;//địa chỉ
	private String fax;//số fax
	private String cell_phone;//số điện thoại di động
	private String home_phone;//điện thoại bàn
	private String location_delivery;//địa điểm giao hàng.
	private int gender;//giới tính
    private String passport_number;//số hộ chiếu
    private String identify_number;// số chứng minh nhân dân
    private String visa_number;// số visa
    private String driving_license_number;//số lái xe
    private String personal_image_path;// hình ảnh cá nhân.
    private String company_name;//tên công ty;
    private String email;
    private String commission_type;//loại hoa hồng bột giặt
    private double commission_value; // hoa hồng bột giặt
    private String commission_ntrl_type;//loại hoa hồng ntrl
    private double commission_ntrl_value;//hoa hồng ntrl
    private double amount_npp;//số tiền npp chuyển
    private String supplier_code;//mã nhà cung cấp
    private double days_debt_quantity;//số ngày nợ
    private double encourage_rate;//tỉ lệ khuyến khích tiêu thụ sản phẩm tphcm
    @ManyToOne(fetch=FetchType.LAZY)
    private City city;// thành phố
    private String note;//ghi chú
    @ManyToOne(fetch=FetchType.LAZY)
    private CustomerTypes customer_types;// loại khách hàng
    private boolean disable;// khách hàng nghỉ bán
    private boolean not_print_customer_name;// không in tên khách hàng trên hóa đơn
    private String bank_info;// thông tin ngân hàng
    private String bank_account_no;//số tài khoản ngân hàng
    private String customer_dfcode;// mã số khách hàng
    private boolean khongxuatUNC;// khách hàng nghỉ bán
    private String ghichudongbo;
    private boolean capnhat;
    //tên người mua hàng
    private String nguoi_mua_hang;
    private String ghichu;
    private String maxn;
    
    private String CitizenIDNumber;// : Số định danh cá nhân - Chỉ giới hạn nhập tối đa 12 ký tự dạng số, không validate theo cấu trúc chi tiết
    
    public Customer(CustomerDTO customerDTO) {
    	this.customer_code = customerDTO.getCustomer_code();
    	this.customer_name = customerDTO.getCustomer_name();
    	this.tax_code = customerDTO.getTax_code();
    	this.address = customerDTO.getAddress();
    	this.addressOld = customerDTO.getAddressOld();
    	this.fax = customerDTO.getFax();
    	this.cell_phone = customerDTO.getCell_phone();
    	this.home_phone = customerDTO.getHome_phone();
    	this.location_delivery = customerDTO.getLocation_delivery();
    	this.gender = customerDTO.getGender();
    	this.passport_number = customerDTO.getPassport_number();
    	this.identify_number = customerDTO.getIdentify_number();
    	this.visa_number = customerDTO.getVisa_number();
    	this.driving_license_number = customerDTO.getDriving_license_number();
    	this.personal_image_path = customerDTO.getPersonal_image_path();
    	this.company_name = customerDTO.getCompany_name();
    	this.email = customerDTO.getEmail();
    	this.commission_type = customerDTO.getCommission_type();
    	this.commission_value = customerDTO.getCommission_value();
    	this.commission_ntrl_type = customerDTO.getCommission_ntrl_type();
    	this.commission_ntrl_value = customerDTO.getCommission_ntrl_value();
    	this.amount_npp = customerDTO.getAmount_npp();
    	this.supplier_code = customerDTO.getSupplier_code();
    	this.days_debt_quantity = customerDTO.getDays_debt_quantity();
    	this.encourage_rate = customerDTO.getEncourage_rate();
    	this.note = customerDTO.getNote();
    	this.disable = customerDTO.isDisable();
    	this.not_print_customer_name = customerDTO.isNot_print_customer_name();
    	this.bank_info = customerDTO.getBank_info();
    	this.bank_account_no = customerDTO.getBank_account_no();
    	this.customer_dfcode = customerDTO.getCustomer_dfcode();
    	this.khongxuatUNC = customerDTO.isKhongxuatUNC();
    	this.nguoi_mua_hang = customerDTO.getNguoi_mua_hang();
    	this.maxn = customerDTO.getMaxn();
    	this.CitizenIDNumber=customerDTO.getCitizenIDNumber();
    	}
	public Customer() {
	}
	public Customer(long id) {
		this.id=id;
	}
	public Customer(long id, String customer_code, String customer_name) {
		this.id = id;
		this.customer_code = customer_code;
		this.customer_name = customer_name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public Date getLast_modifed_date() {
		return last_modifed_date;
	}
	public void setLast_modifed_date(Date last_modifed_date) {
		this.last_modifed_date = last_modifed_date;
	}
	public String getLast_modifed_by() {
		return last_modifed_by;
	}
	public void setLast_modifed_by(String last_modifed_by) {
		this.last_modifed_by = last_modifed_by;
	}
	public String getCustomer_code() {
		return customer_code;
	}
	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getTax_code() {
		return tax_code;
	}
	public void setTax_code(String tax_code) {
		this.tax_code = tax_code;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getCell_phone() {
		return cell_phone;
	}
	public void setCell_phone(String cell_phone) {
		this.cell_phone = cell_phone;
	}
	public String getHome_phone() {
		return home_phone;
	}
	public void setHome_phone(String home_phone) {
		this.home_phone = home_phone;
	}
	public String getLocation_delivery() {
		return location_delivery;
	}
	public void setLocation_delivery(String location_delivery) {
		this.location_delivery = location_delivery;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getPassport_number() {
		return passport_number;
	}
	public void setPassport_number(String passport_number) {
		this.passport_number = passport_number;
	}
	public String getIdentify_number() {
		return identify_number;
	}
	public void setIdentify_number(String identify_number) {
		this.identify_number = identify_number;
	}
	public String getVisa_number() {
		return visa_number;
	}
	public void setVisa_number(String visa_number) {
		this.visa_number = visa_number;
	}
	public String getDriving_license_number() {
		return driving_license_number;
	}
	public void setDriving_license_number(String driving_license_number) {
		this.driving_license_number = driving_license_number;
	}
	public String getPersonal_image_path() {
		return personal_image_path;
	}
	public void setPersonal_image_path(String personal_image_path) {
		this.personal_image_path = personal_image_path;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCommission_type() {
		return commission_type;
	}
	public void setCommission_type(String commission_type) {
		this.commission_type = commission_type;
	}
	public double getCommission_value() {
		return commission_value;
	}
	public void setCommission_value(double commission_value) {
		this.commission_value = commission_value;
	}
	public String getCommission_ntrl_type() {
		return commission_ntrl_type;
	}
	public void setCommission_ntrl_type(String commission_ntrl_type) {
		this.commission_ntrl_type = commission_ntrl_type;
	}
	public double getCommission_ntrl_value() {
		return commission_ntrl_value;
	}
	public void setCommission_ntrl_value(double commission_ntrl_value) {
		this.commission_ntrl_value = commission_ntrl_value;
	}
	public double getAmount_npp() {
		return amount_npp;
	}
	public void setAmount_npp(double amount_npp) {
		this.amount_npp = amount_npp;
	}
	public String getSupplier_code() {
		return supplier_code;
	}
	public void setSupplier_code(String supplier_code) {
		this.supplier_code = supplier_code;
	}
	public double getDays_debt_quantity() {
		return days_debt_quantity;
	}
	public void setDays_debt_quantity(double days_debt_quantity) {
		this.days_debt_quantity = days_debt_quantity;
	}
	public double getEncourage_rate() {
		return encourage_rate;
	}
	public void setEncourage_rate(double encourage_rate) {
		this.encourage_rate = encourage_rate;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public CustomerTypes getCustomer_types() {
		return customer_types;
	}
	public void setCustomer_types(CustomerTypes customer_types) {
		this.customer_types = customer_types;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public boolean isNot_print_customer_name() {
		return not_print_customer_name;
	}
	public void setNot_print_customer_name(boolean not_print_customer_name) {
		this.not_print_customer_name = not_print_customer_name;
	}
	public String getBank_info() {
		return bank_info;
	}
	public void setBank_info(String bank_info) {
		this.bank_info = bank_info;
	}
	public String getCustomer_dfcode() {
		return customer_dfcode;
	}
	public void setCustomer_dfcode(String customer_dfcode) {
		this.customer_dfcode = customer_dfcode;
	}
	public String getBank_account_no() {
		return bank_account_no;
	}
	public void setBank_account_no(String bank_account_no) {
		this.bank_account_no = bank_account_no;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public Customer clone() throws CloneNotSupportedException {
		return (Customer) super.clone();
	}
	public boolean isKhongxuatUNC() {
		return khongxuatUNC;
	}
	public void setKhongxuatUNC(boolean khongxuatUNC) {
		this.khongxuatUNC = khongxuatUNC;
	}
	public String getGhichudongbo() {
		return ghichudongbo;
	}
	public void setGhichudongbo(String ghichudongbo) {
		this.ghichudongbo = ghichudongbo;
	}
	public boolean isCapnhat() {
		return capnhat;
	}
	public void setCapnhat(boolean capnhat) {
		this.capnhat = capnhat;
	}
	public String getSotaikhoan() {
		return sotaikhoan;
	}
	public void setSotaikhoan(String sotaikhoan) {
		this.sotaikhoan = sotaikhoan;
	}
	public String getNguoi_mua_hang() {
		return nguoi_mua_hang;
	}
	public void setNguoi_mua_hang(String nguoi_mua_hang) {
		this.nguoi_mua_hang = nguoi_mua_hang;
	}
	public String getGhichu() {
		return ghichu;
	}
	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}
	public String getMaxn() {
		return maxn;
	}
	public void setMaxn(String maxn) {
		this.maxn = maxn;
	}
	public String getCitizenIDNumber() {
		return CitizenIDNumber;
	}
	public void setCitizenIDNumber(String citizenIDNumber) {
		CitizenIDNumber = citizenIDNumber;
	}
	public String getAddressOld() {
		return addressOld;
	}
	public void setAddressOld(String addressOld) {
		this.addressOld = addressOld;
	}
	
}

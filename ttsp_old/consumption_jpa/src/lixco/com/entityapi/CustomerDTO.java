package lixco.com.entityapi;

import lixco.com.entity.Customer;
import lombok.Data;

@Data
public class CustomerDTO {

	private long id;
	private String customer_code;// mã khách hàng
	private String customer_name;// tên khách hàng
	private String tax_code;// mã số thuế
	private String address;// địa chỉ
	private String fax;// số fax
	private String cell_phone;// số điện thoại di động
	private String home_phone;// điện thoại bàn
	private String location_delivery;// địa điểm giao hàng.
	private int gender;// giới tính
	private String passport_number;// số hộ chiếu
	private String identify_number;// số chứng minh nhân dân
	private String visa_number;// số visa
	private String driving_license_number;// số lái xe
	private String personal_image_path;// hình ảnh cá nhân.
	private String company_name;// tên công ty;
	private String email;// địa chỉ
	private String commission_type;// loại hoa hồng bột giặt
	private double commission_value; // hoa hồng bột giặt
	private String commission_ntrl_type;// loại hoa hồng ntrl
	private double commission_ntrl_value;// hoa hồng ntrl
	private double amount_npp;// số tiền npp chuyển
	private String supplier_code;// mã nhà cung cấp
	private double days_debt_quantity;// số ngày nợ
	private double encourage_rate;// tỉ lệ khuyến khích tiêu thụ sản phẩm tphcm
	private String cityCode;// thành phố
	private String note;// ghi chú
	private String customer_typesCode;// loại khách hàng
	private boolean disable;// khách hàng nghỉ bán
	private boolean not_print_customer_name;// không in tên khách hàng trên hóa
											// đơn
	private String bank_info;// thông tin ngân hàng
	private String bank_account_no;// số tài khoản ngân hàng
	private String customer_dfcode;// mã số khách hàng
	private boolean khongxuatUNC;// khách hàng nghỉ bán
	private String nguoi_mua_hang;
	private String ghichu;

	public CustomerDTO(Customer customer) {
		this.id = customer.getId();
		this.customer_code = customer.getCustomer_code();
		this.customer_name = customer.getCustomer_name();
		this.tax_code = customer.getTax_code();
		this.address = customer.getAddress();
		this.fax = customer.getFax();
		this.cell_phone = customer.getCell_phone();
		this.home_phone = customer.getHome_phone();
		this.location_delivery = customer.getLocation_delivery();
		this.gender = customer.getGender();
		this.passport_number = customer.getPassport_number();
		this.identify_number = customer.getIdentify_number();
		this.visa_number = customer.getVisa_number();
		this.driving_license_number = customer.getDriving_license_number();
		this.personal_image_path = customer.getPersonal_image_path();
		this.company_name = customer.getCompany_name();
		this.email = customer.getEmail();
		this.commission_type = customer.getCommission_type();
		this.commission_value = customer.getCommission_value();
		this.commission_ntrl_type = customer.getCommission_ntrl_type();
		this.commission_ntrl_value = customer.getCommission_ntrl_value();
		this.amount_npp = customer.getAmount_npp();
		this.supplier_code = customer.getSupplier_code();
		this.days_debt_quantity = customer.getDays_debt_quantity();
		this.encourage_rate = customer.getEncourage_rate();
		this.cityCode = customer.getCity() != null ? customer.getCity().getCity_code() : "";
		this.note = customer.getNote();
		this.customer_typesCode = customer.getCustomer_types() != null ? customer.getCustomer_types().getCode() : "";
		this.disable = customer.isDisable();
		this.not_print_customer_name = customer.isNot_print_customer_name();
		this.bank_info = customer.getBank_info();
		this.bank_account_no = customer.getBank_account_no();
		this.customer_dfcode = customer.getCustomer_dfcode();
		this.khongxuatUNC = customer.isKhongxuatUNC();
		this.nguoi_mua_hang = customer.getNguoi_mua_hang();
		this.ghichu=customer.getGhichu();
	}
}

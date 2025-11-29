package lixco.com.delivery;

import java.util.Date;
import java.util.List;

public class Delivery {
	private long id;
	private Date createdDate;
	private String voucher;
	private Date delivery_date;
	private ShiftLix shiftLix;
	private TeamLix teamLix;
	private List<DeliveryDetail> listDeliveryDetail;
	private boolean lix;// false la lix , true la phieu U
	private boolean checked;
	private boolean copied;
	private boolean picked;
	private boolean imported;
	public Delivery() {
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	public Date getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(Date delivery_date) {
		this.delivery_date = delivery_date;
	}
	public ShiftLix getShiftLix() {
		return shiftLix;
	}
	public void setShiftLix(ShiftLix shiftLix) {
		this.shiftLix = shiftLix;
	}
	public TeamLix getTeamLix() {
		return teamLix;
	}
	public void setTeamLix(TeamLix teamLix) {
		this.teamLix = teamLix;
	}
	public List<DeliveryDetail> getListDeliveryDetail() {
		return listDeliveryDetail;
	}
	public void setListDeliveryDetail(List<DeliveryDetail> listDeliveryDetail) {
		this.listDeliveryDetail = listDeliveryDetail;
	}
	public boolean isLix() {
		return lix;
	}
	public void setLix(boolean lix) {
		this.lix = lix;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isCopied() {
		return copied;
	}
	public void setCopied(boolean copied) {
		this.copied = copied;
	}
	public boolean isPicked() {
		return picked;
	}
	public void setPicked(boolean picked) {
		this.picked = picked;
	}
	public boolean isImported() {
		return imported;
	}
	public void setImported(boolean imported) {
		this.imported = imported;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Delivery other = (Delivery) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

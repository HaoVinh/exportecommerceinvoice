package lixco.com.reqInfo;

import java.util.Date;
import java.util.Objects;

import lombok.Data;
@Data
public class PalletLixDTO  {
	private Long id;
	private ProductLixDTO productLixDTO;
	private double numPallet;
	private ShiftDTO shiftDTO;
	private TeamLixDTO teamLixDTO;
	private String soThuTu;
	private String kcsCode;
	private String kcsName;
	/*
	 * @thêm ngày 15/11/2023, trạng thái của pallet, sẽ thay thế cho trường
	 * reached
	 * 
	 * @0: chưa kiểm
	 * 
	 * @1: Đạt
	 * 
	 * @2: Chờ kiểm
	 * 
	 * @3: Không đạt
	 */
	private int statusPallet;
	private Date kcsDate;
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PalletLixDTO other = (PalletLixDTO) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	

}

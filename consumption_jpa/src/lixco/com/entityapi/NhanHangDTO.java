package lixco.com.entityapi;

import lixco.com.entity.NhanHang;
import lombok.Data;

@Data
public class NhanHangDTO {
	private long id;
	private String name;

	public NhanHangDTO(NhanHang ppg) {
		this.id = ppg.getId();
		this.name = ppg.getName();
	}

}

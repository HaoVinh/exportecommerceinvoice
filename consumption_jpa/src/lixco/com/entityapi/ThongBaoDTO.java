package lixco.com.entityapi;

import java.util.Date;

import lombok.Data;

@Data
public class ThongBaoDTO {
	protected Date createdDate;
	private String codeEmp;
	private String nameEmp;
	private String chuongtrinh;
	private String tieude;
	private String noidung;
	private String linktruycap;
	private boolean daxem;
	private int phanloai;
	private String note;

}

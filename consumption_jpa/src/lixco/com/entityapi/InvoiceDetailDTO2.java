package lixco.com.entityapi;

import lombok.Data;

@Data
public class InvoiceDetailDTO2 {

	private long id;
	private String masp;
	private String tensp;
	private double soluong;
	private double dongia;
	private double sotien;
	private double dongiant;
	private double sotiennt;

	
	private String maspdh;
	private Long idcthdchinh;

	public InvoiceDetailDTO2(long id, String masp, String tensp, double soluong, double dongia, double sotien,
			double dongiant, double sotiennt, String maspdh, Long idcthdchinh) {
		super();
		this.id = id;
		this.masp = masp;
		this.tensp = tensp;
		this.soluong = soluong;
		this.dongia = dongia;
		this.sotien = sotien;
		this.dongiant = dongiant;
		this.sotiennt = sotiennt;
		this.maspdh=maspdh;
		this.idcthdchinh=idcthdchinh;
	}
}

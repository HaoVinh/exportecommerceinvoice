package lixco.com.reqfox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WrapDataPromotionProgram {
	private PromotionProgram promotion_program;
	private List<PromotionProgramDetail> list_promotion_program_detail;
	private List<CustomerPromotionProgram> list_customer_promotion_program;
	public WrapDataPromotionProgram() {
		this.list_promotion_program_detail=new ArrayList<>();
		this.list_customer_promotion_program=new ArrayList<>();
	}
	public WrapDataPromotionProgram(PromotionProgram promotion_program,
			List<PromotionProgramDetail> list_promotion_program_detail,
			List<CustomerPromotionProgram> list_customer_promotion_program) {
		this.promotion_program = promotion_program;
		this.list_promotion_program_detail = list_promotion_program_detail;
		this.list_customer_promotion_program = list_customer_promotion_program;
	}
	public PromotionProgram getPromotion_program() {
		return promotion_program;
	}
	public void setPromotion_program(PromotionProgram promotion_program) {
		this.promotion_program = promotion_program;
	}
	public List<PromotionProgramDetail> getList_promotion_program_detail() {
		return list_promotion_program_detail;
	}
	public void setList_promotion_program_detail(List<PromotionProgramDetail> list_promotion_program_detail) {
		this.list_promotion_program_detail = list_promotion_program_detail;
	}
	public List<CustomerPromotionProgram> getList_customer_promotion_program() {
		return list_customer_promotion_program;
	}
	public void setList_customer_promotion_program(List<CustomerPromotionProgram> list_customer_promotion_program) {
		this.list_customer_promotion_program = list_customer_promotion_program;
	}
	public static class PromotionProgramDetail{
		private String masp;
		private String maspkm;
		private double slkmai;
		private String masoctkm;
		private double sothung;
		private double sodvsp;
		private String hinhthuckm;
		public PromotionProgramDetail() {
		}
		public PromotionProgramDetail(String masp, String maspkm, double slkmai, String masoctkm, double sothung,
				double sodvsp, String hinhthuckm) {
			this.masp = masp;
			this.maspkm = maspkm;
			this.slkmai = slkmai;
			this.masoctkm = masoctkm;
			this.sothung = sothung;
			this.sodvsp = sodvsp;
			this.hinhthuckm = hinhthuckm;
		}
		public String getMasp() {
			return masp;
		}
		public void setMasp(String masp) {
			this.masp = masp;
		}
		public String getMaspkm() {
			return maspkm;
		}
		public void setMaspkm(String maspkm) {
			this.maspkm = maspkm;
		}
		public double getSlkmai() {
			return slkmai;
		}
		public void setSlkmai(double slkmai) {
			this.slkmai = slkmai;
		}
		public String getMasoctkm() {
			return masoctkm;
		}
		public void setMasoctkm(String masoctkm) {
			this.masoctkm = masoctkm;
		}
		public double getSothung() {
			return sothung;
		}
		public void setSothung(double sothung) {
			this.sothung = sothung;
		}
		public double getSodvsp() {
			return sodvsp;
		}
		public void setSodvsp(double sodvsp) {
			this.sodvsp = sodvsp;
		}
		public String getHinhthuckm() {
			return hinhthuckm;
		}
		public void setHinhthuckm(String hinhthuckm) {
			this.hinhthuckm = hinhthuckm;
		}
	}
	public static class CustomerPromotionProgram{
		private String makh;
		private String masoctkm;
		private Date ngaybd;
		private Date ngaykt;
		private boolean discontinue;
		public CustomerPromotionProgram() {
		}
		
		public CustomerPromotionProgram(String makh, String masoctkm, Date ngaybd, Date ngaykt, boolean discontinue) {
			this.makh = makh;
			this.masoctkm = masoctkm;
			this.ngaybd = ngaybd;
			this.ngaykt = ngaykt;
			this.discontinue = discontinue;
		}
		public String getMakh() {
			return makh;
		}
		public void setMakh(String makh) {
			this.makh = makh;
		}
		public String getMasoctkm() {
			return masoctkm;
		}
		public void setMasoctkm(String masoctkm) {
			this.masoctkm = masoctkm;
		}
		public Date getNgaybd() {
			return ngaybd;
		}
		public void setNgaybd(Date ngaybd) {
			this.ngaybd = ngaybd;
		}
		public Date getNgaykt() {
			return ngaykt;
		}
		public void setNgaykt(Date ngaykt) {
			this.ngaykt = ngaykt;
		}
		public boolean isDiscontinue() {
			return discontinue;
		}
		public void setDiscontinue(boolean discontinue) {
			this.discontinue = discontinue;
		}
	}
	public static class PromotionProgram{
		private String masoctkm;
		private String ghichu;
		private Date tungay;
		private Date denngay;
		
		public PromotionProgram() {
		}
		
		public PromotionProgram(String masoctkm, String ghichu, Date tungay, Date denngay) {
			this.masoctkm = masoctkm;
			this.ghichu = ghichu;
			this.tungay = tungay;
			this.denngay = denngay;
		}

		public String getMasoctkm() {
			return masoctkm;
		}
		public void setMasoctkm(String masoctkm) {
			this.masoctkm = masoctkm;
		}
		public String getGhichu() {
			return ghichu;
		}
		public void setGhichu(String ghichu) {
			this.ghichu = ghichu;
		}
		public Date getTungay() {
			String str="30/12/1899 00:00:00";
			try{
				DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date=dateFormat.parse(str);
				if(tungay!=null && tungay.getTime()==date.getTime()){
					return null;
				}
			}catch(Exception e){
			}
			return tungay;
		}
		public void setTungay(Date tungay) {
			this.tungay = tungay;
		}
		public Date getDenngay() {
			String str="30/12/1899 00:00:00";
			try{
				DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date=dateFormat.parse(str);
				if(denngay!=null && denngay.getTime()==date.getTime()){
					return null;
				}
			}catch(Exception e){
			}
			return denngay;
		}
		public void setDenngay(Date denngay) {
			this.denngay = denngay;
		}
		
	}
}

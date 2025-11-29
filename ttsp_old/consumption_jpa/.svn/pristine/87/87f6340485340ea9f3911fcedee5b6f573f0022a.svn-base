package lixco.com.reqfox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WrapDataPricingProgram {
	private PricingProgram pricing_program;
	private List<PricingProgramDetail> list_pricing_program_detail;
	private List<CustomerPricingProgram> list_customer_pricing_program;
	public WrapDataPricingProgram() {
		this.list_customer_pricing_program=new ArrayList<>();
		this.list_pricing_program_detail=new ArrayList<>();
		
	}
	public PricingProgram getPricing_program() {
		return pricing_program;
	}
	public void setPricing_program(PricingProgram pricing_program) {
		this.pricing_program = pricing_program;
	}
	public List<PricingProgramDetail> getList_pricing_program_detail() {
		return list_pricing_program_detail;
	}
	public void setList_pricing_program_detail(List<PricingProgramDetail> list_pricing_program_detail) {
		this.list_pricing_program_detail = list_pricing_program_detail;
	}
	public List<CustomerPricingProgram> getList_customer_pricing_program() {
		return list_customer_pricing_program;
	}
	public void setList_customer_pricing_program(List<CustomerPricingProgram> list_customer_pricing_program) {
		this.list_customer_pricing_program = list_customer_pricing_program;
	}
	public static class PricingProgramDetail{
		private String masoctdg;
		private String masp;
		private double soluong;
		private double dongia;
		private Date updatetime;
		private double loinhuan;
		public PricingProgramDetail() {
		}
		
		public String getMasoctdg() {
			return masoctdg;
		}public PricingProgramDetail(String masoctdg, String masp, double soluong, double dongia, Date updatetime,
				double loinhuan) {
			this.masoctdg = masoctdg;
			this.masp = masp;
			this.soluong = soluong;
			this.dongia = dongia;
			this.updatetime = updatetime;
			this.loinhuan = loinhuan;
		}
	
		public void setMasoctdg(String masoctdg) {
			this.masoctdg = masoctdg;
		}
		public String getMasp() {
			return masp;
		}
		public void setMasp(String masp) {
			this.masp = masp;
		}
		public double getSoluong() {
			return soluong;
		}
		public void setSoluong(double soluong) {
			this.soluong = soluong;
		}
		public double getDongia() {
			return dongia;
		}
		public void setDongia(double dongia) {
			this.dongia = dongia;
		}
		public Date getUpdatetime() {
			return updatetime;
		}
		public void setUpdatetime(Date updatetime) {
			this.updatetime = updatetime;
		}

		public double getLoinhuan() {
			return loinhuan;
		}

		public void setLoinhuan(double loinhuan) {
			this.loinhuan = loinhuan;
		}
		
	}
	public static class CustomerPricingProgram{
		private String masoctdg;
		private String makh;
		private Date tungay;
		private Date denngay;
		private String ghichu;
		private boolean discontinu;
		public CustomerPricingProgram() {
		}
		public CustomerPricingProgram(String masoctdg, String makh, Date tungay, Date denngay, String ghichu,
				boolean discontinu) {
			this.masoctdg = masoctdg;
			this.makh = makh;
			this.tungay = tungay;
			this.denngay = denngay;
			this.ghichu = ghichu;
			this.discontinu = discontinu;
		}
		public String getMasoctdg() {
			return masoctdg;
		}
		public void setMasoctdg(String masoctdg) {
			this.masoctdg = masoctdg;
		}
		public String getMakh() {
			return makh;
		}
		public void setMakh(String makh) {
			this.makh = makh;
		}
		public Date getTungay() {
			return tungay;
		}
		public void setTungay(Date tungay) {
			this.tungay = tungay;
		}
		public Date getDenngay() {
			return denngay;
		}
		public void setDenngay(Date denngay) {
			this.denngay = denngay;
		}
		public String getGhichu() {
			return ghichu;
		}
		public void setGhichu(String ghichu) {
			this.ghichu = ghichu;
		}
		public boolean isDiscontinu() {
			return discontinu;
		}
		public void setDiscontinu(boolean discontinu) {
			this.discontinu = discontinu;
		}
	}
	public static class PricingProgram{
		private String masoctdg;
		private Date tungay;
		private Date denngay;
		private String ghichu;
		private String masoctdgc;
		private Date updatetime;
		public PricingProgram() {
		}
		public PricingProgram(String masoctdg, Date tungay, Date denngay, String ghichu, String masoctdgc,
				Date updatetime) {
			this.masoctdg = masoctdg;
			this.tungay = tungay;
			this.denngay = denngay;
			this.ghichu = ghichu;
			this.masoctdgc = masoctdgc;
			this.updatetime = updatetime;
		}
		public String getMasoctdg() {
			return masoctdg;
		}
		public void setMasoctdg(String masoctdg) {
			this.masoctdg = masoctdg;
		}
		public Date getTungay() {
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
		public String getGhichu() {
			return ghichu;
		}
		public void setGhichu(String ghichu) {
			this.ghichu = ghichu;
		}
		public String getMasoctdgc() {
			return masoctdgc;
		}
		public void setMasoctdgc(String masoctdgc) {
			this.masoctdgc = masoctdgc;
		}
		public Date getUpdatetime() {
			return updatetime;
		}
		public void setUpdatetime(Date updatetime) {
			this.updatetime = updatetime;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((masoctdg == null) ? 0 : masoctdg.hashCode());
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
			PricingProgram other = (PricingProgram) obj;
			if (masoctdg == null) {
				if (other.masoctdg != null)
					return false;
			} else if (!masoctdg.equals(other.masoctdg))
				return false;
			return true;
		}
		
	}
}

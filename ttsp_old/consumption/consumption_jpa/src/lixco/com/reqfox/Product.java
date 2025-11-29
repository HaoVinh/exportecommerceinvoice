package lixco.com.reqfox;

public class Product {
	private long id;
	private String masp;
	private String tensp;
	private String prod_name;
	private String tenkbhq;
	private NhomSP nhomsp;
	private LoaiSP loaisp;
	private String dvt;
	private double hsqd;
	private double dongia;
	private ProductCom productcom;
	private double sl_carton;
	private String dv_carton;
	private double tlthung;
	private String maspkm;
	private double sldutru;
	private String maspvs;
	private boolean discontinued;
	private boolean lxuatkhau;
	private String thongtinpl;
	private String maspkmai;
	private double slpallet;
	
	public Product() {
	}
	public Product(long id, String masp, String tensp, String tenkbhq,String manhomsp,String tennhomsp, String categ_id,String category,
			String e_category,String dvt, double hsqd,String masp_comp, String tensp_comp, String masp_brandp, String brand_namep, String dvtbp,
			 String dvtp, double sl_carton, String dv_carton,
			double tlthung, double sldutru, String maspvs, boolean discontinued, boolean lxuatkhau,
			String thongtinpl, String maspkmai, double slpallet) {
		this.id = id;
		this.masp = masp;
		this.tensp = tensp;
		this.tenkbhq = tenkbhq;
		this.nhomsp = new NhomSP(manhomsp, tennhomsp);
		this.loaisp = new LoaiSP(categ_id, category, e_category);
		this.dvt = dvt;
		this.hsqd = hsqd;
		this.productcom = new ProductCom(masp_comp, tensp_comp, masp_brandp, brand_namep, dvtbp, dvtp);
		this.sl_carton = sl_carton;
		this.dv_carton = dv_carton;
		this.tlthung = tlthung;
		this.sldutru = sldutru;
		this.maspvs = maspvs;
		this.discontinued = discontinued;
		this.lxuatkhau = lxuatkhau;
		this.thongtinpl = thongtinpl;
		this.maspkmai = maspkmai;
		this.slpallet = slpallet;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMasp() {
		return masp;
	}
	public void setMasp(String masp) {
		this.masp = masp;
	}
	public String getTensp() {
		return tensp;
	}
	public void setTensp(String tensp) {
		this.tensp = tensp;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public String getTenkbhq() {
		return tenkbhq;
	}
	public void setTenkbhq(String tenkbhq) {
		this.tenkbhq = tenkbhq;
	}
	public NhomSP getNhomsp() {
		return nhomsp;
	}
	public void setNhomsp(NhomSP nhomsp) {
		this.nhomsp = nhomsp;
	}
	public LoaiSP getLoaisp() {
		return loaisp;
	}
	public void setLoaisp(LoaiSP loaisp) {
		this.loaisp = loaisp;
	}
	public String getDvt() {
		return dvt;
	}
	public void setDvt(String dvt) {
		this.dvt = dvt;
	}
	public double getHsqd() {
		return hsqd;
	}
	public void setHsqd(double hsqd) {
		this.hsqd = hsqd;
	}
	public double getDongia() {
		return dongia;
	}
	public void setDongia(double dongia) {
		this.dongia = dongia;
	}
	public ProductCom getProductcom() {
		return productcom;
	}
	public void setProductcom(ProductCom productcom) {
		this.productcom = productcom;
	}
	public double getSl_carton() {
		return sl_carton;
	}
	public void setSl_carton(double sl_carton) {
		this.sl_carton = sl_carton;
	}
	public String getDv_carton() {
		return dv_carton;
	}
	public void setDv_carton(String dv_carton) {
		this.dv_carton = dv_carton;
	}
	public double getTlthung() {
		return tlthung;
	}
	public void setTlthung(double tlthung) {
		this.tlthung = tlthung;
	}
	public String getMaspkm() {
		return maspkm;
	}
	public void setMaspkm(String maspkm) {
		this.maspkm = maspkm;
	}
	public double getSldutru() {
		return sldutru;
	}
	public void setSldutru(double sldutru) {
		this.sldutru = sldutru;
	}
	public String getMaspvs() {
		return maspvs;
	}
	public void setMaspvs(String maspvs) {
		this.maspvs = maspvs;
	}
	public boolean isDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(boolean discontinued) {
		this.discontinued = discontinued;
	}
	public boolean isLxuatkhau() {
		return lxuatkhau;
	}
	public void setLxuatkhau(boolean lxuatkhau) {
		this.lxuatkhau = lxuatkhau;
	}
	public String getThongtinpl() {
		return thongtinpl;
	}
	public void setThongtinpl(String thongtinpl) {
		this.thongtinpl = thongtinpl;
	}
	public String getMaspkmai() {
		return maspkmai;
	}
	public void setMaspkmai(String maspkmai) {
		this.maspkmai = maspkmai;
	}
	public double getSlpallet() {
		return slpallet;
	}
	public void setSlpallet(double slpallet) {
		this.slpallet = slpallet;
	}
	public static class NhomSP{
		private String manhomsp;
		private String tennhomsp;
		
		public NhomSP() {
		}
		
		public NhomSP(String manhomsp, String tennhomsp) {
			this.manhomsp = manhomsp;
			this.tennhomsp = tennhomsp;
		}

		public String getManhomsp() {
			return manhomsp;
		}
		public void setManhomsp(String manhomsp) {
			this.manhomsp = manhomsp;
		}
		public String getTennhomsp() {
			return tennhomsp;
		}
		public void setTennhomsp(String tennhomsp) {
			this.tennhomsp = tennhomsp;
		}
	}
	public static class LoaiSP{
		private String categ_id;
		private String category;
		private String e_category;
		
		public LoaiSP() {
		}
		
		public LoaiSP(String categ_id, String category, String e_category) {
			this.categ_id = categ_id;
			this.category = category;
			this.e_category = e_category;
		}

		public String getCateg_id() {
			return categ_id;
		}
		public void setCateg_id(String categ_id) {
			this.categ_id = categ_id;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getE_category() {
			return e_category;
		}
		public void setE_category(String e_category) {
			this.e_category = e_category;
		}
	}
	public static class ProductCom{
		private String masp_com;
		private String tensp_com;
		private ProductBrand productbrand;
		private String dvt;
		private String madm;
		
		public ProductCom() {
		}
		
		public ProductCom(String masp_com, String tensp_com, String masp_brand, String brand_name, String dvtb, String dvt) {
			this.masp_com = masp_com;
			this.tensp_com = tensp_com;
			this.productbrand = new ProductBrand(masp_brand, brand_name, dvtb);
			this.dvt = dvt;
		}

		public String getMasp_com() {
			return masp_com;
		}
		public void setMasp_com(String masp_com) {
			this.masp_com = masp_com;
		}
		public String getTensp_com() {
			return tensp_com;
		}
		public void setTensp_com(String tensp_com) {
			this.tensp_com = tensp_com;
		}
		public ProductBrand getProductbrand() {
			return productbrand;
		}
		public void setProductbrand(ProductBrand productbrand) {
			this.productbrand = productbrand;
		}
		public String getDvt() {
			return dvt;
		}
		public void setDvt(String dvt) {
			this.dvt = dvt;
		}
		public String getMadm() {
			return madm;
		}
		public void setMadm(String madm) {
			this.madm = madm;
		}
		
	}
	public static class ProductBrand{
		private String masp_brand;
		private String brand_name;
		private String dvt;
		private String madt;
		
		public ProductBrand() {
		}
		
		public ProductBrand(String masp_brand, String brand_name, String dvt) {
			this.masp_brand = masp_brand;
			this.brand_name = brand_name;
			this.dvt = dvt;
		}

		public String getMasp_brand() {
			return masp_brand;
		}
		public void setMasp_brand(String masp_brand) {
			this.masp_brand = masp_brand;
		}
		public String getBrand_name() {
			return brand_name;
		}
		public void setBrand_name(String brand_name) {
			this.brand_name = brand_name;
		}
		public String getDvt() {
			return dvt;
		}
		public void setDvt(String dvt) {
			this.dvt = dvt;
		}
		public String getMadt() {
			return madt;
		}
		public void setMadt(String madt) {
			this.madt = madt;
		}
	}
	
}

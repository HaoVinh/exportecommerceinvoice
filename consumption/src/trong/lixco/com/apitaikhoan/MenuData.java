package trong.lixco.com.apitaikhoan;


public class MenuData {
	private Long id;
	private String tenHienThi;
	private String icon;
	private String url;
	private String moTa;
	private Long idprogram;
	private MenuData parent;

	public MenuData(Long id, String tenHienThi, String icon, String url, String moTa, Long idprogram, MenuData parent) {
		super();
		this.id = id;
		this.tenHienThi = tenHienThi;
		this.icon = icon;
		this.url = url;
		this.moTa = moTa;
		this.idprogram = idprogram;
		this.parent = parent;
	}
	public MenuData() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenHienThi() {
		return tenHienThi;
	}

	public void setTenHienThi(String tenHienThi) {
		this.tenHienThi = tenHienThi;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public Long getIdprogram() {
		return idprogram;
	}

	public void setIdprogram(Long idprogram) {
		this.idprogram = idprogram;
	}

	public MenuData getParent() {
		return parent;
	}

	public void setParent(MenuData parent) {
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		MenuData other = (MenuData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

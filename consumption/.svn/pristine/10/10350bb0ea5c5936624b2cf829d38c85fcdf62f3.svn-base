package trong.lixco.com.apitaikhoan;


public class LockTableData{
	private Long id;
	private int month;
	private int year;
	private boolean locks;
	private Long idprogram;
	public LockTableData(Long id, int month, int year, boolean locks, Long idprogram) {
		super();
		this.id = id;
		this.month = month;
		this.year = year;
		this.locks = locks;
		this.idprogram = idprogram;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public boolean isLocks() {
		return locks;
	}
	public void setLocks(boolean locks) {
		this.locks = locks;
	}
	public Long getIdprogram() {
		return idprogram;
	}
	public void setIdprogram(Long idprogram) {
		this.idprogram = idprogram;
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
		LockTableData other = (LockTableData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}

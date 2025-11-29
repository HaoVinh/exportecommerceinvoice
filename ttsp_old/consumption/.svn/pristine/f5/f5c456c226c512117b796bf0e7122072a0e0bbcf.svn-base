package trong.lixco.com.apitaikhoan;


public class UserRightData{
private Long id;
	private FormListData formListData;
	private boolean isAllow;
	private boolean isInsert;
	private boolean isUpdate;
	private boolean isDelete;
	public UserRightData(Long id, FormListData formListData, boolean isAllow, boolean isInsert, boolean isUpdate,
			boolean isDelete) {
		super();
		this.id = id;
		this.formListData = formListData;
		this.isAllow = isAllow;
		this.isInsert = isInsert;
		this.isUpdate = isUpdate;
		this.isDelete = isDelete;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public FormListData getFormListData() {
		return formListData;
	}
	public void setFormListData(FormListData formListData) {
		this.formListData = formListData;
	}
	public boolean isAllow() {
		return isAllow;
	}
	public void setAllow(boolean isAllow) {
		this.isAllow = isAllow;
	}
	public boolean isInsert() {
		return isInsert;
	}
	public void setInsert(boolean isInsert) {
		this.isInsert = isInsert;
	}
	public boolean isUpdate() {
		return isUpdate;
	}
	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	public boolean isDelete() {
		return isDelete;
	}
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
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
		UserRightData other = (UserRightData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}

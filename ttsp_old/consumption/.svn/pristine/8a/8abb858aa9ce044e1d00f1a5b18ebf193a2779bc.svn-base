package trong.lixco.com.apitaikhoan;

import lombok.Data;

@Data
public class UserRightData2 {
	private Long id;
	private UserRightData userRightData;
	private boolean isRole1;
	private boolean isRole2;
	private boolean isRole3;
	private boolean isRole4;
	private boolean isRole5;
	private String roleSpec;//quyen dac biet chuoi string

	

	public UserRightData2(Long id, UserRightData userRightData, boolean isRole1, boolean isRole2, boolean isRole3,
			boolean isRole4, boolean isRole5, String roleSpec) {
		super();
		this.id = id;
		this.userRightData = userRightData;
		this.isRole1 = isRole1;
		this.isRole2 = isRole2;
		this.isRole3 = isRole3;
		this.isRole4 = isRole4;
		this.isRole5 = isRole5;
		this.roleSpec = roleSpec;
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
		UserRightData2 other = (UserRightData2) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

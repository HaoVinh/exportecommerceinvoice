package trong.lixco.com.apitaikhoan;

public class MemberData{
	private Long id;
	private String code;
	private String name;
	private byte[] sign;
	public MemberData(Long id, String code, String name, byte[] sign) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.sign = sign;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getSign() {
		return sign;
	}
	public void setSign(byte[] sign) {
		this.sign = sign;
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
		MemberData other = (MemberData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

package trong.lixco.com.apitaikhoan;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

public class ProgramData{
	private Long id;
	private String fullname;
	private String name;
	private String description;
	private String uRL;
	private String serveraddress;
	private String serveraddressPublic;
	private short indexProgram;
	public ProgramData(Long id, String fullname, String name, String description, String uRL, String serveraddress,
			String serveraddressPublic, short indexProgram) {
		super();
		this.id = id;
		this.fullname = fullname;
		this.name = name;
		this.description = description;
		this.uRL = uRL;
		this.serveraddress = serveraddress;
		this.serveraddressPublic = serveraddressPublic;
		this.indexProgram = indexProgram;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getuRL() {
		return uRL;
	}
	public void setuRL(String uRL) {
		this.uRL = uRL;
	}
	public String getServeraddress() {
		return serveraddress;
	}
	public void setServeraddress(String serveraddress) {
		this.serveraddress = serveraddress;
	}
	public String getServeraddressPublic() {
		return serveraddressPublic;
	}
	public void setServeraddressPublic(String serveraddressPublic) {
		this.serveraddressPublic = serveraddressPublic;
	}
	public short getIndexProgram() {
		return indexProgram;
	}
	public void setIndexProgram(short indexProgram) {
		this.indexProgram = indexProgram;
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
		ProgramData other = (ProgramData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}

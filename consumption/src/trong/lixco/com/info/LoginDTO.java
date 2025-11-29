package trong.lixco.com.info;

public class LoginDTO {
	private long id;
	private String code;
	private String name;
	private String user;
	private String pass;
	
	
	public LoginDTO(long id, String code, String name, String user, String pass) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.user = user;
		this.pass = pass;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
}

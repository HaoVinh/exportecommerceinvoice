package lixco.com.reqInfo;

public class WrapDelOrderLixReqInfo {
	private long id;
	private String member_name;
	
	public WrapDelOrderLixReqInfo() {
	}
	
	public WrapDelOrderLixReqInfo(long id, String member_name) {
		this.id = id;
		this.member_name = member_name;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	
}

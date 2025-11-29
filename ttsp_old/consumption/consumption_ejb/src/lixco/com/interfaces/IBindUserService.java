package lixco.com.interfaces;

import lixco.com.entity.BindUser;

public interface IBindUserService {
	public BindUser  getBindUser(long memberId);
	public BindUser getBindUserByIdFox(String idfox);
}

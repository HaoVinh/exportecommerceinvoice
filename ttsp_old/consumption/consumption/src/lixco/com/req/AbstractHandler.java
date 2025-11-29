package lixco.com.req;

import javax.ws.rs.core.Response.ResponseBuilder;

public abstract class AbstractHandler {
	protected abstract int authenticate(String data,ResponseBuilder responseBuilder,StringBuilder authenErrorMsg);
}

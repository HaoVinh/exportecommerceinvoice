package trong.lixco.com.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;

@Path("account")
public class AccountServlet {
	@Inject
	private AccountHanlder accountHanlder;
	@Inject
	private Logger logger;

	/*
	 * @data:{ "userName": "robot", "passWord": "ROBOT@2022" }
	 */
	@POST
	@Path("login")
	@Produces("application/json; charset=utf-8")
	public Response postAction(String data) throws IOException, ServletException {
		StringBuilder result = new StringBuilder();
		ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
		try {
			if (data != null && !data.equals("")) {
				accountHanlder.processPost(data, result, responseBuilder);
			} else {
				result.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
				responseBuilder = Response.status(Response.Status.NO_CONTENT);
			}
		} catch (Exception e) {
			result.append(CommonModel.toJson(-1, MessagesAPI.MALFORMED_DATA));
			responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//			logger.error("AccountServlet.postAction:" + e.getMessage(), e);
		}
		return responseBuilder.entity(result.toString()).build();
	}
}

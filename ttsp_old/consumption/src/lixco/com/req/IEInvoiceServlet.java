package lixco.com.req;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;

import lixco.com.common.CommonModel;

@Path("ieinvoice")
public class IEInvoiceServlet {
	@Inject
	private Logger logger;
	@Inject
	private IEInvoiceHandler iEInvoiceHandler;
	@Context 
	private HttpServletRequest servletRequest;
//	private final String RESOURCES_TEXT="cms.order_lix";
	@POST
	@Produces("application/json; charset=utf-8")
	public Response postFormDataAction(@FormParam(DefinedName.PARAM_COMMAND) String cmd,@FormParam(DefinedName.PARAM_DATA) String data)throws IOException, ServletException{
		String result=new String();
		ResponseBuilder responseBuilder=Response.status(Response.Status.BAD_REQUEST);
		int ret;
		try{
			 String vrfContent=new String();
			 ret=authenticate(servletRequest, vrfContent);
			 if(ret !=0){
				 result=lixco.com.common.CommonModel.toJson(ret, DefinedName.RESP_MSG_INVALID_REQUEST,vrfContent);
				 responseBuilder=Response.status(Response.Status.UNAUTHORIZED);
			 }else{
				 StringBuilder contentBuilder=new StringBuilder();
				 if(cmd!=null && data !=null){
					 iEInvoiceHandler.processPostFormData(cmd, data,responseBuilder, contentBuilder);
				 }else{
					 contentBuilder.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
	 			 }
				 result=contentBuilder.toString();
			 }
		}catch(Exception e){
			logger.error("IEInvoiceServlet.postAction:"+e.getMessage(),e);
			responseBuilder=Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result).build();
	}
	@GET
	@Produces("application/json; charset=utf-8")
	public Response getQueryDataAction(@QueryParam(DefinedName.PARAM_COMMAND) String cmd,@QueryParam(DefinedName.PARAM_DATA) String data) throws IOException, ServletException{
		String result=new String();
		ResponseBuilder responseBuilder=Response.status(Response.Status.BAD_REQUEST);
		int ret;
		try{
			 String vrfContent=new String();
			 ret=authenticate(servletRequest, vrfContent);
			 if(ret !=0){
				 result=CommonModel.toJson(ret, DefinedName.RESP_MSG_INVALID_REQUEST,vrfContent);
				 responseBuilder=Response.status(Response.Status.UNAUTHORIZED);
			 }else{
				 StringBuilder contentBuilder=new StringBuilder();
				 if(cmd!=null && data !=null){
					 iEInvoiceHandler.processGetQueryData(cmd, data,responseBuilder, contentBuilder);
				 }else{
					 contentBuilder.append(CommonModel.toJson(-1, DefinedName.RESP_MSG_INVALID_REQUEST));
	 			 }
				 result=contentBuilder.toString();
			 }
		}catch(Exception e){
			logger.error("IEInvoiceServlet.postAction:"+e.getMessage(),e);
			responseBuilder=Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return responseBuilder.entity(result).build();
	}
	//authenticate
	private int authenticate(HttpServletRequest request,String authenErrorMsg){
		return 0;
	}
}

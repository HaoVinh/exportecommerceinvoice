package trong.lixco.com.api;

import io.jsonwebtoken.io.IOException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.logging.Logger;

@Path("/data")
public class HoaDonServlet {

    @Inject
    private HoaDonHandler hoaDonHandler;
    
    @Inject
    private Logger logger;

    @Context
    private HttpServletRequest servletRequest;

    @GET
    @Path("/hoadon/code")
    @Produces("application/json; charset=utf-8")
    public Response getByCode(@QueryParam("code") String code, @QueryParam("date") String dateStr) throws IOException, ServletException { 
        String result = "";
        ResponseBuilder responseBuilder = Response.status(Response.Status.BAD_REQUEST);
        try {
            StringBuilder contentBuilder = new StringBuilder();
            if (code != null && !code.trim().isEmpty()) {
                Date fromDate;
                if (dateStr != null && !dateStr.trim().isEmpty()) {
                    // Nếu truyền ngày: parse ngày đó
                	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    fromDate = sdf.parse(dateStr.trim());
                } else {
                    // Nếu không truyền ngày: mặc định là 3 ngày trước
                    LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
                    fromDate = Date.from(threeDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
                }

                hoaDonHandler.process(code.trim(), fromDate, responseBuilder, contentBuilder);
                responseBuilder.status(Response.Status.OK);
            } else {
                contentBuilder.append(CommonService.FormatResponse(-1, "Mã hóa đơn không hợp lệ", "data", null));
                responseBuilder.status(Response.Status.BAD_REQUEST);
            }

            result = contentBuilder.toString();
        } catch (Exception e) {
            logger.error("HoaDonServlet.getByCode:" + e.getMessage(), e);
            result = CommonService.FormatResponse(-1, "Lỗi hệ thống khi xử lý yêu cầu", "data", null);
            responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return responseBuilder.entity(result).build();
    }
}

//package lixco.com.bean;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import javax.inject.Inject;
//
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import lombok.Getter;
//import lombok.Setter;
//import trong.lixco.com.bean.AbstractBean;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import vinh.lixco.com.apiecommerce.OrderResponseDTO;
//import vinh.lixco.com.apiecommerce.OrderItemsResponseDTO;
//import vinh.lixco.com.apiecommerce.LazadaAPIServlet;
//
//@ManagedBean
//@ViewScoped
//public class OrderManagementBean extends AbstractBean implements Serializable {
//    private static final long serialVersionUID = 1L;
//    @Inject
//    private Log logger = LogFactory.getLog(OrderManagementBean.class);
//
//    @Getter
//    @Setter
//    private Date startDate;
//    @Getter
//    @Setter
//    private Date endDate;
//    @Getter
//    @Setter
//    private String orderCode;
//    @Getter
//    @Setter
//    private String statusFilter;
//    @Getter
//    @Setter
//    private String itemCode;
//    @Getter
//    @Setter
//    private String customerCode;
//
//    @Getter
//    @Setter
//    private List<OrderResponseDTO> orderList;
//    @Getter
//    @Setter
//    private OrderResponseDTO selectedOrder;
//    @Getter
//    @Setter
//    private OrderResponseDTO currentOrder;
//    @Getter
//    @Setter
//    private OrderItemsResponseDTO itemDetailsResponse;
//    @Getter
//    @Setter
//    private List<OrderItemsResponseDTO.DataDTO> selectedItems;
//
//    private LazadaAPIServlet apiService;
//    private String baseUrl = "http://localhost:8080/lazada"; // Điều chỉnh URL server của bạn
//
//    @Override
//    protected void initItem() {
//        startDate = new Date(); // Mặc định ngày hiện tại: 01:19 PM +07, 03/07/2025
//        endDate = new Date();
//        currentOrder = new OrderResponseDTO();
//        searchOrders();
//    }
//
//    public void searchOrders() {
//        try {
//            Client client = ClientBuilder.newClient();
//            WebTarget target = client.target(baseUrl + "/orders/get")
//                    .queryParam("created_after", "2025-07-01T09:00:00+07:00")
//                    .queryParam("created_before", "2025-07-03T13:19:00+07:00") // 01:19 PM +07, 03/07/2025
//                    .queryParam("status", statusFilter == null ? "shipped" : statusFilter)
//                    .queryParam("limit", 10);
//            Response response = target.request(MediaType.APPLICATION_JSON).get();
//            if (response.getStatus() == 200) {
//                String jsonResponse = response.readEntity(String.class);
//                ObjectMapper mapper = new ObjectMapper();
//                currentOrder = mapper.readValue(jsonResponse, OrderResponseDTO.class);
//                orderList = new ArrayList<>();
//                orderList.add(currentOrder);
//            }
//            response.close();
//            client.close();
//        } catch (Exception e) {
//            logger.error("Error searching orders: ", e);
//            errorDialog(e.getLocalizedMessage());
//        }
//    }
//
//    @Getter
//    @Setter
//    private String orderSearch;
//
//    public void searchByOrder() {
//        try {
//            if (orderSearch == null || "".equals(orderSearch)) {
//                warn("Chưa nhập dữ liệu tìm kiếm.");
//            } else {
//                Client client = ClientBuilder.newClient();
//                WebTarget target = client.target(baseUrl + "/orders/get")
//                        .queryParam("order_number", orderSearch)
//                        .queryParam("limit", 10);
//                Response response = target.request(MediaType.APPLICATION_JSON).get();
//                if (response.getStatus() == 200) {
//                    String jsonResponse = response.readEntity(String.class);
//                    ObjectMapper mapper = new ObjectMapper();
//                    currentOrder = mapper.readValue(jsonResponse, OrderResponseDTO.class);
//                    orderList = new ArrayList<>();
//                    orderList.add(currentOrder);
//                }
//                response.close();
//                client.close();
//            }
//        } catch (Exception e) {
//            logger.error("Error searching by order: ", e);
//            errorDialog(e.getLocalizedMessage());
//        }
//    }
//
//    public void selectOrder(OrderResponseDTO item) {
//        selectedOrder = item;
//        loadItemDetails(item.getData().getOrders().get(0).getOrderId()); // Giả định lấy orderId đầu tiên
//    }
//
//    public void loadItemDetails(long orderId) {
//        try {
//            Client client = ClientBuilder.newClient();
//            WebTarget target = client.target(baseUrl + "/order/items/get").queryParam("order_id", orderId);
//            Response response = target.request(MediaType.APPLICATION_JSON).get();
//            if (response.getStatus() == 200) {
//                String jsonResponse = response.readEntity(String.class);
//                ObjectMapper mapper = new ObjectMapper();
//                itemDetailsResponse = mapper.readValue(jsonResponse, OrderItemsResponseDTO.class);
//                selectedItems = new ArrayList<>();
//                selectedItems.add(itemDetailsResponse.getData());
//            }
//            response.close();
//            client.close();
//        } catch (Exception e) {
//            logger.error("Error loading item details: ", e);
//            errorDialog(e.getLocalizedMessage());
//        }
//    }
//
//    public void refreshOrder() {
//        try {
//            if (selectedOrder == null) {
//                errorDialog("Đơn hàng không tải lại được.");
//                return;
//            }
//            loadItemDetails(selectedOrder.getData().getOrders().get(0).getOrderId());
//        } catch (Exception e) {
//            logger.error("Error refreshing order: ", e);
//            errorDialog(e.getLocalizedMessage());
//        }
//    }
//
//    @Getter
//    @Setter
//    private int activeTabIndex;
//
//    public void showEditActiveTab() {
//        activeTabIndex = 1;
//        showEdit();
//    }
//
//    public void showEdit() {
//        this.currentOrder = selectedOrder;
//    }
//
//    @Getter
//    @Setter
//    private int detailTabIndex;
//
//    public void showEditDetail() {
//        // Logic hiển thị chi tiết, có thể mở rộng
//    }
//
//    @Override
//    protected Log getLogger() {
//        return logger;
//    }
//}
package vinh.lixco.com.apiecommerce;

import lixco.com.entity.EcomOrder;

public class EcomOrderUtils {
	public static void setMyStatus(EcomOrder order) {
		if (order.getStatus() == null) {
			order.setMyStatus("");
			return;
		}
		String statusLower = order.getStatus().toLowerCase();
		switch (statusLower) {
		case "pending":
		case "processed":
		case "awaiting_shipment":
			order.setMyStatus("Chờ xử lý/Chờ giao hàng");
			break;
		case "delivered":
			order.setMyStatus("Đã giao");
			break;
		case "unpaid":
			order.setMyStatus("Chưa trả tiền");
			break;
		case "packed":
			order.setMyStatus("Đã đóng gói");
			break;
		case "ready_to_ship":
			order.setMyStatus("Sẵn sàng giao");
			break;
		case "shipped":
			order.setMyStatus("Đang giao");
			break;
		case "in_cancel":
		case "cancelled":
		case "canceled":
		case "cancel":
			order.setMyStatus("Đã hủy");
			break;
		case "to_confirm_receive":
			order.setMyStatus("Chờ xác nhận");
			break;
		case "confirmed":
		case "completed":
			order.setMyStatus("Đã nhận được hàng/Đã hoàn thành");
			break;
		case "retry_ship":
		case "awaiting_collection":
			order.setMyStatus("Chờ lấy hàng");
			break;
		case "to_return":
		case "shipped_back":
			order.setMyStatus("Hoàn hàng");
			break;
		case "in_transit":
			order.setMyStatus("Đang trung chuyển");
			break;
		case "shipped_back_success":
			order.setMyStatus("Hoàn hàng thành công");
			break;
		default:
			order.setMyStatus(order.getStatus());
			break;
		}
	}
}

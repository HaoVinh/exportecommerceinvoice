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

		case "processed":
			order.setMyStatus("Người bán đã tạo đơn vận chuyển online và lấy được mã vận đơn");
			break;
		case "pending":
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
			order.setMyStatus("Đang trong quá trình hủy đơn");
			break;
		case "cancelled":
		case "canceled":
		case "cancel":
			order.setMyStatus("Đã hủy");
			break;
		case "to_confirm_receive":
			order.setMyStatus("Đã giao đến người mua");
			break;
		case "confirmed":
		case "completed":
			order.setMyStatus("Đã nhận được hàng/Đã hoàn thành");
			break;
		case "retry_ship":
			order.setMyStatus("Thử lại giao hàng ");
			break;
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

package lixco.com.bean;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import lixco.com.entity.OrderLix;

import java.io.Serializable;

@Named
@SessionScoped
public class DataSessionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private OrderLix orderLix;
	@PostConstruct
	public void init() {
		
	}
	public OrderLix getOrderLix() {
		return orderLix;
	}
	public void setOrderLix(OrderLix orderLix) {
		this.orderLix = orderLix;
	}
}

package com.vmware.ecommerce.model;

import lombok.Data;

@Data
public class OrderMapper {
	
	private int orderId;
	
	private String productName;
	
	private String productPrice;
	
	private int quantity;
	
	private String currentState;
	
	private String status;
	
	private Boolean isOrderConfirmed;
	
	private Boolean isInvoiceConfirmed;
	
	private Boolean isPaymentConfirmed;
	

}

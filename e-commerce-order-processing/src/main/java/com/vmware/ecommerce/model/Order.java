package com.vmware.ecommerce.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "Order_View")
public class Order {
	
	@Id
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

package com.vmware.ecommerce.model;

public enum OrderState {

	Order_Created,
	Order_Confirmed,
	Invoice_Created,
	Payment_Pending,
	Payment_Completed,	
	Shipping_Processed,
	Shipping_Completed,
	Product_Delivered
	
}

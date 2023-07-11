package com.vmware.ecommerce.service;

import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import com.vmware.ecommerce.model.Order;
import com.vmware.ecommerce.model.OrderEvent;
import com.vmware.ecommerce.model.OrderState;

@Service
public interface OrderService {
	
	public void performTransition(StateMachine<OrderState, OrderEvent> stateMachine,
			OrderState sourceState, OrderState targetState, Order order );

}

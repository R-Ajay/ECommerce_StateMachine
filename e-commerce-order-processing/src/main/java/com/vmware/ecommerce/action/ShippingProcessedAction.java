package com.vmware.ecommerce.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

import com.vmware.ecommerce.model.Order;
import com.vmware.ecommerce.model.OrderEvent;
import com.vmware.ecommerce.model.OrderState;
import com.vmware.ecommerce.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShippingProcessedAction extends AbstractOrderAction {

	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	void makeServiceCall(StateContext<OrderState, OrderEvent> stateContext) {
		
		log.info("ShippingProcessedAction class Invoke");

		int orderId = (Integer) stateContext.getExtendedState().getVariables().get("OrderId");

		Order order = orderRepository.findById(orderId).orElseThrow(null);

		order.setCurrentState(stateContext.getTransition().getSource().getId().toString());
		
		if(order.getStatus().equalsIgnoreCase("Payment_Received")) {
			 order.setStatus("Product_Dispatched");
			 orderRepository.save(order);
		}
		else {
			 order.setStatus("Product_Not_Dispatched");
			 orderRepository.save(order);
			 throw new RuntimeException("Payment is not received so product dispatch is stopped");
		}

          
		
		
		
		
	}

}

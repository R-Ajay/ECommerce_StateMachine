package com.vmware.ecommerce.action;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.ecommerce.model.Order;
import com.vmware.ecommerce.model.OrderEvent;
import com.vmware.ecommerce.model.OrderMapper;
import com.vmware.ecommerce.model.OrderState;
import com.vmware.ecommerce.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderPlacedAction extends AbstractOrderAction {
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	private OrderRepository orderRepository;
 
	@Override
	public void makeServiceCall(StateContext<OrderState, OrderEvent> stateContext) {
		
		log.info("OrderPlacedAction class Invoke");
		
		Object payload = stateContext.getExtendedState().getVariables().get("payload");
		
		Order order = new Order();
		
		OrderMapper orderMapper = null;
		
		try {
			orderMapper = mapper.readValue(mapper.writeValueAsString(payload), OrderMapper.class);			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		BeanUtils.copyProperties(orderMapper, order);
		
		order.setCurrentState(stateContext.getTransition().getSource().getId().toString());
		
		String status = (Boolean) stateContext.getExtendedState().getVariables().get("Order_Confirmed")
                            ? "Order_Placed" : "Order_Not_Confirmed";
		
		order.setStatus(status);
		
		orderRepository.save(order);
		
		if(status.equalsIgnoreCase("Order_Not_Confirmed")){
			log.info("Order is not confirmed");
			throw new RuntimeException("Order is not confirmed");
		}else {
			log.info("Order Placed Successfully");
		}

	}

}

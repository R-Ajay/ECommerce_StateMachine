package com.vmware.ecommerce.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.vmware.ecommerce.model.Order;
import com.vmware.ecommerce.model.OrderEvent;
import com.vmware.ecommerce.model.OrderState;
import com.vmware.ecommerce.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private StateMachineFactory<OrderState, OrderEvent> stateMachineFactory;
	
	@Autowired
	private OrderRepository orderRepository;
	    
	
	//Start the orchestration flow
	
	@Override
	public void performTransition(StateMachine<OrderState, OrderEvent> stateMachine, OrderState sourceState,
			OrderState targetState, Order order) {
		
		stateMachine = stateMachineFactory.getStateMachine(String.valueOf(order.getOrderId()));

		stateMachine.start();
			
		stateMachine.getExtendedState().getVariables().put("payload", order);
		
		stateMachine.getExtendedState().getVariables().put("OrderId", order.getOrderId());
		
		stateMachine.getExtendedState().getVariables().put("Order_Confirmed", order.getIsOrderConfirmed());
		
		Iterator<Transition<OrderState, OrderEvent>> transitions = stateMachine.getTransitions().iterator();
		

		Transition<OrderState, OrderEvent> transition;

		while (transitions.hasNext()) {
						
			transition = transitions.next();
			String currentState = transition.getSource().getId().toString();

			OrderEvent orderEvent;
			
			if(stateMachine.hasStateMachineError()) {
				log.info("Orchestration is stopped while moving to this state : " + currentState);
				stateMachine.stop(); 
				break;
				
			}else {
				orderEvent = transition.getTrigger().getEvent();
                stateMachine.sendEvent(orderEvent);
				log.info("Triggering Event Name : {}", transition.getTrigger().getEvent());

			}


		}

	}

}

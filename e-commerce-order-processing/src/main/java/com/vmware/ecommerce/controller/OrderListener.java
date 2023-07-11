package com.vmware.ecommerce.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.vmware.ecommerce.config.MqConfig;
import com.vmware.ecommerce.model.Order;
import com.vmware.ecommerce.model.OrderEvent;
import com.vmware.ecommerce.model.OrderState;
import com.vmware.ecommerce.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderListener {
	
	@Autowired
	private OrderService orderService;
	
	
	@RabbitListener(queues = MqConfig.MESSAGE_QUEUE)
	public void listener(Order order) {
				
		log.info("Payload : " + order);
		orderService.performTransition(null, OrderState.Order_Created, OrderState.Product_Delivered, order);
	}

}

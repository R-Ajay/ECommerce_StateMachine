package com.vmware.ecommerce.action;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.vmware.ecommerce.model.OrderEvent;
import com.vmware.ecommerce.model.OrderState;

@Component
public abstract class AbstractOrderAction implements Action<OrderState, OrderEvent> {
	
	@Override
	public void execute(StateContext<OrderState, OrderEvent> stateContext) {
		makeServiceCall(stateContext);
	}
	
	abstract void makeServiceCall(StateContext<OrderState, OrderEvent> stateContext);

}

package com.vmware.ecommerce.config;

import java.util.EnumSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import com.vmware.ecommerce.action.InvoiceCreatingAction;
import com.vmware.ecommerce.action.OrderConfirmedAction;
import com.vmware.ecommerce.action.OrderPlacedAction;
import com.vmware.ecommerce.action.PaymentProcessingAction;
import com.vmware.ecommerce.action.PaymentReceivingAction;
import com.vmware.ecommerce.action.ShippingCompletedAction;
import com.vmware.ecommerce.action.ShippingProcessedAction;
import com.vmware.ecommerce.model.OrderEvent;
import com.vmware.ecommerce.model.OrderState;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

	   @Override
		public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
			states.withStates()
			.initial(OrderState.Order_Created)
			.end(OrderState.Product_Delivered)
			.states(EnumSet.allOf(OrderState.class));
		}
	   
	   
	   @Override
	   public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
		transitions
		 .withExternal()
		 .source(OrderState.Order_Created).target(OrderState.Order_Confirmed).event(OrderEvent.Order_Placing_Event)
		 .action(orderPlaced(), errorAction())
		 .and()
		 .withExternal()
		 .source(OrderState.Order_Confirmed).target(OrderState.Invoice_Created).event(OrderEvent.Invoice_Processing_Event)
		 .action(orderConfirmed(), errorAction())
		 .and()
		 .withExternal()
		 .source(OrderState.Invoice_Created).target(OrderState.Payment_Pending).event(OrderEvent.Invoice_Creating_Event)
		 .action(invoiceCreated(), errorAction())
		 .and()
		 .withExternal()
		 .source(OrderState.Payment_Pending).target(OrderState.Payment_Completed).event(OrderEvent.Payment_Processing_Event)
		 .action(paymentProcessed(), errorAction())
		 .and()
		 .withExternal()
		 .source(OrderState.Payment_Completed).target(OrderState.Shipping_Processed).event(OrderEvent.Payment_Receiving)
		 .action(paymentReceived(), errorAction())
		 .and()
		 .withExternal()
		 .source(OrderState.Shipping_Processed).target(OrderState.Shipping_Completed).event(OrderEvent.Shipping_Processing_Event)
		 .action(shippingProcessed(), errorAction())
		 .and()
		 .withExternal()
		 .source(OrderState.Shipping_Completed).target(OrderState.Product_Delivered).event(OrderEvent.Delivery_Processing_Event)
		 .action(shippingCompleted());
         
	 }



	@Override
	  public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
	 	
    	  StateMachineListenerAdapter<OrderState, OrderEvent> adapter 
    	                    = new StateMachineListenerAdapter<OrderState, OrderEvent>(){
    		 
    		  @Override
    		  public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
    			 if(from!=null)
    			    log.info("State Changed from " + from.getId() + " to " + to.getId());
    		}   		  
    	  };
    	  
    	  config
  		  .withConfiguration()
  		   .autoStartup(true)
  		    .listener(adapter);
	 }
      
      @Bean
      public Action<OrderState, OrderEvent> orderPlaced(){
   	  
    	  return new OrderPlacedAction();
      }
      
      @Bean
      public Action<OrderState, OrderEvent> orderConfirmed() {
		
		return new OrderConfirmedAction();
	}
     
      @Bean
      public Action<OrderState, OrderEvent> invoiceCreated() {
	
		return new InvoiceCreatingAction();
	}
	
      @Bean
      public Action<OrderState, OrderEvent> paymentProcessed() {

		return new PaymentProcessingAction();
	}
	
      @Bean
      public Action<OrderState, OrderEvent> paymentReceived() {

		return new PaymentReceivingAction();
	}
  	
      @Bean
      public Action<OrderState, OrderEvent> shippingProcessed() {
	
		return new ShippingProcessedAction();
	}
      
      @Bean
  	  public Action<OrderState, OrderEvent> shippingCompleted() {
		
		return new ShippingCompletedAction();
	}

     
      @Bean
  	  public Action<OrderState, OrderEvent> errorAction() {
		return context->{
			context.getStateMachine().setStateMachineError(context.getException());
		};
	}

}

package schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import service.OrderService;

/**
 * This class is a task to be called by spring quartz
 * To synchronize user mobile info
 * @author sunshine
 *
 */
public class ConsumerMobileSchedule {
	private Logger logger = LoggerFactory.getLogger(ConsumerMobileSchedule.class);

	@Autowired 
	private OrderService orderService;
	
	public void schedule() {
	
	}
}

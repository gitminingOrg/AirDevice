package payment;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.ResultMap;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	private Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@RequestMapping("/prepare/{orderId}")
	public ResultMap prepay() {
		ResultMap result = new ResultMap();
		
		return result;
	}
	
	
}

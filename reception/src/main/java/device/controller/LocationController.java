package device.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import location.service.LocationService;

@RestController
@RequestMapping("/location")
public class LocationController {
	private Logger logger = LoggerFactory.getLogger(LocationController.class);

	@Autowired
	private LocationService locationService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/init")
	public void location() {
		locationService.init();
	}
}

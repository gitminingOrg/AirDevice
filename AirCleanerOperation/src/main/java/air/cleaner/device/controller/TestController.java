package air.cleaner.device.controller;

import model.ResultMap;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private NioSocketAcceptor ioAcceptor;
	
	@RequestMapping("/sessions")
	public ResultMap getSessionInfos(){
		ResultMap resultMap = new ResultMap();
		int sessionCount = ioAcceptor.getManagedSessionCount();
		resultMap.addContent("count", sessionCount);
		resultMap.addContent("sessionMap", ioAcceptor.getManagedSessions().keySet());
		return resultMap;
	}
}

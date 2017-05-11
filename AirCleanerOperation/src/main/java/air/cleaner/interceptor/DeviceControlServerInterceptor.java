package air.cleaner.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import air.cleaner.cache.SessionCacheManager;
import air.cleaner.config.Config;
import air.cleaner.model.ResultMap;
import air.cleaner.utils.HttpDeal;

import com.google.gson.Gson;

public class DeviceControlServerInterceptor implements HandlerInterceptor{
	
	public static Logger LOG = LoggerFactory.getLogger(DeviceControlServerInterceptor.class);
	@Autowired
	private SessionCacheManager sessionCacheManager;
	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}
	
	@Autowired
	private Config config;
	public void setConfig(Config config) {
		this.config = config;
	}

	/**
	 * check if the request should be redirect to a remote host
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LOG.info("request path :"+request.getRequestURI() + " query :" + request.getQueryString());
		if(!request.getParameterMap().containsKey("token")){
			return true;
		}else{
			String token = request.getParameter("token");
			if (sessionCacheManager.getSession(token) != null) {
				return true;
			}else{
				String local = config.getLocalDomain();
				String port = config.getWebPort();
				String url = "http://"+config.getNotifyServer()+":"+config.getNotifyPort()+config.getNotifyPath()+"/"+token;
				String notifyString = HttpDeal.getResponse(url);
				LOG.info(url + " : " +  notifyString);
				Gson gson = new Gson();
				ResultMap resultMap = gson.fromJson(notifyString, ResultMap.class);
				if (resultMap.getStatus() == ResultMap.STATUS_FAILURE) {
					LOG.debug("device unavailable");
					return true;
				}else if(resultMap.getStatus() == ResultMap.STATUS_SUCCESS){
					//if local, session unavailable
					String ip = (String) resultMap.getContents().get("ip");
					if(ip.equals(local)){
						LOG.debug("session unavailable");
						return true;
					}else{
						//remote, send redirect
						String redirectURL = "http://"+ip+":"+port+request.getRequestURI()+"?"+request.getQueryString();
						response.sendRedirect(redirectURL);
					}
					
				}
				
				return false;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
}

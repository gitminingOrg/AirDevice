package security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import vip.service.ConsumerSerivce;
import vo.vip.ConsumerVo;

public class WechatRealm extends AuthorizingRealm {
	private Logger logger = LoggerFactory.getLogger(WechatRealm.class);

	@Autowired
	private ConsumerSerivce consumerSerivce;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken up = (UsernamePasswordToken) token;
		String wechat = up.getUsername();
		ConsumerVo consumer = consumerSerivce.login(wechat);
		if(StringUtils.isEmpty(consumer)) {
			return null;
		}
		return new SimpleAuthenticationInfo(consumer, up.getPassword(), getName());
	}
}

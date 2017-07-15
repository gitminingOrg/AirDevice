package security;

import java.util.List;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import model.user.Role;
import model.user.User;
import service.UserService;
import utils.ResponseCode;
import utils.ResultData;
import vo.user.UserVo;

public class AccountRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		UserVo user = (UserVo) principalCollection.getPrimaryPrincipal();
		List<String> role = user.getRoles();
		info.addRoles(role);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String username = token.getUsername();
		String password = new String(token.getPassword());
		User user = new User(username, password);
		ResultData response = userService.login(user);
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			UserVo vo = (UserVo) response.getData();
			if (vo != null) {
				return new SimpleAuthenticationInfo(vo, token.getPassword(), getName());
			}
		}
		return null;
	}
}

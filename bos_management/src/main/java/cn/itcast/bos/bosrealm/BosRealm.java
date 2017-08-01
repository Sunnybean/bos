package cn.itcast.bos.bosrealm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;

//实现安全连接
@Service("bosRealm")
public class BosRealm extends AuthorizingRealm{
	
	@Autowired
//	@Qualifier("userService")
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		// TODO Auto-generated method stub
		System.out.println("shiro授权管理");
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("shiro认证管理");
		
		//转寒token
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		if (user == null) {
			//用户名不存在
			//参数一：期望登陆后，保存在Subject中的信息
			//参数二 ：如果返回null，说明用户名那个不残在，报用户名
			//参数三：realm名称
			return null;
		}else{
			//用户名字存在
			//当返回用户名密码的时候，securityManager安全管理器，自动比骄傲 返回密码和用户密码是否一致
			//如果成功，登陆成功，如果不一致，就报异常
			return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
		}
		
	}

}

package cn.itcast.bos.bosrealm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;

//实现安全连接
//@Service("bosRealm")

public class BosRealm extends AuthorizingRealm{
	
	@Autowired
//	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		// TODO Auto-generated method stub
		System.out.println("shiro授权管理");
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		//根据当前登陆用户查询对应的角色和权限
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//调用业务层，查询角色
		List<Role> roles = roleService.findByUser(user);
		for (Role role : roles) {
			authorizationInfo.addRole(role.getKeyword());
		}
		//调用权限层。查询权限
		List<Permission> permissions = permissionService.findByUser(user);
		for (Permission permission : permissions) {
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("shiro认证管理");
		
		//转换token
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		if (user == null) {
			//用户名不存在
			
			return null;
		}else{
			//用户名字存在
			//当返回用户名密码的时候，securityManager安全管理器，自动比较 返回密码和用户密码是否一致
			//如果成功，登陆成功，如果不一致，就报异常
			//参数一：期望登陆后，保存在Subject中的信息
			//参数二 ：如果返回null，说明用户名那个不存在，报用户名
			//参数三：realm名称
			return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
		}
		
	}

}

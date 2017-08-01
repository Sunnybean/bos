package cn.itcast.bos.web.action.system;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.web.action.common.BaseAction;
@Scope("prototype")
@Controller
@ParentPackage("json-default")
@Namespace("/")
public class UserAction extends BaseAction<User> {
	
	@Action(value="user_login" , results={@Result(name="success",type="redirect" ,location="index.html"),
			@Result(name="login",type="redirect" ,location="login.html")})
	public String login(){
		//用户名密码，都保存在model中
		//基于shiro实现登录
		Subject subject = SecurityUtils.getSubject();
		//用户名和密码信息
		AuthenticationToken  token = new UsernamePasswordToken(model.getUsername(),model.getPassword());
	try{	
		subject.login(token);
		//登陆成功，将用户保存在session中
		return SUCCESS;
	}catch(Exception e ){
		e.printStackTrace();
		return LOGIN;
	}
	}
	
}

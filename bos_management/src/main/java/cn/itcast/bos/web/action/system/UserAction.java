package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.common.BaseAction;

@Scope("prototype")
@Controller
@ParentPackage("json-default")
@Namespace("/")
public class UserAction extends BaseAction<User> {
	
	@Autowired
	private UserService userService;

	@Action(value = "user_login", results = { @Result(name = "success", type = "redirect", location = "index.html"),
			@Result(name = "login", type = "redirect", location = "login.html") })
	public String login() {
		// 用户名密码，都保存在model中
		// 基于shiro实现登录
		Subject subject = SecurityUtils.getSubject();
		// 用户名和密码信息
		AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		try {
			subject.login(token);
			// 登陆成功，将用户保存在session中
			ServletActionContext.getRequest().getSession().setAttribute("custmomer", model);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return LOGIN;
		}
	}
	/**
	 * 退出的方法
	 */
	@Action(value="user_logout",results={@Result(name="success",type="redirect",location="login.html")})
	public String logout(){
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return SUCCESS;
	}
	@Action(value = "user_list", results = { @Result(name = "success", type="json" )})
	public String list(){
		List<User> list = userService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	private String[] roleIds;
	
	
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	@Action(value="user_save",results={@Result(name="success",type="redirect",location="pages/system/userlist.html")})
	public String save(){
		userService.saveUser(model,roleIds);
		return SUCCESS;
	}

}

package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.common.BaseAction;

@Scope("prototype")
@Controller
@ParentPackage("json-default")
@Namespace("/")
public class RoleAction extends BaseAction<Role> {
	private String[]  permissionIds ;
	
	
	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}


	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}


	private String menuIds;
	
	@Autowired
	private RoleService roleService;
	@Action(value = "role_list", results = { @Result(name = "success", type = "json")})
	public String list(){
		List<Role> roles = roleService.findAll();
		ActionContext.getContext().getValueStack().push(roles);
		return SUCCESS;
	}
	
	
	@Action(value = "role_save", results = { @Result(name = "success", type = "redirect",location="pages/system/role.html")})
	public String save(){
		roleService.save(model,permissionIds,menuIds);
		return SUCCESS;
	}
}

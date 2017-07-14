package cn.itcast.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
@Actions
@Controller
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Courier courier = new Courier();

	@Autowired
	private CourierService courierService;

	@Override
	public Courier getModel() {
		// TODO Auto-generated method stub
		return courier;
	}

	@Action(value = "courier_save", results = {
			@Result(name = "success", location = "./page/base/courier.html", type = "redirect"),
			@Result(name = "input", location = "./page/base/courier.html", type = "redirect") })
	public String save() {
				courierService.save(courier);
		return SUCCESS;
	}

}

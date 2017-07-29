package cn.itcast.bos.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.constant.Contants;
import cn.itcast.crm.domain.Customer;

@Scope("prototype")
@Controller
@ParentPackage("json-default")
@Namespace("/")
public class CustomerAction extends BaseAction<Customer> {

	@Action(value = "customer_login", results = {
			@Result(name = "success", location = "index.html#/myhome", type = "redirect"),
			@Result(name = "login", location = "login.html", type = "redirect") })
	public String login() {
		Customer customer = WebClient
				.create(Contants.CRM_MANAGEMENT_URL + "/services/customerService/customer/login?telephone="
						+ model.getTelephone() + "&passoword=" + model.getPassword())
				//accept 接受查到的数据类型
				.accept(MediaType.APPLICATION_JSON).get(Customer.class);

		if (customer == null) {
			return LOGIN;
		} else {
			
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			return SUCCESS;
		}
	}

}

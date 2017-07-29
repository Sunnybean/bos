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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.constant.Contants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;

@Scope("prototype")
@Controller
@ParentPackage("json-default")
@Namespace("/")
public class OrderAction extends BaseAction<Order> {

	private String sendAreaInfo;
	private String recAreaInfo;

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	@Action(value = "order_add", results = { @Result(name = "success", type = "redirect", location = "index.html") })
	public String addOrder() {
		// 封装发出地址信息
		Area sendArea = new Area();
		String[] sendAreaData = sendAreaInfo.split("/");
		sendArea.setProvince(sendAreaData[0]);
		sendArea.setCity(sendAreaData[1]);
		sendArea.setDistrict(sendAreaData[2]);
		// 封装接受地区信息
		Area recArea = new Area();
		String[] recAreaData = sendAreaInfo.split("/");
		recArea.setProvince(recAreaData[0]);
		recArea.setCity(recAreaData[1]);
		recArea.setDistrict(recAreaData[2]);
		// 把地区关联到order里
		model.setSendArea(sendArea);
		model.setRecArea(recArea);
		// 关联当前客户
		Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
		model.setCustomer_id(customer.getId());
		// 调用WebService保存订单
		WebClient.create(Contants.BOS_MANAGEMENT_URL + "/services/orderService/order").type(MediaType.APPLICATION_JSON)
				.post(model);

		return SUCCESS;
	}

}
